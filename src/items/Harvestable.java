package items;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import interfaces.Mobile;
import processes.WorldServer;


// Implementation of Stationary Item that product Holdables.
public class Harvestable extends StationaryItem {
	
	private int maxQuantity;
	private int remainingQuantity;
	private HarvestType type;
	private boolean running = false;
	private int timeToReset = 5; // Seconds

	public Harvestable(HarvestableItemBuilder build) {
		super(build);
		this.maxQuantity = build.getMaxQuantity();
		this.remainingQuantity = maxQuantity;
		this.type = build.getHarvestType();
	}
	
	public int getRemainingQty() {
		return this.remainingQuantity;
	}
	
	public HarvestType getType() {
		return this.type;
	}
	
	//TODO should actually be getExamine
	@Override public String getInfo() {
		return super.getInfo() + " - " + this.type.getEnumInfo(remainingQuantity);
	}
	
	/**
	 * Changes quantity remaining of the Holdable produced by this object. Also begins resetting process if change brings quantity to 0.
	 * @param number int quantity to modify current quantity.
	 * @throws IllegalArgumentException thrown if change would cause remaining quantity to exceed max, or fall below 0.
	 */
	public void changeRemaining(int number) throws IllegalArgumentException {
		int finalQuantity = remainingQuantity + number;
		if (finalQuantity > maxQuantity || finalQuantity < 0) {
			throw new IllegalArgumentException("Change may not cause quantity to exceed defined max, or fall below 0.");
		}
		remainingQuantity = finalQuantity;
		if (remainingQuantity == 0) {
			if (!running) {
				WorldServer.getGameState().getEffectExecutor().schedule(() -> reset(), timeToReset, TimeUnit.SECONDS);
				running = true;
			}
		}
	} 
	
	// possible to access by outside methods - reset on daily for example
	public synchronized void reset() {
		Random n = new Random();
		remainingQuantity = n.nextInt(maxQuantity - 1) + 1; //between 1 and max, no zero
		running = false;
	}
	
	 // Creates a new Holdable of the type specified by HarvestableType, and creates it in Mobile's inventory.
	public void harvest(Mobile currentPlayer) {
		ItemBuilder toCopy = type.getTemplate();
		toCopy.setItemContainer(currentPlayer);
		toCopy.complete();
	}
	
	public static class HarvestableItemBuilder extends StationaryItemBuilder {		
		
		private HarvestType type = HarvestType.IRON;
		private int maxQuantity = 0;
		private int remainingQuantity = 0;
		
		public HarvestType getHarvestType() {return type;}
		public void setHarvestType(HarvestType harvestType) {type = harvestType;}
		
		public int getCurrentQuantity() {return remainingQuantity;}
		public void setCurrentQuantity(int quantity) {this.remainingQuantity = quantity;}
		
		public int getMaxQuantity() {return maxQuantity;}
		public void setMaxQuantity(int quantity) {this.maxQuantity = quantity;}
		
		@Override public StdItem produceType() {
			return new Harvestable(this);
		} 
	} 
	
	public enum HarvestType {
		IRON("You manage to pick out a chunk of iron.") {
			
			@Override public ItemBuilder getTemplate() {
				return  WorldServer.getGameState().itemTemplates.get("iron");
			}
			
			@Override public String getEnumInfo(int remainingQuantity) {
				return "This has " + remainingQuantity + " ores remaining.";
			}
		},
		
		WOOD("You cut a log out of the tree.") {
			
			@Override public ItemBuilder getTemplate() {
				return WorldServer.getGameState().itemTemplates.get("log");
			}
			
			@Override public String getEnumInfo(int remainingQuantity) {
				if (remainingQuantity == 0) {
					return "The tree needs time to regrow.";
				}
				return "This has " + remainingQuantity + " logs remaining.";
			}
/*		},

		GOLD() {
			@Override public String mine(Mobile currentPlayer) {
				if (currentPlayer.addActiveCondition(new Bleed(currentPlayer, 30), 10)){
					return "The caustic liquid starts opening bloody wounds.";
				}
				return failedMine();
			}
		},
			
		GEMS() {
			@Override public String mine(Mobile currentPlayer) {
				if (currentPlayer.hasCondition(new Bleed(currentPlayer, 0))) {
				//	currentPlayer.removeCondition(Bleed.class); Would remove all bleeds
					currentPlayer.addActiveCondition(new Bleed(currentPlayer, -40), 10); // To remove 40 from the bleed amount?
					return "The soothing liquid closes your wounds.";
				}
				return failedMine();
			}
		},
		
		COPPER() {
			@Override public String mine(Mobile currentPlayer) {
				currentPlayer.addActiveCondition(new Regen(currentPlayer, -10), 5); // 10 intensity, 5 times.
//				currentPlayer.addActiveCondition(new Regen(currentPlayer, 10, 5)); // Heals 10 hitpoints every tick for 5 ticks.
				return "The potion gives you a warm, healthy glow."; // Should this be on Regen's doOnCreation()?
			} */
			
		};
	
		public final String message;
		
		private HarvestType(String setMessage) {
			message = setMessage;
		}
		
		public ItemBuilder getTemplate() {
			return null;
		}
		
		public String failedHarvest() {
			return "You can't harvest this... thing.";
		}
		
		public String getEnumInfo(int remainingQuantity) {
			System.out.println("Error Harvestable getInfo: this method should never run.");
			return "";
		}		
	}	
}