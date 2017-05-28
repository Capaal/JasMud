package items;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import processes.Location.Direction;
import processes.WorldServer;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import interfaces.Container;
import interfaces.Holdable;
import items.ItemBuilder.ItemType;

@XStreamAlias("StackableItem")
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
	
	@Override public void moveHoldable(Container finalLocation) {
		moveHoldable(finalLocation, 1);
	}
	
	public void moveAllHoldable(Container finalLocation) {
		moveHoldable(finalLocation, this.quantity);
	}
	
	public int getQuantity() {return quantity;}
	
	// TODO NEEDS to watch for a FALSE return from ACCEPTITEM, then handle the failed insertion.
	public void moveHoldable(Container finalLocation, int number) {		
		TreeMap<String, Holdable> inventoryView = finalLocation.getInventory();
		Map.Entry<String,Holdable> possibleStack = inventoryView.ceilingEntry(this.name);
		// If moving the entire stack
		if (number >= this.quantity) {
			// if a stack of the same item is in final location
			if (possibleStack != null && possibleStack.getValue() instanceof StackableItem && possibleStack.getValue().getName().equals(this.name)) {
				((StackableItem)possibleStack.getValue()).addToStack(number);	
				this.removeFromWorld(); 
				this.delete();
			} else { // No stack in final location, so move THIS there.		
				getContainer().removeItemFromLocation(this);
				finalLocation.acceptItem(this);
				this.itemLocation = finalLocation;						
			}			
		} else { // Else split
			this.quantity -= number;
			// If a possible stack is found in Final location, give number.
			if (possibleStack != null && possibleStack.getValue() instanceof StackableItem && possibleStack.getValue().getName().equals(this.name)) {
				((StackableItem)possibleStack.getValue()).addToStack(number);					
			} else { // Else create new instance of this type in final location and split				
				splitAndNew(number, finalLocation); //method created to allow specific item type override, see Herb
			}
		}
	}
	
	public void splitAndNew(int number, Container finalLocation) {
		ItemBuilder newStack = this.newBuilder();
		newStack.setQuantity(number);
		newStack.setItemContainer(finalLocation);	
		newStack.complete();
	}
	
	@Override public void removeFromWorld() {
		if (this.quantity > 1) {
			this.quantity -= 1;
		} else if (this.quantity == 1) {
			this.getContainer().removeItemFromLocation(this);
			if (!WorldServer.gameState.removeItem(this.getName() + this.getId())) {
				System.out.println("Item tried to be removed that cannot be: " + this.getName() + this.getId());
			}
			this.itemLocation = null;
		} 
	}
	
	public synchronized void addToStack(int quantity) {
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
	
	private Object readResolve() {
		WorldServer.gameState.addItem(name + id, this);
		return this;
	}
	
	private void delete() {
		File file = new File(this.getName() + this.getId());
		file.delete();		
	}
	
	@Override
	public String getInfo() {
		if (quantity == 1) {
			return super.getInfo();
		} else {
			return this.quantity + " " + super.getInfo();
		}
	}
	
	@Override
	public String getShortDesc() {
		if (quantity == 1) {
			return this.getName();
		} else {
			return this.quantity + " " + this.getName();
		}
	}
}
