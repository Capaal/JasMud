package skills;

import Interfaces.Holdable;

import processes.*;

public class Drop implements Command {
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		String toDrop = UsefulCommands.returnTarget(fullCommand).toLowerCase();
		StdMob currentPlayer = playerPrompt.getCurrentPlayer();
		Location thisLocation = (Location) currentPlayer.getMobLocation();
		Holdable tempItem;
		
		// change String input to Item removing
		// remove item from inventory
		for (int i=0; i<currentPlayer.inventory.size(); i++) {
			tempItem = currentPlayer.inventory.get(i);
			String tempItemName = tempItem.getName().toLowerCase();
			if (tempItemName.equals(toDrop) || (tempItemName + tempItem.getId()).equals(toDrop)) {
				currentPlayer.removeItem(tempItem);
				// place item in player's location
				currentPlayer.getMobLocation().acceptItem(tempItem);
				// display msg of dropping to all players in same location
				currentPlayer.tell("You drop " + tempItem.getName() + ".");
				break;
			} else {
				currentPlayer.tell("You are not holding that item.");
			}
		}				
		
		// where should it check if the item should fall from a flying location?
		

		
		
	}
}
