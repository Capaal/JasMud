package effects;

import processes.Type;
import interfaces.*;

public class Regen extends TickingEffect {
	
	private final Mobile currentPlayer;
	private final Type type;

	public Regen(Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
		type = Type.REGEN;
	}
	@Override
	public void run() {
		if (currentPlayer.getCurrentHp() == currentPlayer.getMaxHp()) {
			return;
		}
		currentPlayer.takeDamage(type, -10);		
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
