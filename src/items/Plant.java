package items;

import effects.PassiveCondition;
import effects.Regen;
import interfaces.Container;
import interfaces.Mobile;
import items.ItemBuilder.ItemType;
import processes.Type;

public class Plant extends StackableItem {

	private PlantType type;
	
	public Plant(ItemBuilder build) {
		super(build);
		this.type = build.getPlantType();
	}
	
	public PlantType getPlantType() {
		return this.type;
	}
	
	public String use(Mobile currentPlayer) {
		return type.use(currentPlayer);
	}
	
	//multiple methods for other uses? eat/apply/etc
	
	@Override public ItemBuilder newBuilder() {
		ItemBuilder newBuild = super.newBuilder();
		newBuild.setItemType(ItemType.PLANT);
		return newBuild;
	}
	
	@Override 	public void splitAndNew(int number, Container finalLocation) {
		ItemBuilder newStack = this.newBuilder();
		newStack.setQuantity(number);
		newStack.setItemContainer(finalLocation);
		newStack.setPlantType(this.type);
		newStack.complete();	
	}
	
	public enum PlantType {
		ALOE {
			@Override public String use(Mobile currentPlayer) {
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
				} else {
					return("The comfrey has no effect.");
				}
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
				} else {
					return failedUse();
				}
			}		
			
		};
		
		private PlantType() {}
		
		public String failedUse() {
			return "The plant doesn't seem to have an effect.";
		}
		
		public String use(Mobile currentPlayer) {
			return "Error: failure Herb.use(...).";
		}
	}
	

	


}
