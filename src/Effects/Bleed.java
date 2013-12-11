package effects;

import interfaces.*;

public class Bleed implements Effect {

	private int intensity;
	private Mobile currentPlayer;
	private final int startTick;
	private final int endTick;
	

	public Bleed(Mobile currentPlayer, int duration) {
		setCurrentPlayer(currentPlayer);
		intensity = currentPlayer.getMaxHp()/100;
		startTick = currentPlayer.getTick();
		endTick = startTick + duration;
	}
	
	@Override
	public void destroyEffect() {
	//	currentPlayer.removeEffect("bleed");
		this.currentPlayer = null;
	}

	@Override
	public boolean doEffect() {
		currentPlayer.takeDamage(intensity);
		if (currentPlayer.getTick() >= endTick) {
	//		destroyEffect();
			return true;
		}
		return false;
		
	}

	@Override
	public Mobile getCurrentPlayer() {
		return currentPlayer;
		
	}

	@Override
	public void setCurrentPlayer(Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
		
	}
}
