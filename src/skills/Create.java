package skills;

import interfaces.Creatable;
import interfaces.Item;
import processes.Command;
import processes.Location;
import processes.PlayerPrompt;
import processes.UsefulCommands;
import processes.WorldServer;

public class Create implements Command {

	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		Location currentLocation = (Location) playerPrompt.getCurrentPlayer().getMobLocation();
		String itemToMake = UsefulCommands.returnTarget(fullCommand);
		if (WorldServer.possibleItems.containsKey(itemToMake)) {
			Creatable newItem = WorldServer.possibleItems.get(itemToMake);
			Creatable finalItem = newItem.create();
			finalItem.setContainer(currentLocation);
			currentLocation.acceptItem(finalItem);
		}
		

	}

}
