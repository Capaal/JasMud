package skills;

import java.util.Iterator;

import Interfaces.Container;
import processes.Command;
import processes.Location;
import processes.Mobiles;
import processes.Player;
import processes.PlayerPrompt;
import processes.SendMessage;
import processes.UsefulCommands;
import processes.WorldServer;

public class Move implements Command {
	
	final String LEAVEMSG = " leaves to ";
	final String ENTERMSG = " enters from ";
	
	
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {		
		
		Mobiles currentPlayer = playerPrompt.getCurrentPlayer();
		SendMessage sendBack = playerPrompt.getSendBack();
		if (detGroundType(currentPlayer.getMobLocation(), sendBack, fullCommand)) {
			moveMob(sendBack, currentPlayer, currentPlayer.getMobLocation(), fullCommand);
		} else/* if (Move.detFutureLoc(thisLocation, commandNum) == null)*/ {
			sendBack.printMessage("You can't go that way.");
		}
	}
	
	protected boolean detGroundType(Container container, SendMessage sendBack, String fullCommand) {
		String dir = fullCommand;
		Location futureLoc = (((Location) container).getLocation(dir));
		if (futureLoc != null) { 
			if (futureLoc.getGroundType().equals("water") || ((Location)container).getGroundType().equals("water")) {
				sendBack.printMessage("You'll need to swim to go that way.");
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
	
	protected void moveMob(SendMessage sendBack, Mobiles currentPlayer, Container container, String fullCommand) {
		Location futureLoc = ((Location)container).getLocation(fullCommand);
		if (futureLoc != null) {
			// Prints a message of movement (leaving) to anyone in the Player's pre-move location.		
			printMovement(LEAVEMSG, currentPlayer, 
					fullCommand);
			// Literally changes the players location.
			currentPlayer.setMobLocation(((Location)container).getLocation(fullCommand));
			futureLoc.look(sendBack, currentPlayer.getName());
			// Prints a message of movement (entering) to those in the post-move location.
			printMovement(ENTERMSG, currentPlayer, fullCommand);
		}
	}
	
	protected void printMovement(String movement, Mobiles currentPlayer, String direction) {
		Iterator iter = WorldServer.activeClients.iterator();
		while (iter.hasNext()) {
			PlayerPrompt t = (PlayerPrompt) iter.next();
			if (t.getCurrentPlayer().getMobLocation() == currentPlayer.getMobLocation() && !t.getCurrentPlayer().getName().equals(currentPlayer.getName())) {
				t.getSendBack().printMessage("\n" + currentPlayer.getName() + movement + " the " +  direction);
				UsefulCommands.displayPrompt(t.getCurrentPlayer());
			}
		}	
	}

}
