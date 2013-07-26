package skills;

import Interfaces.*;
import processes.*;

//Needs to be more generic, what if they are flying? WRONGTYPE is bad in that way.
//This could probably be fixed if the methods were re-done. They might be better broken up is some other way.
// Do we need to do "Can we move?" followed by "Ok, Lets move!"?
// PrintMovement is cool, but the "the" is a bit useless? Without it might be even more generic.

public class Move implements Command {
	
	final String LEAVEMSG = " leaves to the ";
	final String ENTERMSG = " enters from the ";
	final String FAILMSG = "You can't go that way.";
	final String WRONGTYPE = "water"; // Bad, waht about flying?
	final String WRONGTYPEMSG = "You'll need to swim to go that way."; // Bad like above.	
	
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {	
		Mobile currentPlayer = playerPrompt.getCurrentPlayer();
		String dir = UsefulCommands.getSecondWord(fullCommand);		
		if (dir == null) {
			dir = fullCommand;
		}	
		Container futureContainer = getFutureLocation(currentPlayer, dir);
		Container currentContainer = currentPlayer.getContainer();
		if (canMove(currentContainer, futureContainer, currentPlayer, dir)) {
			moveMob(currentPlayer, currentContainer, futureContainer, dir);// We already know container is a location.
		} 
	}
	
	protected Container getFutureLocation(Mobile currentPlayer, String dir) {
		Container currentLoc = currentPlayer.getContainer();
		if (!(currentLoc instanceof Location)) {
			currentPlayer.tell("You'll need to find another way out.");
			return null;
		}
		return ((Location)currentLoc).getLocation(dir);
	}
	
	protected boolean canMove(Container currentContainer, Container futureContainer, Mobile currentPlayer, String dir) {		
		Location location = ((Location)currentContainer);		
		Location futureLoc = (Location)futureContainer;
		if (futureLoc != null) { 
			if (futureLoc.getGroundType().equals(WRONGTYPE) || location.getGroundType().equals(WRONGTYPE)) {
				currentPlayer.tell(WRONGTYPEMSG);
				return false;
			}
		} else {
			currentPlayer.tell(FAILMSG);
			return false;
		}
		return true;
	}
	
	protected void moveMob(Mobile currentPlayer, Container currentContainer, Container futureContainer, String dir) {
		// Prints a message of movement (leaving) to anyone in the Player's pre-move location.		
		printMovement(LEAVEMSG, currentPlayer, currentContainer, dir);
		// Literally changes the players location.
		currentPlayer.setContainer(futureContainer);
		((Location)futureContainer).look(currentPlayer);
		// Prints a message of movement (entering) to those in the post-move location.
		printMovement(ENTERMSG, currentPlayer, futureContainer, dir);		
	}
	
	protected void printMovement(String movement, Mobile currentPlayer, Container location, String direction) {
		for (Holdable h : location.groundItems) {
			if ((h instanceof Mobile) && (!(h.getName() + h.getId()).equals(currentPlayer.getName() + currentPlayer.getId()))) {
				((Mobile) h).tell("\n" + currentPlayer.getName() + movement +  direction);
				UsefulCommands.displayPrompt((Mobile)h);
			}
		}			
	}

}
