package skills;

import processes.Command;
import processes.Location;
import processes.Mobiles;
import processes.Player;
import processes.PlayerPrompt;
import processes.UsefulCommands;

public class Look implements Command {

	@Override
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		Mobiles currentPlayer = playerPrompt.getCurrentPlayer();
		String direction = UsefulCommands.getSecondWord(fullCommand);
		Location lookLocation = (Location) currentPlayer.getMobLocation();
		if (!direction.equals("")) {
			lookLocation = lookLocation.getLocation(direction);
		} 
		if (lookLocation != null) {
			lookLocation.look(playerPrompt.getSendBack(), currentPlayer.getName());
		} else {
			playerPrompt.getSendBack().printMessage("You can't look through walls!");
		}
	}

}
