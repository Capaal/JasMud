package skills;

import java.util.Iterator;

import Interfaces.*;
import processes.*;

public class Move implements Command {
	
	final String LEAVEMSG = " leaves to ";
	final String ENTERMSG = " enters from ";
	
	
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {		
		
		Mobile currentPlayer = playerPrompt.getCurrentPlayer();
		if (detGroundType(currentPlayer.getContainer(), currentPlayer, fullCommand)) {
			moveMob(currentPlayer, ((Location)currentPlayer.getContainer()), fullCommand);// We already know container is a location.
		} 
	}
	
	protected boolean detGroundType(Container container, Mobile currentPlayer, String fullCommand) {
		if (!(container instanceof Location)) {
			currentPlayer.tell("You'll need to find another way out.");
			return false;
		}
		Location location = ((Location)container);
		String dir = fullCommand;
		Location futureLoc = location.getLocation(dir);
		if (futureLoc != null) { 
			if (futureLoc.getGroundType().equals("water") || ((Location)container).getGroundType().equals("water")) {
				currentPlayer.tell("You'll need to swim to go that way.");
				return false;
			}
		} else {
			currentPlayer.tell("You can't go that way.");
			return false;
		}
		return true;
	}
	
	protected void moveMob(Mobile currentPlayer, Location currentLocation, String fullCommand) {
		Location futureLoc = currentLocation.getLocation(fullCommand);// Doesn't work if they type "Move North"?
		// Prints a message of movement (leaving) to anyone in the Player's pre-move location.		
		printMovement(LEAVEMSG, currentPlayer, currentLocation, 
				fullCommand);
		// Literally changes the players location.
		currentPlayer.setContainer(futureLoc);
		futureLoc.look(currentPlayer);
		// Prints a message of movement (entering) to those in the post-move location.
		printMovement(ENTERMSG, currentPlayer, futureLoc, fullCommand);
		
	}
	
	protected void printMovement(String movement, Mobile currentPlayer, Location location, String direction) {
		for (Holdable h : location.groundItems) {
			if ((h instanceof Mobile) && (!(h.getName() + h.getId()).equals(currentPlayer.getName() + currentPlayer.getId()))) {
				((Mobile) h).tell("\n" + currentPlayer.getName() + movement + " the " +  direction);
				UsefulCommands.displayPrompt((Mobile)h);
			}
		}			
	}

}
