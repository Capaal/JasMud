package processes;

import items.Armor.ArmorItemBuilder;
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
import processes.Location.Direction;

import java.util.ArrayList;
import java.util.Arrays;

import Quests.FarmerQuest;
import skills.*;
import skills.Arcanist.ArcanistSkillbook;
import skills.Arcanist.Commands.*;
import skills.Mercenary.*;

public class CreateWorld {
	
	
	public static void loadMap() {
		makeWorldFromNowhere();
	}
	
	public static void createWorldWithItems() {
//		makeWorldFromNowhere();
		makeSkills();
		makeItems();
		spawnMobs();
		makeClasses();
	}
	
	public static void createWorld() {
	//	makeWorldFromNowhere();
		makeSkills();
		spawnMobs();
		makeClasses();
	}
	
	//Hardcoded skill list
	public static void makeSkills() {
		SkillBook moveSkills = new SkillBook("movement", 1);
		WorldServer.getGameState().addBook(1, moveSkills);
		moveSkills.addSkill(new Move(null, null));		
		
		SkillBook generalSkills = new SkillBook("generals", 2);
		WorldServer.getGameState().addBook(2, generalSkills);	
		
		generalSkills.addSkill(new SkillList(null, null));
		//general
		generalSkills.addSkill(new Punch(null, null));
		generalSkills.addSkill(new Throw(null, null));
		generalSkills.addSkill(new Get(null, null));
		generalSkills.addSkill(new Move(null, null));
		generalSkills.addSkill(new Inventory(null, null));
		generalSkills.addSkill(new Shoot(null, null));
		generalSkills.addSkill(new Give(null, null));
		generalSkills.addSkill(new Look(null, null));
		generalSkills.addSkill(new Drop(null, null));
		generalSkills.addSkill(new Info(null, null));
		generalSkills.addSkill(new Wield(null, null));
		generalSkills.addSkill(new Drink(null, null));
		generalSkills.addSkill(new Say(null, null));
		generalSkills.addSkill(new Struggle(null, null));
		generalSkills.addSkill(new Eat(null, null));
		generalSkills.addSkill(new Put(null, null));
		generalSkills.addSkill(new TakeOut(null, null));
		generalSkills.addSkill(new Open(null, null));
		generalSkills.addSkill(new Close(null, null));
		generalSkills.addSkill(new Follow(null, null));
		generalSkills.addSkill(new Apply(null, null));
		generalSkills.addSkill(new Examine(null, null));
		generalSkills.addSkill(new Mine(null, null));
		generalSkills.addSkill(new Chop(null, null)); //same as mine except for many messages..
		generalSkills.addSkill(new Sleep(null, null));
		generalSkills.addSkill(new Wake(null, null));
		generalSkills.addSkill(new Unwield(null, null));
		generalSkills.addSkill(new Diagnose(null, null));
		generalSkills.addSkill(new Messaging(null, null));
		generalSkills.addSkill(new Who(null, null));
		generalSkills.addSkill(new CharacterSheet(null, null));
		generalSkills.addSkill(new RollDice(null, null));
		generalSkills.addSkill(new ListAll(null, null));
		
		//crafting
		generalSkills.addSkill(new CraftItem(null, null));
		generalSkills.addSkill(new Salvage(null, null));
		//quest
		generalSkills.addSkill(new Greet(null, null));
		generalSkills.addSkill(new Nod(null, null));
		//mage?
		generalSkills.addSkill(new Heal(null, null));
		generalSkills.addSkill(new Root(null, null));
		generalSkills.addSkill(new VineTrip(null, null));
		generalSkills.addSkill(new LightningWhip(null, null));
		generalSkills.addSkill(new Icewall(null, null));
		
		SkillBook mercSkills = new SkillBook("mercenary", 3);
		WorldServer.getGameState().addBook(3, mercSkills);
		//mercenary only
		mercSkills.addSkill(new Headshot(null, null));
		mercSkills.addSkill(new BreakLimb(null, null));
		mercSkills.addSkill(new Intimidate(null, null));
		mercSkills.addSkill(new Straighten(null, null));
		mercSkills.addSkill(new SpinKick(null, null));
		mercSkills.addSkill(new Clearmind(null, null));
		mercSkills.addSkill(new Shove(null, null));
		mercSkills.addSkill(new Attack(null, null));
		mercSkills.addSkill(new DualAttack(null, null));
		mercSkills.addSkill(new Staunch(null, null));
		mercSkills.addSkill(new MercRegenSkill(null, null));
		mercSkills.addSkill(new Shieldblock(null, null));
		
		// ARCANIST SKILLS
		SkillBook arcanistSkills = new SkillBook("arcanist", 5);
		WorldServer.getGameState().addBook(5, arcanistSkills);
		arcanistSkills.addSkill(new ArcanistAlter(null, null));
		arcanistSkills.addSkill(new ArcanistCreate(null, null));
		arcanistSkills.addSkill(new ArcanistComplete(null, null));
		arcanistSkills.addSkill(new ArcanistAnalyze(null, null));
		arcanistSkills.addSkill(new ArcanistComponents(null, null));
		
		SkillBook arcanistSpells = new ArcanistSkillbook("spells", 6);
		WorldServer.getGameState().addBook(6, arcanistSpells);

		
		
		// ADMIN SKILLS
		SkillBook adminSkills = new SkillBook("admin", 4);
		adminSkills.addSkill(new GoTo(null, null));
		WorldServer.getGameState().addBook(4, adminSkills);
	}
	
