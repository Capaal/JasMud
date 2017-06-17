package effects;

import interfaces.Mobile;
import interfaces.TickingEffect;
import processes.WorldServer;
import skills.MoveFear;

public class Fear extends TickingEffect {
	
	private MoveFear move;	
	
	public Fear(Mobile currentPlayer) {
		super(currentPlayer, 2000);
		this.move = new MoveFear();
	}	

	@Override
	public void run() {
		WorldServer.gameState.addToQueue(move, "", currentPlayer);
	}
	
	@Override
	public boolean stackedInstance(TickingEffect stackedInstance, int times) {
		return true;
		// Do nothing, fear does not stack and does not modify existing fear.
	}
	
	@Override
	public void doOnCreation() {		
		currentPlayer.displayPrompt();
	}

	@Override
	public void doOnDestruction() {
		// TODO Auto-generated method stub		
	}

}
