package items;

import effects.PassiveCondition;
import effects.Bleed;
import effects.Regen;
import interfaces.Mobile;

//almost same as stackable, except can't split quantity (sips)
public class Drinkable extends StdItem {
	
	// Probably handle recovering IDs at some point
	private int maxSips;
	private int currentSips;
	private DrinkType type;

	public Drinkable(DrinkableItemBuilder build) {
		super(build);
		this.maxSips = build.getMaxSips();
		this.currentSips = maxSips;
		this.type = build.getDrinkType();
	}
	
	public int getSips() {
		return this.currentSips;
	}
	
	/**
	 * Changes current sips by given number. Negative to remove quantity. WILL remove/add sips up to max/min but returns false.
	 * @param number Quantity of sips to add (+) or remove (-).
	 * @throws IllegalArgumentException thrown if given number would cause currentSips to go above max or below 0.
	 */
	public void changeSips(int number) throws IllegalArgumentException {
		int finalCount = currentSips + number;		
		if (finalCount > maxSips || finalCount < 0) {
			throw new IllegalArgumentException("Change in quantity cannot exceed bounds.");
		}
		currentSips = finalCount;	
	}
	
	// Triggers the drink's effect and displays appropriate message.
	public void drink(Mobile currentPlayer) {
		type.drink(currentPlayer);
	}	
	
	// Enum list of each drink type, detailing their messages and effects.
	public enum DrinkType {
		HEALTH() {
			@Override public void drink(Mobile currentPlayer) {
				currentPlayer.takeDamage(-50);
				currentPlayer.tell("The warm liquid heals you a bit.");
			}
		},
		
		DEFENSE() {
			@Override public void drink(Mobile currentPlayer) {	
				if (currentPlayer.addPassiveCondition(PassiveCondition.DEFENCE,  -1)) {
					currentPlayer.tell("The potion makes you feel tougher.");
				}
				currentPlayer.tell(failedSip());
			}
		},

		BLEED() {
			@Override public void drink(Mobile currentPlayer) {
				currentPlayer.addActiveCondition(new Bleed(currentPlayer, 30), 10);
				currentPlayer.tell("The caustic liquid starts opening bloody wounds.");
			}
		},
			
		ANTIDOTE() {
			@Override public void drink(Mobile currentPlayer) {
				if (currentPlayer.hasCondition(new Bleed(currentPlayer, 0))) {
				//	currentPlayer.removeCondition(Bleed.class); Would remove all bleeds
					currentPlayer.addActiveCondition(new Bleed(currentPlayer, -40), 10); // To remove 40 from the bleed amount?
					currentPlayer.tell("The soothing liquid closes your wounds.");
				}
				currentPlayer.tell(failedSip());
			}
		},
		
		REGEN() {
			@Override public void drink(Mobile currentPlayer) {
				currentPlayer.addActiveCondition(new Regen(currentPlayer, -10), 10); // 10 intensity, 5 times.
//				currentPlayer.addActiveCondition(new Regen(currentPlayer, 10, 5)); // Heals 10 hitpoints every tick for 5 ticks.
				currentPlayer.tell("The potion gives you a warm, healthy glow.");// Should this be on Regen's doOnCreation()?
			}
			
		};
	
		private DrinkType() {}
		
		public String failedSip() {
			return "The potion doesn't seem to have an effect.";
		}
		
		public void drink(Mobile currentPlayer) {
			System.out.println("Something odd happened, default drink type called.");
		}		
	}
	
	public static class DrinkableItemBuilder extends ItemBuilder {
		
		private int maxSips = 0;
		private DrinkType drinkType;
		private int currentSips = 0;
		
		public int getMaxSips() {return maxSips;}
		public void setMaxSips(int sips) {this.maxSips = sips;}
		public int getCurrentSips() { return currentSips;}
		public void setCurrentSips(int sips) {this.currentSips = sips;}
		public DrinkType getDrinkType() {return drinkType;}
		public void setDrinkType(DrinkType drinkType) {this.drinkType = drinkType;}
		
		@Override public StdItem produceType() {
			return new Drinkable(this);
		} 		
	}	
}