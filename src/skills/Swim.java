package skills;

import java.util.*;
import processes.Command;
import processes.Location;
import processes.Mobiles;
import processes.Player;
import processes.PlayerPrompt;
import processes.SendMessage;
import processes.UsefulCommands;

public class Swim extends Move implements Command {
	
	final String LEAVEMSG = " swims to";
	final String ENTERMSG = " swims in from";
	
	
	//Pulls out direction of swim
/*	public static int detDir(Location thisLocation, String fullCommand, int commandNum) {
		for (int i = 0; i < 12; i++) { 
			if (thisLocation.DIRNAMES[i].toLowerCase().equals(UsefulCommands.returnTarget(fullCommand)) || 
					thisLocation.dirAbbrev[i].toLowerCase().equals(UsefulCommands.returnTarget(fullCommand))) {
				commandNum = i;
//		  		setFutureLoc(WorldServer.locationCollection.get(thisLocation.roomExits[commandNum]));
			}
		}
		return commandNum;
	}*/
	
	
	@Override
	protected boolean detGroundType(Location thisLocation, SendMessage sendBack, String fullCommand) {
		Location futureLoc = thisLocation.getLocation(UsefulCommands.getSecondWord(fullCommand));
		if (futureLoc == null) {
			sendBack.printMessage("You can't go that way, let alone swim there.");
			return false;
		}
		if (thisLocation.getGroundType().equals("land") && (futureLoc.getGroundType().equals("land"))) {
			sendBack.printMessage("You flop onto the ground and flail wildly, but don't seem to make much progress.");
			return false;
		}
		return true;
	}
	
	@Override
	protected void moveMob(SendMessage sendBack, Mobiles currentPlayer, Location thisLocation, String fullCommand) {
		Location futureLoc = thisLocation.getLocation(UsefulCommands.getSecondWord(fullCommand));
		if (futureLoc != null) {
			// Prints a message of movement (leaving) to anyone in the Player's pre-move location.		
			printMovement(LEAVEMSG, currentPlayer, 
					fullCommand);
			// Literally changes the players location.
			currentPlayer.setMobLocation(futureLoc);
			futureLoc.look(sendBack, currentPlayer.getName());
			// Prints a message of movement (entering) to those in the post-move location.
			printMovement(ENTERMSG, currentPlayer, fullCommand);
		}
	}
	//Finally moves mob
//	public void moveMob(SendMessage sendBack, String fullCommand, Player currentPlayer, 
//		Location thisLocation, int commandNum) {
//		super.moveMob(sendBack, currentPlayer, thisLocation, commandNum, this.futureLoc);
//	}

}