package skills;

import interfaces.*;

import java.util.Iterator;



import processes.Command;
import processes.Location;
import processes.StdMob;
import processes.Player;
import processes.PlayerPrompt;
import processes.UsefulCommands;
import processes.WorldServer;

public class Get implements Command {
	
	public String defaultName = "get";

	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		String toGet = UsefulCommands.returnTarget(fullCommand).toLowerCase();
		Mobile currentPlayer = playerPrompt.getCurrentPlayer();
		Location thisLocation = (Location) currentPlayer.getContainer();

		Holdable item = UsefulCommands.stringToHoldable(toGet, thisLocation);
		
		if (item!=null && item instanceof Item) {
			thisLocation.removeItemFromLocation(item);
			currentPlayer.acceptItem(item);
			currentPlayer.tell("You pick up a " + item.getShortDescription() + ".");
		} else {
			currentPlayer.tell("There is no such item here.");
		}
		
		// display msg of picking up item to all players in same location
		
		// location should know which players are in it
		
	}	
		
}

