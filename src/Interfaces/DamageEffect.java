package interfaces;

import java.util.Set;
import processes.Type;

public interface DamageEffect extends Effect, Runnable {
	
	public double checkAgainstIncomingDamage(Set<Type> incomingTypes, double damage);

}
