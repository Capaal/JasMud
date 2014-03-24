package processes;


import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import interfaces.*;


// Contains all information relating to each "room" a player may visit.
// Consider pulling builder out externally?
public class Location implements Container {
		
	private int id;
	private String name;
	private String description;
	private GroundType groundType;
	private Map<Direction, Location> locationMap;
	private Set<Holdable> inventory = new HashSet<Holdable>();
	
	public Location(LocationBuilder builder) {
		if (builder.getId() == -1) {
			this.id = setId();
		} else {
			this.id = builder.getId();
		}
		this.name = builder.getName();
		this.description = builder.getDescription();
		this.groundType = builder.getGroundType();
		this.locationMap = builder.locationMap;
		WorldServer.gameState.locationCollection.put(this.id, this);		
		for (int s : builder.locationConnections.keySet()){
			Location futureLoc = WorldServer.gameState.locationCollection.get(s);
			if (futureLoc != null) {
				Direction currentDirection = builder.locationConnections.get(s);
				futureLoc.setLocation(this, currentDirection);
			}
		}
		save();
	}
	
	private void setLocation(Location futureLoc, Direction currentDirection) {
		if (futureLoc != null) {
			this.locationMap.put(currentDirection, futureLoc);
		} else {
			System.out.println("setLocation just tried to set a null, normal?");
		}		
	}		
	//Displays exits
	public String displayExits() {
		boolean atLeastOne = false;
		String toSay = "You can see no exits.";
		StringBuffer sb = new StringBuffer();
		for (Direction k : locationMap.keySet()) {
			if (!atLeastOne) {
				sb.append("You can see these exits: ");
				sb.append(k.toString());
				atLeastOne = true;
			} else {
				sb.append(", " + k.toString());
			}			
		}
		if (atLeastOne) {
			sb.append(".");
			toSay = sb.toString();
		} 
		return toSay;
	}
	//Looks
	public void look(Mobile currentPlayer) {
		currentPlayer.tell(UsefulCommands.ANSI.MAGENTA + name + UsefulCommands.ANSI.SANE);
		currentPlayer.tell(UsefulCommands.ANSI.GREEN + description + UsefulCommands.ANSI.SANE);
		displayAll(currentPlayer);				
		currentPlayer.tell(UsefulCommands.ANSI.CYAN + displayExits() + UsefulCommands.ANSI.SANE);
		currentPlayer.tell("(God sight) Location number: " + id + ". Ground type: " + groundType.name() + ".");
	}
	//Glance
	public void glance(Mobile currentPlayer) {
		currentPlayer.tell(UsefulCommands.ANSI.MAGENTA + name + UsefulCommands.ANSI.SANE);
		displayAll(currentPlayer);
		currentPlayer.tell(UsefulCommands.ANSI.CYAN + displayExits() + UsefulCommands.ANSI.SANE);
		currentPlayer.tell("(God sight) Location number: " + id + ". Ground type: " + groundType.name() + ".");
	}	
	//Displays items
	public void displayAll(Mobile currentPlayer) {
		boolean anItem = false;
		StringBuilder sb = new StringBuilder();
		sb.append("Looking around you see: ");
		for (Holdable h : inventory) {
			sb.append(UsefulCommands.ANSI.BLUE + h.getName() + ". " + UsefulCommands.ANSI.SANE);
			anItem = true;
		}
		if (anItem) {
			currentPlayer.tell(sb.toString());
		}
	}
	
	public String getDescription() {
		return description;
	}
			
	public int getId() {return id;}
	public GroundType getGroundType() {return groundType;}	
	public void acceptItem(Holdable newItem) {inventory.add(newItem);}	

	public Location getLocation(String dir) {
		Direction trueDirection = Direction.getDirectionName(dir);
		if (trueDirection == null) {
			return null;
		} else {
			return getLocation(trueDirection);
		}
	/*	String trueLocation = UsefulCommands.getDirName(dir);
		Direction locDir = null;
		try {
			locDir = Direction.valueOf(trueLocation.toUpperCase());
		} catch (IllegalArgumentException e) {
			return null;
		}	
		return getLocation(locDir);*/
	}		
	
	public Location getLocation(Direction dir) {		
		return locationMap.get(dir);
	}
	
