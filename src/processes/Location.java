package processes;
import java.io.*;
import java.util.*;

// If this file is edited, remember to comment out locationCollection load in WorldServer.

public class Location implements Serializable {
	
	static final long serialVersionUID= -8355031359961875724L; 
	
	protected String number;
	protected String name;
	protected String description;
	protected ArrayList<Item> groundItems = new ArrayList<Item>();
	protected String groundType;
	
	protected int[] exits = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
	protected int[] exitsOpp = new int[] {4, 5, 6, 7, 0, 1, 2, 3, 9, 8, 11, 10};
	
	protected int[] roomExits;
	protected String[] dirNames = new String[] {"north", "northeast", "east", "southeast", "south", 
		"southwest", "west", "northwest", "in", "out", "up", "down"};
	protected String[] dirAbbrev = new String[] {"n", "ne", "e", "se", "s", "sw", "w", "nw", "in", "out", "u", "d"};
	
	public Location() {
		roomExits = new int[12];
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
					sb.append(dirNames[i]);
					firstExit = true;
				}
				else {
					sb.append(", " + dirNames[i]);
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
		displayMob(number, sendBack);
		displayPeople(number, yourName, sendBack);
		sendBack.printMessage(UsefulCommands.ANSI.CYAN + displayExits() + UsefulCommands.ANSI.SANE);
		sendBack.printMessage("(God sight) Location number: " + number + ". Ground type: " + groundType + ".");
	}
	//Glance
	public void glance(SendMessage sendBack, String yourName) {
		sendBack.printMessage(UsefulCommands.ANSI.MAGENTA + name + UsefulCommands.ANSI.SANE);
		displayItems(sendBack);
		displayMob(number, sendBack);
		displayPeople(number, yourName, sendBack);
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
	public void displayPeople(String location, String yourName, SendMessage sendBack) {
		int myLocation = Integer.parseInt(location);		
		String toSay = "";
		StringBuffer sb = new StringBuffer();
		Iterator iter = WorldServer.activeClients.iterator(); //All possible players to be displayed are in activeClients
		boolean noOne = true;
		while (iter.hasNext()) {
			Player currentPlayer = ((PlayerPrompt) iter.next()).currentPlayer;
			try {
				if (currentPlayer.mobLocation == myLocation && !currentPlayer.name.equals(yourName)) {
					if (currentPlayer.dead == false) {
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
	public static void displayMob(String location, SendMessage sendBack) {
		StringBuffer sb = new StringBuffer();
		Set s = WorldServer.mobList.keySet();
		Iterator iter = s.iterator();
		while (iter.hasNext()) {
			Mobiles mm = WorldServer.mobList.get(iter.next());
			if (mm instanceof Mob) {
				Mob m = (Mob) mm;		
				if (m.mobLocation == Integer.parseInt(location) && m.id != -1 ) {
					if (m.dead == false) {
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
}