package items;

import java.util.Map;
import java.util.TreeMap;
import interfaces.Container;
import interfaces.Holdable;
import items.ItemBuilder.ItemType;

public class StackableItem extends StdItem {
	
	// Probably handle recovering IDs at some point
	private int quantity;
	private String descriptionSingle;
	private String descriptionMany;

	public StackableItem(ItemBuilder build) {
		super(build);
		this.quantity = build.getQuantity();
		this.descriptionMany = build.getDescription();
		this.descriptionSingle = build.getDescriptionSingle();
	}
	
	@Override public String getDescription() {
		if (quantity == 1) {
			return descriptionSingle;
		}
		return descriptionMany;
	}
	
	@Override public boolean save() {
		return true;
	}
	
	@Override public void moveHoldable(Container finalLocation) {
		moveHoldable(finalLocation, 1);
	}
	
	public void moveAllHoldable(Container finalLocation) {
		moveHoldable(finalLocation, this.quantity);
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void moveHoldable(Container finalLocation, int number) {		
		TreeMap<String, Holdable> inventoryView = finalLocation.getInventory();
		Map.Entry<String,Holdable> possibleStack = inventoryView.ceilingEntry(this.name);
		if (number >= this.quantity) {
			this.removeFromWorld(); // Should probably be a full fledged DELETE method
		} else {
			this.quantity -= number;
		}
		if (possibleStack != null && possibleStack.getValue() instanceof StackableItem && possibleStack.getValue().getName().equals(this.name)) {
			((StackableItem)possibleStack.getValue()).addToStack(number);			
		} else {
			if (number >= this.quantity) {
				finalLocation.acceptItem(this);
				this.itemLocation = finalLocation;
			} else {
				ItemBuilder newStack = this.newBuilder();
				newStack.setQuantity(number);
				newStack.setItemContainer(finalLocation);	
				newStack.complete();
			}			
		}
	}
	
	protected void addToStack(int quantity) {
		this.quantity += quantity;
	}
	
	@Override public ItemBuilder newBuilder() {
		ItemBuilder newBuild = super.newBuilder();
		newBuild.setQuantity(this.quantity);
		newBuild.setDescription(this.description);
		newBuild.setDescriptionSingle(this.descriptionSingle);
		newBuild.setItemType(ItemType.STACKABLEITEM);
		return newBuild;
	}
}
