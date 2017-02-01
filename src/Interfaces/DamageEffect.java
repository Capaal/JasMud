package interfaces;

import processes.Type;

// WTF is this for?
public interface DamageEffect extends Effect, Runnable {
	
	public double checkAgainstIncomingDamage(Type incomingType, double damage);

}
