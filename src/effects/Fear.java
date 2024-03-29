package effects;

import interfaces.Mobile;
import interfaces.TickingEffect;
import processes.WorldServer;
import skills.MoveFear;

public class Fear extends TickingEffect {	
	
	public Fear(Mobile currentPlayer) {
		super(currentPlayer, 2000);
	}	

	@Override
	public void run() {
		WorldServer.getGameState().addToQueue(new MoveFear(currentPlayer, ""));
	}
	
	@Override
	public void stackedInstance(TickingEffect stackedInstance, int times) {
		// Do nothing, fear does not stack and does not modify existing fear.
	}
	
	@Override
	public void doOnCreation() {		
	//	currentPlayer.displayPrompt();
	}

	@Override
	public void doOnDestruction() {
		// TODO Auto-generated method stub		
	}

}
