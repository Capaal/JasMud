package effects;

import java.util.Map;
import java.util.Random;
import interfaces.Mobile;
import interfaces.TickingEffect;
import processes.Location;
import processes.Location.Direction;
import skills.Look;

public class Fear extends TickingEffect {
	
	private final Mobile currentPlayer;
//	private final int interval = 2500; // milliseconds between running
	
	public Fear(Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
		this.interval = 2500;
	}	

	@Override
	public void run() {
		if (!currentPlayer.hasBalance()) {
			currentPlayer.tell("Off balance.");
			return;
		}
		if (currentPlayer.hasAllConditions(PassiveCondition.BROKENLEGS)) {
			currentPlayer.tell("You try to flee, but your legs are broken.");
			return;
		}
		Location startContainer;
		Location endContainer;
		startContainer = currentPlayer.getContainer();
		endContainer = null;

		Map<Direction, Location> availDir = startContainer.getLocationMap();
		if (availDir.size() == 0) { //also hidden exits should stay hidden
			return;
		}
		Random r = new Random();
		Object[] values = availDir.values().toArray(); 
		Object randomDir = values[r.nextInt(values.length)];
		
		if (!(randomDir == null)) {
				endContainer = (Location)randomDir;				
		}
		if (endContainer == null) {
			currentPlayer.tell("You run around in circles, panicking but unable to find an exit.");
			return;
		}
	//	messageOthers(currentPlayer.getName() + " leaves to the " + dir + ".", Arrays.asList(currentPlayer));
		currentPlayer.moveHoldable(endContainer);
	//	messageOthers(currentPlayer.getName() + " arrives from the " + Location.Direction.getDirectionName(dir).getOpp() + ".", Arrays.asList(currentPlayer));
		Look look = new Look();
		look.perform("", currentPlayer);		
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
		currentPlayer.displayPrompt();
	}

	@Override
	public void doOnDestruction() {
		// TODO Auto-generated method stub
		
	}

}
