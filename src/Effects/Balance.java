package effects;

import java.util.*;

import processes.Type;
import interfaces.*;

public class Balance extends Effect implements Runnable {	

	public Balance(Mobile currentPlayer) {
		super(currentPlayer, 0);
	}	
	@Override
	public void run() {
		super.run();
		destroyEffect();
	}
	// TODO Silly implementation
	@Override
	public int doRunEffect(Set<Type> incomingTypes, int damage) {
		return damage;
	}
}
