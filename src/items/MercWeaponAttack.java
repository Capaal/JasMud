package items;

import effects.Bleed;
import effects.PassiveCondition;
import effects.Regen;
import interfaces.Mobile;
import items.Drinkable.DrinkType;
import items.ItemBuilder.ItemType;
import processes.Type;

public class MercWeaponAttack extends StdItem {
	
	//private int maxSips;
	private MercEffect type;
	
	public MercWeaponAttack(ItemBuilder build) {
		super(build);
	}
	
	@Override public ItemBuilder newBuilder() {
		ItemBuilder newBuild = super.newBuilder();
		return newBuild;
	}

	public String applyEffect(Mobile currentPlayer) {
		return type.applyEffect(currentPlayer);
	}
	
	public enum MercEffect {
		
		DEFENSE() {
			@Override public String applyEffect(Mobile currentPlayer) {
				if (currentPlayer.addPassiveCondition(PassiveCondition.DEFENCE, 10000)) {
					return "Defense added.";
				}
				return failedApply();
			}
		},

		BLEED() {
			@Override public String applyEffect(Mobile currentPlayer) {
				if (currentPlayer.addActiveCondition(new Bleed(currentPlayer), 2000, 5)) {
					return "Bleeding caused.";
				}
				return failedApply();
			}
		},	
		
		FASTBALANCE() {
			@Override public String applyEffect(Mobile currentPlayer) {
				return "";
			}
			
		};
	
		private MercEffect() {}
		
		public String failedApply() {
			return "The effect doesn't seem to do anything.";
		}
		
		public String applyEffect(Mobile currentPlayer) {
			return "Wrong method, tells the coders they screwed up.";
		}
		

		
	}
	

}
