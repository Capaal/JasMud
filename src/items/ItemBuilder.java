package items;

import interfaces.Container;
import items.Drinkable.DrinkType;
import items.MercWeapon.MercEffect;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import processes.WorldServer;
import processes.Equipment.EquipmentEnum;
import skills.Attack;

public class ItemBuilder {
	
	private double damageMult = 1.0;
	private int id = -1;
	private String name = "";
	private String description = "default";
	private double balanceMult = 1.0;
	private double defenseMult = 0;
	private int maxDurability = 1;
	private int currentDurability = 1;
	private Container itemContainer = WorldServer.gameState.viewLocations().get(1);		
	private EnumSet<EquipmentEnum> allowedSlots = EnumSet.of(EquipmentEnum.RIGHTHAND, EquipmentEnum.LEFTHAND);// EnumSet.noneOf(EquipmentEnum.class);
	private List<String> components = new ArrayList<String>();
	private boolean salvageable = false;
	private static Map<String, Integer> idMap = new HashMap<String, Integer>();
	private StdItem finishedItem =  null;
	private  MercWeapon.MercEffect mercEffect = null;
	
	// Declares which specific item type to produce.
	private ItemType typeToProduce = ItemType.STDITEM;
	
	/*
	 * Specific to StackableItem types
	 */
	private int quantity = 1;
	private String descriptionSingle = "";
	
	// Done with StackableItem
	
	//potion stuff
	private int maxSips = 0;
	private DrinkType drinkType;
	public int getMaxSips() {return maxSips;}
	public void setMaxSips(int sips) {this.maxSips = sips;}
	public DrinkType getDrinkType() {return drinkType;}
	public void setDrinkType(DrinkType drinkType) {this.drinkType = drinkType;}
	//end potions stuff
	
	public void setItemType (ItemType type) {
		this.typeToProduce = type;
	}
	
	public void setDescriptionSingle(String desc) {
		this.descriptionSingle = desc;
	}
	
