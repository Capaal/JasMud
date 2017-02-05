package processes;

import processes.GameState.QueueInformation;

public class SkillExecutor extends Thread {
	
	public void run() {
		while(true) {			
			QueueInformation nextSkill = WorldServer.gameState.takeFromQueue();
			nextSkill.skill.perform(nextSkill.fullCommand, nextSkill.currentPlayer);	
		}
	}

}
