package skills;

import java.util.*;
import Interfaces.*;
import processes.*;

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
	protected boolean detGroundType(Container thisLocation, Mobile currentPlayer, String fullCommand) {
		if (!(thisLocation instanceof Location)) {
			currentPlayer.tell("You'll need to find a different way out.");
			return false;
		}
		Location futureLoc = ((Location)thisLocation).getLocation(UsefulCommands.getSecondWord(fullCommand));
		if (futureLoc == null) {
			currentPlayer.tell("You can't go that way, let alone swim there.");
			return false;
		}
		if (((Location) thisLocation).getGroundType().equals("land") && (futureLoc.getGroundType().equals("land"))) {
			currentPlayer.tell("You flop onto the ground and flail wildly, but don't seem to make much progress.");
			return false;
		}
		return true;
	}
	
	@Override
	protected void moveMob(Mobile currentPlayer, Location thisLocation, String fullCommand) {
		Location futureLoc = thisLocation.getLocation(UsefulCommands.getSecondWord(fullCommand));
		if (futureLoc != null) {
			// Prints a message of movement (leaving) to anyone in the Player's pre-move location.		
			printMovement(LEAVEMSG, currentPlayer, thisLocation, 
					fullCommand);
			// Literally changes the players location.
			currentPlayer.setContainer(futureLoc);
			futureLoc.look(currentPlayer);
			// Prints a message of movement (entering) to those in the post-move location.
			printMovement(ENTERMSG, currentPlayer, futureLoc, fullCommand);
		}
	}
	//Finally moves mob
//	public void moveMob(SendMessage sendBack, String fullCommand, Player currentPlayer, 
//		Location thisLocation, int commandNum) {
//		super.moveMob(sendBack, currentPlayer, thisLocation, commandNum, this.futureLoc);
//	}

}