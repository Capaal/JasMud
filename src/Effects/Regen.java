package effects;

import processes.Type;
import interfaces.*;

public class Regen implements TickingEffect {
	
	private final Mobile currentPlayer;
	private final Type type;

	public Regen(Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
		type = Type.REGEN;
	}
	@Override
	public boolean isInstanceOf(Effect otherEffect) {
		if (otherEffect.getClass() == Regen.class) {
			return true;
		}
		return false;
	}
	@Override
	public void run() {
		if (currentPlayer.getCurrentHp() == currentPlayer.getMaxHp()) {
			return;
		}
		currentPlayer.takeDamage(type, -10);		
	}
}
