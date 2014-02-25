package costs;

import java.sql.SQLException;
import java.util.HashMap;

import actions.Godcreate;
import processes.SQLInterface;
import processes.Skill;
import checks.BalanceCheck;
import interfaces.*;

public class BalanceCost extends Action {
	
	private final Who who;
	private final Where where;
	private final boolean goesTo;
	
	public BalanceCost() {
		this(true, Who.SELF, Where.HERE);
	}
	
	public BalanceCost(Boolean goesTo, Who who, Where where) {
		this.who = who;
		this.where = where;
		this.goesTo = goesTo;
	}

	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		BalanceCheck thisCheck = new BalanceCheck(!goesTo, who, where);
		if (!thisCheck.activate(s, fullCommand, currentPlayer)) {
			return false;
		}
		for (Mobile m : who.findTarget(s, fullCommand, currentPlayer, where.findLoc(s, fullCommand, currentPlayer))) {
			m.setBalance(goesTo);
		}
		return true;
	}
	@Override
	public Action newBlock(Mobile player) {
		boolean newBalance = goesTo;
		Who newWho = who;
		Where newWhere = where;
		String answerBoolean = Godcreate.askQuestion("What might their balance go to? true/false.", player);
		if ("true".equals(answerBoolean) || "false".equals(answerBoolean)) {
			newBalance = Boolean.parseBoolean(answerBoolean);
		} else {
			player.tell("Error, your answer was not detected as a boolean.");
			return this.newBlock(player);
		}
		try {
			newWho = Who.valueOf((Godcreate.askQuestion("Who's balance is being changed? (this is using Syntax).", player)).toUpperCase());
			newWhere = Where.valueOf((Godcreate.askQuestion("Where is this target? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new BalanceCost(newBalance, newWho, newWhere);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='BALANCECOST' AND BLOCKPOS=" + position + " AND BOOLEANONE='" + goesTo
				+ "' AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	@Override
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
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Sets balance to the new value. DOES run a balancecheck first to ensure the switch will occur.");
		player.tell("Who: " + who.toString() + " Where: " + where.toString() + " Goes to: " + goesTo);
	}
	
}
