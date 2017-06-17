package items;

import effects.Belladonna;
import effects.PassiveCondition;
import effects.Regen;
import interfaces.Container;
import interfaces.Mobile;
import items.StackableItem.StackableItemBuilder;
import processes.Type;

public class Plant extends StackableItem {

	private PlantType type;
	
	public Plant(PlantItemBuilder build) {
		super(build);
		this.type = build.getPlantType();
	}
	
	public PlantType getPlantType() {
		return this.type;
	}
	
	public String use(Mobile currentPlayer) {
		return type.use(currentPlayer);
	}
	
	@Override 	public void splitAndNew(int number, Container finalLocation) {		
		PlantItemBuilder newStack = (PlantItemBuilder) this.newBuilder();
		newStack.setQuantity(number);
		newStack.setItemContainer(finalLocation);
		newStack.setPlantType(this.type);
		newStack.complete();	
	}
	
	
	public enum PlantType {
		ALOE {
			@Override public String use(Mobile currentPlayer) {
				if (currentPlayer.getCurrentHp() >= currentPlayer.getMaxHp()) {
					return failMsg;
				}
				currentPlayer.takeDamage(Type.BLUNT, -20);
				return "The herb soothes your wounds and restores some life.";
			}
		},
		
		COMFREY {
			@Override public String use(Mobile currentPlayer) {
				if (currentPlayer.hasAllConditions(PassiveCondition.BROKENLEFTARM)) {
					currentPlayer.removeAllConditions(PassiveCondition.BROKENLEFTARM);
					return("The herbs quickly knits your bones together and fixes your left arm.");
				} else if (currentPlayer.hasAllConditions(PassiveCondition.BROKENRIGHTARM)) {
					currentPlayer.removeAllConditions(PassiveCondition.BROKENRIGHTARM);
					return("The herbs quickly knits your bones together and fixes your right arm.");
				} else { return failMsg; }
			}
		},
		
		VALERIAN {
			@Override public String use(Mobile currentPlayer) {
				if (!(currentPlayer.hasAllConditions(PassiveCondition.SLEEP))) {
					currentPlayer.addAllConditions(PassiveCondition.SLEEP);
					return "The root causes a sudden lethargy and you fall asleep.";
				} else { return failMsg; }
			}
		},
		
		BELLADONNA {
			@Override public String use(Mobile currentPlayer) {
				//if need different msg per stage, check if player has belladonna - then check stage and display
				currentPlayer.addActiveCondition(new Belladonna(currentPlayer), 1);
				return "Your vision becomes a bit bright and blurry."; 
			}
		},
			
		GINSENG {
				@Override public String use(Mobile currentPlayer) {
					currentPlayer.addActiveCondition(new Regen(currentPlayer, -10), 4);
					return "The root revitalizes and energizes you.";
				}
		},
		
		OLEANDER {
			@Override public String use(Mobile currentPlayer) {
				if (!(currentPlayer.hasAllConditions(PassiveCondition.DIZZY))) {
					currentPlayer.addAllConditions(PassiveCondition.DIZZY);
					return "You feel dizzy.";
				} else { return failMsg; }
			}		
			
		};
		
		private PlantType() {}
		
		String failMsg = "The plant doesn't seem to have an effect.";
		
		public String use(Mobile currentPlayer) {
			return "Error: failure Herb.use(...).";
		}
	}
	

	public static class PlantItemBuilder extends StackableItemBuilder {
		private PlantType herbType = PlantType.ALOE;
		public PlantType getPlantType() {return herbType;}
		public void setPlantType(PlantType herbType) {this.herbType = herbType;}
		
		@Override public StdItem produceType() {
			return new Plant(this);
		} 
	}
	
	@Override public ItemBuilder newBuilder() {
		return newBuilder(new PlantItemBuilder());
	}
	
	/*protected ItemBuilder newBuilder(StackableItemBuilder newBuild) {
		super.newBuilder(newBuild);
		return newBuild;
	} */ // Should not be necessary, identical to stackableitem's


}
