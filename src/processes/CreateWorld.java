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
	}
	
	//Hardcoded skill list
	public static void makeSkills() {
		WorldServer.gameState.addBook(1, generalSkills);
		kickSkill();
		addGodCreateSkill();
		addGetSkill();
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
	
//Hardcoded Kick skill
	public static void kickSkill() {
		Skill kickSkill;
		SkillBuilder kickBuilder = new SkillBuilder();
		kickBuilder.addAction(new Damage(10, new WhatStrategySelf(), new WhereStrategyHere(), false, null));
		kickBuilder.addAction(new Message("You kicked yourself.", new WhatStrategySelf(), new WhereStrategyHere(), new ArrayList<msgStrings>()));
		kickBuilder.setId(10);
		kickBuilder.setName("kick");
		kickBuilder.addSyntax(Skill.Syntax.SKILL);
		kickBuilder.complete();
		kickSkill = new Skill(kickBuilder);
		generalSkills.addSkill(kickSkill);
	}
	
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
}