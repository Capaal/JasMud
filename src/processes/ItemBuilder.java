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
	public int id = 1;
	public String name = "";
	public String description = "default";
	public String shortDescription = "shortDefault";
	public double balanceMult = 1.0;
	public int maxDurability = 1;
	public int currentDurability = 1;
	public ArrayList<Type> types = new ArrayList<Type>();
	public ArrayList<ItemType> itemTags = new ArrayList<ItemType>();
	public Container itemLocation = WorldServer.gameState.locationCollection.get(1);		
	public EnumSet<EquipmentEnum> allowedSlots = EnumSet.noneOf(EquipmentEnum.class);
	
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
	public void setTypes(Type type) {
		types.add(type);
	}	
	public void setItemTags(ArrayList<ItemType> itemTags) {
		this.itemTags = itemTags;
	}	
	public void setItemTags(ItemType itemTag){
		itemTags.add(itemTag);
	}	
	public void setItemLocation(Container itemLocation) {
		this.itemLocation = itemLocation;
	}
	public void setAllowedSlots(EnumSet<EquipmentEnum> allowedSlots) {
		this.allowedSlots = allowedSlots;
	}
	public void setAllowedSlots(EquipmentEnum allowedSlots) {
		this.allowedSlots.add(allowedSlots);
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
	
	
	public StdItem complete() {
		return new StdItem(this);		
	}
	
	public static boolean newItem(Mobile player, ItemBuilder builderItem) {
		String nextTask = Godcreate.askQuestion("What next? 1:name 2:longdescription 3:shortdescription 4:physicalmult"
				+ " 5:balancemult 6:location 7:maxdurability 8:currentdurability 9:types 10:tags 11:slots 12:preview"
				+ " 13:complete 14:quit", player);
		switch(nextTask) {
		case "1":
		case "name":
			builderItem.setName(Godcreate.askQuestion("What is the name of this new item?", player));
			return newItem(player, builderItem); 		
		case "2":
		case "longdescription":
			builderItem.setDescription(Godcreate.askQuestion("What is the long description for this item?", player));
			return newItem(player, builderItem); 
		case "3":
		case "shortdescrption":
			builderItem.setShortDescription(Godcreate.askQuestion("What is the short description for this item?", player));
			return newItem(player, builderItem);
		case "4":
		case "physicalmult":
			try {
				builderItem.setPhysicalMult(Double.parseDouble(Godcreate.askQuestion("What is the physical multiplier for this item? 1.0 is normal.", player)));
			} catch(NumberFormatException e) {
				player.tell("That was not a valid number, be sure you use only doubles i.e. 1.0");
			}
			return newItem(player, builderItem);
		case "5":
		case "balancemult":
			try {
				builderItem.setBalanceMult(Double.parseDouble(Godcreate.askQuestion("What is the balance multiplier for this item? 1.0 is normal.", player)));
			} catch(NumberFormatException e) {
				player.tell("That was not a valid number, be sure you use only doubles i.e. 1.0");
			}
			return newItem(player, builderItem);
		case "6":
		case "location":
			String tryLocation = Godcreate.askQuestion("Which location should the item be in upon creation? here or the location id", player);
			Container newItemLocation;	
			if ("here".equals(tryLocation.toLowerCase())) {
				newItemLocation = player.getContainer();
			} else {
				try {
					newItemLocation = WorldServer.gameState.locationCollection.get(Integer.parseInt(tryLocation));
				} catch(NumberFormatException e) {
					player.tell("That wasn't a valid number, be sure to keep it to just a number i.e. 1");
					return newItem(player, builderItem);
				}
			}			
			builderItem.setItemLocation(newItemLocation);
			return newItem(player, builderItem);
		case "7":
		case "maxdurability":
			try {
				builderItem.setMaxDurability(Integer.parseInt(Godcreate.askQuestion("What is this item's max durablity?", player)));
			} catch(NumberFormatException e) {
				player.tell("That was not a valid number, keep it to integers i.e. 100");
			}
			return newItem(player, builderItem);
		case "8":
		case "currentdurability":
			try {
				builderItem.setCurrentDurability(Integer.parseInt(Godcreate.askQuestion("What is this item's current durablity?", player)));
			} catch(NumberFormatException e) {
				player.tell("That was not a valid number, keep it to integers i.e. 100");
			}
			return newItem(player, builderItem);
		case "9":
		case "types":
			try {
				builderItem.setTypes(Type.valueOf((Godcreate.askQuestion("What type will the item have?", player).toUpperCase())));
			} catch(IllegalArgumentException e) {
				player.tell("That was not a valid type, keep it to Types like SHARP");
			}
			return newItem(player, builderItem);
		case "10":
		case "tags":
			try {
				builderItem.setItemTags(ItemType.valueOf((Godcreate.askQuestion("What tags will the item have?", player).toUpperCase())));
			} catch(IllegalArgumentException e) {
				player.tell("That was not a valid tag, keep it to ItemTags like WOODEN");
			}
			return newItem(player, builderItem);
		case "11":
		case "slots":
			try {
				builderItem.setAllowedSlots(EquipmentEnum.valueOf(Godcreate.askQuestion("What slot may be filled?", player).toUpperCase()));
			} catch (IllegalArgumentException e) {
				player.tell("that was not a valid enum choice");
				return newItem(player, builderItem);
			}
			return newItem(player, builderItem);
		case "12":
		case "preview":
			player.tell("Name: " + builderItem.name);
			player.tell("Id: " + builderItem.id);
			player.tell("Long Description: " + builderItem.description);
			player.tell("Short description: " + builderItem.shortDescription);
			player.tell("Physical Multiplier: " + builderItem.physicalMult);
			player.tell("Balance Multiplier: " + builderItem.balanceMult);
			player.tell("Max Durability: " + builderItem.maxDurability);
			player.tell("Current Durability: " + builderItem.currentDurability);
			StringBuilder sb = new StringBuilder();
			sb.append("Types:");
			for (Type t : builderItem.types) {
				sb.append(" ");
				sb.append(t.toString());
			}
			player.tell(sb.toString());
			sb = new StringBuilder();
			sb.append("Item Tags:");
			for (ItemType it : builderItem.itemTags) {
				sb.append(" ");
				sb.append(it.toString());
			}
			player.tell(sb.toString());
			sb = new StringBuilder();
			sb.append("Allowed Slots:");
			for (EquipmentEnum ee : builderItem.allowedSlots) {
				sb.append(" ");
				sb.append(ee.toString());
			}
			player.tell(sb.toString());
			return newItem(player, builderItem);
		case "13":
		case "complete":
			builderItem.complete();
			return true;
		case "14":
		case "quit":
		case "exit":
			return false;
		default:
			player.tell("That wasn't a valid option.");
			return newItem(player, builderItem);
		}
	}
}
