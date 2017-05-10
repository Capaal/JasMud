package items;

import interfaces.Container;
import interfaces.Mobile;
import items.ItemBuilder.ItemType;
import processes.Type;

public class Herb extends StackableItem {

	private HerbType type;
	
	public Herb(ItemBuilder build) {
		super(build);
		this.type = build.getHerbType();
	}
	
	public HerbType getHerbType() {
		return this.type;
	}
	
	public String use(Mobile currentPlayer) {
		this.removeFromWorld();
		return type.use(currentPlayer);
	}
	
	//multiple methods for other uses? eat/apply/etc
	
	@Override public ItemBuilder newBuilder() {
		ItemBuilder newBuild = super.newBuilder();
		newBuild.setItemType(ItemType.HERB);
		return newBuild;
	}
	
	@Override 	public void splitAndNew(int number, Container finalLocation) {
		ItemBuilder newStack = this.newBuilder();
		newStack.setQuantity(number);
		newStack.setItemContainer(finalLocation);
		newStack.setHerbType(this.type);
		newStack.complete();	
	}
	
	public enum HerbType {
		ALOE {
			@Override public String use(Mobile currentPlayer) {
				currentPlayer.takeDamage(Type.BLUNT, -20);
				return "The herb soothes your wounds and restores some life.";
			}
			
		};
		
		private HerbType() {}
		
		public String failedUse() {
			return "The herb doesn't seem to have an effect.";
		}
		
		public String use(Mobile currentPlayer) {
			return "Error: failure Herb.use(...).";
		}
	}
	

	


}
