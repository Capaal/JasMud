package items;

import effects.PassiveCondition;
import effects.Bleed;
import effects.Regen;
import interfaces.Mobile;
import items.StackableItem.StackableItemBuilder;
import processes.Type;

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
	
	public boolean changeSips(int number) {
		currentSips = currentSips - number; //should change this for fill
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
	
	@Override public ItemBuilder newBuilder() {
		System.out.println("Drinkable's newBuilder should not be called. Possible invalid state");
		return newBuilder(new DrinkableItemBuilder());
	}
	
	public ItemBuilder newBuilder(DrinkableItemBuilder newBuild) {
		super.newBuilder(newBuild);
		newBuild.setCurrentSips(this.currentSips);
		newBuild.setMaxSips(this.maxSips);
		newBuild.setDrinkType(this.type);
		return newBuild;
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
		
		
	}
	
	
	
}