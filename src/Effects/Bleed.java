package effects;

import java.util.*;
import processes.Type;
import interfaces.*;

public class Bleed extends Effect {

	private int intensity;
	private final int startTick;
	private final int endTick;
	private final Type type = Type.BLEED;
	

	public Bleed(Mobile currentPlayer, int duration) {
		super(currentPlayer);
		intensity = currentPlayer.getMaxHp()/10;
		startTick = currentPlayer.getTick();
		endTick = startTick + duration;
	}

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
}
