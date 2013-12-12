package interfaces;

import java.util.ArrayList;
import java.util.List;

import processes.Type;

public abstract class Effect {
	
	private Boolean removed = false;
	protected Mobile currentPlayer;
	
	public Effect(Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	public abstract void doTickEffect();
	
	public abstract int doRunEffect(List<Type> incomingTypes, int damage);
	
	public Mobile getCurrentPlayer() {
		return currentPlayer;
	}

	public void destroyEffect() {
	//	currentPlayer.removeEffect(this);
		this.currentPlayer = null;
		this.removed = true;
	}
	
	public boolean wasRemoved() {
		return removed;
	}
}
