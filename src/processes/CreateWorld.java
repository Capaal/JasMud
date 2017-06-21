package processes;

import items.Bag.BagItemBuilder;
import items.Door;
import items.Drinkable.DrinkType;
import items.Drinkable.DrinkableItemBuilder;
import items.Harvestable.HarvestableItemBuilder;
import items.Plant.PlantItemBuilder;
import items.Plant.PlantType;
import items.ItemBuilder;
import items.Pouch.PouchItemBuilder;
import items.StackableItem.StackableItemBuilder;
import items.StdItem;
import items.Weapon.MercEffect;
import items.Harvestable.HarvestType;
import items.Weapon.WeaponItemBuilder;
import processes.Equipment.EquipmentEnum;
import processes.Location.Direction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import Quests.FarmerQuest;
import skills.*;
import skills.Mercenary.*;

public class CreateWorld {
	
	
	public static SkillBook generalSkills = new SkillBook("generalSkills", 1);
	private static Map<String, ItemBuilder> itemTemplates = new TreeMap<String, ItemBuilder>(); //list of all template items
	
	public static void setTemplates(Map<String, ItemBuilder> newTemplates) {
		itemTemplates = newTemplates;
	}
	
	public static void createWorldWithItems() {
		makeWorldFromNowhere();
		makeSkills();
		makeItems();
		spawnMobs();
	}
	
	public static void createWorld() {
		makeWorldFromNowhere();
		makeSkills();
		spawnMobs();
	}
	
	//Hardcoded skill list
	public static void makeSkills() {
		WorldServer.gameState.addBook(1, generalSkills);
		generalSkills.addSkill(new SkillList());
		//general
		generalSkills.addSkill(new Punch());
		generalSkills.addSkill(new Throw());
		generalSkills.addSkill(new Get());
		generalSkills.addSkill(new Move());
		generalSkills.addSkill(new Inventory());
		generalSkills.addSkill(new Shoot());
		generalSkills.addSkill(new Give());
		generalSkills.addSkill(new Look());
		generalSkills.addSkill(new Drop());
		generalSkills.addSkill(new Info());
		generalSkills.addSkill(new Wield());
		generalSkills.addSkill(new Drink());
		generalSkills.addSkill(new Say());
		generalSkills.addSkill(new Struggle());
		generalSkills.addSkill(new Eat());
		generalSkills.addSkill(new Put());
		generalSkills.addSkill(new TakeOut());
		generalSkills.addSkill(new Open());
		generalSkills.addSkill(new Close());
		generalSkills.addSkill(new Follow());
		generalSkills.addSkill(new Apply());
		generalSkills.addSkill(new Examine());
		generalSkills.addSkill(new Mine());
		generalSkills.addSkill(new Chop()); //same as mine except for many messages..
		generalSkills.addSkill(new Sleep());
		generalSkills.addSkill(new Wake());
		generalSkills.addSkill(new Unwield());
		generalSkills.addSkill(new Diagnose());
		//crafting
		generalSkills.addSkill(new CraftItem());
		generalSkills.addSkill(new Salvage());
		//quest
		generalSkills.addSkill(new Greet());
		generalSkills.addSkill(new Nod());
		//mage?
		generalSkills.addSkill(new Heal());
		generalSkills.addSkill(new Root());
		generalSkills.addSkill(new VineTrip());
		generalSkills.addSkill(new LightningWhip());
		//mercenary only
		generalSkills.addSkill(new Headshot());
		generalSkills.addSkill(new BreakLimb());
		generalSkills.addSkill(new Intimidate());
		generalSkills.addSkill(new Straighten());
		generalSkills.addSkill(new SpinKick());
		generalSkills.addSkill(new Clearmind());
		generalSkills.addSkill(new Shove());
		generalSkills.addSkill(new Attack());
		generalSkills.addSkill(new DualAttack());
		generalSkills.addSkill(new Staunch());
		generalSkills.addSkill(new MercRegenSkill());
		generalSkills.addSkill(new Shieldblock());

	}
	
