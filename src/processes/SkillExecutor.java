package processes;



public class SkillExecutor extends Thread {
	
	
	
	public void run() {
		while(true) {			
			Runnable nextSkill = WorldServer.gameState.takeFromQueue();
			nextSkill.run();
		}
	}

}
