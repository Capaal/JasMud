package processes;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import interfaces.Container;
import interfaces.Mobile;
import processes.Location.Direction;

//import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.List;
import java.util.Map;

//import processes.Skill.Syntax;
//import tests.DamageTest.SkillStub;
import TargettingStrategies.*;
import actions.*;
import actions.Message.msgStrings;

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
		addGodCreateSkill();
		addMoveSkill();
		addLookSkill();
		addDropSkill();
		addGetSkill();
		addExamineSkill();
		addSaySkill();
		addThrowSkill();
		addGiveSkill();
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
		int loc1 = 1;	
		LocationBuilder firstLoc = new LocationBuilder();
		firstLoc.setId(loc1);
		firstLoc.setName("Start.");
		firstLoc.complete();	

		//2nd location, south exit to 1, north exit to 3
		int loc2 = 2;
		LocationBuilder newLoc2 = new LocationBuilder();
		newLoc2.setId(loc2);
		newLoc2.setName("North of Start.");
		newLoc2.south(1,"north"); //location2 connects south to loc1 from loc1's north
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
		newLoc6.south(5, "east");
		newLoc6.complete();	
		
		//7th location, west exit to 6
		int loc7 = 7;
		LocationBuilder newLoc7 = new LocationBuilder();
		newLoc7.setId(loc7);
		newLoc7.setName("End of trail.");
		newLoc7.south(6, "east");
		newLoc7.out(2,"in");
		newLoc7.complete();	
		
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
		newItem.complete();
	}
	
	public static void makeGoblin() {
		MobileBuilder newGoblin = new MobileBuilder();
		newGoblin.setId(1);
		newGoblin.setName("goblin");
		newGoblin.setLoadOnStartUp(true);
		newGoblin.complete();
	}
	
	// get dagger12345
	public static void addGetSkill() {
		Skill get;
		SkillBuilder getBuilder = new SkillBuilder();
		getBuilder.addAction(new genericMoveItem(new WhereStrategyHere(), new WhereStrategySelfInventory()));
		getBuilder.addAction(new Message("You get something.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<msgStrings>())); //bah, wtf messages
		getBuilder.addBook(generalSkills);
		getBuilder.setName("get");
		getBuilder.addSyntax(Skill.Syntax.SKILL);
		getBuilder.addSyntax(Skill.Syntax.ITEM);
		getBuilder.setId(11);
		getBuilder.complete();
		get = new Skill(getBuilder);
		generalSkills.addSkill(get);
	}
	
	//hardcoded kick skill 9
	public static void kickSkill() {
		Skill kickSkill;
		SkillBuilder kickBuilder = new SkillBuilder();
		kickBuilder.addAction(new Damage(10, new WhatStrategySelf(), new WhereStrategyHere(), false, null));
		kickBuilder.addAction(new Message("You kicked yourself.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<msgStrings>()));
		kickBuilder.setId(9);
		kickBuilder.setName("kick");
		kickBuilder.addSyntax(Skill.Syntax.SKILL);
		kickBuilder.complete();
		kickSkill = new Skill(kickBuilder);
		generalSkills.addSkill(kickSkill);
	}
	
	//hardcoded godcreate skill 10
	public static void addGodCreateSkill() {
		Skill godCreate;
		SkillBuilder godCreateBuilder = new SkillBuilder();
		godCreateBuilder.addAction(new Godcreate());
		godCreateBuilder.addBook(generalSkills);
		godCreateBuilder.setName("godcreate");
		godCreateBuilder.addSyntax(Skill.Syntax.SKILL);
		godCreateBuilder.setId(10);
		godCreateBuilder.complete();
		godCreate = new Skill(godCreateBuilder);
		generalSkills.addSkill(godCreate);
	}
	
	//hardcoded move skill 12
	public static void addMoveSkill() {
		Skill move;
		SkillBuilder moveBuilder = new SkillBuilder();
		moveBuilder.addAction(new Move(new WhatStrategySelf() , new WhereStrategyHere(), new WhereStrategyOneAway()));
	//	moveBuilder.addAction(new Message("You move ", null, null , null));
		moveBuilder.addAction(new Look(new WhereStrategyHere()));
		moveBuilder.addBook(generalSkills);
		moveBuilder.setName("move");
		moveBuilder.addSyntax(Skill.Syntax.SKILL);
		moveBuilder.addSyntax(Skill.Syntax.DIRECTION);
		moveBuilder.setId(12);
		moveBuilder.complete();
		move = new Skill(moveBuilder);
		generalSkills.addSkill(move);
	}
	
	//hardcoded look skill 13
	public static void addLookSkill() {
		Skill look;
		SkillBuilder lookBuilder = new SkillBuilder();
		lookBuilder.addAction(new Look(new WhereStrategyHere())); //need both Here & OneLocAway
		lookBuilder.addBook(generalSkills);
		lookBuilder.setName("look");
		lookBuilder.addSyntax(Skill.Syntax.SKILL);
		lookBuilder.addSyntax(Skill.Syntax.DIRECTION);
		lookBuilder.setId(13);
		lookBuilder.complete();
		look = new Skill(lookBuilder);
		generalSkills.addSkill(look);
	}
	
	//hardcoded drop skill 14
	public static void addDropSkill() {
		Skill drop;
		SkillBuilder dropBuilder = new SkillBuilder();
		dropBuilder.addAction(new genericMoveItem(new WhereStrategySelfInventory(), new WhereStrategyHere()));
		dropBuilder.addAction(new Message("You drop something.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<msgStrings>()));
		dropBuilder.addBook(generalSkills);
		dropBuilder.setName("drop");
		dropBuilder.addSyntax(Skill.Syntax.SKILL);
		dropBuilder.addSyntax(Skill.Syntax.ITEM);
		dropBuilder.setId(14);
		dropBuilder.complete();
		drop = new Skill(dropBuilder);
		generalSkills.addSkill(drop);		
	}
	
	//hardcoded examine skill 15
	public static void addExamineSkill() {
		Skill examine;
		SkillBuilder examineBuilder = new SkillBuilder();
		examineBuilder.addAction(new Examine(new WhereStrategyHere()));
		examineBuilder.addBook(generalSkills);
		examineBuilder.setName("examine");
		examineBuilder.addSyntax(Skill.Syntax.SKILL);
		examineBuilder.addSyntax(Skill.Syntax.ITEM);
		examineBuilder.setId(15);
		examineBuilder.complete();
		examine = new Skill(examineBuilder);
		generalSkills.addSkill(examine);		
	}
	
	//hardcoded say skill 16
	public static void addSaySkill() {
		Skill say;
		SkillBuilder sayBuilder = new SkillBuilder();
		sayBuilder.addAction(new Say());
		sayBuilder.addBook(generalSkills);
		sayBuilder.setName("say");
		sayBuilder.addSyntax(Skill.Syntax.SKILL);
		sayBuilder.setId(16);
		sayBuilder.complete();
		say = new Skill(sayBuilder);
		generalSkills.addSkill(say);		
	}
	
	//hardcoded throw skill 17
	public static void addThrowSkill() {
		Skill throwItem;
		SkillBuilder throwBuilder = new SkillBuilder();
		throwBuilder.addAction(new genericMoveItem(new WhereStrategySelfInventory(), new WhereStrategyHere())); //need Here & OneLocAway
		throwBuilder.addAction(new Damage(5, new WhatStrategySelf(), new WhereStrategyHere(), false, null)); //dmg based on item?
		throwBuilder.addBook(generalSkills);
		throwBuilder.setName("throw");
		throwBuilder.addSyntax(Skill.Syntax.SKILL);
		throwBuilder.addSyntax(Skill.Syntax.ITEM);
		throwBuilder.addSyntax(Skill.Syntax.TARGET); //how to syntax throw (item) at (target)?
		throwBuilder.setId(17);
		throwBuilder.complete();
		throwItem = new Skill(throwBuilder);
		generalSkills.addSkill(throwItem);		
	}
	
	//hardcoded give skill 18
	public static void addGiveSkill() {
		Skill give;
		SkillBuilder giveBuilder = new SkillBuilder();
		giveBuilder.addAction(new genericMoveItem(new WhereStrategyHere(), new WhereStrategyHere()));
		giveBuilder.addBook(generalSkills);
		giveBuilder.setName("give");
		giveBuilder.addSyntax(Skill.Syntax.SKILL);
		giveBuilder.addSyntax(Skill.Syntax.ITEM);
		giveBuilder.addSyntax(Skill.Syntax.TARGET);
		giveBuilder.setId(18);
		giveBuilder.complete();
		give = new Skill(giveBuilder);
		generalSkills.addSkill(give);		
	}
	
}