package processes;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import interfaces.Container;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
/*		
		//3rd location, south exit to 2
		int loc3 = 2;
		LocationBuilder newLoc3 = new LocationBuilder();
		newLoc3.setId(loc3);
		newLoc3.setName("Road.");
		newLoc3.complete();	
		
		//adding to map/connections
		Direction newDir1 = Direction.NORTH;
		Map<Direction, Location> loc1Map = new HashMap<Direction, Location>() ;
		loc1Map.put(newDir1, newLoc2.getFinishedLocation());
		firstLoc.north(loc2, "north");
		firstLoc.locationMap = loc1Map;
		
		Map<Integer, Direction> firstLocDir = new HashMap<Integer, Direction>();
		firstLocDir.put(2, newDir1);
		*/
		
	}
	
	public static void makeADagger() {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(1);
		newItem.setName("dagger");
		newItem.complete();
	}
	
	public static void makeGoblin() {
		MobileBuilder newGoblin = new MobileBuilder();
		newGoblin.setId(2);
		newGoblin.setName("goblin");
		newGoblin.setLoadOnStartUp(true);
		newGoblin.complete();
	}
	
	// get dagger12345
	public static void addGetSkill() {
		Skill get;
		SkillBuilder getBuilder = new SkillBuilder();
		getBuilder.addAction(new genericMoveItem(new WhereStrategyHere(), new WhereStrategySelfInventory()));
	//	getBuilder.addAction(new Message("You p")); bah, wtf messages
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
		kickBuilder.addAction(new Message("you kicked yourself.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>()));
		kickBuilder.setId(9);
		kickBuilder.setName("kick");
		kickBuilder.addSyntax(Skill.Syntax.SKILL);
		kickBuilder.complete();
		kickSkill = new Skill(kickBuilder);
		generalSkills.addSkill(kickSkill);
	}
	
	public static void addPunchSkill() {
		Skill punchSkill;
		SkillBuilder punchBuilder = new SkillBuilder();
		punchBuilder.addAction(new Damage(10, new WhatStrategyTarget(), new WhereStrategyHere(), false, null));
//		ArrayList<msgStrings> punchStrings = new ArrayList<msgStrings>();
//		punchBuilder.addAction(new Message("You punch %s.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<msgStrings>(Arrays.asList(msgStrings.TARGET))));
		punchBuilder.addAction(new Message("You punch %s.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.TARGET))));
		punchBuilder.addAction(new Message("%s punches %s.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<Syntax>(Arrays.asList(Syntax.SELF, Syntax.TARGET))));
//		punchStrings.add(msgStrings.SELF);
//		punchStrings.add(msgStrings.TARGET);
	//	punchBuilder.addAction(new Message("%s punched %s.", new WhatStrategyOtherMobiles(), new WhereStrategyHere(), punchStrings));
		punchBuilder.setId(20);
		punchBuilder.setName("punch");
		punchBuilder.addSyntax(Skill.Syntax.SKILL);
		punchBuilder.addSyntax(Skill.Syntax.TARGET);
		punchBuilder.complete();
		punchSkill = new Skill(punchBuilder);
		generalSkills.addSkill(punchSkill);
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
		lookBuilder.addAction(new Look(new WhereStrategyHere()));
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
//		dropBuilder.addAction(new Message("You drop ", null, null, null));
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