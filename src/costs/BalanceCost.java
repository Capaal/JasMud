package costs;

import java.sql.SQLException;
import java.util.HashMap;

import processes.SQLInterface;
import processes.Skill;
import checks.BalanceCheck;
import interfaces.*;

public class BalanceCost extends Action {
	
	private final Who who;
	private final Where where;
	private final boolean goesTo;
	
	public BalanceCost(Boolean goesTo, Who who, Where where) {
		this.who = who;
		this.where = where;
		this.goesTo = goesTo;
	}

	@Override
	public boolean activate(Skill s) {
		BalanceCheck thisCheck = new BalanceCheck(!goesTo, who, where);
		if (!thisCheck.activate(s)) {
			return false;
		}
		for (Mobile m : who.findTarget(s, where.findLoc(s))) {
			m.setBalance(goesTo);
		}
		return true;
	}
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='BALANCECOST' AND BLOCKPOS=" + position + " AND BOOLEANONE='" + goesTo
				+ "' AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, BOOLEANONE, TARGETWHO, TARGETWHERE) VALUES ('BALANCECOST', " 
				+ position + ", '" + goesTo + ", '" + who.toString() + "', '" + where.toString() + "');";
		try {
			SQLInterface.saveAction(sql);
		} catch (SQLException e) {
			System.out.println("BalanceCost failed to save via sql : " + sql);
			e.printStackTrace();
		}
	}
}
