package skills;

import processes.Command;
import processes.CommandProcess;
import processes.DefenceLength;
import processes.Mob;
import processes.Mobiles;
import processes.Player;
import processes.PlayerPrompt;
import processes.SendMessage;
import processes.UsefulCommands;

public class Punch extends Skill implements Command {
	
	public final int DAMAGE = 8;
	public final int BALANCELENGTH = 3000;

	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		System.out.println("doPunch accessed.");
		Mobiles currentPlayer = playerPrompt.getCurrentPlayer();
		SendMessage sendBack = playerPrompt.getSendBack();		
		Mobiles mob = obtainMobile(fullCommand);	
		if (skillCheck(mob, currentPlayer, sendBack)) {					
			doSkill(mob, currentPlayer, sendBack);
		} 
	}
	
	private void doSkill(Mobiles mob, Mobiles currentPlayer, SendMessage sendBack) {
		sendBack.printMessage("You punch " + mob.getName() + ".");
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
		UsefulCommands.getPlayerPromptFromPlayer(player).getSendBack().printMessage(currentPlayer.getName() + " punches you.");
		UsefulCommands.displayPrompt(player);
	}
}
