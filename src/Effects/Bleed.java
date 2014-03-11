package effects;

import java.util.*;
import processes.Type;
import interfaces.*;

public class Bleed implements TickingEffect {
	
	private final Mobile currentPlayer;
	private final Set<Type> type = new HashSet<Type>();	

	public Bleed(Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
		type.add(Type.BLEED);
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
}
