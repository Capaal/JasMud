package items;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

import effects.Bleed;
import effects.Fear;
import interfaces.Mobile;


//may want to have multiple effects per weapon (aoe + bleed) someday
public class Weapon extends StdItem {
	
	private MercEffect type;
	private ArrayDeque<Plant> appliedPlants = new ArrayDeque<Plant>(); 

	
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
	
	@Override public ItemBuilder newBuilder() {
		return newBuilder(new WeaponItemBuilder());
	}
	
	protected ItemBuilder newBuilder(WeaponItemBuilder newBuild) {
		super.newBuilder(newBuild);
		newBuild.setMercEffect(this.type);
		return newBuild;
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
	
//	public String displayEffectOthers() {
//		return "";
//	}
	
	public enum MercEffect {

		BLEED() {
			@Override public boolean applyEffect(Mobile target) {
				if (target.addActiveCondition(new Bleed(target, 10), 5)) {
					target.tell("Bleeding caused.");
					return true;
				}
				return failedApply(target);
			}
		},	
		
		FASTBALANCE() {

		},
			
		FEAR() {
			@Override public boolean applyEffect(Mobile target) {
				if (target.addActiveCondition(new Fear(target), 10)) {
					target.tell("Fear applied.");
					return true;
				}
				return failedApply(target);
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

	//	public String displayEffectOthers() {
	//		return displayEffectOthers;		
	//	}
		
	//	private static String displayEffectOthers;
		
	}
	

}
