package skills;

import Interfaces.Creatable;
import items.Item;
import processes.Command;
import processes.Location;
import processes.PlayerPrompt;
import processes.UsefulCommands;
import processes.WorldServer;

public class Create implements Command {

	@Override
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		Location currentLocation = playerPrompt.getCurrentPlayer().getMobLocation();
		String itemToMake = UsefulCommands.returnTarget(fullCommand);
		if (WorldServer.possibleItems.containsKey(itemToMake)) {
			Creatable newItem = WorldServer.possibleItems.get(itemToMake);
			Creatable finalItem = newItem.create();
			finalItem.setItemLocation(currentLocation);
			currentLocation.acceptItem(finalItem);
		}
		

	}

}
