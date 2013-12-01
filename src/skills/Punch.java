package skills;

import processes.Command;


import processes.StdMob;
import processes.StdMob;
import processes.Player;
import processes.PlayerPrompt;
import processes.SendMessage;
import processes.UsefulCommands;

public class Punch extends Skill implements Command {
	
	public final int DAMAGE = 8;
	public final int BALANCELENGTH = 3000;

	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		System.out.println("doPunch accessed.");
		StdMob currentPlayer = playerPrompt.getCurrentPlayer();
		SendMessage sendBack = playerPrompt.getSendBack();		
		StdMob mob = obtainMobile(fullCommand);	
		if (skillCheck(mob, currentPlayer, sendBack)) {					
			doSkill(mob, currentPlayer, sendBack);
		} 
	}
	
	private void doSkill(StdMob mob, StdMob currentPlayer, SendMessage sendBack) {
		sendBack.printMessage("You punch " + mob.getName() + ".");
		mob.takeDamage(DAMAGE);	
	//	DefenceLength newDefence = new DefenceLength(currentPlayer, sendBack, "balance", BALANCELENGTH, 
	//			"You have regained your balance.");
	//	newDefence.start();
	//	mob.checkHp(sendBack, currentPlayer);	
		if (mob instanceof StdMob) {
			mobExtras((StdMob) mob, currentPlayer);
		} else {
			playerExtras((StdMob) mob, currentPlayer);
		}					
	}
	
	private void playerExtras(StdMob player, StdMob currentPlayer) {
		UsefulCommands.getPlayerPromptFromPlayer(player).getSendBack().printSpace();
		UsefulCommands.getPlayerPromptFromPlayer(player).getSendBack().printMessage(currentPlayer.getName() + " punches you.");
		UsefulCommands.displayPrompt(player);
	}
}
