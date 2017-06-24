package processes;

public class SkillExecutor extends Thread {	
	public void run() {
		while(true) {			
			Runnable nextSkill = WorldServer.getGameState().takeFromQueue();
			nextSkill.run();
		}
	}
}
