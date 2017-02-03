package items;

import java.util.*;

import interfaces.*;
import processes.*;
import processes.Equipment.EquipmentEnum;

public class StdItem implements Holdable, Weapon {
	
	private final String name;
	private final int id;	
	private final String description;
	private final double physicalMult;
	private final double balanceMult; // Currently unused
	
	private Container itemLocation;	
	private final int maxDurability;
	private int currentDurability;
	private final List<Type> types; // Add to WEAPON interface?
	private final List<ItemType> itemTags; // add to Weapon interface? or delete?
	private final List<String> components; // Add to weapon interface? Make a craftable interface?
	private final boolean salvageable; // same as components?
	
	private final Set<EquipmentEnum> allowedEquipSlots;

	public StdItem(ItemBuilder build) {
		this.id = build.getId();
		this.name = build.getName();
		this.physicalMult = build.getDamageMult();
		this.description = build.getDescription();
		this.balanceMult = build.getBalanceMult();
		this.itemLocation = build.getItemContainer();		
		this.maxDurability = build.getMaxDurability();
		this.currentDurability = build.getCurrentDurability();
		this.types = build.getTypes();
		this.itemTags = build.getItemTags();
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
	
	public int getMaxDurability() {return maxDurability;}
	
	public synchronized int getCurrentDurability() {return currentDurability;}
	public synchronized void setDurability(int newDurability) {
		if (newDurability > maxDurability) {
			newDurability = maxDurability;
		}
		this.currentDurability = newDurability;
	}
	
	public Set<EquipmentEnum> getAllowedEquipSlots() {return allowedEquipSlots;}
	public List<Type> getTypes() {return types;}
	public List<ItemType> getItemTags() {return itemTags;}
	public List<String> getComponents() {return components;}
	
	@Override
	public boolean containsType(Type type) {
		return types.contains(type);
	}
	
	public double getDamageMult() {
		return physicalMult;
	}
	
	// not sure how packs will work yet...
	@Override
	public boolean save() {			
		String updateItem = "UPDATE ITEMSTATS SET ITEMCURDUR=" + currentDurability + ", WHERE ITEMID=" + getId() + ";";
		WorldServer.databaseInterface.saveAction(updateItem);
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
		databaseInterface.saveAction("Insert into ITEMSTATS (ITEMID, ITEMNAME, ITEMPHYS, ITEMBAL, ITEMDESC, ITEMMAXDUR, ITEMCURDUR, ITEMLOC, EQUIPSLOTS)"
				+ " values ("
				+ "'" + id + "', "
				+ "'" + name + "', "
				+ "'" + physicalMult + "', "
				+ "'" + balanceMult + "', "
				+ "'" + description + "', "
				+ "'" + maxDurability + "', "
				+ currentDurability + ");"); 
		return true;
	}
	
	@Override
	public void removeFromWorld() {
	//	save();
		this.getContainer().removeItemFromLocation(this);
		WorldServer.gameState.removeItem(this.getName() + this.getId());
	}
	
	// Unused and unimplemented, should probably be re-worked or removed.
	public enum ItemType {
		
		MATERIAL() {
			
		},
		
		PLANT() {
			
		},
		
		WOODWORKING() {
			
		},
		
		ENCHANTABLE() {
			
		},
		
		METALWORKING() {
			
		};
		
		private ItemType() {};
				
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
}
