package actions;

import interfaces.*;
import java.util.*;
import TargettingStrategies.*;
import processes.*;


public class Damage extends Action {
	
	
	private final int intensity;
	private final WhatStrategyInterface what;
	private final WhereStrategyInterface where;
	private final boolean doesWeaponMatter;
	private final Type damageType;

	public Damage() {
		this(0, new WhatStrategySelf(), new WhereStrategyHere(), true, null);
	}
	
	public Damage(int intensity, WhatStrategyInterface what, WhereStrategyInterface where, boolean weaponMatter, Type newType) {
		this.intensity = intensity;
		this.what = what;
		this.where = where;
		this.doesWeaponMatter = weaponMatter;
		this.damageType = newType;
	}	
	
	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		List<Container> loc = where.findWhere(s, fullCommand, currentPlayer);
		List<Holdable> target = what.findWhat(s, fullCommand, currentPlayer, loc);
		if (target.isEmpty()) {
			return false;
		}
		int finalIntensity = determineFinalIntensity(currentPlayer);
		for (Holdable m : target) {
			if (m instanceof Mobile) {
				((Mobile)m).takeDamage(damageType, finalIntensity);
			}	
		}
		return true;
	}
	
	private int determineFinalIntensity(Mobile currentPlayer) {
		if (doesWeaponMatter) {
			double weaponMultiplier = currentPlayer.getWeaponMultiplier();
			return (int) (intensity * weaponMultiplier);
		}
		return intensity;
	}
	
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='DAMAGE' AND INTVALUE=" + intensity + " AND BLOCKPOS=" + position
				+ " AND TARGETWHO='" + what.toString() + "' AND TARGETWHERE='" + where.toString() + "' AND BOOLEANONE='" + doesWeaponMatter + "'"
				+ " AND TYPE='" + damageType.toString() + "';";
		return WorldServer.databaseInterface.returnBlockView(blockQuery);
	}
	
	@Override
	public void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO BLOCK (BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE, BOOLEANONE, TYPE) VALUES ('DAMAGE', " 
					+ position + ", " +  intensity + ", '" + what.toString() + "', '" + where.toString() + "', '" + doesWeaponMatter + "'"
					+ ", '" + damageType.toString() + "');";
			WorldServer.databaseInterface.saveAction(sql);
		}
	}
	
	@Override
	public Action newBlock(Mobile player) {
		int newIntensity = intensity;
		WhatStrategyInterface newWho = what;
		WhereStrategyInterface newWhere = where;
		WhatFactory whatFactory = new WhatFactory();
		WhereFactory whereFactory = new WhereFactory();
		Type newType = damageType;
		try {
			newIntensity = Integer.parseInt(Godcreate.askQuestion("How much damage would you like to cause? Negative is ok.", player));
		} catch (NumberFormatException e) {
			player.tell("That value of intensity is invalid, keep it to integers. (i.e. 10)");
			return this.newBlock(player);
		}
		try {
			newWho = whatFactory.parse((Godcreate.askQuestion("Who do you want to target (this is using Syntax).", player)).toUpperCase());
			newWhere = whereFactory.parse((Godcreate.askQuestion("Where must this target be? (this is using Syntax).", player)).toUpperCase());
			newType = Type.valueOf((Godcreate.askQuestion("What type of damage will this deal? (In syntax null is ok i.e SHARP)", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE, SHARP)");
			return this.newBlock(player);
		}
		boolean doesNewWeaponMatter = true;
		String answerWeaponMatter = Godcreate.askQuestion("Does this damage get affected by equipped weapon's stats? true/false.", player);
		if ("true".equals(answerWeaponMatter) || "false".equals(answerWeaponMatter)) {
			doesNewWeaponMatter = Boolean.parseBoolean(answerWeaponMatter);
		} else {
			player.tell("Error, your answer was not detected as a boolean.");
			return this.newBlock(player);
		}
		return new Damage(newIntensity, newWho, newWhere, doesNewWeaponMatter, newType);
	}
	
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Affects hp in a positive or negative way.");
		player.tell("Intensity: " + intensity + " Who: " + what.toString() + " Where: " + where.toString());
		player.tell("Uses weapon: " + doesWeaponMatter + " Damage type: " + damageType.toString());
	}

}
