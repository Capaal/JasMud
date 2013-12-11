package skills;

import interfaces.Holdable;
import interfaces.Item;

import processes.*;

public class Drop implements Command {
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		String toDrop = UsefulCommands.returnTarget(fullCommand).toLowerCase();
		StdMob currentPlayer = playerPrompt.getCurrentPlayer();
		Location thisLocation = (Location) currentPlayer.getMobLocation();

		Holdable item = UsefulCommands.stringToHoldable(toDrop, currentPlayer);
		
		// change String input to Item removing
		// remove item from inventory
		if (item!=null && item instanceof Item) {
			currentPlayer.removeItem(item);
			thisLocation.acceptItem(item);
			currentPlayer.tell("You drop a " + item.getName().toLowerCase() + ".");
		} else {
			currentPlayer.tell("You are not holding that item.");
		}
		// where should it check if the item should fall from a flying location?
		

		
		
	}
}
