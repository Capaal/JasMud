package skills;

import items.StdItem;
import Interfaces.*;
import processes.Command;
import processes.PlayerPrompt;
import processes.StdMob;

public class Stab implements Command {
	
	public static String defaultName = "stab";

	@Override
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		// TODO Auto-generated method stub
		
		//Check if wielded weapon's itemCommands contains "stab"
		//Then stab.
		boolean canStab = false;
		StdMob player = playerPrompt.getCurrentPlayer();
		for (Holdable i : player.inventory) {
			if ((i instanceof StdItem) && ((StdItem)i).itemCommands.contains("stab")) {
				canStab = true;
				break;
			}
		}
		if (canStab) {
			System.out.println("Stab activated");
		}
	}

}
