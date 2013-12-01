package skills;

import processes.Command;

import processes.StdMob;
import processes.Player;
import processes.PlayerPrompt;
import processes.SendMessage;

public class Hide extends Skill implements Command {
	
	private int BALANCELENGTH = 3200;

	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		System.out.println("doHide accessed.");
		StdMob currentPlayer = playerPrompt.getCurrentPlayer();
		SendMessage sendBack = playerPrompt.getSendBack();			
		if (skillCheck(currentPlayer, sendBack)) {					
			doSkill(currentPlayer, sendBack);
		} 
	}
	
	private void doSkill(StdMob currentPlayer, SendMessage sendBack) {
	//	HideAttachment newHide = new HideAttachment(currentPlayer, 10);
		sendBack.printMessage("You fade into the shadows.");
	//	DefenceLength newDefence = new DefenceLength(currentPlayer, sendBack, "balance", BALANCELENGTH, 
	//			"You have regained your balance.");
	//	newDefence.start();
	}
	
	private boolean skillCheck(StdMob currentPlayer, SendMessage sendBack) {
		if (currentPlayer.hasBalance()) {
			return true;			
		} else {
			sendBack.printMessage("You are off balance");
		}
		return false;
	}

}
