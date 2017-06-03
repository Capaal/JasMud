package items;

import interfaces.Container;
import items.Drinkable.DrinkType;
import items.Plant.PlantType;
import items.Weapon.MercEffect;
import items.Mineable.OreType;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import processes.WorldServer;
import processes.Equipment.EquipmentEnum;

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
	private  Weapon.MercEffect mercEffect = null;
	
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
	
	//mining stuff
	private int maxOres = 0;
	private OreType oreType;
	public int getMaxOres() {return maxOres;}
	public void setMaxOres(int ores) {this.maxOres = ores;}
	public OreType getOreType() {return oreType;}
	public void setOreType(OreType oreType) {this.oreType = oreType;}
	//end mining stuff
	
	//herbPouch stuff
	private int maxHerbs = 1000;
	public int getMaxHerbs() {return maxHerbs;}
	public void setMaxHerbs(int herbs) {this.maxHerbs = herbs;}
	//end herbPouch stuff
	
	//herb stuff
	private PlantType herbType;
	public PlantType getPlantType() {return herbType;}
	public void setPlantType(PlantType herbType) {this.herbType = herbType;}
	//end herb stuff

	
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
		},
			
		STATIONARYITEM() {
			@Override public StdItem produceType(ItemBuilder build) {
				return new StationaryItem(build);
			}
		},
		
		MINEABLE() {
			@Override public StdItem produceType(ItemBuilder build) {
				return new Mineable(build);
			}
		},
		
		WEAPON() {
			@Override public StdItem produceType(ItemBuilder build) {
				return new Weapon(build);
			}
		},
		
		PLANT() {
			@Override public StdItem produceType(ItemBuilder build) {
				return new Plant(build);
			}
		},
		
		BAG() {
			@Override public StdItem produceType(ItemBuilder build) {
				return new Bag(build);
			}
		},
		
		HERBPOUCH() {
			@Override public StdItem produceType(ItemBuilder build) {
				return new Pouch(build);
			}

		};
		
		private ItemType() {};
		public StdItem produceType(ItemBuilder build) {
			return null;			
		}
				
	}

	public Weapon.MercEffect getMercEffect() {
		return this.mercEffect;
	}
	
	public void setMercEffect(MercEffect a) {
		mercEffect = a;
	}
}
