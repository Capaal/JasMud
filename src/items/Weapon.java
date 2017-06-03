package items;

import effects.Bleed;
import effects.Fear;
import effects.PassiveCondition;
import effects.Regen;
import interfaces.Mobile;
import items.Drinkable.DrinkType;
import items.ItemBuilder.ItemType;
import processes.Type;


//may want to have multiple effects per weapon (aoe + bleed) someday
public class Weapon extends StdItem {
	
	private MercEffect type;
	
	public Weapon(ItemBuilder build) {
		super(build);
		this.type = build.getMercEffect();
	}
	
	@Override public ItemBuilder newBuilder() {
		ItemBuilder newBuild = super.newBuilder();
		return newBuild;
	}

	@Override
	public double getBalanceMult() {
		if (type == MercEffect.FASTBALANCE) {
			return (this.balanceMult * 0.8);
		}
		return physicalMult;
	}
	
	@Override
	public double getDamageMult() {
		if (type == MercEffect.HIGHDMG) {
			return (this.physicalMult * 1.2);
		}
		return physicalMult;
	}
	
	public MercEffect getMercEffect() {return this.type;}
	
	
	public boolean applyEffect(Mobile target) {
		return type.applyEffect(target);
	}
	
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
			@Override public boolean applyEffect(Mobile target) {
				return true;
			}
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
			@Override public boolean applyEffect(Mobile target) {
				return true;
			}
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
			return false;
		}

		
	}
	

}
