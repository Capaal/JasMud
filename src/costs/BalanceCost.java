package costs;

import java.sql.SQLException;
import java.util.HashMap;

import actions.Godcreate;
import processes.SQLInterface;
import processes.Skill;
import checks.BalanceCheck;
import effects.Balance;
import interfaces.*;

public class BalanceCost extends Action {
	
	private final Who who;
	private final Where where;
	private final int duration;
	
	public BalanceCost() {
		this(0, Who.SELF, Where.HERE);
	}
	
	public BalanceCost(int duration, Who who, Where where) {
		this.who = who;
		this.where = where;
		this.duration = duration;
	}

	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		for (Mobile m : who.findTarget(s, fullCommand, currentPlayer, where.findLoc(s, fullCommand, currentPlayer))) {
			m.addEffect(new Balance(), duration);
		}
		return true;
	}
	@Override
	public Action newBlock(Mobile player) {
		int newDuration = duration;
		Who newWho = who;
		Where newWhere = where;
		try {
			newDuration = Integer.parseInt(Godcreate.askQuestion("How long should they be put off balance? In milliseconds", player));
		} catch (NumberFormatException e) {
			player.tell("That was not in the format of a number, like 1000");
			return this.newBlock(player);
		}
		try {
			newWho = Who.valueOf((Godcreate.askQuestion("Who's balance is being changed? (this is using Syntax).", player)).toUpperCase());
			newWhere = Where.valueOf((Godcreate.askQuestion("Where is this target? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new BalanceCost(newDuration, newWho, newWhere);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='BALANCECOST' AND BLOCKPOS=" + position + " AND INTVALUE='" + duration
				+ "' AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	@Override
	protected void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT INTO block (BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) VALUES ('BALANCECOST', " 
					+ position + ", '" + duration + "', '" + who.toString() + "', '" + where.toString() + "');";
			try {
				SQLInterface.saveAction(sql);
			} catch (SQLException e) {
				System.out.println("BalanceCost failed to save via sql : " + sql);
				e.printStackTrace();
			}
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Gives the unbalanced effect. DOES run a balancecheck first to ensure the switch will occur.");
		player.tell("Who: " + who.toString() + " Where: " + where.toString() + " Duration: " + duration);
	}
	
}
