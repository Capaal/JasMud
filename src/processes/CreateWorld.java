package processes;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import interfaces.Container;
import interfaces.Mobile;

import java.sql.SQLException;

import tests.DamageTest.SkillStub;
import TargettingStrategies.TargetHereWhereStrategy;
import TargettingStrategies.TargetSelfWhatStrategy;
import actions.*;

public class CreateWorld {
	
	public static SkillBook generalSkills = new SkillBook("generalSkills", 1);
	
	public static void createWorld() {
//		makeWorldFromDatabase();
		makeWorldFromNowhere();
		makeSkills();
	}
	
	//Hardcoded skill list
	public static void makeSkills() {
		kickSkill();
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
		kickBuilder.addAction(new Damage(10, new TargetSelfWhatStrategy(), new TargetHereWhereStrategy(), false, null));
		kickBuilder.addAction(new Message("You kicked yourself.", new TargetSelfWhatStrategy(), new TargetHereWhereStrategy(), null));
		kickBuilder.setName("kick");
		kickBuilder.setId(10);;
		kickBuilder.complete();
		kickSkill = new Skill(kickBuilder);
		generalSkills.addSkill(kickSkill);
	}
}