package skills;

import interfaces.Holdable;
import interfaces.Item;
import processes.*;

public class Throw implements Command {

	@Override
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		String toThrow = UsefulCommands.getFirstWord(UsefulCommands.returnTarget(fullCommand)).toLowerCase();
		String dir = UsefulCommands.getSecondWord(UsefulCommands.returnTarget(fullCommand)).toLowerCase();
		StdMob currentPlayer = playerPrompt.getCurrentPlayer();
		Location thisLocation = (Location) currentPlayer.getMobLocation();

		
		Holdable item = UsefulCommands.stringToHoldable(toThrow, currentPlayer);
		
		if (item!=null && item instanceof Item) {
			currentPlayer.removeItem(item);
			thisLocation.getLocation(dir).acceptItem(item);
			currentPlayer.tell("You throw " + item.getShortDescription().toLowerCase() + " to the " + UsefulCommands.getDirName(dir) + ".");
		} else {
			currentPlayer.tell("You are not holding that item.");
		}

	}

}
