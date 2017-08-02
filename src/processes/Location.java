package processes;

import java.util.*;
import java.util.Map.Entry;

import processes.LocationBuilder.LocationConnectionDataBox;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import effects.Blocking;
import Quests.Quest;
import Quests.Quest.Trigger;
import interfaces.*;
import items.Door;

/*
 *  Contains all information relating to each "room" a player may visit.
 *  Should be initiated safely using LocationBuilder
 */
@XStreamAlias("Location")
public class Location implements Container {
		
	private final int id;
	private final String name;
	private final String description;
	private Map<Direction, LocationConnection> locationMap; // Not final, new Locations will change this Location's map.
	protected TreeMap<String, Holdable> inventory = new TreeMap<String, Holdable>();
	protected TreeMap<String, Mobile> mobiles = new TreeMap<String, Mobile>();
	
	private final Quest bondedQuest;
	
	public Location(LocationBuilder builder) {
		if (!builder.isCompleted()) {
			throw new IllegalStateException("Builder is not in a valid state, Location not built.");
		}
		this.id = builder.getId();
		this.name = builder.getName();
		this.description = builder.getDescription();
		this.bondedQuest = builder.getQuest();
		if (bondedQuest != null) {
			bondedQuest.bondLocation(this);
		}		
		// Build all Location Connections
		locationMap = new HashMap<Direction, LocationConnection>();
		for (LocationConnectionDataBox db : builder.getLocationBuildingBlocks()) {
			LocationConnection newLocCon = new LocationConnection(this, db.door, db.other);
			locationMap.put(db.toOther, newLocCon);
			db.other.setLocation(db.otherLocationToCurrentDirection, newLocCon);
		}		
		WorldServer.getGameState().addLocation(this.id, this);	
	}
	
	// OTHER Locations call this to set THIS location's direction to that new location.
	private void setLocation(Direction directionToThere, LocationConnection newLocCon) {
		this.locationMap.put(directionToThere, newLocCon);
	}
	
	public Map<Direction, LocationConnection> getLocationMap() {
		return new HashMap<Direction, LocationConnection>(locationMap);
	}	
	
	public String getDescription() {return description;}			
	@Override public int getId() {return id;}
	@Override public String getName() {return name;}
	
	// The HOLDABLE being moved is EXPECTED to handle adding/removing itself properly.
	public ContainerErrors acceptItem(Holdable newItem) {
		inventory.put(newItem.getName().toLowerCase() + newItem.getId(), newItem);
		return null; // Always successful, no weight or type restrictions.
	}	
	
	// Accepting Mobiles to the Location.
	public void acceptItem(Mobile newMob) {
		mobiles.put(newMob.getName().toLowerCase() + newMob.getId(), newMob);
		for (Mobile m : mobiles.values()) {
			m.informEntered(newMob);
		}
		// add an items.informEntered()
		// Add an effects.informEntered();
	}
	
	/** Get Location based on string, specificity up to startsWith for wanted Direction.
	* If string is invalid, uses null instead, thus returning a null value.
	* @return Location Location that given string points to, or Null if none or invalid.
	* @param dir String indicating desired direction with startsWith accuracy.
	* @see Enum Direction.
	*/
	public Location getLocation(String dir) {
		Direction trueDirection = Direction.getDirectionName(dir);
		return getLocation(trueDirection);
	}		
	
	/** Get Location based on DIRECTION eunum.
	* @return Location Location that given Direction points to, or Null if none or invalid.
	* @param dir Direction indicating desired direction.
	* @see Enum Direction.
	*/
	public Location getLocation(Direction dir) {	
		if (locationMap.get(dir) != null) {
			return locationMap.get(dir).getNotOneself(this);
		}
		return null;
	}
	
	/**
	 * Gets door separating two locations defined by current Location's location Map and the given Direction.
	 * There may only be one door, or none, separating locations.
	 * @param dir Direction from current Location to search for the door.
	 * @return Door returned if a door exists, Null if there is no door, or no location in said direction.
	 */
	public Door getDoor(Direction dir) {
		LocationConnection con = locationMap.get(dir);
		if (con == null) {
			return null;
		}
		return con.getDoor();
	}
	
	/**
	 * Removes Holdable from this Location's Holdable List. 
	 * @throws IllegalStateException thrown if Holdable is NOT present.
	 */
	public void removeItemFromLocation(Holdable oldItem) {
		if ((inventory.remove(oldItem.getName().toLowerCase() + oldItem.getId()) == null)) {
			System.out.println(inventory);
			throw new IllegalStateException("removeItemFromLocation failed to remove Holdable: " + oldItem.getName() + oldItem.getId());
		}
	}
	