	//Classes! (1) movement, (2) generals, (3) mercenary, (4) admin, (5) arcanist
	public static void makeClasses() {
		//merc class
		String className = "mercenary";
		ArrayList<SkillBook> books = new ArrayList<SkillBook>();
		books = addMercBooks(books);
		WorldServer.getGameState().addClass(className, books);
		
		//arcanist class
		className = "arcanist";
		ArrayList<SkillBook> booksArcanist = new ArrayList<SkillBook>();
		booksArcanist.add(WorldServer.getGameState().getBook(1));
		booksArcanist.add(WorldServer.getGameState().getBook(2));
		booksArcanist.add(WorldServer.getGameState().getBook(5));
		booksArcanist.add(WorldServer.getGameState().getBook(6));
		WorldServer.getGameState().addClass(className, booksArcanist);
		
		//admin ...class...
		className = "admin";
		ArrayList<SkillBook> booksAdmin = new ArrayList<SkillBook>();
		for (SkillBook b : WorldServer.getGameState().viewAllBooks()) {
			booksAdmin.add(b);
		}
		booksAdmin.add(WorldServer.getGameState().getBook(6)); // Arcanist spellbook
		WorldServer.getGameState().addClass(className, booksAdmin);
	}
	
	private static ArrayList<SkillBook> addMercBooks(ArrayList<SkillBook> books) {
		books.add(WorldServer.getGameState().getBook(1));
		books.add(WorldServer.getGameState().getBook(2));
		books.add(WorldServer.getGameState().getBook(3));
		return books;
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
		addLog();
		addTree();
		makeABow();
		makeAloeHerb();	
		makeComfreyHerb();
		makeOleander();
		makeValerianPoison();
		makeBelladonnaPoison();
		addPouch();
		makeGinsengHerb();
		makeBag();
		makeArmor();
	}
	
