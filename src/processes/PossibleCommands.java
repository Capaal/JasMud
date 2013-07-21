package processes;
import java.io.*;
import java.util.*;

import skills.*;
import skills.Map;

public class PossibleCommands {

	public static void create() {	
	
		// GENERAL SKILLS
		// AllowableCommands is the command entered --> corresponding case.
		// AllowableCommandsGeneralsDesc or whatever is command entered and it's full syntax definition.
		// AllowableCommandsGeneralsShort or whatever is the short description.
		// skillLevels is the command's corresponding case number --> level requirement.
		
		
		// Adds in directions to commands.
//		for (int i = 0; i < 12; i++) {
//			AllSkills temp = new AllSkills(thisLocation.dirNames[i], "", "", thisLocation.dirNames[i], 0, "", 0, i);
//			WorldServer.allAbilities.put(thisLocation.dirNames[i], temp);
//			AllSkills temp2 = new AllSkills(thisLocation.dirAbbrev[i], "", "", thisLocation.dirAbbrev[i], 0, "", 0, i);
//			WorldServer.allAbilities.put(thisLocation.dirAbbrev[i], temp2);
//		}
		
		// Adds move commands, needs work.
		// I am moving skills into each mobiles, so mobiles has all moves, players/heroes have firebolt, and so on.
		WorldServer.skillCommands.put("north", new Move());
		WorldServer.skillCommands.put("ne", new Move());
		WorldServer.skillCommands.put("nw", new Move());
		WorldServer.skillCommands.put("sw", new Move());
		WorldServer.skillCommands.put("se", new Move());
		WorldServer.skillCommands.put("northeast", new Move());
		WorldServer.skillCommands.put("east", new Move());
		WorldServer.skillCommands.put("south", new Move());
		WorldServer.skillCommands.put("southeast", new Move());
		
		WorldServer.skillCommands.put("southwest", new Move());
		WorldServer.skillCommands.put("west", new Move());
		WorldServer.skillCommands.put("northwest", new Move());
		WorldServer.skillCommands.put("up", new Move());
		WorldServer.skillCommands.put("down", new Move());
		WorldServer.skillCommands.put("in", new Move());
		WorldServer.skillCommands.put("out", new Move());
		WorldServer.skillCommands.put("swim", new Swim());
		
		WorldServer.skillCommands.put("punch", new Punch());
		WorldServer.skillCommands.put("kick", new Kick());
		
		WorldServer.skillCommands.put("hide", new Hide());
		
		WorldServer.skillCommands.put("firebolt", new Firebolt());
		WorldServer.skillCommands.put("icebolt", new Icebolt());
		
		WorldServer.skillCommands.put("look", new Look());
		WorldServer.skillCommands.put("examine", new Examine());
		WorldServer.skillCommands.put("map", new Map());
		WorldServer.skillCommands.put("create", new Create());
		WorldServer.skillCommands.put("bug", new Bug());
		WorldServer.skillCommands.put("bugs", new Bugs());
		
	/*	
		
		AllSkills look = new AllSkills("look", "", "", "look", 0, "", 0, 12);
		WorldServer.allAbilities.put("look", look);
		AllSkills l = new AllSkills("l", "", "", "l", 0, "", 0, 12);
		WorldServer.allAbilities.put("l", l);
		AllSkills say = new AllSkills("say", "", "", "say", 0, "", 0, 13);
		WorldServer.allAbilities.put("say", say);
		AllSkills skills = new AllSkills("skills", "", "", "skills", 0, "", 0, 14);
		WorldServer.allAbilities.put("skills", skills);
		AllSkills ab = new AllSkills("ab", "", "", "ab", 0, "", 0, 15);
		WorldServer.allAbilities.put("ab", ab);
		AllSkills create = new AllSkills("create", "Creates items, weapons only.", 
				"Creates items, weapons only.", "create", 0, "", 0, 16);
		WorldServer.allAbilities.put("create", create);
		AllSkills wield = new AllSkills("wield", "", "", "wield", 0, "", 0, 17);
		WorldServer.allAbilities.put("wield", wield);
		AllSkills unwield = new AllSkills("unwield", "", "", "unwield", 0, "", 0, 18);
		WorldServer.allAbilities.put("unwield", unwield);
		AllSkills delete = new AllSkills("delete", "Deletes items, weapons only.", 
				"Deletes items, weapons only.", "delete", 0, "", 0, 19);
		WorldServer.allAbilities.put("delete", delete);
		AllSkills inventory = new AllSkills("inventory", "", "", "inventory", 0, "", 0, 20);
		WorldServer.allAbilities.put("inventory", inventory);
		AllSkills inv = new AllSkills("inv", "", "", "inv", 0, "", 0, 20);
		WorldServer.allAbilities.put("inv", inv);
		AllSkills i = new AllSkills("i", "", "", "i", 0, "", 0, 20);
		WorldServer.allAbilities.put("i", i);
		AllSkills info = new AllSkills("info", "", "", "info", 0, "", 0, 21);
		WorldServer.allAbilities.put("info", info);
		AllSkills probe = new AllSkills("probe", "", "", "probe", 0, "", 0, 21);
		WorldServer.allAbilities.put("probe", probe);
		AllSkills p = new AllSkills("p", "", "", "p", 0, "", 0, 21);
		WorldServer.allAbilities.put("p", p);
		AllSkills drop = new AllSkills("drop", "", "", "drop", 0, "", 0, 22);
		WorldServer.allAbilities.put("drop", drop);
		AllSkills get = new AllSkills("get", "", "", "get", 0, "", 0, 23);
		WorldServer.allAbilities.put("get", get);
		AllSkills createloc = new AllSkills("createloc", "Creates a new location.", 
				"Creates a new location.", "createloc", 0, "", 0, 24);
		WorldServer.allAbilities.put("createloc", createloc);
		AllSkills deleteloc = new AllSkills("deleteloc", "Deletes a location.", 
				"Deletes a location.", "deleteloc", 0, "", 0, 25);
		WorldServer.allAbilities.put("deleteloc", deleteloc);
		AllSkills createmob = new AllSkills("createmob", "Creates a new mob.", 
				"Creates a new mob.", "createmob", 0, "", 0, 26);
		WorldServer.allAbilities.put("createmob", createmob);
		AllSkills deletemob = new AllSkills("deletemob", "Deletes a mob.", 
				"Deletes a mob.", "deletemob", 0, "", 0, 27);
		WorldServer.allAbilities.put("deletemob", deletemob);	
		AllSkills greet = new AllSkills("greet", "", "", "greet", 0, "", 0, 28);
		WorldServer.allAbilities.put("greet", greet);
		AllSkills bug = new AllSkills("bug", "", "", "bug", 0, "", 0, 29);
		WorldServer.allAbilities.put("bug", bug);
		AllSkills bugs = new AllSkills("bugs", "", "", "bugs", 0, "", 0, 30);
		WorldServer.allAbilities.put("bugs", bugs);
		AllSkills bugedit = new AllSkills("bugedit", "Appends admin comment to player bug report.", 
				"Appends admin comment to player bug report.", "bugedit", 0, "", 0, 31);
		WorldServer.allAbilities.put("bugedit", bugedit);
		AllSkills msg = new AllSkills("msg", "", "", "msg", 0, "", 0, 32);
		WorldServer.allAbilities.put("msg", msg);
		AllSkills rmsg = new AllSkills("rmsg", "", "", "rmsg", 0, "", 0, 33);
		WorldServer.allAbilities.put("rmsg", rmsg);
		AllSkills msgs = new AllSkills("msgs", "", "", "msgs", 0, "", 0, 33);
		WorldServer.allAbilities.put("msgs", msgs);
		AllSkills cmsg = new AllSkills("cmsg", "", "", "cmsg", 0, "", 0, 34);
		WorldServer.allAbilities.put("cmsg", cmsg);
		AllSkills status = new AllSkills("status", "", "", "status", 0, "", 0, 35);
		WorldServer.allAbilities.put("status", status);
		AllSkills stat = new AllSkills("stat", "", "", "stat", 0, "", 0, 35);
		WorldServer.allAbilities.put("stat", stat);
		AllSkills examine = new AllSkills("examine", "", "", "examine", 0, "", 0, 36);
		WorldServer.allAbilities.put("examine", examine);
		AllSkills ex = new AllSkills("ex", "", "", "ex", 0, "", 0, 36);
		WorldServer.allAbilities.put("ex", ex);
		AllSkills map = new AllSkills("map", "", "", "map", 0, "", 0, 37);
		WorldServer.allAbilities.put("map", map);
		AllSkills learn = new AllSkills("learn", "", "", "learn", 0, "", 0, 38);
		WorldServer.allAbilities.put("learn", learn);
		AllSkills concentrate = new AllSkills("concentrate", "", "", "concentrate", 0, "", 0, 39);
		WorldServer.allAbilities.put("concentrate", concentrate);
		AllSkills tell = new AllSkills("tell", "", "", "tell", 0, "", 0, 40);
		WorldServer.allAbilities.put("tell", tell);
		AllSkills glance = new AllSkills("glance", "", "", "glance", 0, "", 0, 41);
		WorldServer.allAbilities.put("glance", glance);
		AllSkills gl = new AllSkills("gl", "", "", "gl", 0, "", 0, 41);
		WorldServer.allAbilities.put("gl", gl);
		AllSkills givelevel = new AllSkills("givelevel", "", "", "givelevel", 0, "", 0, 42);
		WorldServer.allAbilities.put("givelevel", givelevel);
		AllSkills give = new AllSkills("give", "", "", "givelevel", 0, "", 0, 43);
		WorldServer.allAbilities.put("give", give);
		AllSkills who = new AllSkills("who", "", "", "who", 0, "", 0, 44);
		WorldServer.allAbilities.put("who", who);
		AllSkills createnewloc = new AllSkills("createnewloc", "Creates a new location, new method.", 
			"Creates a new location, new method.", "createnewloc", 0, "", 0, 45);
		WorldServer.allAbilities.put("createnewloc", createnewloc);
				
		
		
		// Survival skills
		AllSkills punch = new AllSkills("punch", "Syntax: PUNCH <target>. You will punch the target the target for 10 base damage.", "Punch:         Punches target for set damage.", "punch", 10, "physical", 0, 100);
		WorldServer.survAbilities.put("punch", punch);	
		AllSkills swim = new AllSkills("swim", "Syntax: SWIM <direction>. You will need to swim in, towards, and out of water.", "Swim:       Proficiency in swimming.", "swim", 0, "", 0, 101);
		WorldServer.survAbilities.put("swim", swim);
		AllSkills stab = new AllSkills("stab", "Syntax: STAB <target>. This will utilize the weapon you are wielding to stab your opponent.", "Stab:          Stabs your opponent.", "stab", 0, "physical", 0, 102);
		WorldServer.survAbilities.put("stab", stab);	
		
		
		// Mage skills in object system
		WorldServer.mageAbilities.put("firebolt", firebolt);
		AllSkills heal = new AllSkills("heal", "Syntax: HEAL <target>. You will heal yourself or your target.", "Heal:        Heals target.", "heal", 0, "magical", 5, 301);
		WorldServer.mageAbilities.put("heal", heal);
		AllSkills shield = new AllSkills("shield", "Syntax: SHIELD. This creates a physical shield, decreasing physical damage by 15% for 20 seconds.", "Shield:      Physical barrier.", "shield", 0, "magical", 10, 302);
		WorldServer.mageAbilities.put("shield", shield);
		AllSkills aura = new AllSkills("aura", "Syntax: AURA. This creates a magical barrier, decreasing magical damage by 15% for 20 seconds.", "Aura:        Creates a protective barrier.", "aura", 0, "magical", 15, 303);
		WorldServer.mageAbilities.put("aura", aura);
	
		AllSkills icebolt = new AllSkills("bolt", "Syntax: AURA. This creates a magical barrier, decreasing magical damage by 15% for 20 seconds.", "Aura:        Creates a protective barrier.", "aura", 20, "magical", 15, 304);
		icebolt.setName("icebolt");icebolt.setLongDesc("Syntax:  IceBolt bitches.");
		WorldServer.mageAbilities.put("icebolt", icebolt);

		*/
	}	
	
}

		