	public static void spawnMobs() {
		makeGoblin();
		makeFarmerJames();
		makeHorse();
	}
	
	public static void makeItems() {
		addOre();
		makeIngot();
		makeADagger();
		makeASword();
		makeAStick();
		makeAPike();
		makeAShield();
		addIronPotion();
		
		addHealPotion();
		addBleedPotion();
		addRegenPotion();
		addOreRock();	
		addTree();
		makeAloeHerb();	
		makeComfreyHerb();
		makeOleander();
		makeValerianPoison();
		makeBelladonnaPoison();
		addPouch();
		makeGinsengHerb();
		makeBag();
	}
	
	public static void makeWorldFromNowhere() {

		//first location, north exit to 2
		LocationBuilder firstLoc = new LocationBuilder();
		firstLoc.setName("Start.");
		firstLoc.setDescription("You have to start somewhere");
		firstLoc.complete();	

		//2nd location, south exit to 1, north exit to 3
		LocationBuilder newLoc2 = new LocationBuilder();
		newLoc2.setName("North of Start.");
		newLoc2.setDescription("Slightly north.");
		newLoc2.addLocationConnection(Direction.SOUTH, 1, Direction.NORTH, new Door());
		newLoc2.complete();	
	
		//3rd location, south exit to 2
		LocationBuilder newLoc3 = new LocationBuilder();
		newLoc3.setName("Road.");
		newLoc3.setDescription("On the road to nowhere.");
		newLoc3.addLocationConnection(Direction.SOUTH, 2, Direction.NORTH, null);
		newLoc3.complete();	
		
		//4th location, south exit to 3
		LocationBuilder newLoc4 = new LocationBuilder();
		newLoc4.setName("Along the road.");
		newLoc4.setDescription("A road passing by some farmlands.");		
		newLoc4.setQuest(new FarmerQuest());
		newLoc4.addLocationConnection(Direction.SOUTH, 3, Direction.NORTH, null);
		newLoc4.complete();	
		
		//5th location, south exit to 4
		LocationBuilder newLoc5 = new LocationBuilder();
		newLoc5.setName("On a bridge.");
		newLoc5.setDescription("A bridge over a dried creek.");
		newLoc5.addLocationConnection(Direction.SOUTH, 4, Direction.NORTH, null);
		newLoc5.complete();	
		
		//6th location, west exit to 5
		LocationBuilder newLoc6 = new LocationBuilder();
		newLoc6.setName("Forest trail.");
		newLoc6.setDescription("Off the main path.");
		newLoc6.addLocationConnection(Direction.WEST, 5, Direction.EAST, null);
		newLoc6.complete();	
		
		//7th location, west exit to 6
		LocationBuilder newLoc7 = new LocationBuilder();
		newLoc7.setName("End of trail.");
		newLoc7.setDescription("Oh no, it's a dead end.");
		newLoc7.addLocationConnection(Direction.WEST, 6, Direction.EAST, null);
		newLoc7.addLocationConnection(Direction.IN, 2, Direction.EAST, null);
		newLoc7.complete();	
		
		//8th location, south exit to 5
		LocationBuilder newLoc8 = new LocationBuilder();
		newLoc8.setName("Loop.");
		newLoc8.addLocationConnection(Direction.SOUTH, 5, Direction.NORTH, null);
		newLoc8.addLocationConnection(Direction.NORTH, 5, Direction.NORTH, null);
		newLoc8.complete();	
		
		//9th location, east exit to 1
		LocationBuilder newLoc9 = new LocationBuilder();
		newLoc9.setName("Bank.");
		newLoc9.setDescription("Not the watery type.");
		newLoc9.addLocationConnection(Direction.EAST, 1, Direction.WEST, null);
		newLoc9.complete();	
		
		makeFloatingIsland();
		
		// map: 
		//   [8](loops 5)
		//   [5] - [6] - [7]
		//   [4]
		//   [3]
		//   [2] - [7]
	// [9] - [1]
		
	}
	
