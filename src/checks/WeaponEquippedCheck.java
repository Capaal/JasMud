package checks;

import java.util.*;

import TargettingStrategies.*;
import actions.Godcreate;
import processes.*;
import processes.Equipment.EquipmentEnum;
import interfaces.*;

public class WeaponEquippedCheck extends Action {
	
	private final Type isItThis;
	private final WhatTargettingStrategy who;
	private final WhereTargettingStrategy where;
	
	public WeaponEquippedCheck() {
		this(Type.BLUNT, new TargetSelfWhatStrategy(), new TargetHereWhereStrategy());
	}
	
	public WeaponEquippedCheck(Type isItThis, WhatTargettingStrategy who, WhereTargettingStrategy where) {
		this.isItThis = isItThis;
		this.who = who;
		this.where = where;
	}
	
	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		boolean success = false;
		for (Holdable m : who.findWhat(s, fullCommand, currentPlayer, where.findWhere(s, fullCommand, currentPlayer))) {
			if (m instanceof Mobile) {
				Holdable leftHandItem = ((Mobile) m).getEquipmentInSlot(EquipmentEnum.LEFTHAND);
				Holdable rightHandItem = ((Mobile) m).getEquipmentInSlot(EquipmentEnum.RIGHTHAND);
				if (rightHandItem != null && rightHandItem.containsType(isItThis)) {
					success = true;				 
				} else if (leftHandItem != null && leftHandItem.containsType(isItThis)) {
					success = true;				 
				} else {
					return false;
				}
			}
		}
		return success;
	}
	@Override
	public Action newBlock(Mobile player) {
		WhatTargettingFactory whatFactory = new WhatTargettingFactory();
		WhereTargettingFactory whereFactory = new WhereTargettingFactory();
		Type newType = isItThis;
		WhatTargettingStrategy newWho = who;
		WhereTargettingStrategy newWhere = where;
		try {
			newWho = whatFactory.parse((Godcreate.askQuestion("Who do you want to check? (this is using Syntax).", player)).toUpperCase());
			newWhere = whereFactory.parse((Godcreate.askQuestion("Where is this person? (this is using Syntax).", player)).toUpperCase());
			newType = Type.valueOf(Godcreate.askQuestion("What type of weapon must they have equipped?", player).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new WeaponEquippedCheck(newType, newWho, newWhere);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='WEAPONEQUIPPEDCHECK' AND BLOCKPOS=" + position + " AND TYPE='" 
				+ isItThis.toString() + "' AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return WorldServer.databaseInterface.returnBlockView(blockQuery);
	}
	@Override
	public void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, TYPE, TARGETWHO, TARGETWHERE) VALUES ('WEAPONEQUIPPEDCHECK', " 
					+ position + ", '" + isItThis.toString() + "', '" + who.toString() + "', '" + where.toString() + "');";
			WorldServer.databaseInterface.saveAction(sql);
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Checks equipped weapons in left and right hand and compares types to what is desired.");
		player.tell("Type desired: " + isItThis.toString() + " Who: " + who.toString() + " Where; " + where.toString());
	}
}
