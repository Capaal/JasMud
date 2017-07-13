package skills.Arcanist;

import java.util.List;

import interfaces.Mobile;

public class DamageBlock implements ArcanistBlock {
	
	protected final int damage;
	protected final List<ArcanistBlock> addedEffects;
	
	public DamageBlock(int damage, List<ArcanistBlock> newEffects) {
		this.damage = damage;
		addedEffects = newEffects;
	}
	
	@Override
	public void perform(ArcanistSkill skill) {
		for (Mobile t : skill.getCurrentTargets()) {
			if (!isBlocking(t)) { 
				t.takeDamage(damage);							
			}
		}
		if (addedEffects != null) { // UGLY, probably better solution, but by implementation empty is allowed but currently null is allowed.
			for (ArcanistBlock e : addedEffects) {
				e.perform(skill);
			}
	
		}		
	}
	
	protected boolean isBlocking(Mobile target) {
//		if (target.isBlocking()) {
//			messageSelf(target.getName() + " deftly blocks your attack.");
//			messageTarget("Your careful defence blocks an attack from " + currentPlayer.getName(), Arrays.asList(target));
//		}		
		return target.isBlocking();
	}
	
	public int determineCost() {
		int cost = -damage;
		if (addedEffects != null) { // UGLY, probably better solution, but by implementation empty is allowed but currently null is allowed.
			for (ArcanistBlock e : addedEffects) {
				cost += e.determineCost();
			}	
		}
		return cost;
	}
	
	@Override
	public StringBuilder describeOneself(StringBuilder sb) {
		sb.append(System.lineSeparator());
		sb.append("Damage dealt: " );
		sb.append(damage);
		sb.append(" percent. Cost: ");
		sb.append(determineCost());
		if (addedEffects != null) {
			for (ArcanistBlock e : addedEffects) {
				e.describeOneself(sb);
			}
		}
		
		return sb;
	}
	
	public List<ArcanistBlock> getAddedEffects() {
		return addedEffects;
	}

	// No need to check addedEffects for validness, as null is valid, but if not null, we DO need to ask those effects if valid.
	@Override
	public boolean isValid() {
		if (damage < 0) {
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

	public void addEffect(ArcanistBlock newBlock) {
		addedEffects.add(newBlock);
	}

	public int getDamage() {
		return damage;
	}

	public DamageBlock getNewInstance(int damage2, List<ArcanistBlock> effects) {
		return new DamageBlock(damage2, effects);
	}
}
