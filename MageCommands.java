import java.util.*;

public class MageCommands {
	


	//Test of new skill as object system. Syntax in game: firebolt
	public static void doFirebolt(String fullCommand, Player currentPlayer, SendMessage sendBack) {
		System.out.println("doFirebolt accessed.");
		String target = UsefulCommands.returnTarget(fullCommand);
		AllSkills skill = WorldServer.mageAbilities.get("firebolt");
		GeneralCommands.attackPlayer(target, "You firebolt " + target + ".", currentPlayer.name + 
				" firebolts you.", skill.dmg, skill.type, currentPlayer, sendBack, 3200);
	}
	
	public static void doIcebolt(String fullCommand, Player currentPlayer, SendMessage sendBack) {
		System.out.println("doIcebolt accessed.");
		String target = UsefulCommands.returnTarget(fullCommand);
		AllSkills skill = WorldServer.mageAbilities.get("icebolt");
		GeneralCommands.attackPlayer(target, "You firebolt " + target + ".", currentPlayer.name + 
				" firebolts you.", skill.dmg, skill.type, currentPlayer, sendBack, 3200);
	}

	
	public static void doHeals(String fullCommand, Player currentPlayer, SendMessage sendBack) {
		System.out.println("doHeals accessed.");
		String target = UsefulCommands.returnTarget(fullCommand);
		boolean noPlayer = true;
		if (!target.equals("")) {			
			Iterator iter = WorldServer.activeClients.iterator();
			while (iter.hasNext()) {
				PlayerPrompt t = (PlayerPrompt) iter.next();
				if (t.currentPlayer.name.toLowerCase().equals(target.toLowerCase())) {
					if (!target.toLowerCase().equals(currentPlayer.name)) {
						sendBack.printMessage("You place your hand on the shoulder of " + t.currentPlayer.name + 
								" and will healing energy through your hand.");
						t.sendBack.printMessage(currentPlayer.name + " puts their hand on your shoulder and heals your wounds.");
					} else {
						sendBack.printMessage("You concentrate momentarily, and infuse yourself with the essence of healing.");
					}
					if ((t.currentPlayer.currentHp + 10) > t.currentPlayer.maxHp) {
						t.currentPlayer.currentHp = t.currentPlayer.maxHp;
					} else {
						t.currentPlayer.currentHp = t.currentPlayer.currentHp + 10;
					}
					DefenceLength newDefence = new DefenceLength(currentPlayer, sendBack, "balance", 
							2400, "You have regained your balance.");
					newDefence.start();
					noPlayer = false;
					break;
				}
			}
		} else {
			sendBack.printMessage("You concentrate momentarily, and infuse yourself with the essence of healing.");
			if ((currentPlayer.currentHp + 10) > currentPlayer.maxHp) {
				currentPlayer.currentHp = currentPlayer.maxHp;
			} else {
				currentPlayer.currentHp = currentPlayer.currentHp + 10;
			}
			DefenceLength newDefence = new DefenceLength(currentPlayer, sendBack, "balance", 
					2400, "You have regained your balance.");
			newDefence.start();
			noPlayer = false;
		}
		if (noPlayer == true) {
			sendBack.printMessage("You detect nothing by that name here.");
		}
	}


	// Skill to put up a defence that lowers incoming physical damage.
	public static void doShield(Player currentPlayer, SendMessage sendBack) {
		System.out.println("doShield accessed.");
		sendBack.printMessage("You draw a small circle in the ground around yourself and release a burst " +
				"of energy to fill the void, creating a shield of protection.");
		DefenceLength newDefence = new DefenceLength(currentPlayer, sendBack, "shield", 
				20000, "The aura of dampening surrounding you melts away.");
		newDefence.start();
		DefenceLength newBalance = new DefenceLength(currentPlayer, sendBack, "balance", 
				2400, "You have regained your balance.");
		newBalance.start();
	}
	
	// Timed defence that lowers magical damage taken.
	public static void doAura(Player currentPlayer, SendMessage sendBack) {
		System.out.println("doAura accessed.");
		sendBack.printMessage("You concentrate, sending your internal energy externally, " +
				"creating a field of magic negation.");
		DefenceLength newDefence = new DefenceLength(currentPlayer, sendBack, "aura", 
				20000, "The field of negation surrounding you melts away.");
		newDefence.start();
		DefenceLength newBalance = new DefenceLength(currentPlayer, sendBack, "balance", 
				2400, "You have regained your balance.");
		newBalance.start();
	}

}