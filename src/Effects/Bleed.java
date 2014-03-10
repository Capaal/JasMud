package effects;

import java.util.*;

import processes.Type;
import processes.UsefulCommands;
import interfaces.*;

public class Bleed extends Effect {

	private final int intensity;
	private final int startTick;
	private final int endTick;
	private final Type type = Type.BLEED;
	

	public Bleed(Mobile currentPlayer, int duration) {
		super(currentPlayer);
		intensity = currentPlayer.getMaxHp()/5;
		startTick = currentPlayer.getTick();
		endTick = startTick + duration;
	}

	@Override
	public void doTickEffect() {
		if (currentPlayer.getTick() > endTick) {
			destroyEffect();		
		}	else {
			currentPlayer.takeDamage(EnumSet.of(type), intensity);
			currentPlayer.displayPrompt();
		}
	}
	
	@Override
	public int doRunEffect(Set<Type> incomingTypes, int damage) {
		return damage;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