	private static void makeFloatingIsland() {
		makeLandingPad();
		makeMerchantRow();
	}

	private static void makeLandingPad() {
		LocationBuilder newLoc1 = new LocationBuilder();
		newLoc1.setName("Landing entrance.");
		newLoc1.setDescription("This is the primary entrance to the floating island. It's an open platform jutting out into the air. A dock is visible to the north.");
		newLoc1.addLocationConnection(Direction.SOUTH, 7);
		newLoc1.complete();

		LocationBuilder newLoc2 = new LocationBuilder();
		newLoc2.setName("Dock.");
		newLoc2.setDescription("A large dock with multiple receiving points. A few small airships hover nearby, next to two portal terminals. The wind occasionally brings misty water onto the east side of the dock, carried from the waterfall not far off. The westside of the dock offers a view of the busy merchant district, while northwards there is a sprawling, peaceful garden, and in the distance, the inner city dome is visible.");
		newLoc2.addLocationConnection(Direction.SOUTH, 10);
		newLoc2.complete();

		LocationBuilder newLoc3 = new LocationBuilder();
		newLoc3.setName("Landing Pad");
		newLoc3.setDescription("A guardhouse is a few steps eastward, with a guard outside keeping an attentive eye towards the docks. A sign \"Landing's Tavern\" adorns a two-story building to the west.");
		newLoc3.addLocationConnection(Direction.SOUTH, 11);
		newLoc3.complete();

		LocationBuilder newLoc4 = new LocationBuilder();
		newLoc4.setName("Landing Guardhouse");
		newLoc4.setDescription("A clean, grey, unassuming building. At least one guard is always present within.");
		newLoc4.addLocationConnection(Direction.WEST, 12);
		newLoc4.complete();

		LocationBuilder newLoc5 = new LocationBuilder();
		newLoc5.setName("Landing's Tavern");
		newLoc5.setDescription("A well-stocked and clean tavern, serving a surprising variety of food and beverage.");
		newLoc5.addLocationConnection(Direction.EAST, 12);
		newLoc5.complete();

		LocationBuilder newLoc6 = new LocationBuilder();
		newLoc6.setName("Office");
		newLoc6.setDescription("This is a typical office, with a large wooden table as the central fixture, and a chaotic array of too much paperwork.");
		newLoc6.addLocationConnection(Direction.DOWN, 14);
		newLoc6.complete();

		LocationBuilder newLoc7 = new LocationBuilder();
		newLoc7.setName("Landing Street by stables");
		newLoc7.setDescription("The street passes by a stable to the west and nears Main Street. The road north looks older and worn, though still well maintained.");
		newLoc7.addLocationConnection(Direction.SOUTH, 12);
		newLoc7.complete();

		LocationBuilder newLoc8 = new LocationBuilder();
		newLoc8.setName("Stables");
		newLoc8.setDescription("A small stable where travelers can borrow horses for their convenience.");
		newLoc8.addLocationConnection(Direction.EAST, 16);
		newLoc8.complete();
	}

