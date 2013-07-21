package skills;

import java.util.Iterator;

import Interfaces.Holdable;

import processes.Command;
import items.Item;
import processes.Location;
import processes.Mobiles;
import processes.Player;
import processes.PlayerPrompt;
import processes.UsefulCommands;
import processes.WorldServer;

public class Get implements Command {

	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		String toGet = UsefulCommands.returnTarget(fullCommand);
		Mobiles currentPlayer = playerPrompt.getCurrentPlayer();
		Location thisLocation = currentPlayer.getMobLocation();
		
		boolean success = false;
		int i = 0;
		while (i < thisLocation.groundItems.size() && success == false) {
			Holdable posItem = thisLocation.groundItems.get(i);
			String posItemName = posItem.getName();
			if (posItemName.equals(toGet) || (posItemName + posItem.getId()).equals(toGet)) {
				// check if item is get-able, if player is allowed to get item - currently assuming Holdable=Get-able
				// remove item from location
				thisLocation.removeItemFromLocation(posItem);
				// add item to player inventory
				// Container needs removeItem method
				currentPlayer.acceptItem(posItem);
				currentPlayer.tell("You pick up a dagger.");
				success = true;
			}
		} 
		
		// display msg of picking up item to all players in same location
		
		// location should know which players are in it
		
	}	
		
}

