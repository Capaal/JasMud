package MobileAI;

import processes.WorldServer;
import interfaces.Mobile;

public class EntersMobileDecorator extends MobileDecorator {

	private final Runnable doesOnEnter;	
	
	public EntersMobileDecorator(Mobile decoratedMobile, Runnable toDo) {
		super(decoratedMobile);
		doesOnEnter = toDo;
	}
	
	@Override
	protected void makeDecision() {
		WorldServer.getGameState().addToQueue(doesOnEnter);
	}
	
	@Override
	public void informEntered(Mobile newMob) {
		decoratedMobile.informEntered(newMob);
		makeDecision();
	}
}
