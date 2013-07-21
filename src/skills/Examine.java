package skills;

import java.util.Iterator;

import Interfaces.Holdable;
import Interfaces.Item;

import processes.Command;
import processes.Location;
import processes.Mobiles;
import processes.Player;
import processes.PlayerPrompt;
import processes.UsefulCommands;
import processes.WorldServer;

public class Examine implements Command {

	@Override
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		String toExamine = UsefulCommands.returnTarget(fullCommand);

		
		boolean success = false;
		Mobiles currentPlayer = playerPrompt.getCurrentPlayer();
		Location thisLocation = (Location) currentPlayer.getMobLocation();
		int i = 0;
		while (i < thisLocation.groundItems.size() && success == false) {
			Holdable posItem = thisLocation.groundItems.get(i);
			String posItemName = posItem.getName();
			if (posItemName.equals(toExamine) || (posItemName + posItem.getId()).equals(toExamine)) {
				playerPrompt.getSendBack().printMessage(posItem.getDescription());
				success = true;
			}
		} 
		
		
		String toSay = "";
		
		Iterator iter = WorldServer.activeClients.iterator(); //All possible players to be displayed are in activeClients
		
		while (iter.hasNext() && success == false) {
			Mobiles possiblePlayer = ((PlayerPrompt) iter.next()).getCurrentPlayer();
			try {
				if (possiblePlayer.getMobLocation() == thisLocation) {
					toSay = possiblePlayer.getShortDescription();
					
					success = true;
				}
			} catch (NullPointerException ie) {
				System.out.println("Oh snap!");
			}	
		}		
		
		if (!toSay.equals("")) {
			playerPrompt.getSendBack().printMessage(toSay);
		}				
	}
}
