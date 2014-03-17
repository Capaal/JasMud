package interfaces;

import processes.Type;

public interface DamageEffect extends Effect, Runnable {
	
	public double checkAgainstIncomingDamage(Type incomingType, double damage);

}
