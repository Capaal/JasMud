package processes;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import skills.*;
import processes.Skills.Syntax;
import TargettingStrategies.*;
import actions.*;
import items.StdItem;

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
		generalSkills.addSkill(new Look());
	/*	kickSkill();
		addPunchSkill();
		addGodCreateSkill();
		addMoveSkill();
		addLookSkill();
		addDropSkill();
		addGetSkill();
		addExamineSkill();
		addThrowSkill();
		addGiveSkill();
		addMsgWorldSkill();
		addSaySkill();
		addSelfInvSkill();
		addExitsSkill(); 
		addTellSkill();
		addEmotesSkill();
		addMakeItemSkill(); */
	}
	
	public static void makeItems() {
		makeADagger(1);
		makeASword(2);
		makeAStick(3);
		makeGoblin();
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
		newLoc2.setDescription("Slightly north");
		newLoc2.south(1, "north");
		newLoc2.complete();	
	
		//3rd location, south exit to 2
		int loc3 = 3;
		LocationBuilder newLoc3 = new LocationBuilder();
		newLoc3.setId(loc3);
		newLoc3.setName("Road.");
		newLoc3.south(2, "north");
		newLoc3.complete();	
		
		//4th location, south exit to 3
		int loc4 = 4;
		LocationBuilder newLoc4 = new LocationBuilder();
		newLoc4.setId(loc4);
		newLoc4.setName("Along the road.");
		newLoc4.south(3, "north");
		newLoc4.complete();	
		
		//5th location, south exit to 4
		int loc5 = 5;
		LocationBuilder newLoc5 = new LocationBuilder();
		newLoc5.setId(loc5);
		newLoc5.setName("On a bridge.");
		newLoc5.south(4, "north");
		newLoc5.complete();	
		
		//6th location, west exit to 5
		int loc6 = 6;
		LocationBuilder newLoc6 = new LocationBuilder();
		newLoc6.setId(loc6);
		newLoc6.setName("Forest trail.");
		newLoc6.west(5, "east");
		newLoc6.complete();	
		
		//7th location, west exit to 6
		int loc7 = 7;
		LocationBuilder newLoc7 = new LocationBuilder();
		newLoc7.setId(loc7);
		newLoc7.setName("End of trail.");
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
		
		// map: 
		//   [8](loops 5)
		//   [5] - [6] - [7]
		//   [4]
		//   [3]
		//   [2] - [7]
		//   [1]
		
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
		newItem.complete();
		itemTemplates.put("dagger", newItem);
	}
	
	public static void makeASword(int i) {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(i);
		newItem.setName("sword");
		newItem.setDescription("It's a sword!");
		itemTemplates.put("sword", newItem);
	}
	
	public static void makeAStick(int i) {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(i);
		newItem.setName("stick");
		newItem.setDescription("It's a stick!");
		itemTemplates.put("stick", newItem);
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
	
	// (29) making an item, for crafting. Needs checks
	// first assuming all items have a blueprint
	// how to make ID a new number for all new items?
/*	public static void addMakeItemSkill() {
		SkillBuilder makeItemBuilder = new SkillBuilder();
		makeItemBuilder.addAction(new MakeItem(new WhatStrategyItem(), new WhereStrategyHere()));
		makeItemBuilder.addAction(new Message("You made %s.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.ITEM))));
		makeItemBuilder.setFailMsg(new Message("You cannot make \"%s\".", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.ITEM))));
		makeItemBuilder.addBook(generalSkills);
		makeItemBuilder.setName("make");
//		makeItemBuilder.addSyntax(Skill.Syntax.SKILL);
//		makeItemBuilder.addSyntax(Skill.Syntax.ITEM);
		makeItemBuilder.setId(29);
		makeItemBuilder.complete();
	} */
	
	
	// hardcoded get skill 11
	public static void addGetSkill() {
		SkillBuilder getBuilder = new SkillBuilder();
		getBuilder.addAction(new MoveHoldable(new WhatStrategyItem(), new WhereStrategyHere(), new WhereStrategySelfInventory()));
		getBuilder.addAction(new Message("You get %s.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.ITEM))));
		getBuilder.addAction(new Message("%s gets %s.", new WhatStrategyNotSelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.SELF, Syntax.ITEM))));
		getBuilder.addBook(generalSkills);
		getBuilder.setName("get");
		getBuilder.setFailMsg(new Message("There is no \"%s\" here.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.ITEM)))); 
		getBuilder.addSyntax(Skills.Syntax.SKILL);
		getBuilder.addSyntax(Skills.Syntax.ITEM);
		getBuilder.setId(11);
		getBuilder.complete();
	}
	
	//hardcoded kick skill 9
	public static void kickSkill() {
		SkillBuilder kickBuilder = new SkillBuilder();
		kickBuilder.addAction(new Damage(10, new WhatStrategySelf(), new WhereStrategyHere(), false, null));
		kickBuilder.addAction(new Message("You kicked yourself.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>()));
		kickBuilder.addBook(generalSkills);
		kickBuilder.setId(9);
		kickBuilder.setName("kick");
		kickBuilder.addSyntax(Skills.Syntax.SKILL);
		kickBuilder.complete();
	}
	
	//hardcoded punch skill 20
	public static void addPunchSkill() {
		SkillBuilder punchBuilder = new SkillBuilder();
		punchBuilder.addAction(new Damage(10, new WhatStrategyTarget(), new WhereStrategyHere(), false, null));
		punchBuilder.addAction(new Message("You punch %s.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.TARGET))));
		punchBuilder.addAction(new Message("%s punches %s.", new WhatStrategyOtherMobiles(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.SELF, Syntax.TARGET))));
		punchBuilder.addAction(new Message("%s punches you.", new WhatStrategyTarget(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.SELF, Syntax.TARGET))));
		punchBuilder.setFailMsg(new Message("There is no \"%s\" here for you to punch.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.TARGET))));
		punchBuilder.addBook(generalSkills);
		punchBuilder.setId(20);
		punchBuilder.setName("punch");
		punchBuilder.addSyntax(Skills.Syntax.SKILL);
		punchBuilder.addSyntax(Skills.Syntax.TARGET);
		punchBuilder.complete();
	}
	
	//hardcoded godcreate skill 8
	public static void addGodCreateSkill() {
		SkillBuilder godCreateBuilder = new SkillBuilder();
		godCreateBuilder.addAction(new Godcreate());
		godCreateBuilder.addBook(generalSkills);
		godCreateBuilder.setName("godcreate");
		godCreateBuilder.addSyntax(Skills.Syntax.SKILL);
		godCreateBuilder.setId(8);
		godCreateBuilder.complete();
	}
	
	// Currently displays "%s leaves" even if they couldn't move a direction, and thus don't leave
	//hardcoded move skill 12
	public static void addMoveSkill() {
		SkillBuilder moveBuilder = new SkillBuilder();
		moveBuilder.addAction(new Message("%s leaves.", new WhatStrategySelf(), new WhereStrategyOneAway(), new ArrayList<Syntax>(Arrays.asList(Syntax.SELF))));
		moveBuilder.addAction(new MoveHoldable(new WhatStrategySelf(), new WhereStrategyHere(), new WhereStrategyOneAway()));
///\		moveBuilder.addAction(new Look());
		moveBuilder.addBook(generalSkills);
		moveBuilder.setName("move");
		moveBuilder.setFailMsg(new Message("You can't go %s.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.DIRECTION))));
		moveBuilder.addAction(new Message("%s enters.", new WhatStrategySelf(), new WhereStrategyOneAway(), new ArrayList<Syntax>(Arrays.asList(Syntax.SELF))));
		moveBuilder.addSyntax(Skills.Syntax.SKILL);
		moveBuilder.addSyntax(Skills.Syntax.DIRECTION);
		moveBuilder.setId(12);
		moveBuilder.complete();
	}
	
	//hardcoded look skill 13
/*	public static void addLookSkill() {
		SkillBuilder lookBuilder = new SkillBuilder();
//		lookBuilder.addAction(new Look(new WhereStrategyHere())); //need both Here & OneLocAway
		lookBuilder.addBook(generalSkills);
		lookBuilder.setName("look");
		lookBuilder.addSyntax(Skills.Syntax.SKILL);
		lookBuilder.addSyntax(Skills.Syntax.DIRECTION);
		lookBuilder.setId(13);
		lookBuilder.complete();
	} */
		
	//hardcoded drop skill 14
	public static void addDropSkill() {
		SkillBuilder dropBuilder = new SkillBuilder();
		dropBuilder.addAction(new MoveHoldable(new WhatStrategyItem(), new WhereStrategySelfInventory(), new WhereStrategyHere()));
		dropBuilder.addAction(new Message("You drop %s.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.ITEM))));
		dropBuilder.addAction(new Message("%s drops %s.", new WhatStrategyNotSelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.SELF, Syntax.ITEM))));		
		dropBuilder.addBook(generalSkills);
		dropBuilder.setName("drop");
		dropBuilder.setFailMsg(new Message("You failed to drop \"%s\".", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.ITEM))));
		dropBuilder.addSyntax(Skills.Syntax.SKILL);
		dropBuilder.addSyntax(Skills.Syntax.ITEM);
		dropBuilder.setId(14);
		dropBuilder.complete();	
	}
	
	//hardcoded examine skill 15
	public static void addExamineSkill() {
		SkillBuilder examineBuilder = new SkillBuilder();
		examineBuilder.addAction(new Examine(new WhereStrategyHere()));
		examineBuilder.addBook(generalSkills);
		examineBuilder.setName("examine");
		examineBuilder.setFailMsg(new Message("You don't see \"%s\".", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.ITEM)))); 
		examineBuilder.addSyntax(Skills.Syntax.SKILL);
		examineBuilder.addSyntax(Skills.Syntax.ITEM);
		examineBuilder.setId(15);
		examineBuilder.complete();		
	}
	
	// try OR instead of multiplestrategies
	//hardcoded throw skill 17
	public static void addThrowSkill() {
		SkillBuilder throwBuilder = new SkillBuilder();
		
		throwBuilder.addAction(new Or(  // Runs the first chain, if it fails, runs the second chain, moves on to next action if one version succeeds.
				new And(  // Version one deals damage, then moves the thrown item. Searchs for the target HERE
						new Damage(5, new WhatStrategyTarget(), new WhereStrategyHere(), false, null),
						new MoveHoldable(new WhatStrategyItem(), new WhereStrategySelfInventory(), new WhereStrategyHere())),
				new And( // Version one deals damage, then moves the thrown item. Searchs for the target ONE AWAY
						new Damage(5, new WhatStrategyTarget(), new WhereStrategyOneAway(), false, null),
						new MoveHoldable(new WhatStrategyItem(), new WhereStrategySelfInventory(), new WhereStrategyOneAway())))); 	
	
		throwBuilder.addAction(new Message("You throw a %s at %s.", new WhatStrategySelf(), new WhereStrategyHere(),
				new ArrayList<Syntax>(Arrays.asList(Syntax.ITEM, Syntax.TARGET))));
		throwBuilder.addAction(new Message("%s throws a %s at you!", new WhatStrategyTarget(), new WhereStrategyMultiples(new WhereStrategyHere(),
				new WhereStrategyOneAway()), new ArrayList<Syntax>(Arrays.asList(Syntax.SELF, Syntax.ITEM))));
		throwBuilder.setFailMsg(new Message("You don't have a \"%s\" to throw.", new WhatStrategySelf(), new WhereStrategyHere(), 
				new ArrayList<Syntax>(Arrays.asList(Syntax.ITEM)))); //multiple fail messages?
		throwBuilder.addBook(generalSkills);
		throwBuilder.setName("throw");
		throwBuilder.addSyntax(Skills.Syntax.SKILL);
		throwBuilder.addSyntax(Skills.Syntax.ITEM);
		throwBuilder.addSyntax(Skills.Syntax.TARGET); //how to syntax throw (item) at (target)?
		throwBuilder.addSyntax(Skills.Syntax.DIRECTION);
		throwBuilder.setId(17);
		throwBuilder.complete();	
	}
	
	//hardcoded give skill 18
	public static void addGiveSkill() {
		SkillBuilder giveBuilder = new SkillBuilder();
		giveBuilder.addAction(new MoveHoldable(new WhatStrategyItem(), new WhereStrategySelfInventory(), new WhereStrategyHere(), new WhatStrategyTarget()));				
		giveBuilder.addAction(new Message("You give %s to %s.", new WhatStrategySelf(), new WhereStrategyHere(), 
				new ArrayList<Syntax>(new ArrayList<Syntax>(Arrays.asList(Syntax.ITEM,Syntax.TARGET)))));
		giveBuilder.addAction(new Message("%s gives %s to %s.", new WhatStrategyOtherMobiles(), new WhereStrategyHere(), 
				new ArrayList<Syntax>(new ArrayList<Syntax>(Arrays.asList(Syntax.SELF, Syntax.ITEM,Syntax.TARGET)))));
		giveBuilder.addAction(new Message("%s gives %s to you.", new WhatStrategyTarget(), new WhereStrategyHere(), 
				new ArrayList<Syntax>(new ArrayList<Syntax>(Arrays.asList(Syntax.SELF, Syntax.ITEM)))));
		giveBuilder.setFailMsg(new Message("You can't give that away. (generic)", new WhatStrategySelf(), new WhereStrategyHere(), 
				new ArrayList<Syntax>())); //need multiple fail messages - don't have item, can't find target, (later: can't give away)
		giveBuilder.addBook(generalSkills);
		giveBuilder.setName("give");
		giveBuilder.addSyntax(Skills.Syntax.SKILL);
		giveBuilder.addSyntax(Skills.Syntax.ITEM);
		giveBuilder.addSyntax(Skills.Syntax.TARGET);
		giveBuilder.setId(18);
		giveBuilder.complete();	
	}
	
	//hardcoded msgWorld skill 19
	public static void addMsgWorldSkill() {
		SkillBuilder msgWorldBuilder = new SkillBuilder();
		msgWorldBuilder.addAction(new Message("%s says to the world: %s", new WhatStrategyAllMobiles(), new WhereStrategyEverywhere(), 
				new ArrayList<Syntax>(new ArrayList<Syntax>(Arrays.asList(Syntax.SELF,Syntax.LIST))))); 
		msgWorldBuilder.addBook(generalSkills);
		msgWorldBuilder.setName("msg");
		msgWorldBuilder.addSyntax(Skills.Syntax.SKILL);
		msgWorldBuilder.addSyntax(Skills.Syntax.LIST);
		msgWorldBuilder.setId(19);
		msgWorldBuilder.complete();	
	}
	
	//hardcoded Say skill 22
	public static void addSaySkill() {
		SkillBuilder sayBuilder = new SkillBuilder();
		sayBuilder.addAction(new Message("%s says: %s", new WhatStrategyAllMobiles(), new WhereStrategyHere(), 
				new ArrayList<Syntax>(new ArrayList<Syntax>(Arrays.asList(Syntax.SELF,Syntax.LIST))))); 
		sayBuilder.addBook(generalSkills);
		sayBuilder.setName("say");
		sayBuilder.addSyntax(Skills.Syntax.SKILL);
		sayBuilder.addSyntax(Skills.Syntax.LIST);
		sayBuilder.setId(22);
		sayBuilder.complete();	
	}
	
	//hardcoded tell skill 27
	public static void addTellSkill() {
		SkillBuilder tellBuilder = new SkillBuilder();
		tellBuilder.addAction(new Message("%s tells you: %s", new WhatStrategyTarget(), new WhereStrategyEverywhere(), 
				new ArrayList<Syntax>(new ArrayList<Syntax>(Arrays.asList(Syntax.SELF,Syntax.LIST))))); 
		tellBuilder.addBook(generalSkills);
		tellBuilder.setName("tell");
		tellBuilder.addSyntax(Skills.Syntax.SKILL);
		tellBuilder.addSyntax(Skills.Syntax.TARGET);
		tellBuilder.addSyntax(Skills.Syntax.LIST);
		tellBuilder.setId(27);
		tellBuilder.complete();	
	}
	
	//hardcoded selfInventory skill 21
	public static void addSelfInvSkill() {
		SkillBuilder selfInvBuilder = new SkillBuilder();
		selfInvBuilder.addAction(new Message("Your inventory: %s", new WhatStrategySelf(), new WhereStrategySelfInventory(), 
				new ArrayList<Syntax>(new ArrayList<Syntax>(Arrays.asList(Syntax.SELF)))));
		selfInvBuilder.addBook(generalSkills);
		selfInvBuilder.setName("inv");
		selfInvBuilder.addSyntax(Skills.Syntax.SKILL);
		selfInvBuilder.addSyntax(Skills.Syntax.SELF);
		selfInvBuilder.setId(21);
		selfInvBuilder.complete();	
	}
	
	//hardcoded who skill 25
	public static void addWhoSkill() {
		SkillBuilder whoBuilder = new SkillBuilder();
		whoBuilder.addAction(new Message("Online: \n%s", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(new ArrayList<Syntax>())));
		whoBuilder.addBook(generalSkills);
		whoBuilder.setName("who");
		whoBuilder.addSyntax(Skills.Syntax.SKILL);
		whoBuilder.setId(25);
		whoBuilder.complete();
	}
	
	//hardcoded exits skill 26
	public static void addExitsSkill() {
		SkillBuilder exitsBuilder = new SkillBuilder();
		//need location.getExits() to display
		exitsBuilder.addBook(generalSkills);
		exitsBuilder.setName("exits"); //should include "ex"
		exitsBuilder.addSyntax(Skills.Syntax.SKILL);
		exitsBuilder.setId(26);
		exitsBuilder.complete();
	}
	
	//hardcoded custom emotes skill 28
	public static void addEmotesSkill() {
		SkillBuilder emotesBuilder = new SkillBuilder();
		emotesBuilder.addAction(new Message("You %s", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(new ArrayList<Syntax>(Arrays.asList(Syntax.LIST)))));
		emotesBuilder.addAction(new Message("%s %s", new WhatStrategyNotSelf(), new WhereStrategyHere(), new ArrayList<Syntax>(new ArrayList<Syntax>(Arrays.asList(Syntax.SELF, Syntax.LIST)))));
		emotesBuilder.addBook(generalSkills);
		emotesBuilder.setName("emote"); //should include "em"
		emotesBuilder.addSyntax(Skills.Syntax.SKILL);
		emotesBuilder.addSyntax(Skills.Syntax.LIST);
		emotesBuilder.setId(28);
		emotesBuilder.complete();
	}
	
	//hardcoded info skill 29 - should handle info here, info item, info inventory
	public static void addInfoSkill() {
		SkillBuilder infoBuilder = new SkillBuilder();
		infoBuilder.addAction(new Message("You see: \n%s", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(new ArrayList<Syntax>(Arrays.asList(Syntax.LIST)))));
		infoBuilder.addBook(generalSkills);
		infoBuilder.setName("info"); 
//		infoBuilder.addSyntax(Skill.Syntax.SKILL);
//		infoBuilder.addSyntax(Skill.Syntax.TARGET);		
		infoBuilder.setId(26);
		infoBuilder.complete();
	}
	
}