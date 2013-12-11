package effects;

import interfaces.Effect;
import interfaces.Mobile;

public class PierceDefence implements Effect {

	private Mobile currentPlayer;
	
	@Override
	public void destroyEffect() {
		currentPlayer.removeEffect("piercedefence");
		this.currentPlayer = null;
	}

	@Override
	public boolean doEffect() {
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
