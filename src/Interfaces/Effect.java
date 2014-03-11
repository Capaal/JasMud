package interfaces;

import java.util.*;

import processes.Type;

public interface Effect  {
	
	public boolean isInstanceOf(Effect otherEffect);
	public double checkAgainstIncomingDamage(Set<Type> incomingTypes, double damage);
	
}
