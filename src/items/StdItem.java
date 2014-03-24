package items;

/**
 * Ok, current idea for items. Many items will be StdItem, where the only differences will be name id description, short description and other stats.
 * Other ideas that require special code will extend stditem, such as stackable items, wearable items, consumable items, rideable items.
 * There will exist in a secret room a single version of each unique type, which is defined by a class. These unique defaults will be used
 * as copyable versions to be changed and then used.
 * Consumable items will use actions blocks just like skills in order to define their use and their effects.
 * 
 * id
 * description
 * shortdescription
 * current durability
 * max durability
 * physical multiplier
 * balance multiplier
 * location
 * location type (location, inventory, pack, pouch)
 * types list (SHARP, BLUNT, PIERCE, TWOHANDED and so on.)
 * item type (tags for how they can be used, ie metal crafted for skills to know they can be crafted by metalworkers, materials, plants, tools etc.)
 * weight
 * 
 * special things:
 * coins/herbs, stackables
 * horses/turtles, rideables
 * food/potions/enchanted items with spells, consumables
 * packs/pouches, containers, though those two examples might need to be seperated?
 * wearable? it needs to change stats or other things, needs to know where to be worn
 * 
 * Need to implement item? Item makes things holdable and creatable, both of which all items are. But what about trees? They are unmovable.
 * Is there any item that is NOT holdable? Creatable? (Creatable by gods, different from craftable).
 * 
 * A REASON to use HOLDABLE and CREATABLE, is because other things might be these things as well as item.
 * Such as mobiles are holdable and creatable as well? I think thats it though, so interfaces for these seem silly.
 * So really, just some interface that represents everything all mobiles and items have in common, and have both impletement this seems best.
 * 
 * DATABASE INFORMATION:
 * 		The database will be designed on a super-table principle. ONE table representing ALL items. This means there will be a lot of columns needed
 * 		for more complicated item types that will be unused by other items, and that the item type will define what is loaded.
 * 		It shouldn't matter though, as it only defines what info is loaded, and any unused columns aren't even viewed.
 */



import java.sql.SQLException;
import java.util.*;

import interfaces.*;
import processes.*;
import processes.Equipment.EquipmentEnum;
// all items are not actually equipable, should be pushed down to subclass
public class StdItem implements Holdable {
	
	private final String name;
	private final int id;	
	private final String description;
	private final String shortDescription;
	private final double physicalMult;
	private final double balanceMult;
	private Container itemLocation;	
	private final int maxDurability;
	private int currentDurability;
	private final ArrayList<Type> types;
	private final ArrayList<ItemType> itemTags;
	
	private final EnumSet<EquipmentEnum> allowedEquipSlots;

	public StdItem(ItemBuilder build) {
		if (WorldServer.gameState.allItems.containsKey(build.id)){ 
			this.id = setId();
		} else {
			this.id = build.id;
		}
		this.name = build.name;
		this.physicalMult = build.physicalMult;
		this.description = build.description;
		this.balanceMult = build.balanceMult;
		this.itemLocation = build.itemLocation;		
		this.shortDescription = build.shortDescription;
		this.maxDurability = build.maxDurability;
		this.currentDurability = build.currentDurability;
		this.types = build.types;
		this.itemTags = build.itemTags;
		this.allowedEquipSlots = build.allowedSlots;
		WorldServer.gameState.allItems.put(name + id, this);
		itemLocation.acceptItem(this);
	}
	
	public String getName() {return name;}
	public int getId() {return id;}
	public String getDescription() {return description;}	
	public String getShortDescription() {return shortDescription;}
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
	public EnumSet<EquipmentEnum> getAllowedEquipSlots() {return allowedEquipSlots;}
	public ArrayList<Type> getTypes() {return types;}
	public ArrayList<ItemType> getItemTags() {return itemTags;}
	
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
		String locationType = null;
		if (itemLocation instanceof Location) {
			locationType = "LOCATION";
			String updateItem = "UPDATE ITEMSTATS SET EQUIPSLOT=null WHERE ITEMID=" + getId() + ";";
			WorldServer.databaseInterface.saveAction(updateItem);
		} else if (itemLocation instanceof StdMob) {
			locationType = "INVENTORY";
			String updateItem = "UPDATE ITEMSTATS SET EQUIPSLOT=null WHERE ITEMID=" + getId() + ";";
			WorldServer.databaseInterface.saveAction(updateItem);
		} else {
			locationType = "EQUIPMENT";
			String updateItem = "UPDATE ITEMSTATS SET EQUIPSLOT='" + ((Equipment)getContainer()).getKey(this) + "' WHERE ITEMID=" + getId() + ";";
			WorldServer.databaseInterface.saveAction(updateItem);
		} 
		String updateItem = "UPDATE ITEMSTATS SET ITEMCURDUR=" + currentDurability + ", ITEMLOC=" + itemLocation.getId() 
				+ ", ITEMLOCTYPE='" + locationType + "' WHERE ITEMID=" + getId() + ";";
		WorldServer.databaseInterface.saveAction(updateItem);
		return true;
	}
	
	@Override
	public void removeFromWorld() {
		save();
		WorldServer.gameState.allItems.remove(this.getName() + this.getId());
	}
	
	private int setId() {
		String sqlQuery = "SELECT sequencetable.sequenceid FROM sequencetable"
				+ " LEFT JOIN itemstats ON sequencetable.sequenceid = itemstats.itemid"
				+ " WHERE itemstats.itemid IS NULL";		
		Object availableId = (int) WorldServer.databaseInterface.viewData(sqlQuery, "sequenceid");
		if (availableId == null || !(availableId instanceof Integer)) {
			WorldServer.databaseInterface.increaseSequencer();
			return setId();
		} else {
			return (int)availableId;
		}		
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
