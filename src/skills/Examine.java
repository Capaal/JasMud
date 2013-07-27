package skills;

import java.util.Iterator;

import Interfaces.Holdable;
import Interfaces.Item;

import processes.Command;
import processes.Location;
import processes.StdMob;
import processes.Player;
import processes.PlayerPrompt;
import processes.UsefulCommands;
import processes.WorldServer;

public class Examine implements Command {
	
	public static String defaultName = "examine";

	@Override
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		String toExamine = UsefulCommands.returnTarget(fullCommand);

		
		boolean success = false;
		StdMob currentPlayer = playerPrompt.getCurrentPlayer();
		Location thisLocation = (Location) currentPlayer.getMobLocation();
		int i = 0;
		while (i < thisLocation.inventory.size() && success == false) {
			Holdable posItem = thisLocation.inventory.get(i);
			String posItemName = posItem.getName();
			if (posItemName.equals(toExamine) || (posItemName + posItem.getId()).equals(toExamine)) {
				currentPlayer.tell(posItem.getDescription());
				success = true;
			}
			i++;
		} 
		
		if (success == false) {
			currentPlayer.tell("You examine the space in front of you, it appears empty.");
		}
		
		
	//	String toSay = "";
		/*
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
		}	*/			
	}
}
