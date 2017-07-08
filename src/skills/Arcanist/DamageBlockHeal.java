package skills.Arcanist;

import java.util.List;

import interfaces.Mobile;

public class DamageBlockHeal extends DamageBlock {

	public DamageBlockHeal(int damage, List<ArcanistBlock> newEffects) {
		super(-damage, newEffects);
	}
	
	@Override
	protected boolean isBlocking(Mobile target) {
		return false; // Don't care about blocking for heals.
	}
	
	@Override
	public int determineCost() {
		double cost = damage*1.5;
		if (addedEffects != null) { // UGLY, probably better solution, but by implementation empty is allowed but currently null is allowed.
			for (ArcanistBlock e : addedEffects) {
				cost -= e.determineCost();
			}	
		}
		return (int) cost;
	}
	
	@Override
	public StringBuilder describeOneself(StringBuilder sb) {
		sb.append(System.lineSeparator());
		sb.append("Healing done: " );
		sb.append(-damage);
		sb.append(" percent. Cost: ");
		sb.append(determineCost());
		if (addedEffects != null) {
			for (ArcanistBlock e : addedEffects) {
				e.describeOneself(sb);
			}
		}
		
		return sb;
	}
	
	@Override
	public boolean isValid() {
		if (damage > 0) {
			return false;
		}
		if (addedEffects != null) {
			for (ArcanistBlock e : addedEffects) {
				if (!e.isValid()) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public int getDamage() {
		return -damage;
	}
	
	@Override
	public DamageBlock getNewInstance(int damage2, List<ArcanistBlock> effects) {
		return new DamageBlockHeal(damage2, effects);
	}
}
