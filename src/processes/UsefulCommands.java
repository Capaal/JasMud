package processes;
import java.util.*;

import Interfaces.*;

public class UsefulCommands {
	// Converts a single incoming word so that first letter is capitalized.
	public static String firstToCap(String str) {
		StringBuffer sb2 = new StringBuffer();
		int i;
		char firstch = str.charAt(0);
		sb2.append(Character.toUpperCase(firstch));
		for (i = 1;  i < str.length();  i++) {
	     	char ch = str.charAt(i);
			sb2.append(ch);
		}
		return sb2.toString();
	}
	// Converts incoming word so that only numbers are returned.
	public static String getOnlyNumerics(String str) {
   	if (str == null) {
   		return null;
   	}
		StringBuffer strBuff = new StringBuffer();
	   char c;
	   for (int i = 0; i < str.length() ; i++) {
		   c = str.charAt(i);
 			if (Character.isDigit(c)) {
   			strBuff.append(c);
   		}
  		}
  		return strBuff.toString();
	}
	
	public static String getFirstWord(String fullCommand) {
		StringTokenizer st = new StringTokenizer(fullCommand);
		String command = st.nextToken();
		return command;
	}
	
	public static String getSecondWord(String fullCommand) {
		StringTokenizer st = new StringTokenizer(fullCommand);
		st.nextToken();
		if (st.hasMoreTokens()) {
			return (st.nextToken()).toString();
		} else {
			return null;
		}
	}
	
	// Converts incoming word so that numbers are stripped out and only letters are returned.
	public static String getOnlyStrings(String str) {
		if (str == null) {
			return null;
		}
		StringBuffer strBuff = new StringBuffer();
		char c;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if (!Character.isDigit(c)) {
				strBuff.append(c);
			}
		}
		return strBuff.toString();
	}
	// Returns a set stringTokenizer.
	public static StringTokenizer getST(String fullCommand) {
		StringTokenizer st = new StringTokenizer(fullCommand);
		st.nextToken();
		return st;
	}
	
	// Removes first word of command, returns rest of command.
	public static String returnTarget(String fullCommand) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(fullCommand);
		st.nextToken();
		if (st.hasMoreTokens()) {
			sb.append(st.nextToken());
		} else {
			return "";
		}
		while (st.hasMoreTokens()) {
			sb.append(" " + st.nextToken());
		} 
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
	
	// Gets player prompt (Give all player prompts an ID that represents that connection?)
	public static PlayerPrompt getPlayerPromptFromPlayer(Mobile currentPlayer) {
		Iterator iter = WorldServer.activeClients.iterator();
		while (iter.hasNext()) {
			PlayerPrompt t = (PlayerPrompt) iter.next();
			if (t.getCurrentPlayer().getName().equals(currentPlayer.getName())) {
				return t;
			}
		}	
		return null;
	}
	
	public final class ANSI {

		public static final String SANE = "\u001B[0m";
		public static final String HIGH_INTENSITY = "\u001B[1m";
		public static final String LOW_INTESITY = "\u001B[2m";
		
		public static final String ITALIC = "\u001B[3m";
		public static final String UNDERLINE = "\u001B[4m";
		public static final String BLINK = "\u001B[5m";
		public static final String RAPID_BLINK = "\u001B[6m";
		public static final String REVERSE_VIDEO = "\u001B[7m";
		public static final String INVISIBLE_TEXT = "\u001B[8m";

		public static final String BLACK = "\u001B[30m";
		public static final String RED = "\u001B[31m";
		public static final String GREEN = "\u001B[32m";
		public static final String YELLOW = "\u001B[33m";
		public static final String BLUE = "\u001B[34m";
		public static final String MAGENTA = "\u001B[35m";
		public static final String CYAN = "\u001B[36m";
		public static final String WHITE = "\u001B[37m";

		public static final String BACKGROUND_BLACK = "\u001B[40m";
		public static final String BACKGROUND_RED = "\u001B[41m";
		public static final String BACKGROUND_GREEN = "\u001B[42m";
		public static final String BACKGROUND_YELLOW = "\u001B[43m";
		public static final String BACKGROUND_BLUE = "\u001B[44m";
		public static final String BACKGROUND_MAGENTA = "\u001B[45m";
		public static final String BACKGROUND_CYAN = "\u001B[46m";
		public static final String BACKGROUND_WHITE = "\u001B[47m";

		private ANSI() {} // disable automatic constructor
	}
	
	public static void displayPrompt(Mobile currentPlayer) {
		String balance = "b";
		if (!currentPlayer.hasBalance()) {
			balance = "-";
		}
		currentPlayer.tellLine(currentPlayer.getCurrentHp() + "/" + currentPlayer.getMaxHp() + " " + balance + ": ");
	}
	
	//Obsolete?
	public static int getPosID(int i) {
		boolean fail = false;
		int posID = (i);
		for (int j = 0; j < WorldServer.allItems.size(); j++) {
			if (WorldServer.allItems.get(j).getId() == posID) {
				posID = getPosID(posID+1);
				fail = true;
				break;
			}
		}
		if (fail == false) {
			return posID;
		}
	return posID;
	}
	
	// string, container to holdable
	public static Holdable stringToHoldable(String item, Container place) {	
		Holdable tempItem;
		
		for (int i=0; i<place.getInventory().size(); i++) {
			tempItem = place.getInventory().get(i);
			String tempItemName = tempItem.getName().toLowerCase();
			if (tempItemName.equals(item) || (tempItemName + tempItem.getId()).equals(item)) {
				return tempItem;
			}
		}
		return null;
			
	}
	
	
	/* Obsolete
	public static void displayToAllLocation(Location location, String message) {
		Iterator iter = WorldServer.activeClients.iterator();
		while (iter.hasNext()) {
			PlayerPrompt t = (PlayerPrompt) iter.next();
			if (t.currentPlayer.getMobLocation() == location) {
				t.sendBack.printSpace();
				t.sendBack.printMessage(message);
				displayPrompt(t.currentPlayer);
	//			String balance = "b";
	//			if (t.currentPlayer.balance == false) {
	//				balance = "-";
	//			}
	//			t.sendBack.printMessageLine(t.currentPlayer.currentHp + "/" + t.currentPlayer.maxHp + 
	//					" " + balance + ": ");
			}				
		}
	}	*/
}