	/**
	 * Removes Mobile from this Location's Mobile List. 
	 * @throws IllegalStateException thrown if Mobile is NOT present.
	 */
	public void removeItemFromLocation(Mobile oldMob) {
		if ((mobiles.remove(oldMob.getName().toLowerCase() + oldMob.getId()) == null)) {
			throw new IllegalStateException("removeItemFromLocation failed to remove Mobile: " + oldMob.getName() + oldMob.getId());
		}
	}
	
	/**
	 * Returns a VIEW of this Location's Inventory.
	 * @return TreeMap<String,Holdable> is a sortable navigatable map.
	 */
	public TreeMap<String, Holdable> viewInventory() {
		return new TreeMap<String, Holdable>(this.inventory);
	}
	
	/**
	 * Returns a VIEW of this Location's list of Mobiles.
	 * @return TreeMap<String,Mobile> is a sortable navigatable map.
	 */
	public TreeMap<String, Mobile> viewMobiles() {
		return new TreeMap<String, Mobile>(this.mobiles);
	}
	
	/**
	 * Attempts to locate the single most likely Mobile as designated by given string. Accepts both
	 * name and name+id with startsWith accuracy.
	 * @param mobileString String to use when searching for Mobile.
	 * @return Mobile or Null if nothing within an acceptable accuracy is found.
	 */
	public Mobile getMobileFromString(String mobileString) {	
		mobileString = mobileString.toLowerCase();
		String ceiling = mobiles.ceilingKey(mobileString);
		String floor = mobiles.floorKey(mobileString);
		if (ceiling != null) {
			if (ceiling.equalsIgnoreCase(mobileString)) {
				return mobiles.get(ceiling);
			} else if (ceiling.toLowerCase().startsWith(mobileString)) {
				return mobiles.get(ceiling);
			}
		} 
		if (floor != null) {
			if (floor.equalsIgnoreCase(mobileString)) {
				return mobiles.get(floor);
			} else if (floor.toLowerCase().startsWith(mobileString)) {
				return mobiles.get(floor);
			}
		}
		return null;	
	}
	
	@Override
	public Holdable getHoldableFromString(String holdableString) {			
		holdableString = holdableString.toLowerCase();
		String ceiling = inventory.ceilingKey(holdableString);
		String floor = inventory.floorKey(holdableString);			
		String hasNum = UsefulCommands.getOnlyNumerics(holdableString);
		if (ceiling != null) {
			if (!hasNum.equals("") && ceiling.equalsIgnoreCase(holdableString)) {
				return inventory.get(ceiling);				
			} else if (ceiling.toLowerCase().startsWith(holdableString)) {
				return inventory.get(ceiling);				
			}
		}
		if (floor != null) {
			if (!hasNum.equals("") && floor.equalsIgnoreCase(holdableString)) {
				return inventory.get(floor);				
			} else if (floor.toLowerCase().startsWith(holdableString)) {
				return inventory.get(floor);				
			}
		}
		return null;		
	}	
	
	/**
	 * Attempts to determine if current Location has any connection to the given Location, and returns which Direction if true.
	 * @param askingLocation Location to which you want to find a location to.
	 * @return Direction that connects to the given Location from the current Location.
	 */
	public Direction getDirectionToLocation(Location askingLocation) {
		for (Entry<Direction, LocationConnection> entry : locationMap.entrySet()) {
			if (entry.getValue().getNotOneself(this) == askingLocation) {
				return entry.getKey();
			}
		}
		return null;
	}	
	
	public enum Direction {		
		NORTH() {
			@Override
			public String getOpp() {
				return "south";
			}
			@Override
			public String getAbbreviation() {
				return "n";
			}
		},
		
		SOUTH() {
			@Override
			public String getOpp() {
				return "north";
			}
			@Override
			public String getAbbreviation() {
				return "s";
			}
		},
		
		EAST() {
			@Override
			public String getOpp() {
				return "west";
			}
			@Override
			public String getAbbreviation() {
				return "e";
			}
		},
		
		WEST() {
			@Override
			public String getOpp() {
				return "east";
			}
			@Override
			public String getAbbreviation() {
				return "w";
			}
		},
		
		NORTHEAST() {
			@Override
			public String getOpp() {
				return "southwest";
			}
			@Override
			public String getAbbreviation() {
				return "ne";
			}
		},		
		
		SOUTHEAST() {
			@Override
			public String getOpp() {
				return "northwest";
			}
			@Override
			public String getAbbreviation() {
				return "se";
			}
		},		
		
		SOUTHWEST() {
			@Override
			public String getOpp() {
				return "northeast";
			}
			@Override
			public String getAbbreviation() {
				return "sw";
			}
		},		
		
