package items;

import java.util.Collection;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import interfaces.Container;
import interfaces.Holdable;
import items.StackableItem.StackableItemBuilder;
import processes.ContainerErrors;

public class Bag extends StdItem implements Container { //wearable

	protected TreeMap<String, Holdable> inventory = new TreeMap<String, Holdable>();	
	
	public Bag(ItemBuilder build) {
		super(build);
	}

	@Override
	public TreeMap<String, Holdable> getInventory() {
		return new TreeMap<String, Holdable>(this.inventory);
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
			return s.toString();
		} else
			return ("That bag is empty.");
	}
	
	
	@Override
	public ContainerErrors acceptItem(Holdable newItem) {
		inventory.put(newItem.getName().toLowerCase() + newItem.getId(), newItem);
		return null; // TODO should actually check from return
	}	
	
	@Override
	public void removeItemFromLocation(Holdable oldItem) {
		if ((inventory.remove(oldItem.getName().toLowerCase() + oldItem.getId()) == null)) {
			System.out.println("Failed to remove item from location: " + oldItem);
		}
	}
	
	@Override
	public Holdable getHoldableFromString(String holdableString) {
		holdableString = holdableString.toLowerCase();
		String ceiling = inventory.ceilingKey(holdableString);
		String floor = inventory.floorKey(holdableString);		
		if (ceiling != null) {
			if ((ceiling.equalsIgnoreCase(holdableString) || inventory.get(ceiling).getName().equalsIgnoreCase(holdableString))) {
				return inventory.get(ceiling);
			}
		}
		if (floor != null) {
			if ((floor.equalsIgnoreCase(holdableString) || inventory.get(floor).getName().equalsIgnoreCase(holdableString))) {
				return inventory.get(floor);
			}
		} 
		return null;
	}

	@Override
	public int getMaxQty() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public int getCurrentQty() {
		// TODO Auto-generated method stub
		return -1;
	}
	
	@Override
	public Collection<Holdable> getListMatchingString(String holdableString) {
		holdableString = holdableString.toLowerCase();		
		SortedMap<String, Holdable> subMap = inventory.subMap(holdableString, true, holdableString + Character.MAX_VALUE, true);		
		Collection<Holdable> set = subMap.values();
		System.out.println(set.toString());		
		if (set.isEmpty() || set == null) {
			Holdable h = getHoldableFromString(holdableString);
			if (h != null) {
				set.add(h);
			}
		}
		return set;
	}
/*
	@Override //same as StdMob...
	public Collection<Holdable> getListMatchingString(String holdableString) {
		holdableString = holdableString.toLowerCase();
		String ceiling = inventory.ceilingKey(holdableString);
		String floor = inventory.floorKey(holdableString);
		NavigableMap<String, Holdable> subMap = null;
		if (ceiling != null && floor != null && ceiling != floor) {
			subMap = inventory.subMap(floor, true, ceiling, false);
		}
		return subMap;
	}
*/
	
	@Override public ItemBuilder newBuilder() {
		return newBuilder(new BagItemBuilder());
	}
	
	protected ItemBuilder newBuilder(BagItemBuilder newBuild) {
		super.newBuilder(newBuild);
		return newBuild;
	}
	
	public static class BagItemBuilder extends ItemBuilder {
		
		
		
		@Override public StdItem produceType() {
			return new Bag(this);
		} 
	}

}

