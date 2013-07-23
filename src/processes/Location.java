package processes;


import java.awt.Point;
import java.io.*;
import java.util.*;

import Interfaces.*;

// Mobiles should be remembered in the location, for look and stuff, they can go with items and still work atm.
public class Location implements Container {
		
	private final int id;
	private String name;
	private String description;
	public ArrayList<Holdable> groundItems = new ArrayList<Holdable>();
	private String groundType;
	
	private HashMap<String, Location> locationMap;
	
	private HashMap<String, String> abbrevNames;
/*	
	private Location north;
	private Location northeast;
	private Location east;
	private Location southeast;
	private Location south;
	private Location southwest;
	private Location west;
	private Location northwest;
	private Location up;
	private Location down;
	private Location in;
	private Location out;*/
	//private Location[] locations;
//	public Point point; // Related to map?
		
//	public final static int[] EXITS = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
//	public final static int[] EXITSOPP = new int[] {3, 5, 6, 0, 7, 1, 2, 4, 9, 8, 11, 10};	
//	public static final String[] DIRNAMES = new String[] {"north", "northeast", "east", "south", "southeast", 
//		"southwest", "west", "northwest", "in", "out", "up", "down"};
//	public static final String[] DIRABBREV = new String[] {"n", "ne", "e", "s", "se", "sw", "w", "nw", "in", "out", "u", "d"};
	
	
//	public final static int[] EXITS = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
//	public final static int[] EXITSOPP = new int[] {4, 5, 6, 7, 0, 1, 2, 3, 9, 8, 11, 10};	
//	public static final String[] DIRNAMES = new String[] {"north", "northeast", "east", "southeast", "south", 
//			"southwest", "west", "northwest", "in", "out", "up", "down"};
	//public static final String[] dirAbbrev = new String[] {"n", "ne", "e", "se", "s", "sw", "w", "nw", "in", "out", "u", "d"};
	
	public static class Builder {
		
		private final int id;
		
		private String name = "blank";
		private String description = "blank";
		private String groundType = "land";
		private Location north;
		private Location northeast;
		private Location east;
		private Location southeast;
		private Location south;
		private Location southwest;
		private Location west;
		private Location northwest;
		private Location up;
		private Location down;
		private Location in;
		private Location out;
		
		private HashMap<String, Location> locationMap = new HashMap<String, Location>();
		private HashMap<Integer, String> locationConnections = new HashMap<Integer, String>();
	//	private Location[] locations = new Location[12];
	//	private boolean[] locationConnections = new boolean[12];
		
		public Builder(int val) {
			if (WorldServer.locationCollection.containsKey(val)) {
				throw new IllegalStateException("A location of the id already exists.");
			}
			id = val;
		}
		
		public Builder name(String val) {
			name = val;
			return this;
		}
		
		public Builder description(String val) {
			description = val;
			return this;
		}
		
		public Builder groundType(String val) {
			groundType = val;
			return this;
		}
		
		public Builder north(int futureId, String connectionDirection) {
			buildDirections(id, "north", futureId, connectionDirection);
			return this;
		}	
		
		public Builder northEast(int futureId, String connectionDirection) {
			buildDirections(id, "northeast", futureId, connectionDirection);
			return this;
		}	
		
		public Builder east(int futureId, String connectionDirection) {
			buildDirections(id, "east", futureId, connectionDirection);
			return this;
		}		
		
		public Builder southEast(int futureId, String connectionDirection) {
			buildDirections(id, "southeast", futureId, connectionDirection);
			return this;
		}
		
		public Builder south(int futureId, String connectionDirection) {
			buildDirections(id, "south", futureId, connectionDirection);
			return this;
		}	
		
		public Builder southWest(int futureId, String connectionDirection) {
			buildDirections(id, "southwest", futureId, connectionDirection);
			return this;
		}	
		
		public Builder west(int futureId, String connectionDirection) {
			buildDirections(id, "west", futureId, connectionDirection);
			return this;
		}	
		
		public Builder northWest(int futureId, String connectionDirection) {
			buildDirections(id, "northwest", futureId, connectionDirection);
			return this;
		}	
		
		public Builder up(int futureId, String connectionDirection) {
			buildDirections(id, "up", futureId, connectionDirection);
			return this;
		}	
		
		public Builder down(int futureId, String connectionDirection) {
			buildDirections(id, "down", futureId, connectionDirection);
			return this;
		}	
		
