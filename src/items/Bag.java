package items;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import interfaces.Container;
import interfaces.Holdable;
import processes.ContainerErrors;
import processes.UsefulCommands;

// Defines a basic implementation of a Holdable item that is also a Container.
// Thus defines a container within a container.
public class Bag extends StdItem implements Container { //wearable

	protected TreeMap<String, Holdable> inventory = new TreeMap<String, Holdable>();	
	
	protected final double maxWeight;	
	
	protected double currentWeight;
	
	public Bag(BagItemBuilder build) {
		super(build);
		maxWeight = build.getMaxWeight();
	}

	// Overrides Container, returning a copy of the internal inventory.
	@Override
	public TreeMap<String, Holdable> viewInventory() {
		return new TreeMap<String, Holdable>(this.inventory);
	}
	
	// Returns weight of self + weight of all help Holdables.
	@Override public double getWeight() {
		return getCurrentWeight() + weight;
	}
	
	// Used specifically for the "examine" core skill when viewing Holdables.
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
	
	/**
	 * Adds given Holdable to Inventory assuming space is available.
	 * @return ContainerErrors if fail, null otherwise.
	 * @param newItem Holdable item to be added to this container.
	 */
	@Override public synchronized ContainerErrors acceptItem(Holdable newItem) {	
		if ((getCurrentWeight() + newItem.getWeight()) > getMaxWeight()) {
			return ContainerErrors.QTYFULL;
		}
		inventory.put(newItem.getName().toLowerCase() + newItem.getId(), newItem);
		changeWeight(newItem.getWeight());
		return null; // Success	
	}	
	
	/**
	 * Removes indicated Holdable from inventory of this container. Assumes item is present.
	 * @throws IllegalArgumentException Thrown if item is not present to be removed.
	 */
	@Override public synchronized void removeItemFromLocation(Holdable oldItem) {	
		if ((inventory.remove(oldItem.getName().toLowerCase() + oldItem.getId()) == null)) {
			throw new IllegalStateException("Failed to remove item from a bag (MAJOR BUG): " + oldItem + " " + this.getShortDesc());
		} else {
			changeWeight(-oldItem.getWeight());		
		}
	}
	
	/**
	 * Returns Holdable that matches given string within certain deviation, primarily startsWith.
	 * @param HoldableString String to match against, either name or name + id.
	 * @return Holdable that closest matches string, or null if none found.
	 */
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
	
	public synchronized void changeWeight(double change) {
		this.currentWeight += change;
	}
	
	/**
	 * Returns list of Holdables closest matching given string. primarily startsWith.
	 * @param holdableString String on which to match holdables against name and name + id.
	 * @return Collection of Holdables matching given string. Or Empty collection.
	 */
	@Override
	public Collection<Holdable> getListMatchingString(String holdableString) {
		holdableString = holdableString.toLowerCase();	
		// Sub map between the given string and the given string + a large integer value to account for all id values.
		SortedMap<String, Holdable> subMap = inventory.subMap(holdableString, true, holdableString + Character.MAX_VALUE, true);		
		Collection<Holdable> set = subMap.values();
		if (set.isEmpty()) { // If none found, possible there is exactly 1, so try to get only 1.
			Holdable h = getHoldableFromString(holdableString);
			if (h != null) {
				set.add(h);
			}
		}
		return set;
	}
	
	// Extend ItemBuilder to handle extra Bag initialization required.
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