	private static void makeMerchantRow() {
		LocationBuilder newLoc9 = new LocationBuilder();
		newLoc9.setName("South Main Street");
		newLoc9.setDescription("The largest and best maintained road in the city. Lanterns line the street for all-day and all-weather illumination. The inner dome at the far northern end and the Landing Pad, the primary welcoming area for the floating city, to the south.");
		newLoc9.addLocationConnection(Direction.SOUTH, 16);
		newLoc9.complete();

		LocationBuilder newLoc10 = new LocationBuilder();
		newLoc10.setName("Main Street by Merchant Row");
		newLoc10.setDescription("");
		newLoc10.addLocationConnection(Direction.SOUTH, 18);
		newLoc10.complete();

		LocationBuilder newLoc11 = new LocationBuilder();
		newLoc11.setName("Merchant Row");
		newLoc11.setDescription("This road is crowded with small stalls and stores. A variety of smells flow through in the air - food, perfumes, flowers, leathers, and sometimes less appealing scents.");
		newLoc11.addLocationConnection(Direction.EAST, 19);
		newLoc11.complete();

		LocationBuilder newLoc12 = new LocationBuilder();
		newLoc12.setName("Merchant Row by the apothecary");
		newLoc12.setDescription("");
		newLoc12.addLocationConnection(Direction.EAST, 20);
		newLoc12.complete();

		LocationBuilder newLoc13 = new LocationBuilder();
		newLoc13.setName("Apothecary");
		newLoc13.setDescription("");
		newLoc13.addLocationConnection(Direction.SOUTH, 21);
		newLoc13.complete();

		LocationBuilder newLoc14 = new LocationBuilder();
		newLoc14.setName("Curve in Merchant Row");
		newLoc14.setDescription("");
		newLoc14.addLocationConnection(Direction.SOUTHEAST, 21);
		newLoc14.complete();

		LocationBuilder newLoc15 = new LocationBuilder();
		newLoc15.setName("Shop");
		newLoc15.setDescription("");
		newLoc15.addLocationConnection(Direction.SOUTH, 23);
		newLoc15.complete();

		LocationBuilder newLoc16 = new LocationBuilder();
		newLoc16.setName("Warehouse");
		newLoc16.setDescription("");
		newLoc16.addLocationConnection(Direction.SOUTHEAST, 23);
		newLoc16.complete();

		LocationBuilder newLoc17 = new LocationBuilder();
		newLoc17.setName("Crescent Moon Tavern");
		newLoc17.setDescription("");
		newLoc17.addLocationConnection(Direction.EAST, 23);
		newLoc17.complete();

		LocationBuilder newLoc18 = new LocationBuilder();
		newLoc18.setName("Merchant Row sideroad");
		newLoc18.setDescription("");
		newLoc18.addLocationConnection(Direction.NORTHEAST, 23);
		newLoc18.complete();

		LocationBuilder newLoc19 = new LocationBuilder();
		newLoc19.setName("Merchant Row near apartements");
		newLoc19.setDescription("");
		newLoc19.addLocationConnection(Direction.SOUTHEAST, 27);
		newLoc19.complete();

		LocationBuilder newLoc20 = new LocationBuilder();
		newLoc20.setName("Apartments south of Merchant Row");
		newLoc20.setDescription("");
		newLoc20.addLocationConnection(Direction.NORTH, 27);
		newLoc20.complete();

		LocationBuilder newLoc21 = new LocationBuilder();
		newLoc21.setName("Apartments north of Merchant Row");
		newLoc21.setDescription("");
		newLoc21.addLocationConnection(Direction.SOUTH, 27);
		newLoc21.complete();

		LocationBuilder newLoc22 = new LocationBuilder();
		newLoc22.setName("End of Merchant Row");
		newLoc22.setDescription("");
		newLoc22.addLocationConnection(Direction.SOUTHEAST, 27);
		newLoc22.complete();
	}
	
	public static Map<String, ItemBuilder> viewItemTemplates() {
		return new HashMap<String, ItemBuilder>(itemTemplates);
	}
	
	//template items should be stored as builders, not actually existing items
	
	
	public static void addOre() {
		StackableItemBuilder newItem = new StackableItemBuilder();
		newItem.setName("iron");
		newItem.setDescriptionMany("A pile of iron ore chunks.");
		newItem.setDescription("A piece of iron ore.");
		newItem.setDamageMult(0.2);
		newItem.setQuantity(1);
		itemTemplates.put("iron", newItem); // not sure it should be in itemTemplates
	//	newItem.complete();
//		WorldServer.gameState.addItem("ore", newItem.getFinishedItem()); //added here instead of templates, not a craftable item
	}
	