		public Builder in(int futureId, String connectionDirection) {
			buildDirections(id, "in", futureId, connectionDirection);
			return this;
		}	
		
		public Builder out(int futureId, String connectionDirection) {
			buildDirections(id, "out", futureId, connectionDirection);
			return this;
		}	
		
		
		private Builder buildDirections(int currentId, String currentDirection,  int futureId, String futureDirection) {
			if (WorldServer.locationCollection.containsKey(futureId)) {
				Location futureLoc = WorldServer.locationCollection.get(futureId);
								
				locationConnections.put(futureId, futureDirection);
				
				locationMap.put(currentDirection,  futureLoc);
							
			/*	switch (currentDirection) {					
					case "north":
						this.north = futureLoc;						
						break;
					case "northeast":
						this.northeast = futureLoc;
						break;		
					case "east":
						this.east = futureLoc;
						break;	
					case "southeast":
						this.southeast = futureLoc;
						break;	
					case "south":
						this.south = futureLoc;
						break;	
					case "southwest":
						this.southwest = futureLoc;
						break;	
					case "west":
						this.west = futureLoc;
						break;	
					case "northwest":
						this.northwest = futureLoc;
						break;	
					case "up":
						this.up = futureLoc;
						break;	
					case "down":
						this.down = futureLoc;
						break;	
					case "in":
						this.in = futureLoc;
						break;	
					case "out":
						this.out = futureLoc;
						break;	
				}*/
		//		locations[dir] = loc;			
			} else {
		//		locations[dir] = null;
				System.out.println("I think a location was made that is pointing to an unmade location: " + currentId);
			}
		//	locationConnections[dir] = connected;
			return this;
		}
		
		
		
		public Location build() {
			return new Location(this);
		}
	}
	
