package checks;

import java.sql.SQLException;
import java.util.*;

import actions.Damage;
import actions.Godcreate;
import processes.*;
import processes.Equipment.EquipmentEnum;
import interfaces.*;
import interfaces.Action.Where;
import interfaces.Action.Who;

public class WeaponEquippedCheck extends Action {
	
	private final Type isItThis;
	private final Who who;
	private final Where where;
	
	public WeaponEquippedCheck() {
		this(Type.BLUNT, Who.SELF, Where.HERE);
	}
	
	public WeaponEquippedCheck(Type isItThis, Who who, Where where) {
		this.isItThis = isItThis;
		this.who = who;
		this.where = where;
	}
	
	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		boolean success = false;
		for (Mobile m : who.findTarget(s, fullCommand, currentPlayer, where.findLoc(s, fullCommand, currentPlayer))) {
			Equipable leftHandItem = m.getEquipmentInSlot(EquipmentEnum.LEFTHAND);
			Equipable rightHandItem = m.getEquipmentInSlot(EquipmentEnum.RIGHTHAND);
			if (rightHandItem != null && rightHandItem.containsType(isItThis)) {
				success = true;				 
			} else if (leftHandItem != null && leftHandItem.containsType(isItThis)) {
				success = true;				 
			} else {
				return false;
			}
		}
		return success;
	}
	@Override
	public Action newBlock(Mobile player) {
		Type newType = isItThis;
		Who newWho = who;
		Where newWhere = where;
		try {
			newWho = Who.valueOf((Godcreate.askQuestion("Who do you want to check? (this is using Syntax).", player)).toUpperCase());
			newWhere = Where.valueOf((Godcreate.askQuestion("Where is this person? (this is using Syntax).", player)).toUpperCase());
			newType = Type.valueOf(Godcreate.askQuestion("What type of weapon must they have equipped?", player).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new WeaponEquippedCheck(newType, newWho, newWhere);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='WEAPONEQUIPPEDCHECK' AND BLOCKPOS=" + position + " AND SKILLTYPEID=" 
				+ isItThis.returnTypeId() + " AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	@Override
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, SKILLTYPEID, TARGETWHO, TARGETWHERE) VALUES ('WEAPONEQUIPPEDCHECK', " 
				+ position + ", " + isItThis.returnTypeId() + ", '" + who.toString() + "', '" + where.toString() + "');";
		try {
			SQLInterface.saveAction(sql);
		} catch (SQLException e) {
			System.out.println("WeaponEquippedCheck failed to save via sql : " + sql);
			e.printStackTrace();
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Checks equipped weapons in left and right hand and compares types to what is desired.");
		player.tell("Type desired: " + isItThis.toString() + " Who: " + who.toString() + " Where; " + where.toString());
	}
}
