package checks;

import java.sql.SQLException;
import java.util.HashMap;

import actions.Damage;
import actions.Godcreate;
import processes.SQLInterface;
import processes.Skill;
import interfaces.*;
import interfaces.Action.Where;
import interfaces.Action.Who;

public class BalanceCheck extends Action {
	
	private final boolean isItThis;
	private final Who who;
	private final Where where;
	
	public BalanceCheck() {
		this(true, Who.SELF, Where.HERE);
	}
	
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
	@Override
	public Action newBlock(Mobile player) {
		boolean newCheck = isItThis;
		Who newWho = who;
		Where newWhere = where;
		String answerBalance = Godcreate.askQuestion("Is this a check of having balance? true/false.", player);
		if ("true".equals(answerBalance) || "false".equals(answerBalance)) {
			newCheck = Boolean.parseBoolean(answerBalance);
		} else {
			player.tell("Error, your answer was not detected as a boolean.");
			return this.newBlock(player);
		}
		try {
			newWho = Who.valueOf((Godcreate.askQuestion("Who do you want to check? (this is using Syntax).", player)).toUpperCase());
			newWhere = Where.valueOf((Godcreate.askQuestion("Where is this target? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new BalanceCheck(newCheck, newWho, newWhere);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='BALANCECHECK' AND BLOCKPOS=" + position + " AND BOOLEANONE='" + isItThis
				+ "' AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	@Override
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, BOOLEANONE, TARGETWHO, TARGETWHERE) VALUES ('BALANCECHECK', " 
				+ position + ", '" + isItThis + "', '" + who.toString() + "', '" + where.toString() + "');";
		try {
			SQLInterface.saveAction(sql);
		} catch (SQLException e) {
			System.out.println("BalanceCheck failed to save via sql : " + sql);
			e.printStackTrace();
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Does a check to compare player's balance state against the desired state.");
		player.tell("Desired balance state: " + isItThis + " Who: " + who.toString() + " Where: " + where.toString());
	}
}
