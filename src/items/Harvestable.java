package items;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import interfaces.Mobile;
import processes.CreateWorld;


//almost same as stackable, except can't split quantity
public class Harvestable extends StationaryItem {
	
	// Probably handle recovering IDs at some point
	private int maxQuantity;
	private int remainingQuantity;
	private HarvestType type;
	private boolean running = false;
	private int timeToReset = 5;
	private Map<String, ItemBuilder> allItemTemplates = CreateWorld.viewItemTemplates();

	//add timeToRegen
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
	
	//shouldn't this just be removeOne?
	public boolean changeRemaining(int number) {
		remainingQuantity = remainingQuantity - number;
		if (remainingQuantity < 0) {
			remainingQuantity = 0;
			if (!running) {
				schedule();
				running = true;
			}
			return false;
		}
		if (remainingQuantity > maxQuantity) {
			remainingQuantity = maxQuantity;
			return false;
		}
		return true;
	} 
	
	//timer stuff
	private ScheduledExecutorService effectExecutor = Executors.newScheduledThreadPool(1);

	private class SkillWrapper implements Runnable {
		public SkillWrapper() {}
		public void run() {
			reset();
			running = false;
		}		
	}
	
	private void schedule() {
		SkillWrapper wrapper = new SkillWrapper();
		effectExecutor.schedule(wrapper, timeToReset, TimeUnit.SECONDS); //20s for test
	}
	//end timer stuff
	
	// possible to access by outside methods - reset on daily for example
	public void reset() {
		Random n = new Random();
		remainingQuantity = n.nextInt(maxQuantity - 1) + 1; //between 1 and max, no zero
		System.out.println(this.type.toString() + " reset done.");
	}
	
	//not sure this is correct, why not just use an arraylist to store the type?
	public void harvest(Mobile currentPlayer) {
		ItemBuilder toCopy = allItemTemplates.get(type.harvest(currentPlayer)); 
		toCopy.setItemContainer(currentPlayer);
		toCopy.complete();
	}
	
	@Override public ItemBuilder newBuilder() {
		return newBuilder(new HarvestableItemBuilder());
	}
	
	protected ItemBuilder newBuilder(HarvestableItemBuilder newBuild) {
		super.newBuilder(newBuild);
		newBuild.setCurrentQuantity(this.remainingQuantity);
		newBuild.setMaxQuantity(this.maxQuantity);
		newBuild.setHarvestType(this.type);
		return newBuild;
	}
	
	public static class HarvestableItemBuilder extends ItemBuilder {		
		
		private HarvestType type;
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
		IRON() {
			@Override public String harvest(Mobile currentPlayer) {
				currentPlayer.tell("You manage to pick out a chunk of iron."); //or return just "iron" and the message can otherwise be the same
				return "iron";
			}
			
			@Override public String getEnumInfo(int remainingQuantity) {
				return "This has " + remainingQuantity + " ores remaining.";
			}
		},
		
		WOOD() {
			@Override public String harvest(Mobile currentPlayer) {	
				currentPlayer.tell("You cut a log out of the tree.");
				return "log";
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
	
		Map<String, ItemBuilder> allItemTemplates = CreateWorld.viewItemTemplates();
		private HarvestType() {}
		
		public String failedHarvest() {
			return "You can't harvest this... thing.";
		}
		
		public String harvest(Mobile currentPlayer) {
			System.out.println("Error Harvestable harvest: this method should never run.");
			return "";
		}
		
		public void createHarvestedItem(Mobile currentPlayer, String itemToCreate) {
			ItemBuilder toCopy = allItemTemplates.get(itemToCreate); 
			toCopy.setItemContainer(currentPlayer);
			toCopy.complete();
		}
		
		public String getEnumInfo(int remainingQuantity) {
			System.out.println("Error Harvestable getInfo: this method should never run.");
			return "";
		}
		
	}
	
	
	
}