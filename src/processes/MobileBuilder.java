package processes;

import interfaces.Container;
import interfaces.Effect;
import interfaces.Holdable;
import interfaces.Mobile;
import items.StdItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import actions.Godcreate;
import processes.Equipment.EquipmentEnum;

public class MobileBuilder {	

	public String name;
	public int id;	
	public String password = "";
	public String description = "Generic.";
	public String shortDescription = "Short and Generic.";
	public int maxHp = 100;
	public int currentHp = 100;
	public Container location = WorldServer.gameState.locationCollection.get(1);
	public boolean isDead = false;
	public int xpWorth = 1;
	public int experience = 1;
	public int level = 1;
	public int age = 1;
	public boolean loadOnStartUp = false;
	public Set<Holdable> inventory = new HashSet<Holdable>();
	public Equipment equipment = new Equipment();	
	public ArrayList<Effect> effectList = new ArrayList<Effect>();		
	
	public MobileBuilder() {
		// might change based on implementation.
		equipment(EquipmentEnum.HEAD,  null);
		equipment(EquipmentEnum.NECK,  null);
		equipment(EquipmentEnum.LEFTEAR,  null);
		equipment(EquipmentEnum.RIGHTEAR,  null);
		equipment(EquipmentEnum.LEFTHAND,  null);
		equipment(EquipmentEnum.RIGHTHAND,  null);
		equipment(EquipmentEnum.CHEST,  null);
		equipment(EquipmentEnum.LEGS,  null);
		equipment(EquipmentEnum.FEET,  null);
		equipment(EquipmentEnum.LEFTFINGER,  null);
		equipment(EquipmentEnum.RIGHTFINGER,  null);
	}		
	
	public void id(int val) {id = val;}
	public void name(String val) {
		if (val != null && UsefulCommands.checkIfValidCharacters(val)) {
			name = val;
		} else {
			throw new IllegalArgumentException("Name givin is invalid: " + val);
		}
	}
	
	
	// The below are effectively the Builder's constructor methods. As they will be gathering data for the finalized StdMob.
	public void password(String val) {password = val;}		
	public void description(String val) {description = val;}		
	public void shortDescription(String val) {shortDescription = val;}		
	public void currentHp(int val) {currentHp = val;}
	public void isDead(boolean val) {isDead = val;}
	public void isDead(int val) {
		if (val == 1) {
			isDead = true;
		} else {
			isDead = false;
		}
	}
	public void location(Container val) {location = val;}			
	public void inventory(Holdable val) {inventory.add(val);}
	public void equipment(EquipmentEnum slot, StdItem val) {equipment.forceEquip(slot, val); }
	public void xpWorth(int val) {xpWorth = val;}	
	public void effect(Effect effect) {effectList.add(effect); }
	public void experience(int val) {experience = val;}
	public void level(int val) {level = val;}
	public void age(int val) {age = val;}
	public void loadOnStartUp(boolean val) {loadOnStartUp = val;}
	
	public void setMaxHp() {
		maxHp = 100 + level * 5;
		if (currentHp > maxHp) {
			currentHp = maxHp;
		}
	}
	
	public StdMob complete() {
		setMaxHp();
		return new StdMob(this);
	}

