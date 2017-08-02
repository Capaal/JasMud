package items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

import interfaces.Holdable;
import processes.ContainerErrors;
import processes.UsefulCommands;

//Pouches for easy accessibility - can eat/rub/use directly out of a pouch as though in hand.
public class Pouch extends Bag {
	
	private final ArrayList<Class<? extends Holdable>> allowedTypes; // i.e. can be define to ONLY hold plants.
	
	// Unlike Bag, can hold only 1 STACKABLE item, such as plants or coins.
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
			return "empty pouch";
		} else {
			return this.inventory.getName().toLowerCase() + " pouch";
		}
	}
	
	@Override
	public Holdable getHoldableFromString(String holdableString) {
		holdableString = holdableString.toLowerCase();			
		String hasNum = UsefulCommands.getOnlyNumerics(holdableString);
		if (inventory != null) {
			if (!hasNum.equals("") && (inventory.getName()+inventory.getId()).equalsIgnoreCase(holdableString)) {
				return inventory;				
			} else if ((inventory.getName()+inventory.getId()).startsWith(holdableString)) {
				return inventory;				
			}
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
	public TreeMap<String, Holdable> viewInventory() {
		TreeMap<String, Holdable> newLook = new TreeMap<String,Holdable>();
		if (inventory != null) {
			newLook.put(inventory.getName() + inventory.getId(), inventory);
		}
		return newLook;
	}
	
	@Override
	public synchronized ContainerErrors acceptItem(Holdable newItem) {
		 	if (!allowedTypes.contains(newItem.getClass())) {
				return ContainerErrors.WRONGTYPE;
			}
			if (inventory != null || newItem.getWeight() > this.maxWeight) {
				return ContainerErrors.QTYFULL;
			}
			inventory = (Plant) newItem;
			changeWeight(newItem.getWeight());
			return null;
	}
	
	@Override
	public synchronized void removeItemFromLocation(Holdable oldItem) {
		if (inventory == oldItem) {
			inventory = null;
		}
		changeWeight(-oldItem.getWeight());
	}
	
	@Override
	public String getExamine() {
		StringBuilder s = new StringBuilder();
		if (this.inventory != null) {
			s.append(this.getDescription());
			s.append(" ");
			s.append("This pouch contains: ");
			s.append(inventory.getQuantity());
			s.append(" ");
			s.append(inventory.getName());
			s.append(".");
			return s.toString();
		} else {
			s.append(this.getDescription());
			s.append(" ");
			s.append("There is nothing in the pouch.");
			return s.toString();
		}
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