	public static void makeWorldFromNowhere() {

		//first location, north exit to 2
		LocationBuilder firstLoc = new LocationBuilder();
		firstLoc.setName("Start");
		firstLoc.setDescription("You have to start somewhere.");
		firstLoc.complete();	

		//2nd location, south exit to 1, north exit to 3
		LocationBuilder newLoc2 = new LocationBuilder();
		newLoc2.setName("North of Start");
		newLoc2.setDescription("Slightly north.");
		newLoc2.addLocationConnection(Direction.SOUTH, 1, Direction.NORTH, new Door());
		newLoc2.complete();	
	
		//3rd location, south exit to 2
		LocationBuilder newLoc3 = new LocationBuilder();
		newLoc3.setName("Road");
		newLoc3.setDescription("On the road to nowhere.");
		newLoc3.addLocationConnection(Direction.SOUTH, 2);
		newLoc3.complete();	
		
		//4th location, south exit to 3
		LocationBuilder newLoc4 = new LocationBuilder();
		newLoc4.setName("Along the road");
		newLoc4.setDescription("A road passing by some farmlands.");		
		newLoc4.setQuest(new FarmerQuest());
		newLoc4.addLocationConnection(Direction.SOUTH, 3);
		newLoc4.complete();	
		
		//5th location, south exit to 4
		LocationBuilder newLoc5 = new LocationBuilder();
		newLoc5.setName("On a bridge");
		newLoc5.setDescription("A bridge over a dried creek.");
		newLoc5.addLocationConnection(Direction.SOUTH, 4);
		newLoc5.complete();	
		
		//6th location, west exit to 5
		LocationBuilder newLoc6 = new LocationBuilder();
		newLoc6.setName("Forest trail");
		newLoc6.setDescription("Off the main path.");
		newLoc6.addLocationConnection(Direction.WEST, 5);
		newLoc6.complete();	
		
		//7th location, west exit to 6
		LocationBuilder newLoc7 = new LocationBuilder();
		newLoc7.setName("End of trail");
		newLoc7.setDescription("Oh no, it's a dead end.");
		newLoc7.addLocationConnection(Direction.WEST, 6, Direction.EAST, null);
		newLoc7.addLocationConnection(Direction.IN, 2, Direction.EAST, null);
		newLoc7.complete();	
		
		//8th location, south exit to 5
		LocationBuilder newLoc8 = new LocationBuilder();
		newLoc8.setName("Loop");
		newLoc8.addLocationConnection(Direction.SOUTH, 5, Direction.NORTH, null);
		newLoc8.addLocationConnection(Direction.NORTH, 5, Direction.NORTH, null);
		newLoc8.complete();	
		
		//9th location, east exit to 1
		LocationBuilder newLoc9 = new LocationBuilder();
		newLoc9.setName("Bank");
		newLoc9.setDescription("Not the watery type.");
		newLoc9.addLocationConnection(Direction.EAST, 1);
		newLoc9.complete();	
		
		makeFloatingIsland();
		
		// map: 
		//				 [18]	
		//		  [17] - [16]
		//	(up15)[14] - [12] - [13]
		//               [11]
		//               [10]
		//   [8](loops 5) |
		//   [5] - [6] - [7]
		//   [4]
		//   [3]
		//   [2] - [7]
	// [9] - [1]
		
	}
	
	private static void makeFloatingIsland() {
		makeLandingPad();
		makeMerchantRow();
		makeGarden();
	}

	private static void makeLandingPad() {
		LocationBuilder newLoc10 = new LocationBuilder();
		newLoc10.setName("Landing entrance");
		newLoc10.setDescription("This is the primary entrance to the floating island. It's an open platform jutting out into the air. A dock is visible to the north.");
		newLoc10.addLocationConnection(Direction.SOUTH, 7);
		newLoc10.complete();

		LocationBuilder newLoc11 = new LocationBuilder();
		newLoc11.setName("Dock");
		newLoc11.setDescription("A large dock with multiple receiving points. A few small airships hover nearby, next to two portal terminals. The wind occasionally brings misty water onto the east side of the dock, carried from the waterfall not far off. The westside of the dock offers a view of the busy merchant district, while northwards there is a sprawling, peaceful garden, and in the distance, the inner city dome is visible.");
		newLoc11.addLocationConnection(Direction.SOUTH, 10);
		newLoc11.complete();

		LocationBuilder newLoc12 = new LocationBuilder();
		newLoc12.setName("Landing Pad");
		newLoc12.setDescription("A guardhouse is a few steps eastward, with a guard outside keeping an attentive eye towards the docks. A sign \"Landing's Tavern\" adorns a two-story building to the west.");
		newLoc12.addLocationConnection(Direction.SOUTH, 11);
		newLoc12.complete();

		LocationBuilder newLoc13 = new LocationBuilder();
		newLoc13.setName("Landing Guardhouse");
		newLoc13.setDescription("A clean, grey, unassuming building. At least one guard is always present within.");
		newLoc13.addLocationConnection(Direction.WEST, 12);
		newLoc13.complete();

		LocationBuilder newLoc14 = new LocationBuilder();
		newLoc14.setName("Landing's Tavern");
		newLoc14.setDescription("A well-stocked and clean tavern, serving a surprising variety of food and beverage.");
		newLoc14.addLocationConnection(Direction.EAST, 12);
		newLoc14.complete();

		LocationBuilder newLoc15 = new LocationBuilder();
		newLoc15.setName("Office");
		newLoc15.setDescription("This is a typical office, with a large wooden table as the central fixture, and a chaotic array of too much paperwork.");
		newLoc15.addLocationConnection(Direction.DOWN, 14);
		newLoc15.complete();

		LocationBuilder newLoc16 = new LocationBuilder();
		newLoc16.setName("Landing Street by stables");
		newLoc16.setDescription("The street passes by a stable to the west and nears Main Street. The road north looks older and worn, though still well maintained.");
		newLoc16.addLocationConnection(Direction.SOUTH, 12);
		newLoc16.complete();

		LocationBuilder newLoc17 = new LocationBuilder();
		newLoc17.setName("Stables");
		newLoc17.setDescription("A small stable where travelers can borrow horses for their convenience.");
		newLoc17.addLocationConnection(Direction.EAST, 16);
		newLoc17.complete();
	}

