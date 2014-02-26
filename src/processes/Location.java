package processes;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import interfaces.*;


// Contains all information relating to each "room" a player may visit.

public class Location implements Container {
		
	private final int id;
	private final String name;
	private final String description;
	public Set<Holdable> inventory = new HashSet<Holdable>();
	private final GroundType groundType;	
	private final Map<Direction, Location> locationMap;
	
	// The BUILDER is an internal class meant to be used to instantly build a new location.
	// It allows the constructor to more clearly indicate what is happening, and allow variable information.
	
	// Location loc = new Location.Builder(2).name("Mud Shack").description("This is north of the beach on the bridge.").groundType("land").south(1, "north").build();
	// Builder(2) indicates it is location number 21, there can be no duplicates.
	// .name("Mud Shack") is the name of the location, and descrption is description, easy to read.
	// .south(1, "north") means if I go south from that location, I go to location with the id 1, and
	// if I go north from there, it will take me back to the location I just made.
	
	public static class Builder {
		
		private int id;		
		private String name = "blank";
		private String description = "blank";
		private GroundType groundType = GroundType.GROUND;			
		private Map<Direction, Location> locationMap = new EnumMap<Direction, Location>(Direction.class);
		private Map<Integer, Direction> locationConnections = new HashMap<Integer, Direction>();
		
		public Builder(int val) {
			if (WorldServer.locationCollection.containsKey(val)) {
				throw new IllegalStateException("A location of the id already exists.");
			}
			id = val;
		}
		
		public Builder name(String val) {name = val;return this;}		
		public Builder description(String val) {description = val;return this;}		
		public Builder groundType(GroundType val) {groundType = val;return this;}		
		public Builder north(int futureId, String connectionDirection) {buildDirections(id, Direction.NORTH, futureId, connectionDirection);return this;}			
		public Builder northEast(int futureId, String connectionDirection) {buildDirections(id, Direction.NORTHEAST, futureId, connectionDirection);return this;}			
		public Builder east(int futureId, String connectionDirection) {buildDirections(id, Direction.EAST, futureId, connectionDirection);return this;}				
		public Builder southEast(int futureId, String connectionDirection) {buildDirections(id, Direction.SOUTHEAST, futureId, connectionDirection);return this;}		
		public Builder south(int futureId, String connectionDirection) {buildDirections(id, Direction.SOUTH, futureId, connectionDirection);return this;}			
		public Builder southWest(int futureId, String connectionDirection) {buildDirections(id, Direction.SOUTHWEST, futureId, connectionDirection);return this;}			
		public Builder west(int futureId, String connectionDirection) {buildDirections(id, Direction.WEST, futureId, connectionDirection);return this;}			
		public Builder northWest(int futureId, String connectionDirection) {buildDirections(id, Direction.NORTHWEST, futureId, connectionDirection);return this;}			
		public Builder up(int futureId, String connectionDirection) {buildDirections(id, Direction.UP, futureId, connectionDirection);return this;}			
		public Builder down(int futureId, String connectionDirection) {buildDirections(id, Direction.DOWN, futureId, connectionDirection);return this;}		
		public Builder in(int futureId, String connectionDirection) {buildDirections(id, Direction.IN, futureId, connectionDirection);return this;}			
		public Builder out(int futureId, String connectionDirection) {buildDirections(id, Direction.OUT, futureId, connectionDirection);return this;}			
		
		private Builder buildDirections(int currentId, Direction currentDirection,  int futureId, String futureD) {
			// SQL will call for a direction even if there is no location here, so just returns.
			if (currentId == 0 || futureD == null) {
				return this;				
			}
			Direction futureDirection = Direction.NORTH;
			try {
				futureDirection = Direction.valueOf(futureD.toUpperCase());
			} catch (IllegalArgumentException e) {
				System.out.println(futureD + " is not a valid direction loaded from database. CRITICAL ERROR, Defaulted to north.");				
			}
			if (WorldServer.locationCollection.containsKey(futureId)) {
				Location futureLoc = WorldServer.locationCollection.get(futureId);								
				locationConnections.put(futureId, futureDirection);				
				locationMap.put(currentDirection, futureLoc);		
			} else {
				System.out.println("I think a location was made that is pointing to an unmade location: " + currentId);
			}
			return this;
		}		
		
		public Location build() {return new Location(this);}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public GroundType getGroundType() {
			return groundType;
		}
	}
	
	public Location(Builder builder) {
		this.id = builder.getId();
		this.name = builder.getName();
		this.description = builder.getDescription();
		this.groundType = builder.getGroundType();
		this.locationMap = builder.locationMap;
		WorldServer.locationCollection.put(this.id, this);
		for (int s : builder.locationConnections.keySet()){
			Location futureLoc = WorldServer.locationCollection.get(s);
			if (futureLoc != null) {
				Direction currentDirection = builder.locationConnections.get(s);
				futureLoc.setLocation(this, currentDirection);
			}
		}
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
			
	public int getId() {return id;}
	public GroundType getGroundType() {return groundType;}	
	public void acceptItem(Holdable newItem) {inventory.add(newItem);}	
		
	public Location getLocation(String dir) {
		String trueLocation = UsefulCommands.getDirName(dir);
		Direction locDir = null;
		try {
			locDir = Direction.valueOf(trueLocation.toUpperCase());
		} catch (IllegalArgumentException e) {
			return null;
		}	
		return locationMap.get(locDir);
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

	@Override
	public Container getContainer(String dir) {
		return getLocation(dir);
	}
	
	public enum Direction {
		
		NORTH() {
			@Override
			public String getOpp() {
				return "south";
			}
		},
		
		NORTHEAST() {
			@Override
			public String getOpp() {
				return "southwest";
			}
		},
		
		EAST() {
			@Override
			public String getOpp() {
				return "west";
			}
		},
		
		SOUTHEAST() {
			@Override
			public String getOpp() {
				return "northwest";
			}
		},
		
		SOUTH() {
			@Override
			public String getOpp() {
				return "north";
			}
		},
		
		SOUTHWEST() {
			@Override
			public String getOpp() {
				return "northeast";
			}
		},
		
		WEST() {
			@Override
			public String getOpp() {
				return "east";
			}
		},
		
		NORTHWEST() {
			@Override
			public String getOpp() {
				return "southeast";
			}
		},
		
		UP() {
			@Override
			public String getOpp() {
				return "down";
			}
		},
		
		DOWN() {
			@Override
			public String getOpp() {
				return "up";
			}
		},
		
		IN() {
			@Override
			public String getOpp() {
				return "out";
			}
		},
		
		OUT() {
			@Override
			public String getOpp() {
				return "in";
			}
		};
		
		private Direction() {}
		
		public abstract String getOpp();
		
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
}