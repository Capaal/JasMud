package checks;

import java.util.HashMap;

import processes.SQLInterface;
import processes.Skill;
import interfaces.*;

public class BalanceCheck extends Action {
	
	private final boolean isItThis;
	private final Who who;
	private final Where where;
	
	public BalanceCheck(Boolean isItThis, Who who, Where where) {
		this.isItThis = isItThis;
		this.who = who;
		this.where = where;
	}
	
	@Override
	public boolean activate(Skill s) {
		boolean success = false;
		for (Mobile m : who.findTarget(s,  where.findLoc(s))) {
			if (m.hasBalance() == isItThis) {
				success = true;
			} else {
				return false;
			}
		}
		return success;
	}
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='BALANCECHECK' AND BLOCKPOS=" + position + " AND BOOLEANONE='" + isItThis
				+ "' AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, BOOLEANONE, TARGETWHO, TARGETWHERE) VALUES ('BALANCECHECK', " 
				+ position + ", '" + isItThis + "', '" + who.toString() + "', '" + where.toString() + "');";
		SQLInterface.saveAction(sql);
	}
}
