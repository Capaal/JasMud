package items;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import processes.ContainerErrors;
import processes.WorldServer;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import interfaces.Container;
import interfaces.Holdable;

@XStreamAlias("StackableItem")
public class StackableItem extends StdItem {
	
	// Probably handle recovering IDs at some point
	private int quantity;
	
	private final String descriptionSingle;
	private final String descriptionMany;
	private final Lock lock = new ReentrantLock();

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
	
	@Override public ContainerErrors moveHoldable(Container finalLocation) {
		return moveHoldable(finalLocation, 1);
	}
	
	public ContainerErrors moveAllHoldable(Container finalLocation) {
		return moveHoldable(finalLocation, this.quantity);
	}
	
	public int getQuantity() {return quantity;}
	
	// TODO NEEDS to watch for a FALSE return from ACCEPTITEM, then handle the failed insertion.
	//should return true/false or case (ok or why can't go in - wrong (herb) type, too full)
	public ContainerErrors moveHoldable(Container finalLocation, int number) {	
		lock.lock();
		try {
			int maxQty = (int) ((finalLocation.getMaxWeight() - finalLocation.getCurrentWeight()) / this.weight); // Translates Space in bag to qty of stackable.
			if (maxQty > 0) { // If the bag can hold less than the number requested, set number requested to lower max.
				if (number > maxQty) {
					number = maxQty;
				}
			} else {
				return ContainerErrors.QTYFULL; // If the bag can accept none of the stackable, return full error.
			}
			TreeMap<String, Holdable> inventoryView = finalLocation.getInventory();
			Map.Entry<String,Holdable> possibleStackEntry = inventoryView.ceilingEntry(this.name);
			Holdable possibleStack = null;
			if (possibleStackEntry != null) {
				possibleStack = possibleStackEntry.getValue();
			}
			// If moving the entire stack
			if (number >= this.quantity) {
				number = quantity;
				// if a stack of the same item is in final location, destroy this and add to that
				if (possibleStack != null &&  possibleStack.getName().equalsIgnoreCase(this.name)) {
					((StackableItem)possibleStack).addToStack(number);	
				//	this.removeFromWorld(); 
				//	this.delete(); // Is there ever a file to be deleted?
					removeFromStack(number);
					return null;
				} else { // No stack in final location, so move THIS there.	
					ContainerErrors error = finalLocation.acceptItem(this);
					if (error == null) { // If successfully moved the stack.
						getContainer().removeItemFromLocation(this);
						this.itemLocation = finalLocation;
					}
					return error;					
				}			
			} else { // Else split
				removeFromStack(number);
		//		this.quantity -= number;
				// If a possible stack is found in Final location, give number.
				if (possibleStack != null && possibleStack.getName().equals(this.name)) {
					((StackableItem)possibleStack).addToStack(number);	
					return null;
				} else { // Else create new instance of this type in final location and split				
					splitAndNew(number, finalLocation); //method created to allow specific item type override, see Herb
					return null;
				}
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void splitAndNew(int number, Container finalLocation) {
		StackableItemBuilder newStack = (StackableItemBuilder) newBuilder();
		newStack.setQuantity(number);
		newStack.setItemContainer(finalLocation);	
		newStack.complete();
	}	
	
	public  void addToStack(int quantity) {
		lock.lock();
		try {
			this.quantity += quantity;
			this.getContainer().changeWeight(quantity * weight);
		} finally {
			lock.unlock();
		}
	}
	
	public void removeFromStack(int qty) {
		lock.lock();
		try {			
			this.quantity -= qty;
			this.getContainer().changeWeight(-qty * weight);
			if (this.quantity <= 0) {
				this.removeFromWorld();
				this.delete();
			}
		} finally {
			lock.unlock();
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
	
	@Override public double getWeight() {
		return weight * quantity;
	}
}