	private static void makeMerchantRow() {
		LocationBuilder newLoc18 = new LocationBuilder();
		newLoc18.setName("South Main Street");
		newLoc18.setDescription("The largest and best maintained road in the city. Lanterns line the street for all-day and all-weather illumination. The inner dome at the far northern end and the Landing Pad, the primary welcoming area for the floating city, to the south.");
		newLoc18.addLocationConnection(Direction.SOUTH, 16);
		newLoc18.complete();

		LocationBuilder newLoc19 = new LocationBuilder();
		newLoc19.setName("Main Street by Merchant Row");
		newLoc19.setDescription("");
		newLoc19.addLocationConnection(Direction.SOUTH, 18);
		newLoc19.complete();

		LocationBuilder newLoc20 = new LocationBuilder();
		newLoc20.setName("Merchant Row");
		newLoc20.setDescription("This road is crowded with small stalls and stores. A variety of smells flow through in the air - food, perfumes, flowers, leathers, and sometimes less appealing scents.");
		newLoc20.addLocationConnection(Direction.EAST, 19);
		newLoc20.complete();

		LocationBuilder newLoc21 = new LocationBuilder();
		newLoc21.setName("Merchant Row by the apothecary");
		newLoc21.setDescription("");
		newLoc21.addLocationConnection(Direction.EAST, 20);
		newLoc21.complete();

		LocationBuilder newLoc22 = new LocationBuilder();
		newLoc22.setName("Apothecary");
		newLoc22.setDescription("");
		newLoc22.addLocationConnection(Direction.SOUTH, 21);
		newLoc22.complete();

		LocationBuilder newLoc23 = new LocationBuilder();
		newLoc23.setName("Curve in Merchant Row");
		newLoc23.setDescription("");
		newLoc23.addLocationConnection(Direction.SOUTHEAST, 21);
		newLoc23.complete();

		LocationBuilder newLoc24 = new LocationBuilder();
		newLoc24.setName("Shop");
		newLoc24.setDescription("");
		newLoc24.addLocationConnection(Direction.SOUTH, 23);
		newLoc24.complete();

		LocationBuilder newLoc25 = new LocationBuilder();
		newLoc25.setName("Warehouse");
		newLoc25.setDescription("");
		newLoc25.addLocationConnection(Direction.SOUTHEAST, 23);
		newLoc25.complete();

		LocationBuilder newLoc26 = new LocationBuilder();
		newLoc26.setName("Crescent Moon Tavern");
		newLoc26.setDescription("");
		newLoc26.addLocationConnection(Direction.EAST, 23);
		newLoc26.complete();

		LocationBuilder newLoc27 = new LocationBuilder();
		newLoc27.setName("Merchant Row sideroad");
		newLoc27.setDescription("");
		newLoc27.addLocationConnection(Direction.NORTHEAST, 23);
		newLoc27.complete();

		LocationBuilder newLoc28 = new LocationBuilder();
		newLoc28.setName("Merchant Row near apartments");
		newLoc28.setDescription("");
		newLoc28.addLocationConnection(Direction.SOUTHEAST, 27);
		newLoc28.complete();

		LocationBuilder newLoc29 = new LocationBuilder();
		newLoc29.setName("Apartments south of Merchant Row");
		newLoc29.setDescription("");
		newLoc29.addLocationConnection(Direction.NORTH, 28);
		newLoc29.complete();

		LocationBuilder newLoc30 = new LocationBuilder();
		newLoc30.setName("Apartments north of Merchant Row");
		newLoc30.setDescription("");
		newLoc30.addLocationConnection(Direction.SOUTH, 28);
		newLoc30.complete();

		LocationBuilder newLoc31 = new LocationBuilder();
		newLoc31.setName("End of Merchant Row");
		newLoc31.setDescription("");
		newLoc31.addLocationConnection(Direction.SOUTHEAST, 28);
		newLoc31.complete();
	}
	
