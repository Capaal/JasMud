package items;

import java.util.Collection;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.TreeMap;

import interfaces.Container;
import interfaces.Holdable;
import processes.ContainerErrors;

//Pouches for easy accessibility - can eat/rub/use directly out of a pouch as though in hand.
public class HerbPouch extends StdItem implements Container {
	
	private int maxHerbs = 1000; //variable for different sizes of pouches	  NOT currently in use.
	private Herb inventory; //includes qty (stackableitem)

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
			return "empty herbpouch";
		} else {
			return this.inventory.getHerbType().toString().toLowerCase() + " herbpouch";
		}
	}
	@Override
	public int getMaxQty() {
		return this.maxHerbs;
	}
	
	@Override
	public int getCurrentQty() {
		if (inventory == null) {
			return 0;
		}
		return this.inventory.getQuantity();
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
	public ContainerErrors acceptItem(Holdable newItem) {
		if (newItem instanceof Herb) {
			if (inventory == null) {
				inventory = (Herb) newItem;
				return null;
			} else if (inventory != null && (((Herb)newItem).getHerbType() != inventory.getHerbType())) {
				return ContainerErrors.WRONGTYPE;
			} else if (inventory.getQuantity() == maxHerbs) {
				return ContainerErrors.QTYFULL;
			} 
		} 
		return ContainerErrors.WRONGTYPE;
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

	@Override //useless method for herbpouch
	public Collection<Holdable> getListMatchingString(String holdableString) {
		Collection<Holdable> set = new HashSet<Holdable>();
		set.add(inventory);
		return set;
	}	
}
