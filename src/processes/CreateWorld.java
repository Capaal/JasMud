package processes;

import items.Drinkable.DrinkType;
import items.ItemBuilder;
import items.ItemBuilder.ItemType;
import processes.Equipment.EquipmentEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import skills.*;
import Quests.FarmerQuest;

public class CreateWorld {
	
	
	public static SkillBook generalSkills = new SkillBook("generalSkills", 1);
	private static Map<String, ItemBuilder> itemTemplates = new TreeMap<String, ItemBuilder>(); //list of all template items
	
	public static void createWorld() {
//		makeWorldFromDatabase();
		makeWorldFromNowhere();
		makeSkills();
		makeItems();
	}
	
	//Hardcoded skill list
	public static void makeSkills() {
		WorldServer.gameState.addBook(1, generalSkills);
		generalSkills.addSkill(new Punch());
		generalSkills.addSkill(new Throw());
		generalSkills.addSkill(new Get());
		generalSkills.addSkill(new Move());
		generalSkills.addSkill(new Inventory());
		generalSkills.addSkill(new Greet());
		generalSkills.addSkill(new Shoot());
		generalSkills.addSkill(new Give());
		generalSkills.addSkill(new Headshot());
		generalSkills.addSkill(new CraftItem());
		generalSkills.addSkill(new Nod());
		generalSkills.addSkill(new Look());
		generalSkills.addSkill(new Salvage());
		generalSkills.addSkill(new Heal());
		generalSkills.addSkill(new SkillList());
		generalSkills.addSkill(new Drop());
		generalSkills.addSkill(new Info());
		generalSkills.addSkill(new Wield());
		generalSkills.addSkill(new BreakLimb());
		generalSkills.addSkill(new Drink());
		generalSkills.addSkill(new Intimidate());
	}
	
	public static void makeItems() {
		makeADagger(1);
		makeASword(2);
		makeAStick(3);
		makeAPike(4);
		makeIngot(5);
		int quantity = 27;
		while (quantity > 7) {
			addOre(quantity);
			quantity=quantity-1;
		}
		addIronPotion(29);
		makeGoblin();
		makeFarmerJames();
		addHealPotion(30);
		addBleedPotion(31);
		addRegenPotion(32);
	}
	
	public static void makeWorldFromDatabase() {
//		WorldServer.databaseInterface.connect("root", "".toCharArray());
//		WorldServer.databaseInterface.loadLocations();
//		WorldServer.databaseInterface.loadSkillBooks();
//		WorldServer.databaseInterface.loadMobs();
//		WorldServer.databaseInterface.disconnect();
	}
	