	private static void makeGarden() {
		LocationBuilder newLoc32 = new LocationBuilder();
		newLoc32.setName("Along Main Street");
		newLoc32.setDescription("");
		newLoc32.addLocationConnection(Direction.SOUTH, 19);
		newLoc32.complete();
		
		LocationBuilder newLoc33 = new LocationBuilder();
		newLoc33.setName("Garden Archway");
		newLoc33.setDescription("");
		newLoc33.addLocationConnection(Direction.WEST, 32);
		newLoc33.complete();
		
		//alternative descriptions for seasons?
		LocationBuilder newLoc34 = new LocationBuilder();
		newLoc34.setName("Amongst the rosebushes");
		newLoc34.setDescription("");
		newLoc34.addLocationConnection(Direction.NORTH, 33);
		newLoc34.complete();

		LocationBuilder newLoc35 = new LocationBuilder();
		newLoc35.setName("Garden trail");
		newLoc35.setDescription("");
		newLoc35.addLocationConnection(Direction.WEST, 33);
		newLoc35.complete();

		LocationBuilder newLoc36 = new LocationBuilder();
		newLoc36.setName("Beneath a wisteria tree");
		newLoc36.setDescription("");
		newLoc36.addLocationConnection(Direction.SOUTH, 35);
		newLoc36.complete();

		LocationBuilder newLoc37 = new LocationBuilder();
		newLoc37.setName("Garden trail beside a lake");
		newLoc37.setDescription("");
		newLoc37.addLocationConnection(Direction.NORTHWEST, 35);
		newLoc37.complete();

		LocationBuilder newLoc38 = new LocationBuilder();
		newLoc38.setName("Medicinal herb garden");
		newLoc38.setDescription("");
		newLoc38.addLocationConnection(Direction.NORTHEAST, 37);
		newLoc38.complete();

		LocationBuilder newLoc39 = new LocationBuilder();
		newLoc39.setName("Duck pond");
		newLoc39.setDescription("A large pond with a few lilypads and sleepy goldfish. Evergreens surround the pond and create a serene, secluded atmosphere.");
		newLoc39.addLocationConnection(Direction.SOUTH, 37);
		newLoc39.complete();

		LocationBuilder newLoc40 = new LocationBuilder();
		newLoc40.setName("Garden trail along a river");
		newLoc40.setDescription("");
		newLoc40.addLocationConnection(Direction.NORTHWEST, 37);
		newLoc40.complete();

		LocationBuilder newLoc41 = new LocationBuilder();
		newLoc41.setName("River through the garden");
		newLoc41.setDescription("");
		newLoc41.addLocationConnection(Direction.SOUTH, 40);
		newLoc41.addLocationConnection(Direction.WEST, 37);
		newLoc41.addLocationConnection(Direction.NORTHWEST, 39);
		newLoc41.complete();

		LocationBuilder newLoc42 = new LocationBuilder();
		newLoc42.setName("River near a waterfall");
		newLoc42.setDescription("");
		newLoc42.addLocationConnection(Direction.WEST, 40);
		newLoc42.addLocationConnection(Direction.NORTHWEST, 41);
		newLoc42.complete();
	}	
	
	public static void addOre() {
		StackableItemBuilder newItem = new StackableItemBuilder();
		newItem.setName("iron");
		newItem.setDescriptionMany("A pile of iron ore chunks.");
		newItem.setDescription("A piece of iron ore.");
		newItem.setDamageMult(0.2);
		newItem.setQuantity(1);
		WorldServer.getGameState().itemTemplates.put("iron", newItem); // not sure it should be in itemTemplates
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
		WorldServer.getGameState().itemTemplates.put("log", newItem); // not sure it should be in itemTemplates
	//	newItem.complete();
	}
	