		NORTHWEST() {
			@Override
			public String getOpp() {
				return "southeast";
			}
			@Override
			public String getAbbreviation() {
				return "nw";
			}
		},
		
		UP() {
			@Override
			public String getOpp() {
				return "down";
			}
			@Override
			public String getAbbreviation() {
				return "u";
			}
		},
		
		DOWN() {
			@Override
			public String getOpp() {
				return "up";
			}
			@Override
			public String getAbbreviation() {
				return "d";
			}
		},
		
		IN() {
			@Override
			public String getOpp() {
				return "out";
			}
			@Override
			public String getAbbreviation() {
				return "in";
			}
		},
		
		OUT() {
			@Override
			public String getOpp() {
				return "in";
			}
			@Override
			public String getAbbreviation() {
				return "o";
			}
		};
		
		private Direction() {}
		
		public abstract String getOpp();
		public abstract String getAbbreviation();
		
		/**
		 * Returns Direction enum matching string supplied. Ignores case, allows abbreviation, and startswith.
		 * @param commandDirection String of direction desired.
		 * @return Direction direction enum, or null if string does not match any.
		 */
		public static Direction getDirectionName(String commandDirection) {
			if (commandDirection.equals("")) {
				return null;
			}
			for (Direction tryDirection : Direction.values()) {
				if (tryDirection.name().equals(commandDirection.toUpperCase()) || tryDirection.getAbbreviation().equals(commandDirection.toLowerCase())
						|| tryDirection.name().startsWith(commandDirection.toUpperCase())) {	
					return tryDirection;	
				}
			}
			return null;
		}		
	}	
	
	public void notifyQuest(Trigger trigger) {
		if (bondedQuest != null) {
			bondedQuest.triggered(trigger);
		}
	}

	public Quest getQuest() {
		return bondedQuest;
	}

	@Override
	public double getMaxWeight() {
		return 100000;		
	}

	// Returns 0 because Location effectively has infinite capabilities.
	@Override
	public double getCurrentWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<Holdable> getListMatchingString(String holdableString) {
		holdableString = holdableString.toLowerCase();		
		SortedMap<String, Holdable> subMap = inventory.subMap(holdableString, true, holdableString + Character.MAX_VALUE, true);		
		Collection<Holdable> set = subMap.values();
		System.out.println("Location getListMatchingString: " + set.toString());		
		if (set.isEmpty() || set == null) {
			Holdable h = getHoldableFromString(holdableString);
			if (h != null) {
				set.add(h);
			}
		}
		return set;
	}

	@Override public void changeWeight(double change) {
		// Blank, we do not allow Location's weight to change.
	}

	/**
	 * Add Blocking effects, such as walls, to a Connection between two Locations based on the current Location and Direction given.
	 * Blocking effects can stack, and do not remove each other.
	 * @param direction Direction on which this blocking effect will be added.
	 * @param blocking Blocking effect to be added.
	 * @throws IllegalStateException thrown if desired Direction does not lead to a valid Location Connection.
	 */
	public void addBlocking(Direction direction, Blocking blocking) {
		LocationConnection locDir = locationMap.get(direction);
		if (locDir == null) {
			throw new IllegalStateException("Blocking may only be added to existing connections.");
		}
		locDir.addBlocking(blocking);		
	}
	
	/**
	 * Remove Blocking effects, such as walls, from a Connection between two Locations based on the current Location and Direction given.
	 * @param direction Direction on which this blocking effect will be removed.
	 * @param blocking Blocking effect to be removed.
	 * @throws IllegalStateException thrown if desired Direction does not lead to a valid Location Connection.
	 */
	public void removeBlocking(Direction direction, Blocking blocking) {
		LocationConnection locDir = locationMap.get(direction);
		if (locDir == null) {
			throw new IllegalStateException("Blocking may only be renived to existing connections.");
		}
		locDir.removeBlocking(blocking);		
	}

	/**
	 * Returns whether a direction is blocked by a Blocking Effect, unrelated from doors. TODO? Should doors be included here?
	 * @param interestedDir Direction to look for a connection, then look for blocking effects.
	 * @return boolean True if blocked, false otherwise included if there is no direction there.
	 */
	public boolean isDirectionBlocked(Direction interestedDir) {
		LocationConnection locDir = locationMap.get(interestedDir);
		if (locDir != null) {
			return locDir.isBlocked();	
		}
		return false;
	}

	/**
	 * Gets list of blocking effects in the given Direction.
	 * @param interestedDir
	 * @return List<Blocking>
	 */
	public List<Blocking> getBlocking(Direction interestedDir) {
		LocationConnection locDir = locationMap.get(interestedDir);
		return locDir.getBlocking();	
	}
}