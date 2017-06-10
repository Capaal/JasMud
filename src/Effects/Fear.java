package effects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import interfaces.Mobile;
import interfaces.TickingEffect;
import processes.Location;
import processes.WorldServer;
import processes.Location.Direction;
import processes.LocationConnection;
import skills.Look;
import skills.MoveFear;

public class Fear extends TickingEffect {
	
	private final Mobile currentPlayer;
	private MoveFear move;
	
	public Fear(Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
		this.interval = 2500;
		this.move = new MoveFear();
	}	

	@Override
	public void run() {
		WorldServer.gameState.addToQueue(move, "", currentPlayer);
//		move.perform("", currentPlayer);
		
	/*	if (!currentPlayer.hasBalance()) {
			currentPlayer.tell("You try to flee, but are off balance.");
			return;
		}
		if (currentPlayer.hasAllConditions(PassiveCondition.ROOT)) {
			currentPlayer.tell("You try to flee, but you are rooted in place.");
			return;
		}
		if (currentPlayer.hasAllConditions(PassiveCondition.BROKENLEGS)) {
			currentPlayer.tell("You try to flee, but your legs are broken.");
			return;
		}
		 */
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
