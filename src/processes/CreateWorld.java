package processes;

import items.Drinkable.DrinkType;
import items.Herb.HerbType;
import items.ItemBuilder;
import items.ItemBuilder.ItemType;
import items.MercWeapon;
import items.MercWeapon.MercEffect;
import items.Mineable.OreType;
import items.StdItem;
import processes.Equipment.EquipmentEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.thoughtworks.xstream.XStream;

import skills.*;
import skills.Mercenary.Attack;
import skills.Mercenary.BreakLimb;
import skills.Mercenary.Clearmind;
import skills.Mercenary.DualAttack;
import skills.Mercenary.Headshot;
import skills.Mercenary.Intimidate;
import skills.Mercenary.MercRegenSkill;
import skills.Mercenary.Shieldblock;
import skills.Mercenary.Shove;
import skills.Mercenary.SpinKick;
import skills.Mercenary.Staunch;
import skills.Mercenary.Straighten;
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
		//crafting
		generalSkills.addSkill(new CraftItem());
		generalSkills.addSkill(new Salvage());
		//quest
		generalSkills.addSkill(new Greet());
		generalSkills.addSkill(new Nod());
		//mage?
		generalSkills.addSkill(new Heal());
		generalSkills.addSkill(new Root());
//		generalSkills.addSkill(new VineTrip());
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
		// Who knows?
		generalSkills.addSkill(new Shieldblock());
		generalSkills.addSkill(new Mine());
	}
	
	public static void makeItems() {
		makeADagger();
		makeASword();
		makeAStick();
		makeAPike();
		makeIngot();
		int quantity = 27;
		while (quantity > 7) {
			addOre();
			quantity=quantity-1;
		}
		addIronPotion();
		makeGoblin();
		makeFarmerJames();
		addHealPotion(30);
		addBleedPotion(31);
		addRegenPotion(32);
		addOreRock(33);		
		makeAloeHerb(34);	
		makeAloeHerb(35);	
		addHerbPouch(36);
		addHerbPouch(37);
		makeGinsengHerb(37);
		makeBag(38);
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
		firstLoc.setName("Start.");
		firstLoc.setDescription("You have to start somewhere");
		firstLoc.complete();	

		//2nd location, south exit to 1, north exit to 3
		LocationBuilder newLoc2 = new LocationBuilder();
		newLoc2.setName("North of Start.");
		newLoc2.setDescription("Slightly north.");
		newLoc2.south(1, "north");
		newLoc2.complete();	
	
		//3rd location, south exit to 2
		LocationBuilder newLoc3 = new LocationBuilder();
		newLoc3.setName("Road.");
		newLoc3.setDescription("On the road to nowhere.");
		newLoc3.south(2, "north");
		newLoc3.complete();	
		
		//4th location, south exit to 3
		LocationBuilder newLoc4 = new LocationBuilder();
		newLoc4.setName("Along the road.");
		newLoc4.setDescription("A road passing by some farmlands.");
		newLoc4.setQuest(new FarmerQuest());
		newLoc4.south(3, "north");
		newLoc4.complete();	
		
		//5th location, south exit to 4
		LocationBuilder newLoc5 = new LocationBuilder();
		newLoc5.setName("On a bridge.");
		newLoc5.setDescription("A bridge over a dried creek.");
		newLoc5.south(4, "north");
		newLoc5.complete();	
		
		//6th location, west exit to 5
		LocationBuilder newLoc6 = new LocationBuilder();
		newLoc6.setName("Forest trail.");
		newLoc6.setDescription("Off the main path.");
		newLoc6.west(5, "east");
		newLoc6.complete();	
		
		//7th location, west exit to 6
		LocationBuilder newLoc7 = new LocationBuilder();
		newLoc7.setName("End of trail.");
		newLoc7.setDescription("Oh no, it's a dead end.");
		newLoc7.west(6, "east");
		newLoc7.in(2,"east");
		newLoc7.complete();	
		
		//8th location, south exit to 5
		LocationBuilder newLoc8 = new LocationBuilder();
		newLoc8.setName("Loop.");
		newLoc8.south(5, "north");
		newLoc8.north(5, "north");
		newLoc8.complete();	
		
		//9th location, east exit to 1
		LocationBuilder newLoc9 = new LocationBuilder();
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
	public static void makeADagger() {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setName("dagger");
		newItem.setDescription("It's a dagger!");
		newItem.setComponents(Arrays.asList("ingot"));
		newItem.setSalvageable(true);
		newItem.setAllowedSlots(EquipmentEnum.LEFTHAND);
		newItem.setAllowedSlots(EquipmentEnum.RIGHTHAND);
		newItem.setMercEffect(MercEffect.BLEED);
		newItem.complete();
		itemTemplates.put("dagger", newItem);
	}
	
	public static void makeASword() {
		ItemBuilder newItem = new ItemBuilder();			
		newItem.setName("sword");
		newItem.setDescription("It's a sword!");
		newItem.setComponents(Arrays.asList("ingot","ingot"));
		newItem.setSalvageable(true);
		newItem.setDamageMult(1.5);
		newItem.setBalanceMult(1.2);
		itemTemplates.put("sword", newItem);
	}
	
	public static void makeAStick() {	
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setName("stick");
		newItem.setDescription("It's an evil stick.");
		newItem.setDamageMult(0.5);
		newItem.setMercEffect(MercEffect.FEAR);
//		newItem.complete();
		itemTemplates.put("stick", newItem);
//		StdItem item = newItem.getFinishedItem();
//		String xml = item.firstTimeSave(xstream);
//		StdItem loaditem = (StdItem)xstream.fromXML(xml);
//		System.out.println(loaditem);
	}
	
	public static void makeAPike() {
		ItemBuilder newItem = new ItemBuilder();
		newItem.setName("pike");
		newItem.setDescription("It's a pike!");
		newItem.setComponents(Arrays.asList("dagger","stick"));
		newItem.setDamageMult(1.8);
		newItem.setBalanceMult(1.5);
		newItem.setSalvageable(true);
		newItem.setMercEffect(MercEffect.AOE);
		itemTemplates.put("pike", newItem);
	}
	
	public static void makeIngot() {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setName("ingot");
		newItem.setDescription("An iron ingot.");
		newItem.setComponents(Arrays.asList("ore","ore"));
		newItem.setDamageMult(0.4);
		newItem.setSalvageable(true);
		itemTemplates.put("ingot", newItem);
	}
	
	public static void addOre() {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setName("iron");
		newItem.setDescription("A pile of iron ore chunks.");
		newItem.setDescriptionSingle("A piece of iron ore.");
		newItem.setItemType(ItemType.STACKABLEITEM);
		newItem.setDamageMult(0.2);
		itemTemplates.put("iron", newItem); // not sure it should be in itemTemplates
//		newItem.complete();
//		WorldServer.gameState.addItem("ore", newItem.getFinishedItem()); //added here instead of templates, not a craftable item
	}
	
	public static void addIronPotion() {
		ItemBuilder newItem = new ItemBuilder();	
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
	
	public static void makeAloeHerb(int i) {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(i);
		newItem.setName("aloe");
		newItem.setDamageMult(0.1);
		newItem.setItemType(ItemType.HERB);
		newItem.setHerbType(HerbType.ALOE);
		newItem.complete();
		itemTemplates.put("aloe", newItem);
	}
	
	public static void makeGinsengHerb(int i) {
		ItemBuilder newItem = new ItemBuilder();
		newItem.setId(i);
		newItem.setName("ginseng");
		newItem.setDamageMult(0.1);
		newItem.setItemType(ItemType.HERB);
		newItem.setHerbType(HerbType.GINSENG);
		newItem.complete();
		itemTemplates.put("ginseng", newItem);
		}
	
	public static void addHerbPouch(int i) {
		ItemBuilder newItem = new ItemBuilder();
		newItem.setId(i);
		newItem.setName("herbpouch");
		newItem.setItemType(ItemType.HERBPOUCH);
		newItem.complete();
		itemTemplates.put("herbpouch", newItem);
	}
	
	public static void makeBag(int i) {
		ItemBuilder newItem = new ItemBuilder();
		newItem.setId(i);
		newItem.setName("bag");
		newItem.setItemType(ItemType.BAG);
		newItem.complete();
		itemTemplates.put("bag", newItem);
	}

	public static void addOreRock(int i) {
		ItemBuilder newItem = new ItemBuilder();
		newItem.setId(i);
		newItem.setName("ironrock");
		newItem.setMaxOres(5);
		newItem.setItemType(ItemType.MINEABLE);
		newItem.setOreType(OreType.IRON);
		newItem.setItemContainer(WorldServer.gameState.viewLocations().get(7));
		newItem.complete();
		itemTemplates.put("regenpotion", newItem);
	}
	
	public static void makeGoblin() {
		MobileBuilder newGoblin = new MobileBuilder();
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
		newGoblin.addSkillBook(WorldServer.gameState.getBook(1));
		newGoblin.addDecorator(MobileDecorator.DecoratorType.CHASING);
		newGoblin.addDecorator(MobileDecorator.DecoratorType.AGGRESSIVE);
		newGoblin.setName("horse");
		newGoblin.setDescription("A horse stands here.");
		newGoblin.setLoadOnStartUp(true);
		newGoblin.complete();
	}
}