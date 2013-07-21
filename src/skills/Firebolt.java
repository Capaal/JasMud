package skills;

import processes.*;

public class Firebolt extends Skill implements Command {
	
	public final int DAMAGE = 10;
	public final String TYPE = "magical";
	public final int BALANCELENGTH = 3200;
	
	// IDEA: Make a checkAttack() that checks for Balance?, if dead, if null, if right location, and prints proper message!!!!
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		System.out.println("doFirebolt accessed.");
		Mobiles currentPlayer = playerPrompt.getCurrentPlayer();
		SendMessage sendBack = playerPrompt.getSendBack();		
		Mobiles mob = obtainMobile(fullCommand);	
		if (skillCheck(mob, currentPlayer, sendBack)) {					
			doSkill(mob, currentPlayer, sendBack);
		} 	
	}	
	
	private void doSkill(Mobiles mob, Mobiles currentPlayer, SendMessage sendBack) {
		sendBack.printMessage("You firebolt " + mob.getName() + ".");
		mob.takeDamage(DAMAGE);	
	//	DefenceLength newDefence = new DefenceLength(currentPlayer, sendBack, "balance", BALANCELENGTH, 
	//			"You have regained your balance.");
	//	newDefence.start();
	//	mob.checkHp(sendBack, currentPlayer);	
		if (mob instanceof Mob) {
			mobExtras((Mob) mob, currentPlayer);
		} else {
			playerExtras((Mobiles) mob, currentPlayer);
		}					
	}
	
	private void playerExtras(Mobiles player, Mobiles currentPlayer) {
		UsefulCommands.getPlayerPromptFromPlayer(player).getSendBack().printSpace();
		UsefulCommands.getPlayerPromptFromPlayer(player).getSendBack().printMessage(currentPlayer.getName() + " shoots at firebolt at you.");
		UsefulCommands.displayPrompt(player);
	}
	
	
}