	public static void makeIngot() {
		ItemBuilder newItem = new ItemBuilder();
		newItem.setWeight(2);
		newItem.setName("ingot");
		newItem.setDescription("An iron ingot.");
		StdItem iron = WorldServer.getGameState().itemTemplates.get("iron").getNonexistentFinishedItem();
		iron.addToStack(1);
		newItem.setComponents(Arrays.asList(iron)); //TODO should use the object iron instead of string
		newItem.setDamageMult(0.4);
		newItem.setSalvageable(true);
		WorldServer.getGameState().itemTemplates.put("ingot", newItem);
	}
	
	public static void makeADagger() {
		WeaponItemBuilder newItem = new WeaponItemBuilder();	
		newItem.setName("dagger");
		newItem.setItemContainer(WorldServer.getGameState().viewLocations().get(1));
		newItem.setDescription("It's a dagger!");
		newItem.setComponents(Arrays.asList(WorldServer.getGameState().itemTemplates.get("ingot").getNonexistentFinishedItem()));
		newItem.setSalvageable(true);
		newItem.setWeight(1.5);
		newItem.setMercEffect(MercEffect.BLEED);
		newItem.complete();
		WorldServer.getGameState().itemTemplates.put("dagger", newItem);
	}
	
	public static void makeASword() {
		WeaponItemBuilder newItem = new WeaponItemBuilder();			
		newItem.setName("sword");
		newItem.setDescription("It's a sword!");
		newItem.setComponents(Arrays.asList(WorldServer.getGameState().itemTemplates.get("ingot").getNonexistentFinishedItem(), WorldServer.getGameState().itemTemplates.get("ingot").getNonexistentFinishedItem()));
		newItem.setSalvageable(true);
		newItem.setWeight(5);
		newItem.setDamageMult(1.5);
		newItem.setBalanceMult(1.2);
		WorldServer.getGameState().itemTemplates.put("sword", newItem);
	}
	
	public static void makeAStick() {	
		WeaponItemBuilder newItem = new WeaponItemBuilder();	
		newItem.setName("stick");
		newItem.setDescription("It's an evil stick.");
		newItem.setDamageMult(0.5);
		newItem.setWeight(.5);
		newItem.setMercEffect(MercEffect.FEAR);
//		newItem.complete();
		WorldServer.getGameState().itemTemplates.put("stick", newItem);
//		StdItem item = newItem.getFinishedItem();
//		String xml = item.firstTimeSave(xstream);
//		StdItem loaditem = (StdItem)xstream.fromXML(xml);
//		System.out.println(loaditem);
	}
	
	public static void makeAPike() {
		WeaponItemBuilder newItem = new WeaponItemBuilder();
		newItem.setName("pike");
		newItem.setDescription("It's a pike!");
		newItem.setComponents(Arrays.asList(WorldServer.getGameState().itemTemplates.get("dagger").getNonexistentFinishedItem(),WorldServer.getGameState().itemTemplates.get("stick").getNonexistentFinishedItem()));
		newItem.setDamageMult(1.8);
		newItem.setWeight(6);
		newItem.setBalanceMult(1.5);
		newItem.setSalvageable(true);
		WorldServer.getGameState().itemTemplates.put("pike", newItem);
	}
	
	public static void makeAShield() {
		WeaponItemBuilder newItem = new WeaponItemBuilder();
		newItem.setName("shield");
		newItem.setDescription("It's a shield!");
		newItem.setComponents(Arrays.asList(WorldServer.getGameState().itemTemplates.get("ingot").getNonexistentFinishedItem(),WorldServer.getGameState().itemTemplates.get("ingot").getNonexistentFinishedItem()));
		newItem.setDamageMult(1);
		newItem.setWeight(4);
		newItem.setBalanceMult(1.5);
		newItem.setSalvageable(true);
		WorldServer.getGameState().itemTemplates.put("shield", newItem);
	}
	
	public static void makeABow() {
		WeaponItemBuilder newItem = new WeaponItemBuilder();
		newItem.setName("bow");
		newItem.setDescription("It's a bow!");
		newItem.setComponents(Arrays.asList(WorldServer.getGameState().itemTemplates.get("log").getNonexistentFinishedItem()));
		newItem.setDamageMult(1);
		newItem.setWeight(2);
		newItem.setBalanceMult(1.5);
		newItem.setSalvageable(true);
		WorldServer.getGameState().itemTemplates.put("bow", newItem);
	}
	
