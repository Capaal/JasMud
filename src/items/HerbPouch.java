package items;

import java.util.TreeMap;
import interfaces.Container;
import interfaces.Holdable;

//Pouches for easy accessibility - can eat/rub/use directly out of a pouch as though in hand.
public class HerbPouch extends StdItem implements Container {
	
	private int maxHerbs = 1000; //variable for different sizes of pouches	  NOT currently in use.
	private Herb inventory; 

	public HerbPouch(ItemBuilder build) {
		super(build);
		this.inventory = null;
	}
	
	@Override public String getInfo() {
		if (this.inventory == null) {
			return "empty herbpouch" + this.getId();
		} else {
			return this.inventory.getName() + " herbpouch" + this.getId() + ": " + inventory.getQuantity();
		}
	}
	
	@Override public String getShortDesc() {
		if (this.inventory == null) {
			return "empty herbpouch" + this.getId();
		} else {
			return this.inventory.getName() + " herbpouch" + this.getId();
		}
	}

	@Override
	public TreeMap<String, Holdable> getInventory() {
		TreeMap<String, Holdable> newLook = new TreeMap<String,Holdable>();
		if (inventory != null) {
			newLook.put(inventory.getName() + inventory.getId(), inventory);
		}
		return newLook;
	}

	@Override
	public boolean acceptItem(Holdable newItem) {
		if (newItem instanceof Herb) {
			if (inventory != null && ((Herb)newItem).getHerbType() == inventory.getHerbType()) {
				System.out.println("This should not have happened, stackable should have handled I think?");
				return false;
			} else if (inventory == null) {
				inventory = (Herb) newItem;
				return true;
			}			
			return false;
		} 
		return false;
	}

	@Override
	public void removeItemFromLocation(Holdable oldItem) {
		if (oldItem != null && oldItem.equals(inventory)) {
			inventory = null;
		}		
	}

	@Override
	public Holdable getHoldableFromString(String holdableString) {
		if (holdableString != null && inventory != null && 
				(holdableString.equalsIgnoreCase(inventory.getName() + inventory.getId()) || holdableString.equalsIgnoreCase(inventory.getName()))) {
			return inventory;
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		if (inventory == null) {
			return true;
		}
		return false;
	}	
}
