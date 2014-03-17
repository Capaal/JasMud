package actions;

import interfaces.*;

import java.sql.SQLException;
import java.util.*;

import processes.*;

//TODO Should damage contain TYPE, so that devs can specify what type that damage is, rather than relying on skill knowing?
// And what about different weapons changing amount of damage?
// Especially when we don't know if the damage SHOULD be affected by an item.
public class Damage extends Action {
	
	
	private final int intensity;
	private final Who who;
	private final Where where;
	private final boolean doesWeaponMatter;
	private final Type damageType;

	public Damage() {
		this(0, Who.SELF, Where.HERE, true, null);
	}
	
	public Damage(int intensity, Who who, Where where, boolean weaponMatter, Type newType) {
		this.intensity = intensity;
		this.who = who;
		this.where = where;
		this.doesWeaponMatter = weaponMatter;
		this.damageType = newType;
	}	
	
	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		ArrayList<Container> loc = where.findLoc(s, fullCommand, currentPlayer);
		ArrayList<Mobile> target = who.findTarget(s, fullCommand, currentPlayer, loc);
		if (target.isEmpty()) {
			return false;
		}
		int finalIntensity = determineFinalIntensity(currentPlayer);
		for (Mobile m : target) {
			m.takeDamage(damageType, finalIntensity);
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
				+ " AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "' AND BOOLEANONE='" + doesWeaponMatter + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	@Override
	protected void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO BLOCK (BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE, BOOLEANONE) VALUES ('DAMAGE', " 
					+ position + ", " +  intensity + ", '" + who.toString() + "', '" + where.toString() + "', '" + doesWeaponMatter + "');";
			try {
				SQLInterface.saveAction(sql);
			} catch (SQLException e) {
				System.out.println("Damage failed to save via sql : " + sql);
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public Action newBlock(Mobile player) {
		int newIntensity = intensity;
		Who newWho = who;
		Where newWhere = where;
		Type newType = damageType;
		try {
			newIntensity = Integer.parseInt(Godcreate.askQuestion("How much damage would you like to cause? Negative is ok.", player));
		} catch (NumberFormatException e) {
			player.tell("That value of intensity is invalid, keep it to integers. (i.e. 10)");
			return this.newBlock(player);
		}
		try {
			newWho = Who.valueOf((Godcreate.askQuestion("Who do you want to target (this is using Syntax).", player)).toUpperCase());
			newWhere = Where.valueOf((Godcreate.askQuestion("Where must this target be? (this is using Syntax).", player)).toUpperCase());
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
		player.tell("Intensity: " + intensity + " Who: " + who.toString() + " Where: " + where.toString());
	}

}