	public static boolean newMobile(Mobile player, MobileBuilder mobileBuilder) {
		String nextTask = Godcreate.askQuestion("What next? 1:name 2:password 3:longdescrption 4:shortdescription"
				+ " 5:level 6:currentHp 7:isdead 8:location 9:inventory 10:equipment 11:xpworth 12:loadonstartup"
				+ " 13:preview 14:complete 15:quit", player);
		switch(nextTask) {
		case "1":
		case "name":
			mobileBuilder.name(Godcreate.askQuestion("What is the mobile's name?", player));
			return newMobile(player, mobileBuilder);
		case "2":
		case "password":
			mobileBuilder.password(Godcreate.askQuestion("What is the mobile's password?", player));
			return newMobile(player, mobileBuilder);
		case "3":
		case "longdescription":
			mobileBuilder.description(Godcreate.askQuestion("What is the mobile's long description?", player));
			return newMobile(player, mobileBuilder);
		case "4":
		case "shortdescription":
			mobileBuilder.shortDescription(Godcreate.askQuestion("What is the mobile's short description?", player));
			return newMobile(player, mobileBuilder);
		case "5":
		case "level":
			try {
				mobileBuilder.level(Integer.parseInt(Godcreate.askQuestion("What level will this mobile be at?", player)));
			} catch(NumberFormatException e) {
				player.tell("That was not formatted as a valid number.");
			}
			return newMobile(player, mobileBuilder);
		case "6":
		case "currentHp":
			try {
				mobileBuilder.currentHp(Integer.parseInt(Godcreate.askQuestion("What hp will the mobile start at?", player)));
			} catch(NumberFormatException e) {
				player.tell("That was not formatted as a valid number.");
			}
			return newMobile(player, mobileBuilder);
		case "7":
		case "isdead":
			mobileBuilder.isDead(Boolean.parseBoolean(Godcreate.askQuestion("true/false is the mobile already dead?", player)));
			player.tell("isDead has been set to: " + mobileBuilder.isDead);
			return newMobile(player, mobileBuilder);
		case "8":
		case "location":		
			int locationId = Integer.parseInt(Godcreate.askQuestion("What location id will this mobile start in?", player));
			Location location = WorldServer.gameState.locationCollection.get(locationId);
			if (location != null) {
				mobileBuilder.location(location);
			} else {
				player.tell("Could not find that location id.");
			}		
			return newMobile(player, mobileBuilder);
		case "9":
		case "inventory": // NOT IMPLEMENTED TODO
			player.tell("Setting up inventory is not implemented yet.");
			return newMobile(player, mobileBuilder);
		case "10":
		case "equipment": // NOT IMPLEMENTED TODO
			player.tell("Setting up equipment is not implemented yet.");
			return newMobile(player, mobileBuilder);
		case "11":
		case "xpworth":
			try {
				mobileBuilder.xpWorth(Integer.parseInt(Godcreate.askQuestion("What is the mobile's xp worth?", player)));
			} catch(NumberFormatException e) {
				player.tell("That was not formatted as a valid number.");
			}
			return newMobile(player, mobileBuilder);
		case "12":
		case "loadonstartup":
			mobileBuilder.loadOnStartUp(Boolean.parseBoolean(Godcreate.askQuestion("true/false should the mobile load on startup?", player)));
			player.tell("loadOnStartUp has been set to: " + mobileBuilder.loadOnStartUp);
			return newMobile(player, mobileBuilder);
		case "13":
		case "preview":
			player.tell("Name: " + mobileBuilder.name);
			player.tell("password: " + mobileBuilder.password);
			player.tell("longdescription: " + mobileBuilder.description);
			player.tell("Shortdescription: " + mobileBuilder.shortDescription);
			player.tell("MaxHp: " + (100 + mobileBuilder.level * 5));
			player.tell("CurrentHp: " + mobileBuilder.currentHp);
			player.tell("level: " + mobileBuilder.level);
			player.tell("isDead: " + mobileBuilder.isDead);
			player.tell("Location: " + mobileBuilder.location.getId());
			player.tell("xpWorth: " + mobileBuilder.xpWorth);
			player.tell("LoadOnStartUp: " + mobileBuilder.loadOnStartUp);
			return newMobile(player, mobileBuilder);
		case "14":
		case "complete":
			Mobile newMob = new StdMob(mobileBuilder);
			newMob.getContainer().acceptItem(newMob);
			return true;
		case "15":
		case "quit":
		case "exit":
			return false;
		default:
			player.tell("That wasn't a valid option, remember, you can use the word, or the number.");
			return newMobile(player, mobileBuilder);
		}
	}
	
}
