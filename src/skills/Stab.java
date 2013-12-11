package skills;

import java.util.Random;

import effects.Bleed;

import interfaces.*;
import items.StdItem;
import processes.*;

public class Stab implements Command {
	
	public static String defaultName = "stab";

	@Override
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		// TODO Auto-generated method stub
		
		//Check if wielded weapon's itemCommands contains "stab"
		//Then stab.
		StdItem weapon = null ;
		Mobile player = playerPrompt.getCurrentPlayer();
		for (Holdable h : player.getInventory()) {
			if ((h instanceof StdItem) && ((StdItem)h).hasCommand("stab")) {
				weapon = (StdItem) h;
				break;
			}
		}
		if (weapon != null) {
			Holdable target = UsefulCommands.stringToHoldable(UsefulCommands.returnTarget(fullCommand), playerPrompt.getCurrentPlayer().getContainer());
			if (target instanceof Mobile) {
				int damage = (int) (player.getBaseDamage() * weapon.getPhysicalMult() * 2.5);
				if (((Mobile)target).hasEffect("piercedefence")) {
					Effect pierceDefence = ((Mobile)target).getEffect("piercedefence");
						damage = (int) (damage * .50);
				}
				((Mobile) target).takeDamage(damage);
				((Mobile) target).addEffect("bleed", new Bleed(((Mobile)target), 4000));
				player.tell("You stab " + target.getName());
				((Mobile) target).tell(player.getName() + " stabs you!");
			}
		}
	}
}
