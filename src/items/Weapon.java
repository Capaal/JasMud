package items;

import java.util.ArrayDeque;
import java.util.Queue;

import effects.Bleed;
import effects.Fear;
import interfaces.Mobile;

//may want to have multiple effects per weapon (aoe + bleed) someday

// Specialized StdItem meant to handle special effects and oddities weapons might need to handle.
// StdItems can be wielded as weapons, but can not do some of the advanced combat related things.
public class Weapon extends StdItem {
	
	private MercEffect type;
	private ArrayDeque<Plant> appliedPlants = new ArrayDeque<Plant>(); // List of plants currently on weapon.
	
	public Weapon(WeaponItemBuilder build) {
		super(build);
		this.type = build.getMercEffect();
	}	
	
	@Override 
	public String getInfo() { 
		StringBuilder s = new StringBuilder();
		s.append(this.getName() + this.getId());
		if (!appliedPlants.isEmpty()) {
			s.append(System.getProperty("line.separator"));
			s.append("These plants have been applied:");
			s.append(System.getProperty("line.separator"));
			for (Plant p : appliedPlants) {
				s.append(p.getName());
				s.append(System.getProperty("line.separator"));
			}
		}
		return s.toString();
	}

	@Override
	public double getBalanceMult() {
		if (type == MercEffect.FASTBALANCE) {
			return (this.balanceMult * 0.8);
		}
		return balanceMult;
	}
	
	@Override
	public double getDamageMult() {
		if (type == MercEffect.HIGHDMG) {
			return (this.physicalMult * 1.2);
		}
		return physicalMult;
	}
	
	public MercEffect getMercEffect() {return this.type;}
	public Queue<Plant> getAppliedList() {return this.appliedPlants;}
	
	public String applyPlant(Mobile target) {
		return this.appliedPlants.poll().getPlantType().use(target);
	}
	
	public boolean applyEffect(Mobile target) {
		if (type != null) {
			return type.applyEffect(target);
		}
		return false;
	}
	
	public static class WeaponItemBuilder extends ItemBuilder {
		
		protected  Weapon.MercEffect mercEffect = null;
		
		public Weapon.MercEffect getMercEffect() {
			return this.mercEffect;
		}
		
		public void setMercEffect(MercEffect a) {
			mercEffect = a;
		}
				
		@Override public StdItem produceType() {
			return new Weapon(this);
		} 
	}
	
	public enum MercEffect {

		BLEED() {
			@Override public boolean applyEffect(Mobile target) {
				target.addActiveCondition(new Bleed(target, 5), 5); // Times arbitrary, bleed doesn't care.
				target.tell("The serraded blade deeply gashes your flesh.");	
				return true;
			}
		},	
		
		FASTBALANCE() {

		},
			
		FEAR() {
			@Override public boolean applyEffect(Mobile target) {
				if (target.hasCondition(new Fear(target))) {
					return true;
				}
				target.addActiveCondition(new Fear(target), 10);
				target.tell("Fear applied.");
				return true;
				
			}
			
		},
		
		HIGHDMG() {

		},
		
		KNOCKDOWN() {
			@Override public boolean applyEffect(Mobile target) {
				return true;
			}
		};
	
		private MercEffect() {}
		
		public boolean failedApply(Mobile target) {
			target.tell("The effect doesn't seem to do anything.");
			return false;
		}
		
		public boolean applyEffect(Mobile target) {
			return true;
		}
	}
}
