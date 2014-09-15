package processes;

import processes.GameState.QueueInformation;

public class SkillExecutor extends Thread {
	
	public void run() {
		while(true) {
			
			QueueInformation nextSkill = WorldServer.gameState.takeFromQueue();
			long start = System.nanoTime();
			nextSkill.skill.perform(nextSkill.fullCommand, nextSkill.currentPlayer);	
			long end = System.nanoTime();
			long elapsedTime = end - start;
			System.out.println(elapsedTime*(10E-7) + " milliseconds.");	
		}
	}

}
