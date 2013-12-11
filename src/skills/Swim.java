package skills;

import interfaces.*;

import java.util.*;
import processes.*;

public class Swim extends Move implements Command {
	
	final String LEAVEMSG = " swims to the ";
	final String ENTERMSG = " swims in from the ";
	final String FAILMSG = "You can't go that way, let alone swim there.";
	final String WRONGTYPE = "land"; // Bad, waht about flying?
	final String WRONGTYPEMSG = "You flop onto the ground and flail wildly, but don't seem to make much progress."; // Bad like above
		
	@Override
	protected boolean canMove(Container currentContainer, Container futureContainer, Mobile currentPlayer, String dir) {
		if (futureContainer == null) {
			currentPlayer.tell(FAILMSG);
			return false;
		}
		if (((Location) currentContainer).getGroundType().equals(WRONGTYPE) && ((Location)futureContainer).getGroundType().equals(WRONGTYPE)) {
			currentPlayer.tell(WRONGTYPEMSG);
			return false;
		}
		return true;
	}
	
/*	@Override
	protected void moveMob(Mobile currentPlayer, Location thisLocation, String dir) {
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
	}*/
	//Finally moves mob
//	public void moveMob(SendMessage sendBack, String fullCommand, Player currentPlayer, 
//		Location thisLocation, int commandNum) {
//		super.moveMob(sendBack, currentPlayer, thisLocation, commandNum, this.futureLoc);
//	}

}