	public Location(Builder builder) {
	//	locations = new Location[12];
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
		abbrevNames = new HashMap<String, String>();
		abbrevNames.put("n", "north");
		abbrevNames.put("ne", "northeast");
		abbrevNames.put("e", "east");
		abbrevNames.put("se", "southeast");
		abbrevNames.put("s", "south");
		abbrevNames.put("sw", "southwest");
		abbrevNames.put("w", "west");
		abbrevNames.put("nw", "northwest");
		abbrevNames.put("u", "up");
		abbrevNames.put("d", "down");
		abbrevNames.put("i", "in");
		abbrevNames.put("o", "out");
		
	//	for (int i = 0; i <= 11; i++) {
	//		locations[i] = builder.locations[i];
	//		if (builder.locationConnections[i] && locations[i] != null) {
	//			Location anotherLocation = getLocation(i);
	//			anotherLocation.setLocation(this, Location.EXITSOPP[i]);
	//		}
	//	}
		
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
		for (Map.Entry<String, Location> entry : locationMap.entrySet()) {
			String key = entry.getKey();
		//	Location value = entry.getValue(); 
			if (!atLeastOne) {
				sb.append("You can see these exits: ");
				sb.append(key);
				atLeastOne = true;
			} else {
				sb.append(", " + key);
			}			
		}
		if (atLeastOne) {
			sb.append(".");
			toSay = sb.toString();
		} 
		return toSay;
	/*	
		String toSay = "You can see no exits.";
		StringBuffer sb = new StringBuffer();
		boolean firstExit = false;
		for (int i = 0; i < 11; i++) {
			if (locations[i] != null) {
				if (firstExit == false) {
					sb.append("You can see these exits: ");
					sb.append(DIRNAMES[i]);
					firstExit = true;
				}
				else {
					sb.append(", " + DIRNAMES[i]);
				}
			}
		}
		if (firstExit == true) {
			sb.append(".");
			toSay = sb.toString();
		}
		return toSay;*/
	}
	//Looks
	public void look(Mobile currentPlayer) {
		currentPlayer.tell(UsefulCommands.ANSI.MAGENTA + name + UsefulCommands.ANSI.SANE);
		currentPlayer.tell(UsefulCommands.ANSI.GREEN + description + UsefulCommands.ANSI.SANE);
		displayAll(currentPlayer);
	//	displayMob(currentPlayer);
	//	displayPeople(currentPlayer);
		currentPlayer.tell(UsefulCommands.ANSI.CYAN + displayExits() + UsefulCommands.ANSI.SANE);
		currentPlayer.tell("(God sight) Location number: " + id + ". Ground type: " + groundType + ".");
	}
	//Glance
	public void glance(Mobile currentPlayer) {
		currentPlayer.tell(UsefulCommands.ANSI.MAGENTA + name + UsefulCommands.ANSI.SANE);
		displayAll(currentPlayer);
		currentPlayer.tell(UsefulCommands.ANSI.CYAN + displayExits() + UsefulCommands.ANSI.SANE);
		currentPlayer.tell("(God sight) Location number: " + id + ". Ground type: " + groundType + ".");
	}	
	//Location description
	// should just use glance or look
//	public void displayLocation(Mobile currentPlayer) {
//		currentPlayer.tell(name);
//		currentPlayer.tell(description);
//		currentPlayer.tell(displayExits());
//	}
	//Displays people
//	private void displayPeople(String yourName, SendMessage sendBack) {		
	/*	String toSay = "";
		StringBuffer sb = new StringBuffer();
		Iterator iter = WorldServer.activeClients.iterator(); //All possible players to be displayed are in activeClients
		boolean noOne = true;
		while (iter.hasNext()) {
			Player currentPlayer = ((PlayerPrompt) iter.next()).currentPlayer;
			try {
				if (currentPlayer.getMobLocation() == this && !currentPlayer.name.equals(yourName)) {
					if (currentPlayer.getIsDead()) {
						sb.append(currentPlayer.name + " is here. ");
					} else {
						sb.append(currentPlayer.name + "'s body lies in a bloody heap nearby.");
					}
					noOne = false;
				}
			} catch (NullPointerException ie) {
				System.out.println("Oh snap!");
			}	
		}		
		if (noOne == false) {
			toSay = sb.toString();
		}
		if (!toSay.equals("")) {
			sendBack.printMessage(UsefulCommands.ANSI.WHITE + toSay + UsefulCommands.ANSI.SANE);
		}*/
//	}
	//Displays mobs
/*	private void displayMob(SendMessage sendBack, String yourName) {
		StringBuffer sb = new StringBuffer();
		Set s = WorldServer.mobList.keySet();
		Iterator iter = s.iterator();
		while (iter.hasNext()) {
			StdMob mm = WorldServer.mobList.get(iter.next());
		//	if (mm instanceof Mob) {
			//	Mob m = (Mob) mm;		
				if (mm.getMobLocation() == this && mm.id != -1 && !mm.getName().equals(yourName)) {
					if (!mm.getIsDead()) {
						sb.append("There is " + mm.shortDescription + " here. "); // fix for generics, call to a class in specific mobile
					} else if (mm.getIsDead()) {
						sb.append("Lying on the ground is a dead " + mm.name + "."); // fix for generics, call to a class in specific mobile
					}
				}
		//	}
		}
		if (sb.length() > 0) {
			sendBack.printMessage(UsefulCommands.ANSI.RED + sb.toString() + UsefulCommands.ANSI.SANE);
		}
	}*/
	//Displays items
	public void displayAll(Mobile currentPlayer) {
		for (Holdable h : groundItems) {
			currentPlayer.tellLine("Looking around you see: ");
			currentPlayer.tellLine(UsefulCommands.ANSI.BLUE + h.getName() + ". " + UsefulCommands.ANSI.SANE);
		}
		currentPlayer.tell("");
	}
	 // Both of the below are old.
//	public void setLocation(int locId, int dir) {
//		Location loc = WorldServer.locationCollection.get(locId);
//		this.locations[dir] = loc;
//	}
	
//	public void setLocation(Location loc, int dir) {
//		this.locations[dir] = loc;
//	}
		
	public int getId() {
		return id;
	}
		
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String desc) {
		this.description = desc;
	}
	
	public void setGroundType(String type) {
		this.groundType = type;
	}
	
	public Location getLocation(String dir) {
		if (locationMap.containsKey(dir)) {
			return locationMap.get(dir);
		} 
		return locationMap.get(abbrevNames.get(dir));		
	}
	/* old
	public Location getLocation(String dir) {
		if (dir != null) {
			for (int i = 0; i <= 11; i ++) {
				if (DIRABBREV[i].equals(dir)) {
					return getLocation(i);
				}
				if (DIRNAMES[i].startsWith(dir)) {
					return getLocation(i);
				}
			}
		}
		return null;	
	}*/
	
	public String getGroundType() {
		return groundType;
	}
	
	public void acceptItem(Holdable newItem) {
		groundItems.add(newItem);
	}
	
	public void removeItemFromLocation(Holdable oldItem) {
		int indexOfItem = groundItems.indexOf(oldItem);
		groundItems.remove(indexOfItem);	
	}
}