	public static void addLog() {
		StackableItemBuilder newItem = new StackableItemBuilder();
		newItem.setName("log");
		newItem.setDescriptionMany("A pile of wood.");
		newItem.setDescription("Previously part of a tree.");
		newItem.setDamageMult(0.2);
		newItem.setQuantity(1);
		itemTemplates.put("log", newItem); // not sure it should be in itemTemplates
	//	newItem.complete();
	}
	
	public static void makeIngot() {
		ItemBuilder newItem = new ItemBuilder();
		newItem.setWeight(2);
		newItem.setName("ingot");
		newItem.setDescription("An iron ingot.");
		StdItem iron = itemTemplates.get("iron").getNonexistentFinishedItem();
		iron.addToStack(1);
		newItem.setComponents(Arrays.asList(iron)); //TODO should use the object iron instead of string
		newItem.setDamageMult(0.4);
		newItem.setSalvageable(true);
		itemTemplates.put("ingot", newItem);
	}
	
	public static void makeADagger() {
		WeaponItemBuilder newItem = new WeaponItemBuilder();	
		newItem.setName("dagger");
		newItem.setItemContainer(WorldServer.gameState.viewLocations().get(1));
		newItem.setDescription("It's a dagger!");
		newItem.setComponents(Arrays.asList(itemTemplates.get("ingot").getNonexistentFinishedItem()));
		newItem.setSalvageable(true);
		newItem.setWeight(1.5);
		newItem.setAllowedSlots(EquipmentEnum.LEFTHAND);
		newItem.setAllowedSlots(EquipmentEnum.RIGHTHAND);
		newItem.setMercEffect(MercEffect.BLEED);
		newItem.complete();
		itemTemplates.put("dagger", newItem);
	}
	
	public static void makeASword() {
		WeaponItemBuilder newItem = new WeaponItemBuilder();			
		newItem.setName("sword");
		newItem.setDescription("It's a sword!");
		newItem.setComponents(Arrays.asList(itemTemplates.get("ingot").getNonexistentFinishedItem(), itemTemplates.get("ingot").getNonexistentFinishedItem()));
		newItem.setSalvageable(true);
		newItem.setWeight(5);
		newItem.setDamageMult(1.5);
		newItem.setBalanceMult(1.2);
		itemTemplates.put("sword", newItem);
	}
	
	public static void makeAStick() {	
		WeaponItemBuilder newItem = new WeaponItemBuilder();	
		newItem.setName("stick");
		newItem.setDescription("It's an evil stick.");
		newItem.setDamageMult(0.5);
		newItem.setWeight(.5);
		newItem.setMercEffect(MercEffect.FEAR);
//		newItem.complete();
		itemTemplates.put("stick", newItem);
//		StdItem item = newItem.getFinishedItem();
//		String xml = item.firstTimeSave(xstream);
//		StdItem loaditem = (StdItem)xstream.fromXML(xml);
//		System.out.println(loaditem);
	}
	
	public static void makeAPike() {
		WeaponItemBuilder newItem = new WeaponItemBuilder();
		newItem.setName("pike");
		newItem.setDescription("It's a pike!");
		newItem.setComponents(Arrays.asList(itemTemplates.get("dagger").getNonexistentFinishedItem(),itemTemplates.get("stick").getNonexistentFinishedItem()));
		newItem.setDamageMult(1.8);
		newItem.setWeight(6);
		newItem.setBalanceMult(1.5);
		newItem.setSalvageable(true);
		itemTemplates.put("pike", newItem);
	}
	
	public static void makeAShield() {
		WeaponItemBuilder newItem = new WeaponItemBuilder();
		newItem.setName("shield");
		newItem.setDescription("It's a shield!");
		newItem.setComponents(Arrays.asList(itemTemplates.get("ingot").getNonexistentFinishedItem(),itemTemplates.get("ingot").getNonexistentFinishedItem()));
		newItem.setDamageMult(1);
		newItem.setWeight(4);
		newItem.setBalanceMult(1.5);
		newItem.setSalvageable(true);
		itemTemplates.put("shield", newItem);
	}
	
