package items;

import interfaces.Container;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import processes.Equipment.EquipmentSlot;
import processes.WorldServer;

// Builder class for StdItem, and thus cornerstone for nearly all Items and Holdables.
// Can be modified via setters and getters until satisfied, calling complete generates an item of desired type.
// produceType MUST be overridden for all children.
public class ItemBuilder {
	
	protected double damageMult = 1.0;
	protected int id = -1;
	protected String name = "";
	protected String description = "default";
	protected double weight = 1;
	protected double balanceMult = 1;
//	protected int maxDurability = 1;
//	protected int currentDurability = 1;
	protected Container itemContainer;		
	protected EnumSet<EquipmentSlot> allowedSlots = EnumSet.of(EquipmentSlot.RIGHTHAND, EquipmentSlot.LEFTHAND);// EnumSet.noneOf(EquipmentEnum.class);
	protected List<StdItem> components = new ArrayList<StdItem>();
	protected boolean salvageable = false;	
	protected StdItem finishedItem =  null;
	protected int quantity = 1;
	
	public ItemBuilder(StdItem item) {
		this.damageMult = item.getDamageMult();
		this.name = item.getName();
		this.description = item.getDescription();
		this.weight = item.getWeight();
		this.balanceMult = item.getBalanceMult();
		this.salvageable = item.salvageable;
//		this.maxDurability = item.getMaxDurability;
		this.itemContainer = item.getContainer();
		this.allowedSlots = item.getAllowedEquipSlots();
		this.components = item.getComponents();
	}
	
	public ItemBuilder() {
		// TODO Auto-generated constructor stub
	}

	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		if (quantity != this.quantity) {
			throw new IllegalArgumentException("Cannot set quantity on StdItem.");
		}
	}	
	
	public void setDamageMult(double physicalMult) {
		this.damageMult = physicalMult;
	}
	
	public double getDamageMult() {
		return damageMult;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public double getWeight() {return weight;}
	public void setWeight(double newWeight) {this.weight = newWeight;}
	
	public void setBalanceMult(double balanceMult) {
		this.balanceMult = balanceMult;
	}
	
	public double getBalanceMult() {
		return balanceMult;
	}
	
/*	public void setMaxDurability(int maxDurability) {
		this.maxDurability = maxDurability;
	}
	
	public int getMaxDurability() {
		return maxDurability;
	}
	
	public void setCurrentDurability(int currentDurability) {
		this.currentDurability = currentDurability;
	}
	
	public int getCurrentDurability() {
		return currentDurability;
	}*/		
	
	public void setItemContainer(Container itemLocation) {
		this.itemContainer = itemLocation;
	}
	
	public Container getItemContainer() {
		return itemContainer;
	}
	
	public void setAllowedSlots(EnumSet<EquipmentSlot> allowedSlots) {
		this.allowedSlots = allowedSlots;
	}
	public void setAllowedSlots(EquipmentSlot allowedSlots) {
		this.allowedSlots.add(allowedSlots);
	}	
	
	public EnumSet<EquipmentSlot> getAllowedSlots() {
		return EnumSet.copyOf(allowedSlots);
	}
	
	public void setComponents(List<StdItem> allComponent) {
		this.components = allComponent;
	}

	
	public List<StdItem> getComponents() {
		return components;
	}
	
	public void setSalvageable(boolean setSalvage) {
		this.salvageable = setSalvage;
	}
	
	public boolean getSalvageable() {
		return salvageable;
	}
	
	// Attempts to obtain valid ID (and should check validity if given one)
	// Then creates a new item using builder's settings.
	// TODO return boolean?
	public void complete() {
		handleId();
		finishedItem = produceType();
		finishedItem.getContainer().acceptItem(finishedItem);
	}
	
	// Defines implementation of item to produce. MUST be overridden for child classes.
	public StdItem produceType() {
		return new StdItem(this);
	}
	
	// Modifies ID map, maintaining mandate that all items of the same NAME must not share the same ID.
	private synchronized void handleId() {		
		if (WorldServer.getGameState().idMap.containsKey(this.name)) {
			this.id = WorldServer.getGameState().idMap.get(this.name) + 1; 
			WorldServer.getGameState().idMap.put(this.name, this.id);					
		} else {
			this.id = 1;
			WorldServer.getGameState().idMap.put(this.name,  this.id);
		}
	}
	// If complete() has not been called, returns null.
	public StdItem getFinishedItem() {
		return finishedItem;
	}
	
	public StdItem getNonexistentFinishedItem() {
		handleId();
		StdItem notFinishedItem = produceType();
		return notFinishedItem;
	}
}