	public static void makeWorldFromNowhere() {

		//first location, north exit to 2
		LocationBuilder firstLoc = new LocationBuilder();
		firstLoc.setId(1);
		firstLoc.setName("Start.");
		firstLoc.setDescription("You have to start somewhere");
		firstLoc.complete();	

		//2nd location, south exit to 1, north exit to 3
		LocationBuilder newLoc2 = new LocationBuilder();
		newLoc2.setId(2);
		newLoc2.setName("North of Start.");
		newLoc2.setDescription("Slightly north.");
		newLoc2.south(1, "north");
		newLoc2.complete();	
	
		//3rd location, south exit to 2
		int loc3 = 3;
		LocationBuilder newLoc3 = new LocationBuilder();
		newLoc3.setId(loc3);
		newLoc3.setName("Road.");
		newLoc3.setDescription("On the road to nowhere.");
		newLoc3.south(2, "north");
		newLoc3.complete();	
		
		//4th location, south exit to 3
		int loc4 = 4;
		LocationBuilder newLoc4 = new LocationBuilder();
		newLoc4.setId(loc4);
		newLoc4.setName("Along the road.");
		newLoc4.setDescription("A road passing by some farmlands.");
		newLoc4.setQuest(new FarmerQuest());
		newLoc4.south(3, "north");
		newLoc4.complete();	
		
		//5th location, south exit to 4
		int loc5 = 5;
		LocationBuilder newLoc5 = new LocationBuilder();
		newLoc5.setId(loc5);
		newLoc5.setName("On a bridge.");
		newLoc5.setDescription("A bridge over a dried creek.");
		newLoc5.south(4, "north");
		newLoc5.complete();	
		
		//6th location, west exit to 5
		int loc6 = 6;
		LocationBuilder newLoc6 = new LocationBuilder();
		newLoc6.setId(loc6);
		newLoc6.setName("Forest trail.");
		newLoc6.setDescription("Off the main path.");
		newLoc6.west(5, "east");
		newLoc6.complete();	
		
		//7th location, west exit to 6
		int loc7 = 7;
		LocationBuilder newLoc7 = new LocationBuilder();
		newLoc7.setId(loc7);
		newLoc7.setName("End of trail.");
		newLoc7.setDescription("Oh no, it's a dead end.");
		newLoc7.west(6, "east");
		newLoc7.in(2,"east");
		newLoc7.complete();	
		
		//8th location, south exit to 5
		int loc8 = 8;
		LocationBuilder newLoc8 = new LocationBuilder();
		newLoc8.setId(loc8);
		newLoc8.setName("Loop.");
		newLoc8.south(5, "north");
		newLoc8.north(5, "north");
		newLoc8.complete();	
		
		//9th location, east exit to 1
		int loc9 = 9;
		LocationBuilder newLoc9 = new LocationBuilder();
		newLoc9.setId(loc9);
		newLoc9.setName("Bank.");
		newLoc9.setDescription("Not the watery type.");
		newLoc9.east(1, "west");
		newLoc9.complete();	
		
		// map: 
		//   [8](loops 5)
		//   [5] - [6] - [7]
		//   [4]
		//   [3]
		//   [2] - [7]
	// [9] - [1]
		
	}
	
	public static Map<String, ItemBuilder> viewItemTemplates() {
		return new HashMap<String, ItemBuilder>(itemTemplates);
	}
	
	//template items should be stored as builders, not actually existing items
	public static void makeADagger(int i) {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(i);
		newItem.setName("dagger");
		newItem.setDescription("It's a dagger!");
		newItem.setComponents(Arrays.asList("ingot"));
		newItem.setSalvageable(true);
		newItem.setAllowedSlots(EquipmentEnum.LEFTHAND);
		newItem.setAllowedSlots(EquipmentEnum.RIGHTHAND);
		newItem.complete();
		itemTemplates.put("dagger", newItem);
	}
	
	public static void makeASword(int i) {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(i);
		
		newItem.setName("sword");
		newItem.setDescription("It's a sword!");
		newItem.setComponents(Arrays.asList("ingot","ingot"));
		newItem.setSalvageable(true);
		newItem.setDamageMult(1.5);
		newItem.setBalanceMult(1.2);
		itemTemplates.put("sword", newItem);
	}
	
	public static void makeAStick(int i) {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(i);
		newItem.setName("stick");
		newItem.setDescription("It's a stick!");
		newItem.setDamageMult(0.5);
		newItem.complete();
		itemTemplates.put("stick", newItem);
	}
	
	public static void makeAPike(int i) {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(i);
		newItem.setName("pike");
		newItem.setDescription("It's a pike!");
		newItem.setComponents(Arrays.asList("dagger","stick"));
		newItem.setDamageMult(1.8);
		newItem.setBalanceMult(1.5);
		newItem.setSalvageable(true);
		itemTemplates.put("pike", newItem);
	}
	
	public static void makeIngot(int i) {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(i);
		newItem.setName("ingot");
		newItem.setDescription("An iron ingot.");
		newItem.setComponents(Arrays.asList("ore","ore"));
		newItem.setDamageMult(0.4);
		newItem.setSalvageable(true);
		itemTemplates.put("ingot", newItem);
	}
	
