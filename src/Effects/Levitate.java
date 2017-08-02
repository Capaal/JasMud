package effects;

import java.util.Map;
import java.util.Random;
import interfaces.Mobile;
import interfaces.TickingEffect;
import processes.Location;
import processes.Location.Direction;
import skills.Look;

// TODO Not implemented
public class Levitate extends TickingEffect {
	
	//TODO Doesn't do anything
	public Levitate(Mobile currentPlayer) {
		super(currentPlayer, 60000);
	}	

	@Override
	public void run() {
		if (!currentPlayer.hasBalance()) {
			currentPlayer.tell("Off balance.");
			return;
		}		
	}
	
	@Override
	public int getInterval() {
		return interval;
	}
	
	@Override
	public void stackedInstance(TickingEffect stackedInstance, int times) {
		// Not really implemented in any form...
	}
	
	@Override
	public void doOnCreation() {		
	//	currentPlayer.displayPrompt();
		currentPlayer.tell("You float up about a foot above the ground.");
	}

	@Override
	public void doOnDestruction() {
		currentPlayer.tell("You gently fall back to the ground.");
		
	}

}
