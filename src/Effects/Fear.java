package effects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import interfaces.Container;
import interfaces.Effect;
import interfaces.Mobile;
import interfaces.TickingEffect;
import processes.Location;
import processes.Skills;
import processes.Type;
import processes.Location.Direction;
import processes.Skills.Syntax;
import skills.Look;
import processes.StdMob;

public class Fear implements TickingEffect {
	
	private final Mobile currentPlayer;
	
	public Fear(Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	@Override
	public boolean isInstanceOf(Effect otherEffect) {
		if (otherEffect.getClass() == Fear.class) {
			return true;
		}
		return false;
	}
	

	@Override
	public void run() {
		if (!currentPlayer.hasBalance()) {
			currentPlayer.tell("Off balance.");
			return;
		}
		if (currentPlayer.hasAllConditions(ConditionsEnum.BROKENLEGS)) {
			currentPlayer.tell("You try to flee, but your legs are broken.");
			return;
		}
		Location startContainer;
		Container endContainer;
		startContainer = (Location)currentPlayer.getContainer();
		endContainer = null;

		Map<Direction, Location> availDir = startContainer.getLocationMap();
		if (availDir.size() == 0) { //also hidden exits should stay hidden
			return;
		}
		Random r = new Random();
		Object[] values = availDir.values().toArray(); 
		Object randomDir = values[r.nextInt(values.length)];
		
		if (!(randomDir == null)) {
			if (startContainer instanceof Location) {
				endContainer = (Location)randomDir;
			}		
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
	public void doOnCreation() {		
		
	}

	@Override
	public void doOnDestruction() {
		// TODO Auto-generated method stub
		
	}

}
