package processes;


import java.util.ArrayList;
import java.util.Arrays;
import processes.Skill.Syntax;
import TargettingStrategies.*;
import actions.*;

public class CreateWorld {
	
	
	public static SkillBook generalSkills = new SkillBook("generalSkills", 1);
	
	public static void createWorld() {
//		makeWorldFromDatabase();
		makeWorldFromNowhere();
		makeSkills();
		makeItems();
	}
	
	//Hardcoded skill list
	public static void makeSkills() {
		WorldServer.gameState.addBook(1, generalSkills);
		kickSkill();
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
	}
	
	public static void makeItems() {
		makeADagger();
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
		
		// map:
		//   [5] - [6] - [7]
		//   [4]
		//   [3]
		//   [2] - [7]
		//   [1]
		
	}
	
	public static void makeADagger() {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(1);
		newItem.setName("dagger");
		newItem.setDescription("It's a dagger!");
		newItem.complete();
	}
	
	public static void makeGoblin() {
		MobileBuilder newGoblin = new MobileBuilder();
		newGoblin.setId(2);
		newGoblin.setName("goblin");
		newGoblin.setDescription("An ugly goblin.");
		newGoblin.setLoadOnStartUp(true);
		newGoblin.complete();
	}
	
	// get dagger12345
	public static void addGetSkill() {
		SkillBuilder getBuilder = new SkillBuilder();
		getBuilder.addAction(new genericMoveItem(new WhereStrategyHere(), new WhereStrategySelfInventory()));
		getBuilder.addAction(new Message("You get %s.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.ITEM))));
		getBuilder.addBook(generalSkills);
		getBuilder.setName("get");
		getBuilder.addSyntax(Skill.Syntax.SKILL);
		getBuilder.addSyntax(Skill.Syntax.ITEM);
		getBuilder.setId(11);
		getBuilder.complete();
	}
	
	//hardcoded kick skill 9
	public static void kickSkill() {
		SkillBuilder kickBuilder = new SkillBuilder();
		kickBuilder.addAction(new Damage(10, new WhatStrategySelf(), new WhereStrategyHere(), false, null));
		kickBuilder.addAction(new Message("you kicked yourself.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>()));
		kickBuilder.setId(9);
		kickBuilder.setName("kick");
		kickBuilder.addSyntax(Skill.Syntax.SKILL);
		kickBuilder.complete();
	}
	
	public static void addPunchSkill() {
		SkillBuilder punchBuilder = new SkillBuilder();
		punchBuilder.addAction(new Damage(10, new WhatStrategyTarget(), new WhereStrategyHere(), false, null));
		punchBuilder.addAction(new Message("You punch %s.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.TARGET))));
		punchBuilder.addAction(new Message("%s punches %s.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.SELF, Syntax.TARGET))));
		punchBuilder.setId(20);
		punchBuilder.setName("punch");
		punchBuilder.addSyntax(Skill.Syntax.SKILL);
		punchBuilder.addSyntax(Skill.Syntax.TARGET);
		punchBuilder.complete();
	}
	
	//hardcoded godcreate skill 10
	public static void addGodCreateSkill() {
		SkillBuilder godCreateBuilder = new SkillBuilder();
		godCreateBuilder.addAction(new Godcreate());
		godCreateBuilder.addBook(generalSkills);
		godCreateBuilder.setName("godcreate");
		godCreateBuilder.addSyntax(Skill.Syntax.SKILL);
		godCreateBuilder.setId(8);
		godCreateBuilder.complete();
	}
	
	//hardcoded move skill 12
	public static void addMoveSkill() {
		SkillBuilder moveBuilder = new SkillBuilder();
		moveBuilder.addAction(new Move(new WhatStrategySelf() , new WhereStrategyHere(), new WhereStrategyOneAway()));
		moveBuilder.addBook(generalSkills);
		moveBuilder.setName("move");
		moveBuilder.addSyntax(Skill.Syntax.SKILL);
		moveBuilder.addSyntax(Skill.Syntax.DIRECTION);
		moveBuilder.setId(12);
		moveBuilder.complete();
	}
	
	//hardcoded look skill 13
	public static void addLookSkill() {
		SkillBuilder lookBuilder = new SkillBuilder();
		lookBuilder.addAction(new Look(new WhereStrategyHere())); //need both Here & OneLocAway
		lookBuilder.addBook(generalSkills);
		lookBuilder.setName("look");
		lookBuilder.addSyntax(Skill.Syntax.SKILL);
		lookBuilder.addSyntax(Skill.Syntax.DIRECTION);
		lookBuilder.setId(13);
		lookBuilder.complete();
	}
	
	//hardcoded drop skill 14
	public static void addDropSkill() {
		SkillBuilder dropBuilder = new SkillBuilder();
		dropBuilder.addAction(new genericMoveItem(new WhereStrategySelfInventory(), new WhereStrategyHere()));
		dropBuilder.addAction(new Message("You drop %s.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.ITEM))));
		dropBuilder.addBook(generalSkills);
		dropBuilder.setName("drop");
		dropBuilder.addSyntax(Skill.Syntax.SKILL);
		dropBuilder.addSyntax(Skill.Syntax.ITEM);
		dropBuilder.setId(14);
		dropBuilder.complete();	
	}
	
	//hardcoded examine skill 15
	public static void addExamineSkill() {
		SkillBuilder examineBuilder = new SkillBuilder();
		examineBuilder.addAction(new Examine(new WhereStrategyHere()));
		examineBuilder.addBook(generalSkills);
		examineBuilder.setName("examine");
		examineBuilder.addSyntax(Skill.Syntax.SKILL);
		examineBuilder.addSyntax(Skill.Syntax.ITEM);
		examineBuilder.setId(15);
		examineBuilder.complete();		
	}
	
	
	
	//hardcoded throw skill 17
	public static void addThrowSkill() {
		SkillBuilder throwBuilder = new SkillBuilder();
		throwBuilder.addAction(new genericMoveItem(new WhereStrategySelfInventory(), new WhereStrategyHere())); //need Here & OneLocAway
		throwBuilder.addAction(new Damage(5, new WhatStrategySelf(), new WhereStrategyHere(), false, null)); //dmg based on item?
		throwBuilder.addBook(generalSkills);
		throwBuilder.setName("throw");
		throwBuilder.addSyntax(Skill.Syntax.SKILL);
		throwBuilder.addSyntax(Skill.Syntax.ITEM);
		throwBuilder.addSyntax(Skill.Syntax.TARGET); //how to syntax throw (item) at (target)?
		throwBuilder.addSyntax(Skill.Syntax.DIRECTION);
		throwBuilder.setId(17);
		throwBuilder.complete();	
	}
	
	//hardcoded give skill 18
	public static void addGiveSkill() {
		SkillBuilder giveBuilder = new SkillBuilder();
		giveBuilder.addAction(new genericMoveItem(new WhereStrategySelfInventory(), new WhereStrategyHere()));
		giveBuilder.addBook(generalSkills);
		giveBuilder.setName("give");
		giveBuilder.addSyntax(Skill.Syntax.SKILL);
		giveBuilder.addSyntax(Skill.Syntax.ITEM);
		giveBuilder.addSyntax(Skill.Syntax.TARGET);
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
		msgWorldBuilder.addSyntax(Skill.Syntax.SKILL);
		msgWorldBuilder.addSyntax(Skill.Syntax.LIST);
		msgWorldBuilder.setId(19);
		msgWorldBuilder.complete();	
	}
	
	//hardcoded Say skill 20
	public static void addSaySkill() {
		SkillBuilder sayBuilder = new SkillBuilder();
		sayBuilder.addAction(new Message("%s says: %s", new WhatStrategyAllMobiles(), new WhereStrategyHere(), 
				new ArrayList<Syntax>(new ArrayList<Syntax>(Arrays.asList(Syntax.SELF,Syntax.LIST))))); 
		sayBuilder.addBook(generalSkills);
		sayBuilder.setName("say");
		sayBuilder.addSyntax(Skill.Syntax.SKILL);
		sayBuilder.addSyntax(Skill.Syntax.LIST);
		sayBuilder.setId(20);
		sayBuilder.complete();	
	}
	
	//hardcoded selfInventory skill 21
	public static void addSelfInvSkill() {
		SkillBuilder selfInvBuilder = new SkillBuilder();
		selfInvBuilder.addAction(new Message("Your inventory: %s", new WhatStrategySelf(), new WhereStrategySelfInventory(), 
				new ArrayList<Syntax>(new ArrayList<Syntax>(Arrays.asList(Syntax.SELF)))));
		selfInvBuilder.addBook(generalSkills);
		selfInvBuilder.setName("inv");
		selfInvBuilder.addSyntax(Skill.Syntax.SKILL);
		selfInvBuilder.addSyntax(Skill.Syntax.SELF);
		selfInvBuilder.setId(21);
		selfInvBuilder.complete();	
	}
	
	
}