package items;

import java.util.TreeMap;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import items.Herb.HerbType;
import items.ItemBuilder.ItemType;

//Pouches for easy accessibility - can eat/rub/use directly out of a pouch as though in hand.
public class HerbPouch extends StdItem {
	
	private int maxHerbs = 1000; //variable for different sizes of pouches
	private int currentHerbs;
	private HerbType herb; //1 type per pouch

	public HerbPouch(ItemBuilder build) {
		super(build);
		this.maxHerbs = build.getMaxHerbs();
		this.currentHerbs = 0;
		this.herb = build.getHerbType();
	}
	
	public int getHerbQty() {
		return this.currentHerbs;
	}
	
	public HerbType getHerbType() {
		return this.herb;
	}
	
	//should not use if herbtype is null / pouch is empty
	public boolean changeHerbs(int number) {
		currentHerbs += number;
		if (currentHerbs == 0) {
			this.herb = null;  //sets the herbpouch to empty
			return true;
		}
		if (currentHerbs < 0) {  //this case only for removal
			currentHerbs = 0;
			return false;
		}
		if (currentHerbs > maxHerbs) {
			currentHerbs = maxHerbs;
			return false; 
		}
		return true;
	}
	
	// use if pouch is empty/null
	public boolean addHerbs(int number, HerbType herb) {
		if (changeHerbs(1)) {
			this.herb = herb;
			return true;
		} else {
			return false;
		}
	}

/*	public String put(Herb herb, int num) {
		if (herb.getHerbType() == this.herb) {
			currentHerbs += num;
			if (currentHerbs > maxHerbs) {
				currentHerbs = maxHerbs;
				return "Your pouch is full."; 
			}
			return "You add " + num + " " + herb.getName() + " to your pouch.";
		} else if (this.herb == null) {
			currentHerbs += num;
			this.herb = herb.getHerbType();
			if (currentHerbs > maxHerbs) {
				currentHerbs = maxHerbs;
				return "Your pouch is full."; 
			}
			return "You add " + num + " " + herb.getName() + " to your pouch.";
		} else {
			//how to handle pouch full, pouch for different herb, "put herb in pouch123" vs "put herb in pouch"
			return "There is no room for that herb.";
		}
	} */
	
	@Override public ItemBuilder newBuilder() {
		ItemBuilder newBuild = super.newBuilder();
		newBuild.setQuantity(this.currentHerbs);
		newBuild.setDescription(this.description);
		newBuild.setItemType(ItemType.HERB);
		return newBuild;
	}

	//HerbPouch as Container failed
/*	@Override
	public TreeMap<String, Holdable> getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override 
	public void acceptItem(Holdable newItem) {
		Herb herb = (Herb)newItem;
		if (this.herb == null) {
			currentHerbs += num;
			this.herb = herb.getHerbType();
			if (currentHerbs > maxHerbs) {
				currentHerbs = maxHerbs; 
			}
		}
		
	}

	@Override
	public void removeItemFromLocation(Holdable oldItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Holdable getHoldableFromString(String holdableString) {
		// TODO Auto-generated method stub
		return null;
	} */
	
}
