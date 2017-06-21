package items;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import interfaces.Container;
import interfaces.Holdable;
import processes.ContainerErrors;
import processes.UsefulCommands;

//TODO LOCK access to currentWeight
public class Bag extends StdItem implements Container { //wearable

	protected TreeMap<String, Holdable> inventory = new TreeMap<String, Holdable>();	
	
	protected final double maxWeight;	
	private final Lock lock = new ReentrantLock();
	
	protected double currentWeight;
	
	public Bag(BagItemBuilder build) {
		super(build);
		maxWeight = build.getMaxWeight();
	}

	@Override
	public TreeMap<String, Holdable> getInventory() {
		return new TreeMap<String, Holdable>(this.inventory);
	}
	
	@Override public double getWeight() {
		return getCurrentWeight() + weight;
	}
	
	@Override
	public String getExamine() {
		StringBuilder s = new StringBuilder();
		if (this.inventory != null) {
			s.append(System.getProperty("line.separator"));
			s.append("This ");
			s.append(this.getName());
			s.append(" contains: ");
			s.append(System.getProperty("line.separator"));
			for (Holdable h: inventory.values()) {
				s.append("  ");
				s.append(h.getShortDesc());
				s.append(System.getProperty("line.separator"));
			}
			s.append("There are " + inventory.size() + " items inside.");
			s.append(System.getProperty("line.separator"));
			s.append("Weight is " + getCurrentWeight() + " out of " + getMaxWeight() + ".");
			return s.toString();
		} else
			return ("That " + getName() + " is empty.");
	}
	
	//TODO nothing cares about the error.
	@Override
	public ContainerErrors acceptItem(Holdable newItem) {
		lock.lock();
		try {
			if ((getCurrentWeight() + newItem.getWeight()) > getMaxWeight()) {
				return ContainerErrors.QTYFULL;
			}
			inventory.put(newItem.getName().toLowerCase() + newItem.getId(), newItem);
			changeWeight(newItem.getWeight());
			return null; // TODO should actually check from return
		} finally {
			lock.unlock();
		}
	}	
	
	@Override
	public void removeItemFromLocation(Holdable oldItem) {
		lock.lock();
		try {
			if ((inventory.remove(oldItem.getName().toLowerCase() + oldItem.getId()) == null)) {
				System.out.println("Failed to remove item from a bag (MAJOR BUG): " + oldItem);
			}
			changeWeight(-oldItem.getWeight());
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public Holdable getHoldableFromString(String holdableString) {
		holdableString = holdableString.toLowerCase();
		String ceiling = inventory.ceilingKey(holdableString);
		String floor = inventory.floorKey(holdableString);			
		String hasNum = UsefulCommands.getOnlyNumerics(holdableString);
		if (ceiling != null) {
			if (!hasNum.equals("") && ceiling.equalsIgnoreCase(holdableString)) {
				return inventory.get(ceiling);				
			} else if (ceiling.toLowerCase().startsWith(holdableString)) {
				return inventory.get(ceiling);				
			}
		}
		if (floor != null) {
			if (!hasNum.equals("") && floor.equalsIgnoreCase(holdableString)) {
				return inventory.get(floor);				
			} else if (floor.toLowerCase().startsWith(holdableString)) {
				return inventory.get(floor);				
			}
		}
		return null;	
	}

	@Override
	public double getMaxWeight() {
		return maxWeight;
	}

	@Override
	public double getCurrentWeight() {
		return currentWeight;
	}
	
	@Override public void changeWeight(double change) {
		lock.lock();
		try {
			this.currentWeight += change;
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public Collection<Holdable> getListMatchingString(String holdableString) {
		holdableString = holdableString.toLowerCase();		
		SortedMap<String, Holdable> subMap = inventory.subMap(holdableString, true, holdableString + Character.MAX_VALUE, true);		
		Collection<Holdable> set = subMap.values();
		if (set.isEmpty() || set == null) {
			Holdable h = getHoldableFromString(holdableString);
			if (h != null) {
				set.add(h);
			}
		}
		return set;
	}
	
	// This do anyting/required?
	@Override public ItemBuilder newBuilder() {
		return newBuilder(new BagItemBuilder());
	}
	
	protected ItemBuilder newBuilder(BagItemBuilder newBuild) {
		super.newBuilder(newBuild);
		return newBuild;
	}
	
	public static class BagItemBuilder extends ItemBuilder {
		
		private int maxWeight = 250;
		private int currentWeight = 0;
		
		public int getMaxWeight() {
			return maxWeight;
		}
		
		public void setMaxWeight(int newWeight) {
			maxWeight = newWeight;
		}
		
		public int getCurrentWeight() {
			return currentWeight;
		}
		
		@Override public StdItem produceType() {
			return new Bag(this);
		} 
	}

}