	public static void addIronPotion() {
		DrinkableItemBuilder newItem = new DrinkableItemBuilder();	
		newItem.setName("ironpotion");
		newItem.setDescription("A potion made from iron.");
		newItem.setWeight(.5);
		newItem.setComponents(Arrays.asList(itemTemplates.get("iron").getNonexistentFinishedItem()));
		newItem.setDamageMult(0.2);
		newItem.setMaxSips(2);
		newItem.setDrinkType(DrinkType.DEFENSE);
		itemTemplates.put("ironpotion", newItem);
	}
	
	public static void addHealPotion() {
		DrinkableItemBuilder newItem = new DrinkableItemBuilder();	
		newItem.setName("healpotion");
		newItem.setDescription("A potion made from sticks.");
		newItem.setComponents(Arrays.asList(itemTemplates.get("stick").getNonexistentFinishedItem()));
		newItem.setDamageMult(0.2);
		newItem.setWeight(.5);
		newItem.setMaxSips(2);
		newItem.setDrinkType(DrinkType.HEALTH);
		itemTemplates.put("healpotion", newItem);
	}
	
	public static void addBleedPotion() {
		DrinkableItemBuilder newItem = new DrinkableItemBuilder();	
		newItem.setName("bleedpotion");
		newItem.setDescription("Don't drink this.");
		newItem.setDamageMult(0.2);
		newItem.setWeight(.5);
		newItem.setMaxSips(2);
		newItem.setDrinkType(DrinkType.BLEED);
	//	newItem.complete();
		itemTemplates.put("bleedpotion", newItem);
	}
	
	public static void addRegenPotion() {
		DrinkableItemBuilder newItem = new DrinkableItemBuilder();	
		newItem.setName("regenpotion");
		newItem.setDamageMult(0.2);
		newItem.setMaxSips(2);
		newItem.setWeight(.5);
		newItem.setDrinkType(DrinkType.REGEN);
	//	newItem.complete();
		itemTemplates.put("regenpotion", newItem);
	}
	
	public static void makeAloeHerb() {
		PlantItemBuilder newItem = new PlantItemBuilder();	
		newItem.setName("aloe");
		newItem.setDamageMult(0.1);
		newItem.setPlantType(PlantType.ALOE);
		newItem.setWeight(.1);
		newItem.setQuantity(100);
	//	newItem.complete();
		itemTemplates.put("aloe", newItem);
	}
	
	public static void makeComfreyHerb() {
		PlantItemBuilder newItem = new PlantItemBuilder();	
		newItem.setName("comfrey");
		newItem.setDamageMult(0.1);
		newItem.setWeight(.1);
		newItem.setPlantType(PlantType.COMFREY);
	//	newItem.complete();
		itemTemplates.put("comfrey", newItem);
	}
	
	public static void makeOleander() {
		PlantItemBuilder newItem = new PlantItemBuilder();	
		newItem.setName("oleander");
		newItem.setDamageMult(0.1);
		newItem.setWeight(.1);
		newItem.setPlantType(PlantType.OLEANDER);
	//	newItem.complete();
		itemTemplates.put("oleander", newItem);
	}
	
	public static void makeGinsengHerb() {
		PlantItemBuilder newItem = new PlantItemBuilder();
		newItem.setName("ginseng");
		newItem.setDamageMult(0.1);
		newItem.setWeight(.1);
		newItem.setPlantType(PlantType.GINSENG);
	//	newItem.complete();
		itemTemplates.put("ginseng", newItem);
	}
	
	public static void makeValerianPoison() {
		PlantItemBuilder newItem = new PlantItemBuilder();
		newItem.setName("valerian");
		newItem.setDamageMult(0.1);
		newItem.setWeight(.1);
		newItem.setPlantType(PlantType.VALERIAN);
	//	newItem.complete();
		itemTemplates.put("valerian", newItem);
	}
	
