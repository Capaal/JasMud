package items;

import java.util.*;

import interfaces.*;
import items.ItemBuilder.ItemType;
import processes.*;
import processes.Equipment.EquipmentEnum;

public class StdItem implements Holdable, Weapon {
	
	protected final String name;
	protected final int id;	
	protected final String description;
	protected final double physicalMult;
	protected final double balanceMult; 	
	protected Container itemLocation;	
//	protected final int maxDurability;
//  protected int currentDurability;
	protected final List<String> components; // Add to weapon interface? Make a craftable interface?
	protected final boolean salvageable; // same as components?
	
	protected final Set<EquipmentEnum> allowedEquipSlots;

	public StdItem(ItemBuilder build) {
		this.id = build.getId();
		this.name = build.getName();
		this.physicalMult = build.getDamageMult();
		this.description = build.getDescription();
		this.balanceMult = build.getBalanceMult();
		this.itemLocation = build.getItemContainer();		
//		this.maxDurability = build.getMaxDurability();
//		this.currentDurability = build.getCurrentDurability();
		this.allowedEquipSlots = build.getAllowedSlots();
		this.components = build.getComponents();
		this.salvageable = build.getSalvageable();
		
		WorldServer.gameState.addItem(name + id, this);
		itemLocation.acceptItem(this);
	}
	
	@Override public String getName() {return name;}
	@Override public int getId() {return id;}
	@Override public String getDescription() {return description;}	
	public double getPhysicalMult() {return physicalMult;}
	public double getBalanceMult() {return balanceMult;}
	@Override public synchronized Container getContainer() {return itemLocation;}	
	
//	public int getMaxDurability() {return maxDurability;}
	
//	public synchronized int getCurrentDurability() {return currentDurability;}
//	public synchronized void setDurability(int newDurability) {
//		if (newDurability > maxDurability) {
//			newDurability = maxDurability;
//		}
//		this.currentDurability = newDurability;
//	}
	
	public Set<EquipmentEnum> getAllowedEquipSlots() {return allowedEquipSlots;}
	public List<String> getComponents() {return components;}
	
	public double getDamageMult() {
		return physicalMult;
	}
	
	// not sure how packs will work yet...
	@Override
	public boolean save() {			
//		String updateItem = "UPDATE ITEMSTATS SET ITEMCURDUR=" + currentDurability + ", WHERE ITEMID=" + getId() + ";";
//		WorldServer.databaseInterface.saveAction(updateItem);
		return true;
	}
	
	@Override
	public void moveHoldable(Container finalLocation) {
		getContainer().removeItemFromLocation(this);
		finalLocation.acceptItem(this);
		this.itemLocation = finalLocation;
	}
	
	@Override
	public boolean firstTimeSave() { 
		DatabaseInterface databaseInterface = WorldServer.getInterface();		
/*		databaseInterface.saveAction("Insert into ITEMSTATS (ITEMID, ITEMNAME, ITEMPHYS, ITEMBAL, ITEMDESC, ITEMMAXDUR, ITEMCURDUR, ITEMLOC, EQUIPSLOTS)"
				+ " values ("
				+ "'" + id + "', "
				+ "'" + name + "', "
				+ "'" + physicalMult + "', "
				+ "'" + balanceMult + "', "
				+ "'" + description + "', "
//				+ "'" + maxDurability + "', "
				+ currentDurability + ");"); */
		return true;
	}
	
	@Override
	public void removeFromWorld() {
	//	save();
		this.getContainer().removeItemFromLocation(this);
		WorldServer.gameState.removeItem(this.getName() + this.getId());
	}

	@Override
	public int compareTo(Holdable other) {
		String thisItem = this.getName()+this.getId();
		String otherItem = other.getName()+other.getId();
		System.out.println(thisItem.compareToIgnoreCase(otherItem));
		return thisItem.compareToIgnoreCase(otherItem);
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (!(obj instanceof StdItem)) {
	    	return false;
	    }
	    final Holdable other = (Holdable) obj;
	    String thisHoldable = this.getName() + this.getId();
	    String otherHoldable = other.getName() + other.getId();
	    return thisHoldable.equals(otherHoldable);
	 //   if ((thisHoldable.compareToIgnoreCase(otherHoldable)) >= 0) {
	  //  	return true;
	   // }
	//    return false;
	}
	
	public ItemBuilder newBuilder() {
		ItemBuilder newBuilder = new ItemBuilder();
		newBuilder.setId(this.id + 1);  // THIS IS REALLY BAD, need way of getting new ids
		newBuilder.setName(name);
		newBuilder.setDamageMult(physicalMult);
		newBuilder.setDescription(description);
		newBuilder.setBalanceMult(balanceMult);
		newBuilder.setItemContainer(itemLocation);
		newBuilder.setItemType(ItemType.STDITEM);
	//	newBuilder.setAllowedSlots(allowedEquipSlots);
		newBuilder.setComponents(components);
		newBuilder.setSalvageable(salvageable);		
		return newBuilder;
	}
}
