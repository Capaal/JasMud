package MobileAI;

import processes.WorldServer;
import interfaces.Mobile;

public class EntersMobileDecorator extends MobileDecorator {

	private final Runnable doesOnEnter;	
	private long lastRan;
	
	public EntersMobileDecorator(Mobile decoratedMobile, Runnable toDo) {
		super(decoratedMobile);
		lastRan = System.currentTimeMillis();
		doesOnEnter = toDo;
	}
	
	@Override
	protected void makeDecision() {
		long currentTime = System.currentTimeMillis();
		if ((currentTime - lastRan) > 5000) {
			lastRan = currentTime;
			WorldServer.getGameState().addToQueue(doesOnEnter);
		}
	}
	
	@Override
	public void informEntered(Mobile newMob) {
		decoratedMobile.informEntered(newMob);
		makeDecision();
	}
}