	public static void addIronPotion() {
		DrinkableItemBuilder newItem = new DrinkableItemBuilder();	
		newItem.setName("ironpotion");
		newItem.setDescription("A potion made from iron.");
		newItem.setWeight(.5);
		newItem.setComponents(Arrays.asList(WorldServer.getGameState().itemTemplates.get("iron").getNonexistentFinishedItem()));
		newItem.setDamageMult(0.2);
		newItem.setMaxSips(2);
		newItem.setDrinkType(DrinkType.DEFENSE);
		WorldServer.getGameState().itemTemplates.put("ironpotion", newItem);
	}
	
	public static void addHealPotion() {
		DrinkableItemBuilder newItem = new DrinkableItemBuilder();	
		newItem.setName("healpotion");
		newItem.setDescription("A potion made from sticks.");
		newItem.setComponents(Arrays.asList(WorldServer.getGameState().itemTemplates.get("stick").getNonexistentFinishedItem()));
		newItem.setDamageMult(0.2);
		newItem.setWeight(.5);
		newItem.setMaxSips(2);
		newItem.setDrinkType(DrinkType.HEALTH);
		WorldServer.getGameState().itemTemplates.put("healpotion", newItem);
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
		WorldServer.getGameState().itemTemplates.put("bleedpotion", newItem);
	}
	
	public static void addRegenPotion() {
		DrinkableItemBuilder newItem = new DrinkableItemBuilder();	
		newItem.setName("regenpotion");
		newItem.setDamageMult(0.2);
		newItem.setMaxSips(2);
		newItem.setWeight(.5);
		newItem.setDrinkType(DrinkType.REGEN);
	//	newItem.complete();
		WorldServer.getGameState().itemTemplates.put("regenpotion", newItem);
	}
	
	public static void makeAloeHerb() {
		PlantItemBuilder newItem = new PlantItemBuilder();	
		newItem.setName("aloe");
		newItem.setDamageMult(0.1);
		newItem.setPlantType(PlantType.ALOE);
		newItem.setWeight(.1);
		newItem.setQuantity(100);
	//	newItem.complete();
		WorldServer.getGameState().itemTemplates.put("aloe", newItem);
	}
	
	public static void makeComfreyHerb() {
		PlantItemBuilder newItem = new PlantItemBuilder();	
		newItem.setName("comfrey");
		newItem.setDamageMult(0.1);
		newItem.setWeight(.1);
		newItem.setPlantType(PlantType.COMFREY);
	//	newItem.complete();
		WorldServer.getGameState().itemTemplates.put("comfrey", newItem);
	}
	
	public static void makeOleander() {
		PlantItemBuilder newItem = new PlantItemBuilder();	
		newItem.setName("oleander");
		newItem.setDamageMult(0.1);
		newItem.setWeight(.1);
		newItem.setPlantType(PlantType.OLEANDER);
	//	newItem.complete();
		WorldServer.getGameState().itemTemplates.put("oleander", newItem);
	}
	
	public static void makeGinsengHerb() {
		PlantItemBuilder newItem = new PlantItemBuilder();
		newItem.setName("ginseng");
		newItem.setDamageMult(0.1);
		newItem.setWeight(.1);
		newItem.setPlantType(PlantType.GINSENG);
	//	newItem.complete();
		WorldServer.getGameState().itemTemplates.put("ginseng", newItem);
	}
	
	public static void makeFoxgloveHerb() {
		PlantItemBuilder newItem = new PlantItemBuilder();
		newItem.setName("foxglove");
		newItem.setDamageMult(0.1);
		newItem.setWeight(.1);
		newItem.setPlantType(PlantType.FOXGLOVE);
	//	newItem.complete();
		WorldServer.getGameState().itemTemplates.put("foxglove", newItem);
	}
	
	public static void makeValerianPoison() {
		PlantItemBuilder newItem = new PlantItemBuilder();
		newItem.setName("valerian");
		newItem.setDamageMult(0.1);
		newItem.setWeight(.1);
		newItem.setPlantType(PlantType.VALERIAN);
	//	newItem.complete();
		WorldServer.getGameState().itemTemplates.put("valerian", newItem);
	}
	
