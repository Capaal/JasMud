package items;

import java.util.Map;
import java.util.TreeMap;
import processes.ContainerErrors;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import interfaces.Container;
import interfaces.Holdable;

// Specialized Holdable that stores quantity within a single item. Can add and remove instances of the same item type from
// and to other stacks, modifying quantity. Handles creating and destroying instances as new stacks are created or no longer needed.
// Quantity exists in StdItem, but is mandated as 1, unlike stackableItem which is (currently) unbound, or bound by container's max weight.
@XStreamAlias("StackableItem")
public class StackableItem extends StdItem {
	
	// TODO Probably handle recovering IDs at some point

	private final String descriptionMany;

	public StackableItem(StackableItemBuilder build) {
		super(build);
		this.quantity = build.getQuantity();
		this.descriptionMany = build.getDescriptionMany();
	}
	
	@Override public String getDescription() {
		if (quantity == 1) {
			return description;
		}
		return descriptionMany;
	}
	
	/** Override of StdItem's moveHoldable, defaulting to moving a quantity of 1.
	 * @see moveHoldable
	 * @param finalLocation Container which the single stackable item will be moved.
	 */
	@Override public ContainerErrors moveHoldable(Container finalLocation) {
		return moveHoldable(finalLocation, 1);
	}
	
	public ContainerErrors moveAllHoldable(Container finalLocation) {
		return moveHoldable(finalLocation, this.quantity);
	}	
	
	// TODO NEEDS to watch for a FALSE return from ACCEPTITEM, then handle the failed insertion.
	//should return true/false or case (ok or why can't go in - wrong (herb) type, too full)
	@Override public synchronized ContainerErrors moveHoldable(Container finalLocation, int number) {	
		int maxQty = (int) ((finalLocation.getMaxWeight() - finalLocation.getCurrentWeight()) / this.weight); // Translates Space in bag to qty of stackable.
		if (maxQty > 0) { // If the bag can hold less than the number requested, set number requested to lower max.
			if (number > maxQty) {
				number = maxQty;
			}
		} else {
			return ContainerErrors.QTYFULL; // If the bag can accept none of the stackable, return full error.
		}
		TreeMap<String, Holdable> inventoryView = finalLocation.viewInventory();
		Map.Entry<String,Holdable> possibleStackEntry = inventoryView.ceilingEntry(this.name);
		Holdable possibleStack = null;
		if (possibleStackEntry != null) {
			possibleStack = possibleStackEntry.getValue(); // If a stack of current item exists in final location, get that
		}
		// If moving the entire stack
		if (number >= this.quantity) {
			number = quantity;
			// if a stack of the same item is in final location, destroy this and add to that
			if (possibleStack != null &&  possibleStack.getName().equalsIgnoreCase(this.name)) {
				((StackableItem)possibleStack).addToStack(number);	
				removeFromStack(number);
				return null; // success
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
			// If a possible stack is found in Final location, give number.
			if (possibleStack != null && possibleStack.getName().equals(this.name)) {
				((StackableItem)possibleStack).addToStack(number);	
				return null;
			} else { // Else create new instance of this type in final location and split				
				splitAndNew(number, finalLocation); //method created to allow specific item type override, see Herb
				return null;
			}
		}
	}
	
	//Creates a new instance of this
	protected void splitAndNew(int number, Container finalLocation) {
		StackableItemBuilder newStack = new StackableItemBuilder(this);
		newStack.setQuantity(number);
		newStack.setItemContainer(finalLocation);	
		newStack.complete();
	}	
	
	@Override
	public synchronized void addToStack(int quantity) {
		this.quantity += quantity;
		if (getContainer() != null) {
			this.getContainer().changeWeight(quantity * weight);
		}
	}
	
	@Override
	public synchronized void removeFromStack(int qty) {		
		this.quantity -= qty;
		this.getContainer().changeWeight(-qty * weight);
		if (this.quantity <= 0) {
			this.removeFromWorld();
			this.delete();
		}
	}
	
	public static class StackableItemBuilder extends ItemBuilder {	
		
		public StackableItemBuilder(StackableItem item) {
			super(item);
			this.quantity = item.quantity;
			this.descriptionMany = item.descriptionMany;
		}
		
		public StackableItemBuilder() {
			// TODO Auto-generated constructor stub
		}

		private String descriptionMany = "";
		
		@Override public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		
		public void setDescriptionMany(String desc) {
			descriptionMany = desc;
		}
		
		public String getDescriptionMany() {
			return descriptionMany;
		}
		
		@Override public StdItem produceType() {
			return new StackableItem(this);
		} 
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
	
	@Override public synchronized double getWeight() {
		return weight * quantity;
	}
}
