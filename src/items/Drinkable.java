package items;

import effects.PassiveCondition;
import effects.Bleed;
import effects.Regen;
import interfaces.Mobile;
import items.ItemBuilder.ItemType;
import processes.Type;

//almost same as stackable, except can't split quantity (sips)
public class Drinkable extends StdItem {
	
	// Probably handle recovering IDs at some point
	private int maxSips;
	private int currentSips;
	private DrinkType type;

	public Drinkable(ItemBuilder build) {
		super(build);
		this.maxSips = build.getMaxSips();
		this.currentSips = maxSips;
		this.type = build.getDrinkType();
	}
	
	public int getSips() {
		return this.currentSips;
	}
	
	public boolean changeSips(int number) {
		currentSips = currentSips - number;
		if (currentSips < 0) {
			currentSips = 0;
			return false;
		}
		if (currentSips > maxSips) {
			currentSips = maxSips;
			return false;
		}
		return true;
	}
	
	public String drink(Mobile currentPlayer) {
		return type.drink(currentPlayer);
	}
	
	@Override public ItemBuilder newBuilder() {
		ItemBuilder newBuild = super.newBuilder();
		newBuild.setQuantity(this.currentSips);
		newBuild.setDescription(this.description);
		newBuild.setItemType(ItemType.DRINKABLE);
		return newBuild;
	}
	
	public enum DrinkType {
		HEALTH() {
			@Override public String drink(Mobile currentPlayer) {
				currentPlayer.takeDamage(Type.BLUNT, -50);
				return "The warm liquid heals you a bit.";
			}
		},
		
		DEFENSE() {
			@Override public String drink(Mobile currentPlayer) {	
				if (currentPlayer.addPassiveCondition(PassiveCondition.DEFENCE,  -1)) {
					return "The potion makes you feel tougher.";
				}
				return failedSip();
			}
		},

		BLEED() {
			@Override public String drink(Mobile currentPlayer) {
				if (currentPlayer.addActiveCondition(new Bleed(currentPlayer, 30), 10)){
					return "The caustic liquid starts opening bloody wounds.";
				}
				return failedSip();
			}
		},
			
		ANTIDOTE() {
			@Override public String drink(Mobile currentPlayer) {
				if (currentPlayer.hasCondition(new Bleed(currentPlayer, 0))) {
				//	currentPlayer.removeCondition(Bleed.class); Would remove all bleeds
					currentPlayer.addActiveCondition(new Bleed(currentPlayer, -40), 10); // To remove 40 from the bleed amount?
					return "The soothing liquid closes your wounds.";
				}
				return failedSip();
			}
		},
		
		REGEN() {
			@Override public String drink(Mobile currentPlayer) {
				currentPlayer.addActiveCondition(new Regen(currentPlayer, -10), 5); // 10 intensity, 5 times.
//				currentPlayer.addActiveCondition(new Regen(currentPlayer, 10, 5)); // Heals 10 hitpoints every tick for 5 ticks.
				return "The potion gives you a warm, healthy glow."; // Should this be on Regen's doOnCreation()?
			}
			
		};
	
		private DrinkType() {}
		
		public String failedSip() {
			return "The potion doesn't seem to have an effect.";
		}
		
		public String drink(Mobile currentPlayer) {
			return "Wrong method, tells the coders they screwed up.";
		}
		
	}
	
	
	
}