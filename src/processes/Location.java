package processes;

import interfaces.*;

import java.util.*;


// Contains all information relating to each "room" a player may visit.

public class Location implements Container {
		
	private final int id;
	private String name;
	private String description;
	public ArrayList<Holdable> inventory = new ArrayList<Holdable>();
	private GroundType groundType;
	
	private TreeMap<String, Location> locationMap;
	
	// The BUILDER is an internal class meant to be used to instantly build a new location.
	// It allows the constructor to more clearly indicate what is happening, and allow variable information.
	
	// Location loc = new Location.Builder(2).name("Mud Shack").description("This is north of the beach on the bridge.").groundType("land").south(1, "north").build();
	// Builder(2) indicates it is location number 21, there can be no duplicates.
	// .name("Mud Shack") is the name of the location, and descrption is description, easy to read.
	// .south(1, "north") means if I go south from that location, I go to location with the id 1, and
	// if I go north from there, it will take me back to the location I just made.
	
	public static class Builder {
		
		private final int id;		
		private String name = "blank";
		private String description = "blank";
		private GroundType groundType = GroundType.GROUND;
			
		private TreeMap<String, Location> locationMap = new TreeMap<String, Location>();
		private HashMap<Integer, String> locationConnections = new HashMap<Integer, String>();
		
		public Builder(int val) {
			if (WorldServer.locationCollection.containsKey(val)) {
				throw new IllegalStateException("A location of the id already exists.");
			}
			id = val;
		}
		
		public Builder name(String val) {name = val;return this;}		
		public Builder description(String val) {description = val;return this;}		
		public Builder groundType(GroundType val) {groundType = val;return this;}		
		public Builder north(int futureId, String connectionDirection) {buildDirections(id, "north", futureId, connectionDirection);return this;}			
		public Builder northEast(int futureId, String connectionDirection) {buildDirections(id, "northeast", futureId, connectionDirection);return this;}			
		public Builder east(int futureId, String connectionDirection) {buildDirections(id, "east", futureId, connectionDirection);return this;}				
		public Builder southEast(int futureId, String connectionDirection) {buildDirections(id, "southeast", futureId, connectionDirection);return this;}		
		public Builder south(int futureId, String connectionDirection) {buildDirections(id, "south", futureId, connectionDirection);return this;}			
		public Builder southWest(int futureId, String connectionDirection) {buildDirections(id, "southwest", futureId, connectionDirection);return this;}			
		public Builder west(int futureId, String connectionDirection) {buildDirections(id, "west", futureId, connectionDirection);return this;}			
		public Builder northWest(int futureId, String connectionDirection) {buildDirections(id, "northwest", futureId, connectionDirection);return this;}			
		public Builder up(int futureId, String connectionDirection) {buildDirections(id, "up", futureId, connectionDirection);return this;}			
		public Builder down(int futureId, String connectionDirection) {buildDirections(id, "down", futureId, connectionDirection);return this;}		
		public Builder in(int futureId, String connectionDirection) {buildDirections(id, "in", futureId, connectionDirection);return this;}			
		public Builder out(int futureId, String connectionDirection) {buildDirections(id, "out", futureId, connectionDirection);return this;}			
		
		private Builder buildDirections(int currentId, String currentDirection,  int futureId, String futureD) {
			if (currentId == 0 || futureD == null) {
				return this;
			}
			String futureDirection = futureD.toLowerCase();
			if (WorldServer.locationCollection.containsKey(futureId)) {
				Location futureLoc = WorldServer.locationCollection.get(futureId);								
				locationConnections.put(futureId, futureDirection);				
				locationMap.put(currentDirection,  futureLoc);		
			} else {
				System.out.println("I think a location was made that is pointing to an unmade location: " + currentId);
			}
			return this;
		}		
		
		public Location build() {return new Location(this);}
	}
	
	public Location(Builder builder) {
		this.id = builder.id;
		setName(builder.name);
		setDescription(builder.description);
		setGroundType(builder.groundType);
		this.locationMap = builder.locationMap;
		WorldServer.locationCollection.put(this.id, this);
		for (int s : builder.locationConnections.keySet()){
			Location futureLoc = WorldServer.locationCollection.get(s);
			if (futureLoc != null) {
				String currentDirection = builder.locationConnections.get(s);
				futureLoc.setLocation(this, currentDirection);
			}
		}
	}
	
	public void setLocation(Location futureLoc, String currentDirection) {
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
		for (String k : locationMap.keySet()) {
			if (!atLeastOne) {
				sb.append("You can see these exits: ");
				sb.append(k);
				atLeastOne = true;
			} else {
				sb.append(", " + k);
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
		StringBuffer sb = new StringBuffer();
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
	public void setName(String name) {this.name = name;}	
	public void setDescription(String desc) {this.description = desc;}	
	public void setGroundType(GroundType type) {this.groundType = type;}
	public GroundType getGroundType() {return groundType;}	
	public void acceptItem(Holdable newItem) {inventory.add(newItem);}	
		
	public Location getLocation(String dir) {
		String trueLocation = UsefulCommands.getDirName(dir);
		if (trueLocation != null) {
			return locationMap.get(trueLocation);
		}
		return null;
	}		
	
	public void removeItemFromLocation(Holdable oldItem) {
		int indexOfItem = inventory.indexOf(oldItem);
		inventory.remove(indexOfItem);	

	}
	
	public ArrayList<Holdable> getInventory() {
		return new ArrayList<Holdable>(this.inventory);
	}

	@Override
	public String getName() {
		return name;

	}

	@Override
	public Container getContainer(String dir) {
		return getLocation(dir);
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