package effects;

import processes.Type;
import interfaces.*;

public class Bleed implements TickingEffect {
	
	private final Mobile currentPlayer;
	private final Type type;

	public Bleed(Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
		type = Type.BLEED;
	}
	@Override
	public boolean isInstanceOf(Effect otherEffect) {
		if (otherEffect.getClass() == Bleed.class) {
			return true;
		}
		return false;
	}
	@Override
	public void run() {
		currentPlayer.takeDamage(type, currentPlayer.getMaxHp() / 20);		
	}
	@Override
	public void doOnCreation() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doOnDestruction() {
		// TODO Auto-generated method stub
		
	}
}
