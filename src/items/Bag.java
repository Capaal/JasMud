package items;

import java.util.NavigableMap;
import java.util.TreeMap;

import interfaces.Container;
import interfaces.Holdable;
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
	public String getName() {
		return name;
	}
	
	@Override
	public int getId() {return id;}
	
	@Override
	public String getInfo() {
		StringBuilder s = new StringBuilder();
		if (this.inventory != null) {
			s.append("This ");
			s.append(this.getName());
			s.append(" contains: ");
			s.append(System.getProperty("line.separator"));
			for (Holdable h: inventory.values()) {
				s.append("  ");
				s.append(h.getInfo());
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

	@Override //same as StdMob...
	public NavigableMap<String, Holdable> getListMatchingString(String holdableString) {
		holdableString = holdableString.toLowerCase();
		String ceiling = inventory.ceilingKey(holdableString);
		String floor = inventory.floorKey(holdableString);
		NavigableMap<String, Holdable> subMap = null;
		if (ceiling != null && floor != null && ceiling != floor) {
			subMap = inventory.subMap(floor, true, ceiling, false);
		}
		return subMap;
	}


}

