package items;

import java.util.Collection;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.TreeMap;

import interfaces.Container;
import interfaces.Holdable;
import items.Bag.BagItemBuilder;
import processes.ContainerErrors;

//Pouches for easy accessibility - can eat/rub/use directly out of a pouch as though in hand.
public class Pouch extends StdItem implements Container {
	
	private int maxPlants = 1000; //variable for different sizes of pouches	  NOT currently in use.
	private Plant inventory; //includes qty (stackableitem)

	public Pouch(ItemBuilder build) {
		super(build);
		this.inventory = null;
	}
	
	@Override public String getInfo() {
		if (this.inventory == null) {
			return "empty pouch" + this.getId();
		} else {
			return this.inventory.getName() + " pouch" + this.getId() + ": " + inventory.getQuantity();
		}
	}
	
	@Override public String getShortDesc() {
		if (this.inventory == null) {
			return "empty herbpouch";
		} else {
			return this.inventory.getPlantType().toString().toLowerCase() + " pouch";
		}
	}
	@Override
	public int getMaxQty() {
		return this.maxPlants;
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
		if (newItem instanceof Plant) {
			if (inventory == null) {
				inventory = (Plant) newItem;
				return null;
			} else if (inventory != null && (((Plant)newItem).getPlantType() != inventory.getPlantType())) {
				return ContainerErrors.WRONGTYPE;
			} else if (inventory.getQuantity() == maxPlants) {
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

	@Override //useless method for pouch
	public Collection<Holdable> getListMatchingString(String holdableString) {
		Collection<Holdable> set = new HashSet<Holdable>();
		set.add(inventory);
		return set;
	}	
	
	@Override public ItemBuilder newBuilder() {
		return newBuilder(new PouchItemBuilder());
	}
	
	protected ItemBuilder newBuilder(PouchItemBuilder newBuild) {
		super.newBuilder(newBuild);
		return newBuild;
	}
	
	public static class PouchItemBuilder extends BagItemBuilder {
		
		
		
		@Override public StdItem produceType() {
			return new Pouch(this);
		} 
	}
}
