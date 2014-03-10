package interfaces;

import java.util.*;

import processes.Type;

public abstract class Effect implements Runnable {
	
	private Boolean removed = false;
	protected Mobile currentPlayer;
	
	
	public Effect(Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	 // Represents what each effect does per tick.
	public void run() {
		doRunEffect(null, 0);
	}
	// Represents how the effect might affect things happening to the player TODO
	public abstract int doRunEffect(Set<Type> incomingTypes, int damage);
	
	public Mobile getCurrentPlayer() {
		return currentPlayer;
	}
	// At the moment this requires that this effect is ALSO removed from player's affect list to be garbage collected
	public void destroyEffect() {
		// Tell player to remove this?
		this.currentPlayer = null;
		this.removed = true;
	}
	
	public boolean wasRemoved() {
		return removed;
	}

	public int getDestroyedTick() {
		return tickToBeDestroyed;
	}
}
