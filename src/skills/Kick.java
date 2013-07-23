package skills;

import processes.Command;
import processes.DefenceLength;
import processes.StdMob;
import processes.StdMob;
import processes.Player;
import processes.PlayerPrompt;
import processes.SendMessage;
import processes.UsefulCommands;

public class Kick extends Skill implements Command {

	public final int DAMAGE = 12;
	public final int BALANCELENGTH = 3400;

	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		System.out.println("doKick accessed.");
		StdMob currentPlayer = playerPrompt.getCurrentPlayer();
		SendMessage sendBack = playerPrompt.getSendBack();		
		StdMob mob = obtainMobile(fullCommand);	
		if (skillCheck(mob, currentPlayer, sendBack)) {					
			doSkill(mob, currentPlayer, sendBack);
		} 
	}
	
	private void doSkill(StdMob mob, StdMob currentPlayer, SendMessage sendBack) {
		sendBack.printMessage("You kick " + mob.getName() + ".");
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
		UsefulCommands.getPlayerPromptFromPlayer(player).getSendBack().printMessage(currentPlayer.getName() + " kicks you.");
		UsefulCommands.displayPrompt(player);
	}
}