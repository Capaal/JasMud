package items;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import processes.Location.Direction;
import processes.ContainerErrors;
import processes.WorldServer;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import interfaces.Container;
import interfaces.Holdable;

@XStreamAlias("StackableItem")
public class StackableItem extends StdItem {
	
	// Probably handle recovering IDs at some point
	private int quantity;
	private String descriptionSingle;
	private String descriptionMany;

	public StackableItem(StackableItemBuilder build) {
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
	//should return true/false or case (ok or why can't go in - wrong (herb) type, too full)
	public ContainerErrors moveHoldable(Container finalLocation, int number) {	
		ContainerErrors error = null;
		int maxQty = finalLocation.getMaxQty();
		if (maxQty>0) {
			int inContainerQty = finalLocation.getCurrentQty();
			int remainingQty = maxQty - inContainerQty;
			if (number > remainingQty) {
				number = remainingQty;
				error = error.QTYFULL; //only used when all containers are full
				if (number == 0) {return error;}
			}
		}
		TreeMap<String, Holdable> inventoryView = finalLocation.getInventory();
		Map.Entry<String,Holdable> possibleStack = inventoryView.ceilingEntry(this.name);
		// If moving the entire stack
		if (number >= this.quantity) {
			// if a stack of the same item is in final location
			if (possibleStack != null && possibleStack.getValue() instanceof StackableItem && possibleStack.getValue().getName().equals(this.name)) {
				((StackableItem)possibleStack.getValue()).addToStack(number);	
				this.removeFromWorld(); 
				this.delete();
				return error;
			} else { // No stack in final location, so move THIS there.	
				error = finalLocation.acceptItem(this);
				if (error == null) {
					getContainer().removeItemFromLocation(this);
					this.itemLocation = finalLocation;
					return error;
				}
				else {
					return error;
				}		
			}			
		} else { // Else split
			this.quantity -= number;
			// If a possible stack is found in Final location, give number.
			if (possibleStack != null && possibleStack.getValue() instanceof StackableItem && possibleStack.getValue().getName().equals(this.name)) {
				((StackableItem)possibleStack.getValue()).addToStack(number);			
				return error;
			} else { // Else create new instance of this type in final location and split				
				splitAndNew(number, finalLocation); //method created to allow specific item type override, see Herb
				return error;
			}
		}
	}
	
	public void splitAndNew(int number, Container finalLocation) {
		StackableItemBuilder newStack = (StackableItemBuilder) newBuilder();
		newStack.setQuantity(number);
		newStack.setItemContainer(finalLocation);	
		newStack.complete();
	}
	
	@Override public void removeFromWorld() {
	//	if (this.quantity > 1) {
	//		this.quantity -= 1;
	//	} else if (this.quantity == 1) {
		this.getContainer().removeItemFromLocation(this);
		if (!WorldServer.gameState.removeItem(this.getName() + this.getId())) {
			System.out.println("Item tried to be removed that cannot be: " + this.getName() + this.getId());
		}
		this.itemLocation = null;
	} 
	
	
	public synchronized void addToStack(int quantity) {
		this.quantity += quantity;
	}
	
	public void removeFromStack(int qty) {
		this.quantity -= qty;
		if (this.quantity <= 0) {
			this.removeFromWorld();
			this.delete();
		}
	}
	
	@Override public ItemBuilder newBuilder() {
		return newBuilder(new StackableItemBuilder());
	}
	
	protected ItemBuilder newBuilder(StackableItemBuilder newBuild) {
		super.newBuilder(newBuild);
		newBuild.setQuantity(this.quantity);
		newBuild.setDescription(this.description);
		newBuild.setDescriptionSingle(this.descriptionSingle);
		return newBuild;
	}
	
	public static class StackableItemBuilder extends ItemBuilder {
		
		private int quantity = 1;
		private String descriptionSingle = "";
		
		public void setDescriptionSingle(String desc) {
			this.descriptionSingle = desc;
		}
		
		public String getDescriptionSingle() {
			return descriptionSingle;
		}
		
		public int getQuantity() {
			return quantity;
		}
		
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		
		@Override public StdItem produceType() {
			return new StackableItem(this);
		} 
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