	public static void addOre(int i) {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(i);
		newItem.setName("ore");
		newItem.setDescription("A pile of iron ore chunks.");
		newItem.setDescriptionSingle("A piece of iron ore.");
		newItem.setItemType(ItemType.STACKABLEITEM);
		newItem.setDamageMult(0.2);
		newItem.complete();
		WorldServer.gameState.addItem("ore", newItem.getFinishedItem()); //added here instead of templates, not a craftable item
	}
	
	public static void addIronPotion(int i) {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(i);
		newItem.setName("ironpotion");
		newItem.setDescription("A potion made from iron.");
		newItem.setComponents(Arrays.asList("ore"));
		newItem.setDamageMult(0.2);
		newItem.setMaxSips(2);
		newItem.setDefenseMult(10); //wtf is this for?
		newItem.setItemType(ItemType.DRINKABLE);
		newItem.setDrinkType(DrinkType.DEFENSE);
		itemTemplates.put("ironpotion", newItem);
	}
	
	public static void addHealPotion(int i) {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(i);
		newItem.setName("healpotion");
		newItem.setDescription("A potion made from sticks.");
		newItem.setComponents(Arrays.asList("stick"));
		newItem.setDamageMult(0.2);
		newItem.setMaxSips(2);
		newItem.setItemType(ItemType.DRINKABLE);
		newItem.setDrinkType(DrinkType.HEALTH);
		itemTemplates.put("healpotion", newItem);
	}
	
	public static void addBleedPotion(int i) {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(i);
		newItem.setName("bleedpotion");
		newItem.setDescription("Don't drink this.");
		newItem.setDamageMult(0.2);
		newItem.setMaxSips(2);
		newItem.setItemType(ItemType.DRINKABLE);
		newItem.setDrinkType(DrinkType.BLEED);
		newItem.complete();
		itemTemplates.put("bleedpotion", newItem);
	}
	
	public static void addRegenPotion(int i) {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(i);
		newItem.setName("regenpotion");
		newItem.setDamageMult(0.2);
		newItem.setMaxSips(2);
		newItem.setItemType(ItemType.DRINKABLE);
		newItem.setDrinkType(DrinkType.REGEN);
		newItem.complete();
		itemTemplates.put("regenpotion", newItem);
	}
	
	public static void makeGoblin() {
		MobileBuilder newGoblin = new MobileBuilder();
		newGoblin.setId(2);
		newGoblin.addSkillBook(WorldServer.gameState.getBook(1));
		newGoblin.addDecorator(MobileDecorator.DecoratorType.CHASING);
		newGoblin.addDecorator(MobileDecorator.DecoratorType.AGGRESSIVE);
		newGoblin.setName("goblin");
		newGoblin.setDescription("An ugly goblin.");
		newGoblin.setLoadOnStartUp(true);
		newGoblin.complete();
	}
	
	public static void makeFarmerJames() {
		MobileBuilder newFarmerJames = new MobileBuilder();
		newFarmerJames.setId(25);
		newFarmerJames.addSkillBook(WorldServer.gameState.getBook(1));
		newFarmerJames.addDecorator(MobileDecorator.DecoratorType.AGGRESSIVE);
		newFarmerJames.setLocation(WorldServer.gameState.viewLocations().get(4));
		newFarmerJames.setName("FarmerJames");
		newFarmerJames.setDescription("A farmer who looks down on his luck. (try GREETing him)");
		newFarmerJames.setLoadOnStartUp(true);
		newFarmerJames.complete();
	}
	
	public static void makeHorse() {
		MobileBuilder newGoblin = new MobileBuilder();
		newGoblin.setId(3);
		newGoblin.addSkillBook(WorldServer.gameState.getBook(1));
		newGoblin.addDecorator(MobileDecorator.DecoratorType.CHASING);
		newGoblin.addDecorator(MobileDecorator.DecoratorType.AGGRESSIVE);
		newGoblin.setName("horse");
		newGoblin.setDescription("A horse stands here.");
		newGoblin.setLoadOnStartUp(true);
		newGoblin.complete();
	}
}