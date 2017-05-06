package items;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import effects.PassiveCondition;
import effects.Bleed;
import effects.Regen;
import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import items.ItemBuilder.ItemType;
import processes.CreateWorld;
import processes.InductionSkill;
import processes.StdMob;
import processes.Type;


//almost same as stackable, except can't split quantity
public class Mineable extends StationaryItem {
	
	// Probably handle recovering IDs at some point
	private int maxOres;
	private int currentOres;
	private OreType type;
	private boolean running = false;

	public Mineable(ItemBuilder build) {
		super(build);
		this.maxOres = build.getMaxOres();
		this.currentOres = maxOres;
		this.type = build.getOreType();
	}
	
	public int getOres() {
		return this.currentOres;
	}
	
	public boolean changeOres(int number) {
		currentOres = currentOres - number;
		if (currentOres < 0) {
			currentOres = 0;
			if (!running) {
				schedule();
				running = true;
			}
			return false;
		}
		if (currentOres > maxOres) {
			currentOres = maxOres;
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
		effectExecutor.schedule(wrapper, 5, TimeUnit.SECONDS); //20s for test
	}
	//end timer stuff
	
	// possible to access by outside methods - reset on daily for example
	public void reset() {
		Random n = new Random();
		currentOres = n.nextInt(maxOres - 1) + 1; //between 1 and max, no zero
		System.out.println("Rock reset worked.");
	}
	
	public String mine(Mobile currentPlayer) {
		return type.mine(currentPlayer);
	}
	
	@Override public ItemBuilder newBuilder() {
		ItemBuilder newBuild = super.newBuilder();
		newBuild.setQuantity(this.currentOres);
		newBuild.setDescription(this.description);
		newBuild.setItemType(ItemType.MINEABLE);
		return newBuild;
	} 
	
	public enum OreType {
		IRON() {
			@Override public String mine(Mobile currentPlayer) {
				createOre(currentPlayer, "iron");
				return "You manage to pick out a chunk of iron."; //or return just "iron" and the message can otherwise be the same
			}
/*		},
		
		SILVER() {
			@Override public String mine(Mobile currentPlayer) {	
				if (currentPlayer.addPassiveCondition(PassiveCondition.DEFENCE,  -1)) {
					return "The potion makes you feel tougher.";
				}
				return failedMine();
			}
		},

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
		private OreType() {}
		
		public String failedMine() {
			return "You can't mine this rock.";
		}
		
		public String mine(Mobile currentPlayer) {
			return "Wrong method, tells the coders they screwed up.";
		}
		
		public void createOre(Mobile currentPlayer, String oreToCreate) {
			ItemBuilder toCopy = allItemTemplates.get(oreToCreate); // Are all components actually stored in this list?
			toCopy.setItemContainer(currentPlayer);
			toCopy.complete();
		}
		
	}
	
	
	
}