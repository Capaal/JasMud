package items;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import interfaces.Container;
import interfaces.Holdable;
import items.Bag.BagItemBuilder;
import processes.ContainerErrors;

//Pouches for easy accessibility - can eat/rub/use directly out of a pouch as though in hand.
public class Pouch extends Bag {
	
	private final ArrayList<Class<? extends Holdable>> allowedTypes;
	private final Lock lock = new ReentrantLock();
	
	private StackableItem inventory;

	public Pouch(PouchItemBuilder build) {
		super(build);
		allowedTypes = build.getTypes();
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
			return this.inventory.getName().toLowerCase() + " pouch";
		}
	}
	
	@Override
	public Holdable getHoldableFromString(String holdableString) {
		if (inventory != null && inventory.getName().equalsIgnoreCase(holdableString)) {
			return inventory;
		}
		return null;
	}
	
	@Override
	public Collection<Holdable> getListMatchingString(String holdableString) {	
		Collection<Holdable> set = new ArrayList<Holdable>();
		set.add(getHoldableFromString(holdableString));		
		return set;
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
		lock.lock();
		try {
			if (!allowedTypes.contains(newItem.getClass())) {
				return ContainerErrors.WRONGTYPE;
			}
			if (inventory != null || newItem.getWeight() > this.maxWeight) {
				return ContainerErrors.QTYFULL;
			}
			inventory = (Plant) newItem;
			changeWeight(newItem.getWeight());
			return null;
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public void removeItemFromLocation(Holdable oldItem) {
		lock.lock();
		try {
			if (inventory == oldItem) {
				inventory = null;
			}
			changeWeight(-oldItem.getWeight());
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public String getExamine() {
		StringBuilder s = new StringBuilder();
		if (this.inventory != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("This pouch Contains ");
			sb.append(inventory.getQuantity());
			sb.append(" ");
			sb.append(inventory.getName());
			sb.append(".");
			return sb.toString();
		} else
			return ("There is nothing in the pouch.");
	}
	
	@Override public ItemBuilder newBuilder() {		
		return newBuilder(new PouchItemBuilder());
	}
	
	protected ItemBuilder newBuilder(PouchItemBuilder newBuild) {
		super.newBuilder(newBuild);
		newBuild.allowedTypes = this.allowedTypes;
		return newBuild;
	}
	
	public static class PouchItemBuilder extends BagItemBuilder {	
		
		private ArrayList<Class<? extends Holdable>> allowedTypes = new ArrayList<Class<? extends Holdable>>();
		
		public PouchItemBuilder() {
			addType(Plant.class);
			setMaxWeight(100);
		}
		
		public void addType(Class<? extends Holdable> newType) {
			this.allowedTypes.add(newType);
		}
		
		public ArrayList<Class<? extends Holdable>> getTypes() {
			return new ArrayList<Class<? extends Holdable>>(allowedTypes);
		}
		
		@Override public StdItem produceType() {
			return new Pouch(this);
		} 
	}
}
