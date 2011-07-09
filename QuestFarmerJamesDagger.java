import java.io.*;
import java.util.*;

public class QuestFarmerJamesDagger implements Quest, Serializable {
	
	final boolean repeatable = true;
	protected Player currentPlayer;
	private Mob questGiver;
	private boolean questFinished;
	protected ArrayList<Player> playersStarted = new ArrayList<Player>();
		
	public QuestFarmerJamesDagger() {
		this.currentPlayer = null;
		this.questGiver = (Mob) WorldServer.mobList.get("farmer james");
		this.questFinished = false;
	}
	
	public void testQuest(String fullCommand, Player currentPlayer) {
		if (("greet farmer james".equals(fullCommand)) &&
				(currentPlayer.mobLocation == questGiver.mobLocation) && !playersStarted.contains(currentPlayer)) {
			QuestFarmerJamesDagger newQuest = new QuestFarmerJamesDagger();
			newQuest.currentPlayer = currentPlayer;
			currentPlayer.questList.add(newQuest);
			this.playersStarted.add(currentPlayer);
			startQuest(UsefulCommands.getPlayerPromptFromPlayer(currentPlayer).sendBack);
		} else if (("greet farmer james".equals(fullCommand)) &&
				(currentPlayer.mobLocation == questGiver.mobLocation) && playersStarted.contains(currentPlayer)) {
			remindQuest(UsefulCommands.getPlayerPromptFromPlayer(currentPlayer).sendBack);
		}
	}
	
	public void testFinishQuest(String fullCommand, SendMessage sendBack) {
		String command = UsefulCommands.getOnlyStrings(fullCommand);
		if ("give dagger to farmer james".equals(command.toLowerCase())) {
			questFinished = true;
		}
		
	}
	
	public void startQuest(SendMessage sendBack) {
		sendBack.printMessage("Famer James says, \"While you're wanderin' about and find a dagger, would you mind bringing it back here to me?\"");
	}
	
	public void remindQuest(SendMessage sendBack) {
		sendBack.printMessage("Farmer James says, \"I was really hoping you'd returned with that dagger.\"");
	}
	
	public void finishQuest(SendMessage sendBack, String fullCommand) {
		if (questFinished) {
			sendBack.printMessage("Farmer James says, \"Ahhh, wonderful! Thank you for your troubles.\"");
			currentPlayer.addExperience(10);
			Quest quest = WorldServer.allQuests.get(0);
			((QuestFarmerJamesDagger)quest).playersStarted.remove(currentPlayer);
			currentPlayer.questList.remove(this);
		}
	}
}