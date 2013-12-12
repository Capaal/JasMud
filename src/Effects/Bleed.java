package effects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import processes.Type;
import interfaces.*;

public class Bleed extends Effect {

	private int intensity;
//	private final Mobile currentPlayer;
	private final int startTick;
	private final int endTick;
	private final Type type = Type.BLEED;
	

	public Bleed(Mobile currentPlayer, int duration) {
		super(currentPlayer);
		intensity = currentPlayer.getMaxHp()/10;
		startTick = currentPlayer.getTick();
		endTick = startTick + duration;
	}
	
//	@Override
//	public void destroyEffect() {
//		currentPlayer.removeEffect(this);
//		this.currentPlayer = null;
//	}

	@Override
	public void doTickEffect() {
		if (currentPlayer.getTick() > endTick) {
			destroyEffect();		
		}	else {
			currentPlayer.takeDamage(Arrays.asList(type), intensity);
		}
	}
	
	@Override
	public int doRunEffect(List<Type> incomingTypes, int damage) {
		return damage;
	}

//	@Override
//	public Mobile getCurrentPlayer() {
//		return currentPlayer;
//		
//	}

//	@Override
//	public void setCurrentPlayer(Mobile currentPlayer) {
//		this.currentPlayer = currentPlayer;
//		
//	}
}
