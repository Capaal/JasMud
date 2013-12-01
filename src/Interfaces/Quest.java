package Interfaces;

import processes.Player;
import processes.SendMessage;

public interface Quest {

	public void testQuest(String fullCommand, Player currentPlayer);
	public void testFinishQuest(String fullCommand, SendMessage sendBack);
	public void startQuest(SendMessage sendBack);
	public void finishQuest(SendMessage sendBack, String fullCommand);

}