	public static void makeBelladonnaPoison() {
		PlantItemBuilder newItem = new PlantItemBuilder();
		newItem.setName("belladonna");
		newItem.setDamageMult(0.1);
		newItem.setWeight(.1);
		newItem.setPlantType(PlantType.BELLADONNA);
	//	newItem.complete();
		itemTemplates.put("belladonna", newItem);
	}
	
	public static void addPouch() {
		PouchItemBuilder newItem = new PouchItemBuilder();
		newItem.setName("pouch");
		newItem.setWeight(.5);
	//	newItem.complete();
		itemTemplates.put("pouch", newItem);
	}
	
	public static void makeBag() {
		BagItemBuilder newItem = new BagItemBuilder();
		newItem.setName("bag");
		newItem.setWeight(2);
	//	newItem.complete();
		itemTemplates.put("bag", newItem);
	}

	public static void addOreRock() {
		HarvestableItemBuilder newItem = new HarvestableItemBuilder();
		newItem.setName("ironrock");
		newItem.setMaxQuantity(8);
		newItem.setHarvestType(HarvestType.IRON);
		newItem.setCurrentQuantity(8);
		newItem.setItemContainer(WorldServer.gameState.viewLocations().get(7));
		newItem.complete();
		itemTemplates.put("ironrock", newItem);
	}
	
	public static void addTree() {
		HarvestableItemBuilder newItem = new HarvestableItemBuilder();
		newItem.setName("tree");
		newItem.setMaxQuantity(8);
		newItem.setHarvestType(HarvestType.WOOD);
		newItem.setCurrentQuantity(8);
		newItem.setItemContainer(WorldServer.gameState.viewLocations().get(6));
		newItem.complete();
		itemTemplates.put("tree", newItem);
	}
	
	public static void makeGoblin() {
		MobileBuilder newGoblin = new MobileBuilder();
		newGoblin.addSkillBook(WorldServer.gameState.getBook(1));
		newGoblin.addDecorator(MobileDecorator.DecoratorType.CHASING);
		newGoblin.addDecorator(MobileDecorator.DecoratorType.AGGRESSIVE);
		newGoblin.setName("goblin");
		newGoblin.setLocation(WorldServer.gameState.viewLocations().get(1));
		newGoblin.setDescription("A short, ugly goblin glares at you.");
		newGoblin.setShortDescription(", an ugly goblin");
		newGoblin.setLoadOnStartUp(true);
		newGoblin.complete();
	}
	
	public static void makeFarmerJames() {
		MobileBuilder newFarmerJames = new MobileBuilder();
		newFarmerJames.addSkillBook(WorldServer.gameState.getBook(1));
		newFarmerJames.addDecorator(MobileDecorator.DecoratorType.AGGRESSIVE);
		newFarmerJames.setLocation(WorldServer.gameState.viewLocations().get(4));
		newFarmerJames.setName("FarmerJames");
		newFarmerJames.setDescription("A farmer who looks down on his luck. (try GREETing him)");
		newFarmerJames.setShortDescription(", a poor, tired farmer");
		newFarmerJames.setLoadOnStartUp(true);
		newFarmerJames.complete();
	}
	
	public static void makeHorse() {
		MobileBuilder newGoblin = new MobileBuilder();
		newGoblin.addSkillBook(WorldServer.gameState.getBook(1));
		newGoblin.addDecorator(MobileDecorator.DecoratorType.CHASING);
		newGoblin.addDecorator(MobileDecorator.DecoratorType.AGGRESSIVE);
		newGoblin.setName("horse");
		newGoblin.setDescription("A horse stands here.");
		newGoblin.setShortDescription(", a chestnut mare");
		newGoblin.setLoadOnStartUp(true);
		newGoblin.complete();
	}
}