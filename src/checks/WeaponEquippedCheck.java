package checks;

import java.util.HashMap;

import processes.SQLInterface;
import processes.Skill;
import processes.Type;
import interfaces.*;

public class WeaponEquippedCheck extends Action {
	
	private final Type isItThis;
	private final Who who;
	private final Where where;
	
	public WeaponEquippedCheck(Type isItThis, Who who, Where where) {
		this.isItThis = isItThis;
		this.who = who;
		this.where = where;
	}
	
	@Override
	public boolean activate(Skill s) {
		boolean success = false;
		for (Mobile m : who.findTarget(s, where.findLoc(s))) {
			if (m.hasWeaponType(isItThis)) {
				success = true;
			} else {
				return false;
			}
		}
		return success;
	}
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='WEAPONEQUIPPEDCHECK' AND BLOCKPOS=" + position + " AND SKILLTYPEID=" 
				+ isItThis.returnTypeId() + " AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, SKILLTYPEID, TARGETWHO, TARGETWHERE) VALUES ('WEAPONEQUIPPEDCHECK', " 
				+ position + ", " + isItThis.returnTypeId() + ", '" + who.toString() + "', '" + where.toString() + "');";
		SQLInterface.saveAction(sql);
	}
}
