package MobileAI;

import processes.WorldServer;
import interfaces.Mobile;

// Defines AI that performs a single action when a Mobile enters Location.
public class EntersMobileDecorator extends MobileDecorator {

	private final Runnable doesOnEnter;	
	private long lastRan;
	
	private final int TIMEBETWEENTRIGGERS = 5000; // milliseconds
	
	public EntersMobileDecorator(Mobile decoratedMobile, Runnable toDo) {
		super(decoratedMobile);
		lastRan = System.currentTimeMillis();
		doesOnEnter = toDo;
	}
	
	@Override
	protected void makeDecision() {
		long currentTime = System.currentTimeMillis();
		if ((currentTime - lastRan) > TIMEBETWEENTRIGGERS) {
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
