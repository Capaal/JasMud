package processes;

import java.util.*;
import java.util.Map.Entry;

import com.thoughtworks.xstream.annotations.XStreamAlias;

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
	private Map<Direction, LocationConnection> locationMap;
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
		this.locationMap = builder.getlocationMap();
		WorldServer.gameState.addLocation(this.id, this);		
		for (Location otherLocation : builder.locationConnections.keySet()){
			if (otherLocation != null) {
				Direction directionToHere = builder.locationConnections.get(otherLocation).otherLocationToCurrentDirection;
				Door connectingDoor = builder.locationConnections.get(otherLocation).door;
				otherLocation.setLocation(this, connectingDoor, directionToHere);
			} else {
				System.out.println("Location: " + otherLocation + " does not exist to connect to." + this);
			}
		}
	}
	
	// Called from ANOTHER location when connecting locations.
	private void setLocation(Location futureLoc, Door door, Direction directionToThere) {
			this.locationMap.put(directionToThere, new LocationConnection(door, futureLoc));
	}	
	
	public Map<Direction, LocationConnection> getLocationMap() {
		return new HashMap<Direction, LocationConnection>(locationMap);
	}	
	
	public String getDescription() {return description;}			
	public int getId() {return id;}
	
	// The HOLDABLE being moved is EXPECTED to handle adding/removing itself properly.
	public ContainerErrors acceptItem(Holdable newItem) {
		inventory.put(newItem.getName().toLowerCase() + newItem.getId(), newItem);
		return null; //TODO should check the return

	}	
	
	public void acceptItem(Mobile newMob) {
		mobiles.put(newMob.getName().toLowerCase() + newMob.getId(), newMob);
	}
	
	//TODO implement a null location object?
	public Location getLocation(String dir) {
		Direction trueDirection = Direction.getDirectionName(dir);
		return getLocation(trueDirection);
	/*	if (trueDirection == null) {
			return null;
		} else {
			return getLocation(trueDirection);
		}*/
	}		
	
	public Location getLocation(Direction dir) {	
		if (locationMap.get(dir) != null) {
			return locationMap.get(dir).getLocation();
		}
		return null;
	}
	
	public Door getDoor(Direction dir) {
		LocationConnection con = locationMap.get(dir);
		if (con == null) {
			return null;
		}
		return con.getDoor();
	}
	
	public void removeItemFromLocation(Holdable oldItem) {
		if ((inventory.remove(oldItem.getName().toLowerCase() + oldItem.getId()) == null)) {
			throw new IllegalStateException("removeItemFromLocation failed to remove item: " + oldItem.getName() + oldItem.getId());
		}
	}
	
	public void removeItemFromLocation(Mobile oldMob) {
		if ((mobiles.remove(oldMob.getName().toLowerCase() + oldMob.getId()) == null)) {
			throw new IllegalStateException("removeItemFromLocation failed to remove Mobile: " + oldMob.getName() + oldMob.getId());
		}
	}
	
	public TreeMap<String, Holdable> getInventory() {
		return new TreeMap<String, Holdable>(this.inventory);
	}
	
	public TreeMap<String, Mobile> getMobiles() {
		return new TreeMap<String, Mobile>(this.mobiles);
	}
	
	public Mobile getMobileFromString(String mobileString) {	
		mobileString = mobileString.toLowerCase();
		String ceiling = mobiles.ceilingKey(mobileString);
		String floor = mobiles.floorKey(mobileString);
		if (ceiling != null) {
			if ((ceiling.equalsIgnoreCase(mobileString) || mobiles.get(ceiling).getName().equalsIgnoreCase(mobileString))) {
				return mobiles.get(ceiling);
			}
		}
		if (floor != null) {
			if ((floor.equalsIgnoreCase(mobileString) || mobiles.get(floor).getName().equalsIgnoreCase(mobileString))) {
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
		if (ceiling != null) {
			if ((ceiling.equalsIgnoreCase(holdableString) || inventory.get(ceiling).getName().equalsIgnoreCase(holdableString))) {
				return inventory.get(ceiling);
			}
		}
		if (floor != null) {
			if ((floor.equalsIgnoreCase(holdableString) || inventory.get(floor).getName().equalsIgnoreCase(holdableString))) {
				return inventory.get(floor);
			}
		} 
		return null;		
	}

	@Override public String getName() {return name;}
	public Location getContainer(String dir) {return getLocation(dir);}	
	
	public Direction getDirectionToLocation(Location askingLocation) {
		for (Entry<Direction, LocationConnection> entry : locationMap.entrySet()) {
			if (entry.getValue().getLocation() == askingLocation) {
				return entry.getKey();
			}
		}
		return null;
	}
	
//	public static TreeMap<String, String> fullDir;
	
/*	public static String getDirName(String dir) {		
		if (fullDir == null) {
			fullDir = new TreeMap<String, String>();
			fullDir.put("n", "north");
			fullDir.put("ne", "northeast");
			fullDir.put("e","east");
			fullDir.put("s", "south");
			fullDir.put("se", "southeast");			
			fullDir.put("sw", "southwest");
			fullDir.put("w", "west");
			fullDir.put("nw", "northwest");
			fullDir.put("in", "in");
			fullDir.put("o", "out");
			fullDir.put("u", "up");
			fullDir.put("d", "down");
		}
		if (fullDir.containsValue(dir)) {
			return dir;
		} else if (fullDir.containsKey(dir)) {
			return fullDir.get(dir);
		} else {
			for (String s : fullDir.values()) {
				if (s.startsWith(dir)) {
					return s;
				}
			}
		}
		return null;
	}*/
	
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
	public int getMaxQty() {
		return 100000;		
	}

	@Override
	public int getCurrentQty() {
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


}