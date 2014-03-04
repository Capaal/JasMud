package processes;

import interfaces.Container;
import interfaces.Mobile;
import items.StdItem;
import items.StdItem.ItemType;

import java.util.ArrayList;
import java.util.EnumSet;

import actions.Godcreate;
import processes.Equipment.EquipmentEnum;

public class ItemBuilder {
	
	public double physicalMult = 1.0;
	public int id;
	public String name;
	public String description = "default";
	public String shortDescription = "shortDefault";
	public double balanceMult = 1.0;
	public int maxDurability = 1;
	public int currentDurability = 1;
	public ArrayList<Type> types = new ArrayList<Type>();
	public ArrayList<ItemType> itemTags = new ArrayList<ItemType>();
	public Container itemLocation = WorldServer.locationCollection.get(1);		
	public EnumSet<EquipmentEnum> allowedSlots = EnumSet.noneOf(EquipmentEnum.class);
	
	public ItemBuilder() {
		setId();
	}
	public void setPhysicalMult(double physicalMult) {
		this.physicalMult = physicalMult;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public void setBalanceMult(double balanceMult) {
		this.balanceMult = balanceMult;
	}
	public void setMaxDurability(int maxDurability) {
		this.maxDurability = maxDurability;
	}
	public void setCurrentDurability(int currentDurability) {
		this.currentDurability = currentDurability;
	}
	public void setTypes(ArrayList<Type> types) {
		this.types = types;
	}
	public void setItemTags(ArrayList<ItemType> itemTags) {
		this.itemTags = itemTags;
	}
	public void setItemLocation(Container itemLocation) {
		this.itemLocation = itemLocation;
	}
	public void setAllowedSlots(EnumSet<EquipmentEnum> allowedSlots) {
		this.allowedSlots = allowedSlots;
	}
	/*	
		public T physicalMult(Double val) {physicalMult = val;return self();}		
		public T description(String val) {description = val;return self();}		
		public T shortDescription(String val) {shortDescription = val;return self();}		
		public T maxDurability(int val) {maxDurability = val;return self();}		
		public T itemLocation(Container val) {itemLocation = val;return self();}		
		public T balanceMult(Double val) {balanceMult = val;return self();}		
		public T types(ArrayList<Type> val) {types.addAll(val); return self();}
		public T itemTags(ArrayList<ItemType> val) {itemTags.addAll(val); return self();}
		public T currentDurability(int val) {currentDurability = val; return self();}
		public T allowedSlots(EnumSet<EquipmentEnum> val) {allowedSlots = EnumSet.copyOf(val); return self();}
		public StdItem build() {return new StdItem(this);}

	*/
	public void setId() {
		String sqlQuery = "SELECT sequencetable.sequenceid FROM sequencetable"
				+ " LEFT JOIN itemstats ON sequencetable.sequenceid = itemstats.itemid"
				+ " WHERE itemstats.itemid IS NULL";		
		Object availableId = (int) SQLInterface.viewData(sqlQuery, "sequenceid");
		if (availableId == null || !(availableId instanceof Integer)) {
			SQLInterface.increaseSequencer();
			setId();
		} else {
			if (WorldServer.locationCollection.containsKey(availableId)) {
				throw new IllegalStateException("An item of the id already exists.");
			}
			this.id = (int)availableId;
		}		
	}
	
	public StdItem complete() {
		return new StdItem(this);		
	}
	//TODO Make it recursive and not annoying to use.
	public static boolean newItem(Mobile player) {
		String newItemName = Godcreate.askQuestion("What is the name of this new item?", player);
		String newItemDescription = Godcreate.askQuestion("What is the long description for this item?", player);
		String newItemShortDescription = Godcreate.askQuestion("What is the short description for this item?", player);
		double newItemPhysicalMult = Double.parseDouble(Godcreate.askQuestion("What is the physical multiplier for this item? 1.0 is normal.", player));
		double newItemBalanceMult = Double.parseDouble(Godcreate.askQuestion("What is the balance multiplier for this item? 1.0 is normal.", player));;
		String stringItemLocation = Godcreate.askQuestion("Which location should the item be in upon creation?", player);
		Container newItemLocation;	
		if ("here".equals(stringItemLocation.toLowerCase())) {
			newItemLocation = player.getContainer();
		} else {
			newItemLocation = WorldServer.locationCollection.get(Integer.parseInt(stringItemLocation));
		}			
		int newItemMaxDurability = Integer.parseInt(Godcreate.askQuestion("What is this item's max durablity?", player));
		int newItemCurrentDurability = Integer.parseInt(Godcreate.askQuestion("What is this item's currentDurability?", player));
		int howManyItemTypes = Integer.parseInt(Godcreate.askQuestion("How many types will this item be?", player));
		ArrayList<Type> newItemTypes = new ArrayList<Type>();
		for (int i = 1; i <= howManyItemTypes; i++) {
			newItemTypes.add(Type.valueOf((Godcreate.askQuestion("What type will the item have?", player).toUpperCase())));
		}		
		ArrayList<ItemType> newItemTags = new ArrayList<ItemType>();
		int howManyItemTags = Integer.parseInt(Godcreate.askQuestion("How many item tags will this item have?", player));
		for (int i = 1; i <= howManyItemTags; i++) {
			newItemTags.add(ItemType.valueOf((Godcreate.askQuestion("What tag will the item have?", player).toUpperCase())));
		}	
		EnumSet<EquipmentEnum> newItemAllowedEquipSlots = EnumSet.noneOf(EquipmentEnum.class);
		int howManyAllowedEquipSlots = Integer.parseInt(Godcreate.askQuestion("How many slots can this item be in?", player));
		for (int i = 1; i <= howManyAllowedEquipSlots; i++) {
			newItemAllowedEquipSlots.add(EquipmentEnum.valueOf(Godcreate.askQuestion("What slot may be filled?", player).toUpperCase()));
		}
		ItemBuilder newItem = new ItemBuilder();
		newItem.setName(newItemName);
		newItem.setPhysicalMult(newItemPhysicalMult);
		newItem.setDescription(newItemDescription);
		newItem.setShortDescription(newItemShortDescription);
		newItem.setTypes(newItemTypes);
		newItem.setItemTags(newItemTags);
		newItem.setBalanceMult(newItemBalanceMult);
		newItem.setMaxDurability(newItemMaxDurability);
		newItem.setCurrentDurability(newItemCurrentDurability);
		newItem.setItemLocation(newItemLocation);
		newItem.setAllowedSlots(newItemAllowedEquipSlots);
		newItem.complete(); 
		return true;
	}
}
