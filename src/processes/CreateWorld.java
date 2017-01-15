package processes;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import interfaces.Container;
import interfaces.Mobile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import processes.Skill.Syntax;
import tests.DamageTest.SkillStub;
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
	}
	
	public static void makeItems() {
		makeADagger();
	}
	
	public static void makeWorldFromDatabase() {
//		WorldServer.databaseInterface.connect("root", "".toCharArray());
//		WorldServer.databaseInterface.loadLocations();
//		WorldServer.databaseInterface.loadSkillBooks();
//		WorldServer.databaseInterface.loadMobs();
//		WorldServer.databaseInterface.disconnect();
	}
	
	public static void makeWorldFromNowhere() {
		int id = 1;
			
		LocationBuilder newLocation = new LocationBuilder();
		newLocation.setId(id);
		
		newLocation.complete();	
	}
	
	public static void makeADagger() {
		ItemBuilder newItem = new ItemBuilder();	
		newItem.setId(1);
		newItem.setName("dagger");
		newItem.complete();
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
		dropBuilder.addAction(new Message("You drop ", null, null, null));
		dropBuilder.addBook(generalSkills);
		dropBuilder.setName("drop");
		dropBuilder.addSyntax(Skill.Syntax.SKILL);
		dropBuilder.addSyntax(Skill.Syntax.ITEM);
		dropBuilder.setId(14);
		dropBuilder.complete();
		drop = new Skill(dropBuilder);
		generalSkills.addSkill(drop);		
	}
	
}