	public static void makeBelladonnaPoison() {
		PlantItemBuilder newItem = new PlantItemBuilder();
		newItem.setName("belladonna");
		newItem.setDamageMult(0.1);
		newItem.setWeight(.1);
		newItem.setPlantType(PlantType.BELLADONNA);
	//	newItem.complete();
		WorldServer.getGameState().itemTemplates.put("belladonna", newItem);
	}
	
	public static void addPouch() {
		PouchItemBuilder newItem = new PouchItemBuilder();
		newItem.setName("pouch");
		newItem.setDescription("It's a pouch. Pouches hold one stackable item only.");
		newItem.setWeight(.5);
	//	newItem.complete();
		WorldServer.getGameState().itemTemplates.put("pouch", newItem);
	}
	
	public static void makeBag() {
		BagItemBuilder newItem = new BagItemBuilder();
		newItem.setName("bag");
		newItem.setWeight(2);
	//	newItem.complete();
		WorldServer.getGameState().itemTemplates.put("bag", newItem);
	}
	
	public static void makeArmor() {
		ArmorItemBuilder newItem = new ArmorItemBuilder();
		newItem.setName("chainmail");
		newItem.setWeight(35);
	//	newItem.complete();
		WorldServer.getGameState().itemTemplates.put("chainmail", newItem);
	}

	public static void addOreRock() {
		HarvestableItemBuilder newItem = new HarvestableItemBuilder();
		newItem.setName("ironrock");
		newItem.setMaxQuantity(8);
		newItem.setHarvestType(HarvestType.IRON);
		newItem.setCurrentQuantity(8);
		newItem.setItemContainer(WorldServer.getGameState().viewLocations().get(7));
		newItem.complete();
		WorldServer.getGameState().itemTemplates.put("ironrock", newItem);
	}
	
	public static void addTree() {
		HarvestableItemBuilder newItem = new HarvestableItemBuilder();
		newItem.setName("tree");
		newItem.setMaxQuantity(8);
		newItem.setHarvestType(HarvestType.WOOD);
		newItem.setCurrentQuantity(8);
		newItem.setItemContainer(WorldServer.getGameState().viewLocations().get(6));
		newItem.complete();
		WorldServer.getGameState().itemTemplates.put("tree", newItem);
	}
	
	public static void makeGoblin() {
		MobileBuilder newGoblin = new MobileBuilder();
	//	newGoblin.addSkillBook(WorldServer.gameState.getBook(1));
		newGoblin.addDecorator(MobileDecorator.DecoratorType.CHASING);
		newGoblin.addDecorator(MobileDecorator.DecoratorType.AGGRESSIVE);
		newGoblin.setName("goblin");
		newGoblin.setLocation(WorldServer.getGameState().viewLocations().get(1));
		newGoblin.setDescription("A short, ugly goblin glares at you.");
		newGoblin.setShortDescription(", an ugly goblin");
		newGoblin.setLoadOnStartUp(true);
		newGoblin.complete();
	}
	
	public static void makeFarmerJames() {
		MobileBuilder newFarmerJames = new MobileBuilder();
	//	newFarmerJames.addSkillBook(WorldServer.getGameState().getBook(1));
		newFarmerJames.addDecorator(MobileDecorator.DecoratorType.AGGRESSIVE);
		newFarmerJames.setLocation(WorldServer.getGameState().viewLocations().get(4));
		newFarmerJames.setName("FarmerJames");
		newFarmerJames.setDescription("A farmer who looks down on his luck. (try GREETing him)");
		newFarmerJames.setShortDescription(", a poor, tired farmer");
		newFarmerJames.setLoadOnStartUp(true);
		newFarmerJames.complete();
	}
	
	public static void makeHorse() {
		MobileBuilder newGoblin = new MobileBuilder();
	//	newGoblin.addSkillBook(WorldServer.getGameState().getBook(1));
		newGoblin.addDecorator(MobileDecorator.DecoratorType.CHASING);
		newGoblin.addDecorator(MobileDecorator.DecoratorType.AGGRESSIVE);
		newGoblin.setName("horse");
		newGoblin.setDescription("A horse stands here.");
		newGoblin.setShortDescription(", a chestnut mare");
		newGoblin.setLoadOnStartUp(true);
		newGoblin.complete();
	}
}