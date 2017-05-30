package effects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import interfaces.Mobile;
import interfaces.TickingEffect;
import processes.Location;
import processes.Location.Direction;
import processes.LocationConnection;
import skills.Look;

public class Fear extends TickingEffect {
	
	private final Mobile currentPlayer;
	
	public Fear(Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
		this.interval = 2500;
	}	

	@Override
	public void run() {
		if (!currentPlayer.hasBalance()) {
			currentPlayer.tell("You try to flee, but are off balance.");
			return;
		}
		if (currentPlayer.hasAllConditions(PassiveCondition.BROKENLEGS)) {
			currentPlayer.tell("You try to flee, but your legs are broken.");
			return;
		}
		Location endContainer = null;
		Map<Direction, LocationConnection> availDir = currentPlayer.getContainer().getLocationMap();
		ArrayList<LocationConnection> locList = new ArrayList<LocationConnection>();
		locList.addAll(availDir.values());
		Random r = new Random();
		LocationConnection randomDir = locList.get(r.nextInt(locList.size()));		
		if (randomDir != null) {
			endContainer = randomDir.getLocation();				
		}
		if (endContainer == null) {
			currentPlayer.tell("You run around in circles, panicking but unable to find an exit.");
			return;
		}
		messageOthers(currentPlayer.getName() + " flees in a random direction.", Arrays.asList(currentPlayer));
		currentPlayer.moveHoldable(endContainer);new Look().perform("", currentPlayer);	
		messageOthers(currentPlayer.getName() + " flees in from a random direction.", Arrays.asList(currentPlayer));
	}
	
	private void messageOthers(String msg, List<Mobile> toIgnore) {
		for (Mobile h : currentPlayer.getContainer().getMobiles().values()) {
			if (h.isControlled()) {
				Boolean shouldTell = true;
				if (h.equals(currentPlayer)) {
					shouldTell = false;
				} else {
					for (Mobile m : toIgnore) {
						if (h.equals(m)) {
							shouldTell = false;
						}
					}
				}
				if (shouldTell) {
					h.tell(msg);
				}
			}			
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
		currentPlayer.displayPrompt();
	}

	@Override
	public void doOnDestruction() {
		// TODO Auto-generated method stub
		
	}

}
