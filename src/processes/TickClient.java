package processes;

import interfaces.Mobile;

public class TickClient extends Thread {
	
	Mobile currentPlayer;
	private final int TIME = 1000;
	private int tickCount;
	
	public TickClient(Mobile currentPlayer) {
	//	super();
		this.currentPlayer = currentPlayer;
		tickCount = 0;
	}
	
	public void run() {
		while (true) {
			try {
				Thread.sleep(TIME);
				} catch(InterruptedException ie) {
				}
			tickCount ++;
			currentPlayer.runEffects();
		}
		
	}
	
	public int getTick() {
		return tickCount;
	}
}