	public String getDescriptionSingle() {
		return descriptionSingle;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
	
	public void setBalanceMult(double balanceMult) {
		this.balanceMult = balanceMult;
	}
	
	public double getBalanceMult() {
		return balanceMult;
	}
	
	public void setDefenseMult(double defenseMult) {
		this.defenseMult = defenseMult;
	}
	
	public double getDefenseMult() {
		return defenseMult;
	}
	
	public void setMaxDurability(int maxDurability) {
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
	}		
	
	public void setItemContainer(Container itemLocation) {
		this.itemContainer = itemLocation;
	}
	
	public Container getItemContainer() {
		return itemContainer;
	}
	
	public void setAllowedSlots(EnumSet<EquipmentEnum> allowedSlots) {
		this.allowedSlots = allowedSlots;
	}
	public void setAllowedSlots(EquipmentEnum allowedSlots) {
		this.allowedSlots.add(allowedSlots);
	}	
	
	public Set<EquipmentEnum> getAllowedSlots() {
		return EnumSet.copyOf(allowedSlots);
	}
	
	public void setComponents(List<String> allComponent) {
		this.components = allComponent;
	}

	
	public List<String> getComponents() {
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
	public void complete() {
		handleId();
		finishedItem = typeToProduce.produceType(this);
	}
	
	private synchronized void handleId() {
		if (idMap.containsKey(this.name)) {
			this.id = idMap.get(this.name) + 1; 
			idMap.put(this.name, this.id);					
		} else {
			this.id = 1;
			idMap.put(this.name,  this.id);
		}
	}
	
	public StdItem getFinishedItem() {
		return finishedItem;
	}
	
	// BAD!!! Needs to be re-done to obtain valid IDs TODO
	private void setId() throws IllegalStateException {
		// Need to generate valid IDs based on what currently exists
		id = 10; // But instead, only 1 item can exist
/*		String sqlQuery = "SELECT sequencetable.sequenceid FROM sequencetable"
				+ " LEFT JOIN itemstats ON sequencetable.sequenceid = itemstats.itemid"
				+ " WHERE itemstats.itemid IS NULL";
		Object availableId = WorldServer.databaseInterface.viewData(sqlQuery, "sequenceid");
		if (availableId == null || !(availableId instanceof Integer)) {
			WorldServer.databaseInterface.increaseSequencer();
			availableId = WorldServer.databaseInterface.viewData(sqlQuery, "sequenceid");
			if (availableId == null || !(availableId instanceof Integer)) {
				throw new IllegalStateException("The item could not determine a valid id, it is invalid.");				
			} else {
				id = (int)availableId;
			}
		} else {
			id = (int)availableId;
		}	*/	
	}
/*	
	public static boolean newItem(Mobile player, ItemBuilder builderItem) {
		String nextTask = Godcreate.askQuestion("What next? 1:name 2:longdescription 3:physicalmult"
				+ " 4:balancemult 5:location 6:maxdurability 7:currentdurability 8:types 9:tags 10:slots 11:preview"
				+ " 12:complete 13:quit", player);
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
		case "physicalmult":
			try {
				builderItem.setDamageMult(Double.parseDouble(Godcreate.askQuestion("What is the physical multiplier for this item? 1.0 is normal.", player)));
			} catch(NumberFormatException e) {
				player.tell("That was not a valid number, be sure you use only doubles i.e. 1.0");
			}
			return newItem(player, builderItem);
		case "4":
		case "balancemult": // TODO can't seem to do things like ".2" the decimal is being lost somewhere
			try {
				builderItem.setBalanceMult(Double.parseDouble(Godcreate.askQuestion("What is the balance multiplier for this item? 1.0 is normal.", player)));
			} catch(NumberFormatException e) {
				player.tell("That was not a valid number, be sure you use only doubles i.e. 1.0");
			}
			return newItem(player, builderItem);
		case "5":
		case "location":
			String tryLocation = Godcreate.askQuestion("Which location should the item be in upon creation? here or the location id", player);
			Container newItemLocation;	
			if ("here".equals(tryLocation.toLowerCase())) {
				newItemLocation = player.getContainer();
			} else {
				try {
					newItemLocation = WorldServer.gameState.viewLocations().get(Integer.parseInt(tryLocation));
				} catch(NumberFormatException e) {
					player.tell("That wasn't a valid number, be sure to keep it to just a number i.e. 1");
					return newItem(player, builderItem);
				}
			}			
			builderItem.setItemContainer(newItemLocation);
			return newItem(player, builderItem);
		case "6":
		case "maxdurability":
			try {
				builderItem.setMaxDurability(Integer.parseInt(Godcreate.askQuestion("What is this item's max durablity?", player)));
			} catch(NumberFormatException e) {
				player.tell("That was not a valid number, keep it to integers i.e. 100");
			}
			return newItem(player, builderItem);
		case "7":
		case "currentdurability":
			try {
				builderItem.setCurrentDurability(Integer.parseInt(Godcreate.askQuestion("What is this item's current durablity?", player)));
			} catch(NumberFormatException e) {
				player.tell("That was not a valid number, keep it to integers i.e. 100");
			}
			return newItem(player, builderItem);
		case "8":
		case "types":
			try {
				builderItem.setTypes(Type.valueOf((Godcreate.askQuestion("What type will the item have?", player).toUpperCase())));
			} catch(IllegalArgumentException e) {
				player.tell("That was not a valid type, keep it to Types like SHARP");
			}
			return newItem(player, builderItem);
		case "9":
		case "tags":
			try {
				builderItem.setItemTags(ItemType.valueOf((Godcreate.askQuestion("What tags will the item have?", player).toUpperCase())));
			} catch(IllegalArgumentException e) {
				player.tell("That was not a valid tag, keep it to ItemTags like WOODEN");
			}
			return newItem(player, builderItem);
		case "10":
		case "slots":
			try {
				builderItem.setAllowedSlots(EquipmentEnum.valueOf(Godcreate.askQuestion("What slot may be filled?", player).toUpperCase()));
			} catch (IllegalArgumentException e) {
				player.tell("that was not a valid enum choice");
				return newItem(player, builderItem);
			}
			return newItem(player, builderItem);
		case "11":
		case "preview":
			player.tell("Name: " + builderItem.name);
			player.tell("Id: " + builderItem.id);
			player.tell("Location: " + builderItem.itemContainer.getName());
			player.tell("Long Description: " + builderItem.description);
			player.tell("Physical Multiplier: " + builderItem.damageMult);
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
		case "12":
		case "complete":
			builderItem.complete();
			return true;
		case "13":
		case "quit":
		case "exit":
			return false;
		default:
			player.tell("That wasn't a valid option.");
			return newItem(player, builderItem);
		}
	}*/
	
	// Used to mean something else (which was meaningless
	// Defines what to produce.
	public enum ItemType {
		
		STDITEM() {
			@Override public StdItem produceType(ItemBuilder build) {
				return new StdItem(build);
			}
			
		},		
		STACKABLEITEM() {
			@Override public StdItem produceType(ItemBuilder build) {
				return new StackableItem(build);
			}
		},
			
		DRINKABLE() {
			@Override public StdItem produceType(ItemBuilder build) {
				return new Drinkable(build);
			}
			
		};
		
		private ItemType() {};
		public StdItem produceType(ItemBuilder build) {
			return null;			
		}
				
	}

	public MercWeapon.MercEffect getMercEffect() {
//		return this.mercEffect;
//	}
	
//	public void setMercEffect(MercEffect a) {
//		mercEffect = a;
//	}
}
