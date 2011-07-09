import java.util.*;
import java.io.*;

public class Move {

	static String leaveMSG = " leaves to";
	static String enterMSG = " enters from";
	
	public static Location detFutureLoc(Location thisLocation, int commandNum) {
		Location futureLoc = null;
		if (thisLocation.roomExits[commandNum] != 0) { 
			futureLoc = WorldServer.locationCollection.get(thisLocation.roomExits[commandNum]);
			return futureLoc;
		}
		return futureLoc;
	}

	public static boolean detGroundType(Location thisLocation, SendMessage sendBack, int commandNum) {
		if (thisLocation.roomExits[commandNum] != 0) { 
			Location futureLoc = WorldServer.locationCollection.get(thisLocation.roomExits[commandNum]);
			String groundType = futureLoc.groundType;
			if (futureLoc.groundType.equals("water") || thisLocation.groundType.equals("water")) {
				sendBack.printMessage("You'll need to swim to go that way.");
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
	
	public static void moveMob(SendMessage sendBack, Player currentPlayer, Location thisLocation, int commandNum) {
		Location futureLoc = detFutureLoc(thisLocation, commandNum);
		if (futureLoc != null) {
			// Prints a message of movement (leaving) to anyone in the Player's pre-move location.		
			printMovement(leaveMSG, currentPlayer, 
					thisLocation.dirNames[thisLocation.exits[commandNum]]);
			// Literally changes the players location.
			currentPlayer.mobLocation = thisLocation.roomExits[commandNum];
			futureLoc.look(sendBack, currentPlayer.name);
			// Prints a message of movement (entering) to those in the post-move location.
			printMovement(enterMSG, currentPlayer, thisLocation.dirNames[thisLocation.exitsOpp[commandNum]]);
		}
	}
	
	public static void printMovement(String movement, Player cPlayer, String direction) {
		Iterator iter = WorldServer.activeClients.iterator();
		while (iter.hasNext()) {
			PlayerPrompt t = (PlayerPrompt) iter.next();
			if (t.currentPlayer.mobLocation == cPlayer.mobLocation && !t.currentPlayer.name.equals(cPlayer.name)) {
				t.sendBack.printMessage("\n" + cPlayer.name + movement + " the " +  direction);
				UsefulCommands.displayPrompt(t.currentPlayer);
			}
		}	
	}

}