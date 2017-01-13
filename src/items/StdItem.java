package items;

import java.sql.SQLException;
import java.util.*;
import interfaces.*;
import processes.*;
import processes.Equipment.EquipmentEnum;

public class StdItem implements Holdable {
	
	private final String name;
	private final int id;	
	private final String description;
	private final double physicalMult;
	private final double balanceMult;
	private Container itemLocation;	
	private final int maxDurability;
	private int currentDurability;
	private final List<Type> types;
	private final List<ItemType> itemTags;
	
	private final Set<EquipmentEnum> allowedEquipSlots;

	public StdItem(ItemBuilder build) {
		this.id = build.getId();
		this.name = build.getName();
		this.physicalMult = build.getPhysicalMult();
		this.description = build.getDescription();
		this.balanceMult = build.getBalanceMult();
		this.itemLocation = build.getItemContainer();		
		this.maxDurability = build.getMaxDurability();
		this.currentDurability = build.getCurrentDurability();
		this.types = build.getTypes();
		this.itemTags = build.getItemTags();
		this.allowedEquipSlots = build.getAllowedSlots();
		WorldServer.gameState.addItem(name + id, this);
		itemLocation.acceptItem(this);
	}
	
	public String getName() {return name;}
	public int getId() {return id;}
	public String getDescription() {return description;}	
	public double getPhysicalMult() {return physicalMult;}
	public double getBalanceMult() {return balanceMult;}
	
	public synchronized void setContainer(Container con) {this.itemLocation = con;}	
	public synchronized Container getContainer() {return itemLocation;}	
	
	public int getMaxDurability() {return maxDurability;}
	
	public synchronized int getCurrentDurability() {return currentDurability;}
	public synchronized void setDurability(int newDurability) {
		if (newDurability > maxDurability) {
			newDurability = maxDurability;
		}
		this.currentDurability = newDurability;
	}
	
	@Override
	public Set<EquipmentEnum> getAllowedEquipSlots() {return allowedEquipSlots;}
	public List<Type> getTypes() {return types;}
	public List<ItemType> getItemTags() {return itemTags;}
	
	@Override
	public boolean containsType(Type type) {
		return types.contains(type);
	}
	
	@Override
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
		save();
		this.getContainer().removeItemFromLocation(this);
		WorldServer.gameState.removeItem(this.getName() + this.getId());
	}
	
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
}
