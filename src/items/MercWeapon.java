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
public class MercWeapon extends StdItem {
	
	//private int maxSips;
	private MercEffect type;
	
	public MercWeapon(ItemBuilder build) {
		super(build);
	}
	
	@Override public ItemBuilder newBuilder() {
		ItemBuilder newBuild = super.newBuilder();
		return newBuild;
	}

	public boolean applyEffect(Mobile currentPlayer) {
		return type.applyEffect(currentPlayer);
	}
	
	public enum MercEffect {

		BLEED() {
			@Override public boolean applyEffect(Mobile currentPlayer) {
				if (currentPlayer.addActiveCondition(new Bleed(currentPlayer, 10), 5)) {
					currentPlayer.tell("Bleeding caused.");
					return true;
				}
				return failedApply(currentPlayer);
			}
		},	
		
		FASTBALANCE() {
		},
			
		FEAR() {
			@Override public boolean applyEffect(Mobile currentPlayer) {
				if (currentPlayer.addActiveCondition(new Fear(currentPlayer), 10)) {
					currentPlayer.tell("Fear applied.");
					return true;
				}
				return failedApply(currentPlayer);
			}
		},
		
		HIGHDMG() {
		},
			
		AOE() {
		},
		
		KNOCKDOWN() {
			@Override public boolean applyEffect(Mobile currentPlayer) {
				return true;
			}
		};
	
		private MercEffect() {}
		
		public boolean failedApply(Mobile currentPlayer) {
			currentPlayer.tell("The effect doesn't seem to do anything.");
			return false;
		}
		
		public boolean applyEffect(Mobile currentPlayer) {
			currentPlayer.tell("");
			return true;
		}

		
	}
	

}
