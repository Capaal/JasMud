package effects;

import java.util.Map;
import java.util.Random;
import interfaces.Mobile;
import interfaces.TickingEffect;
import processes.Location;
import processes.Location.Direction;
import skills.Look;

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
	public boolean stackedInstance(TickingEffect stackedInstance, int times) {
		return true;
		// Do nothing, fear does not stack and does not modify existing fear.
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