	public void removeItemFromLocation(Holdable oldItem) {
		inventory.remove(oldItem);	
	}
	
	public Set<Holdable> getInventory() {
		return new HashSet<Holdable>(this.inventory);
	}
	
	@Override
	public Holdable getHoldableFromString(String holdableString) {
		for (Holdable h : inventory) {
			String tempItemName = h.getName().toLowerCase();
			if (tempItemName.equals(holdableString) || (tempItemName + h.getId()).equals(holdableString)) {
				return h;
			}
		}
		return null;			
	}

	@Override
	public String getName() {
		return name;

	}

	public Location getContainer(String dir) {
		return getLocation(dir);
	}
	
	public void save() {
		String directionKeyInformation = makeKeyInformation();
		String directionValueInformation = makeValueInformation();
		String insertNewLocation = "insert into locationstats (LOCID, LOCNAME, LOCDES, LOCTYPE"
				+ directionKeyInformation
				+ ") values (" + id + ", '" + name + "', '" + description + "', '" + groundType.toString() + "'"
				+ directionValueInformation + ") ON DUPLICATE KEY UPDATE LOCID=" + id + ";";	
		System.out.println(insertNewLocation);
		WorldServer.databaseInterface.saveAction(insertNewLocation);		
	}
	
	public Direction getHowOtherLocationConnectsToThis(Location askingLocation) {
		for (Entry<Direction, Location> entry : locationMap.entrySet()) {
			if (entry.getValue() == askingLocation) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	private String makeKeyInformation() {
		StringBuilder sb = new StringBuilder();
		if (locationMap.isEmpty()) {
			return "";
		}
		for (Direction dir : locationMap.keySet()) {
			sb.append(", LOC");
			sb.append(dir.toString());
			sb.append(", LOC");
			sb.append(dir.toString());
			sb.append("DIR");			
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	private String makeValueInformation() {
		StringBuilder sb = new StringBuilder();
		if (locationMap.isEmpty()) {
			return "";
		}		
		for (Location loc : locationMap.values()) {
			sb.append(", ");
			sb.append(loc.getId());
			Direction howOtherLocationConnectsHere = loc.getHowOtherLocationConnectsToThis(this);
			if (howOtherLocationConnectsHere == null) {
				sb.append(", null");
			} else {
				sb.append(", '");				
				sb.append(howOtherLocationConnectsHere.toString());
				sb.append("'");
			}
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	public static TreeMap<String, String> fullDir;
	
	public static String getDirName(String dir) {		
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
	}
	
	private int setId() {
		String sqlQuery = "SELECT sequencetable.sequenceid FROM sequencetable"
				+ " LEFT JOIN locationstats ON sequencetable.sequenceid = locationstats.locid"
				+ " WHERE locationstats.locid IS NULL";		
		Object availableId = (int) WorldServer.databaseInterface.viewData(sqlQuery, "sequenceid");
		if (availableId == null || !(availableId instanceof Integer)) {
			WorldServer.databaseInterface.increaseSequencer();
			return setId();
		} else {
			return (int)availableId;
		}		
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
		
		public static Direction getDirectionName(String commandDirection) {
			for (Direction tryDirection : Direction.values()) {
				if (tryDirection.name().equals(commandDirection.toUpperCase()) || tryDirection.getAbbreviation().equals(commandDirection.toLowerCase())
						|| tryDirection.name().startsWith(commandDirection.toUpperCase())) {	
					return tryDirection;	
				}
			}
			return null;
		}
		
	}
	
	//TODO
	public static int getNewId() {
		String sqlQuery = "SELECT sequencetable.sequenceid FROM sequencetable"
				+ " LEFT JOIN locationstats ON sequencetable.sequenceid = locationstats.locid"
				+ " WHERE locationstats.locid IS NULL";		
		return (int) WorldServer.databaseInterface.viewData(sqlQuery, "sequenceid");
	}
	
	public enum GroundType {		
		// GROUND might get broken up into many types of ground? rock, sand, dirt and so on?
		GROUND() {
			
		},
		
		WATER() {
			
		},
		
		AIR() {
			
		},
		
		CONTAINER() {
			
		};
		
		private GroundType() {}
		
	}
	@Override
	public Container getContainer() {
		return null;
	}

	
}