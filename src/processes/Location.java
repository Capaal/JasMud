package processes;
import java.io.*;
import java.util.*;

// If this file is edited, remember to comment out locationCollection load in WorldServer.

public class Location implements Serializable, Comparable {
	
	static final long serialVersionUID= -8355031359961875724L; 
	
	private int id;
	private String number;
	private String name;
	private String description;
	private ArrayList<Item> groundItems = new ArrayList<Item>();
	private String groundType;
	private Location[] locations;
	
	public static final int[] EXITS = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
	public static final int[] EXITSOPP = new int[] {4, 5, 6, 7, 0, 1, 2, 3, 9, 8, 11, 10};
	
	private int[] roomExits;
	public static final String[] DIRNAMES = new String[] {"north", "northeast", "east", "southeast", "south", 
		"southwest", "west", "northwest", "in", "out", "up", "down"};
	protected String[] dirAbbrev = new String[] {"n", "ne", "e", "se", "s", "sw", "w", "nw", "in", "out", "u", "d"};
	
	public Location() {
		locations = new Location[12];
		roomExits = new int[12];
	}
	
	public Location(int locNum, String name, String desc, String groundType, 
			int n, int ne, int e, int se, int s, int sw, int w, int nw, int up, int d, int in, int out) {
		locations = new Location[12];
		roomExits = new int[12];
		this.id = locNum;
		setName(name);
		directionsBlank(); // ????
		setDescription(desc);
		setGroundType(groundType);
		int[] tempExits = new int[] {n, ne, e, se, s, sw, w, nw, up, d, in, out};
		for (int i = 0; i <= 11; i++) {
			if (tempExits[i] != 0 && WorldServer.locationCollection.containsKey(tempExits[i])) {
				setLocation(tempExits[i], i);
			//	if (!temp.number.equals("1")) {
					Location anotherLocation = getLocation(i);
					anotherLocation.setLocation(this, Location.EXITSOPP[i]);
			//	}
			}
		}
		WorldServer.locationCollection.put(locNum, this);
	}
	
	public void directionsBlank() {
		for (int i = 0; i < 11; i++) {
			roomExits[i] = 0;
		}
	}
	//Displays exits
	public String displayExits() {
		String toSay = "You can see no exits.";
		StringBuffer sb = new StringBuffer();
		boolean firstExit = false;
		for (int i = 0; i < 11; i++) {
			if (roomExits[i] != 0) {
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
		displayMob(sendBack);
		displayPeople(yourName, sendBack);
		sendBack.printMessage(UsefulCommands.ANSI.CYAN + displayExits() + UsefulCommands.ANSI.SANE);
		sendBack.printMessage("(God sight) Location number: " + number + ". Ground type: " + groundType + ".");
	}
	//Glance
	public void glance(SendMessage sendBack, String yourName) {
		sendBack.printMessage(UsefulCommands.ANSI.MAGENTA + name + UsefulCommands.ANSI.SANE);
		displayItems(sendBack);
		displayMob(sendBack);
		displayPeople(yourName, sendBack);
		sendBack.printMessage(UsefulCommands.ANSI.CYAN + displayExits() + UsefulCommands.ANSI.SANE);
		sendBack.printMessage("(God sight) Location number: " + number + ". Ground type: " + groundType + ".");

	}
	//Location description
	public void displayLocation(SendMessage sendBack) {
		sendBack.printMessage(name);
		sendBack.printMessage(description);
		sendBack.printMessage(displayExits());
	}
	//Displays people
	private void displayPeople(String yourName, SendMessage sendBack) {		
		String toSay = "";
		StringBuffer sb = new StringBuffer();
		Iterator iter = WorldServer.activeClients.iterator(); //All possible players to be displayed are in activeClients
		boolean noOne = true;
		while (iter.hasNext()) {
			Player currentPlayer = ((PlayerPrompt) iter.next()).currentPlayer;
			try {
				if (currentPlayer.getMobLocation() == this && !currentPlayer.name.equals(yourName)) {
					if (currentPlayer.isDead()) {
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
		}
	}
	//Displays mobs
	private void displayMob(SendMessage sendBack) {
		StringBuffer sb = new StringBuffer();
		Set s = WorldServer.mobList.keySet();
		Iterator iter = s.iterator();
		while (iter.hasNext()) {
			Mobiles mm = WorldServer.mobList.get(iter.next());
			if (mm instanceof Mob) {
				Mob m = (Mob) mm;		
				if (m.getMobLocation() == this && m.id != -1 ) {
					if (m.isDead()) {
						sb.append("There is " + m.shortDescription + " here. ");
					} else if (m.dead == true) {
						sb.append("Lying on the ground is a dead " + m.name + ".");
					}
				}
			}
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
				sendBack.printMessageLine(UsefulCommands.ANSI.BLUE + groundItems.get(i).returnName() + 
					". " + UsefulCommands.ANSI.SANE);
			}
			sendBack.printMessage("");
		}
	}
	// NEEDS TO RETURN A COPY
	public int[] getRoomExits() {
		return roomExits;
	}
	
	public void setLocation(int locId, int dir) {
		Location loc = WorldServer.locationCollection.get(locId);
		this.locations[dir] = loc;
	}
	
	public void setLocation(Location loc, int dir) {
		this.locations[dir] = loc;
	}
	
	public int compareTo(Object other) {
		return (this.id - ((Location) other).getId());	
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
		for (int i = 0; i <= 11; i ++) {
			if (DIRNAMES[i].equals(dir)) {
				return getLocation(i);
			}
		}
		return null;
		
	}
	
	public String getGroundType() {
		return groundType;
	}
}