package processes;


import java.awt.Point;
import java.io.*;
import java.util.*;

import Interfaces.Container;
import Interfaces.Holdable;
import Interfaces.Item;

// Mobiles should be remembered in the location, for look and stuff, they can go with items and still work atm.
public class Location implements Container {
		
	private int id;
	private String name;
	private String description;
	public ArrayList<Holdable> groundItems = new ArrayList<Holdable>();
	private String groundType;
	private Location[] locations;
	public Point point;
		
	public final static int[] EXITS = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
	public final static int[] EXITSOPP = new int[] {3, 5, 6, 0, 7, 1, 2, 4, 9, 8, 11, 10};	
	public static final String[] DIRNAMES = new String[] {"north", "northeast", "east", "south", "southeast", 
		"southwest", "west", "northwest", "in", "out", "up", "down"};
	public static final String[] DIRABBREV = new String[] {"n", "ne", "e", "s", "se", "sw", "w", "nw", "in", "out", "u", "d"};
	
	
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
		private Location[] locations = new Location[12];
		private boolean[] locationConnections = new boolean[12];
		
		public Builder(int id) {
			if (WorldServer.locationCollection.containsKey(id)) {
				throw new IllegalStateException("A location of the id already exists.");
			}
			this.id = id;
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
		
		public Builder north(int id, boolean connected) {
			buildDirections(id, connected, 0);
			return this;
		}	
		
		public Builder northEast(int id, boolean connected) {
			buildDirections(id, connected, 1);
			return this;
		}	
		
		public Builder east(int id, boolean connected) {
			buildDirections(id, connected, 2);
			return this;
		}		
		
		public Builder south(int id, boolean connected) {
			buildDirections(id, connected, 3);
			return this;
		}	
		
		public Builder southEast(int id, boolean connected) {
			buildDirections(id, connected, 4);
			return this;
		}
		
		public Builder southWest(int id, boolean connected) {
			buildDirections(id, connected, 5);
			return this;
		}	
		
		public Builder west(int id, boolean connected) {
			buildDirections(id, connected, 6);
			return this;
		}	
		
		public Builder northWest(int id, boolean connected) {
			buildDirections(id, connected, 7);
			return this;
		}	
		
		public Builder up(int id, boolean connected) {
			buildDirections(id, connected, 8);
			return this;
		}	
		
		public Builder down(int id, boolean connected) {
			buildDirections(id, connected, 9);
			return this;
		}	
		
		public Builder in(int id, boolean connected) {
			buildDirections(id, connected, 10);
			return this;
		}	
		
		public Builder out(int id, boolean connected) {
			buildDirections(id, connected, 11);
			return this;
		}	
		
		
		private Builder buildDirections(int id, boolean connected, int dir) {
			if (WorldServer.locationCollection.containsKey(id)) {
				Location loc = WorldServer.locationCollection.get(id);
				locations[dir] = loc;			
			} else {
				locations[dir] = null;
			}
			locationConnections[dir] = connected;
			return this;
		}
		
		public Location build() {
			return new Location(this);
		}
	}
	
	public Location(Builder builder) {
		locations = new Location[12];
		this.id = builder.id;
		setName(builder.name);
		setDescription(builder.description);
		setGroundType(builder.groundType);
		for (int i = 0; i <= 11; i++) {
			locations[i] = builder.locations[i];
			if (builder.locationConnections[i] && locations[i] != null) {
				Location anotherLocation = getLocation(i);
				anotherLocation.setLocation(this, Location.EXITSOPP[i]);
			}
		}
		WorldServer.locationCollection.put(this.id, this);
	}
		
	//Displays exits
	public String displayExits() {
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
		return toSay;
	}
	//Looks
	public void look(SendMessage sendBack, String yourName) {
		sendBack.printMessage(UsefulCommands.ANSI.MAGENTA + name + UsefulCommands.ANSI.SANE);
		sendBack.printMessage(UsefulCommands.ANSI.GREEN + description + UsefulCommands.ANSI.SANE);
		displayItems(sendBack);
		displayMob(sendBack, yourName);
		displayPeople(yourName, sendBack);
		sendBack.printMessage(UsefulCommands.ANSI.CYAN + displayExits() + UsefulCommands.ANSI.SANE);
		sendBack.printMessage("(God sight) Location number: " + id + ". Ground type: " + groundType + ".");
	}
	//Glance
	public void glance(SendMessage sendBack, String yourName) {
		sendBack.printMessage(UsefulCommands.ANSI.MAGENTA + name + UsefulCommands.ANSI.SANE);
		displayItems(sendBack);
		displayMob(sendBack, yourName);
		displayPeople(yourName, sendBack);
		sendBack.printMessage(UsefulCommands.ANSI.CYAN + displayExits() + UsefulCommands.ANSI.SANE);
		sendBack.printMessage("(God sight) Location number: " + id + ". Ground type: " + groundType + ".");
	}	
	//Location description
	public void displayLocation(SendMessage sendBack) {
		sendBack.printMessage(name);
		sendBack.printMessage(description);
		sendBack.printMessage(displayExits());
	}
	//Displays people
	private void displayPeople(String yourName, SendMessage sendBack) {		
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
	}
	//Displays mobs
	private void displayMob(SendMessage sendBack, String yourName) {
		StringBuffer sb = new StringBuffer();
		Set s = WorldServer.mobList.keySet();
		Iterator iter = s.iterator();
		while (iter.hasNext()) {
			Mobiles mm = WorldServer.mobList.get(iter.next());
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
	}
	//Displays items
	public void displayItems(SendMessage sendBack) {
		if (groundItems.size() != 0) {
			sendBack.printMessageLine("On the ground lies: ");
			for (int i = 0; i < groundItems.size(); i++) {		
				sendBack.printMessageLine(UsefulCommands.ANSI.BLUE + groundItems.get(i).getName() + 
					". " + UsefulCommands.ANSI.SANE);
			}
			sendBack.printMessage("");
		}
	}
	
	public void setLocation(int locId, int dir) {
		Location loc = WorldServer.locationCollection.get(locId);
		this.locations[dir] = loc;
	}
	
	public void setLocation(Location loc, int dir) {
		this.locations[dir] = loc;
	}
		
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
	
	public Location getLocation(int dir) {
		return locations[dir];
	}
	
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
	}
	
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