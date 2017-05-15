package items;

import java.util.TreeMap;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import items.Herb.HerbType;
import items.ItemBuilder.ItemType;

//THIS SHOULD BE A CONTAINER TODO
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
	
	@Override public String getInfo() {
		if (this.herb == null) {
			return "empty herbpouch" + this.getId();
		} else {
			return this.herb.toString().toLowerCase() + " herbpouch" + this.getId() + ": " + this.getHerbQty();
		}
	}
	
	@Override public String getShortDesc() {
		if (this.herb == null) {
			return "empty herbpouch" + this.getId();
		} else {
			return this.herb.toString().toLowerCase() + " herbpouch" + this.getId();
		}
	}
	
	
	public int changeHerbs(int number, HerbType type) {
		int newTotal = currentHerbs + number; 
		if (this.herb == null) {
			this.herb = type;
		}
		if (newTotal <= 0) {  // if removing all or trying to remove more than all
			this.herb = null;
			int actualQtyRemoved = -currentHerbs;
			currentHerbs = 0;
			return actualQtyRemoved; 
		}
		if (newTotal > maxHerbs) {
			number = maxHerbs - currentHerbs; 
			currentHerbs = maxHerbs;
			return number; //returns how many actually got put in
		}
		currentHerbs = newTotal;
		return number; 
	}
	
	@Override public ItemBuilder newBuilder() {
		ItemBuilder newBuild = super.newBuilder();
		newBuild.setQuantity(this.currentHerbs);
		newBuild.setDescription(this.description);
		newBuild.setItemType(ItemType.HERB);
		return newBuild;
	}

	
}
