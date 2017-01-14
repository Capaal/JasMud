package checks;

import java.sql.SQLException;
import java.util.HashMap;

import TargettingStrategies.*;
import actions.Godcreate;
import processes.SQLInterface;
import processes.Skill;
import processes.WorldServer;
import interfaces.*;

public class BalanceCheck extends Action {
	
	private final WhatStrategyInterface what;
	private final WhereStrategyInterface where;
	
	public BalanceCheck() {
		this(new WhatStrategySelf(), new WhereStrategyHere());
	}
	
	public BalanceCheck(WhatStrategyInterface what, WhereStrategyInterface where) {
		this.what = what;
		this.where = where;
	}
	
	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		for (Holdable m : what.findWhat(s, fullCommand, currentPlayer,  where.findWhere(s, fullCommand, currentPlayer))) {
			if (m instanceof Mobile && !((Mobile)m).hasBalance()) {
				return false;
			}
		}
		return true;
	}
	@Override
	public Action newBlock(Mobile player) {
		WhatFactory whatFactory = new WhatFactory();
		WhereFactory whereFactory = new WhereFactory();
		WhatStrategyInterface newWho = what;
		WhereStrategyInterface newWhere = where;
		try {
			newWho = whatFactory.parse((Godcreate.askQuestion("Who do you want to check? (this is using Syntax).", player)).toUpperCase());
			newWhere = whereFactory.parse((Godcreate.askQuestion("Where is this target? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new BalanceCheck(newWho, newWhere);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='BALANCECHECK' AND BLOCKPOS=" + position 
				+ " AND TARGETWHO='" + what.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return WorldServer.databaseInterface.returnBlockView(blockQuery);
	}
	@Override
	public void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, TARGETWHO, TARGETWHERE) VALUES ('BALANCECHECK', " 
					+ position + ", '" + what.toString() + "', '" + where.toString() + "');";
			WorldServer.databaseInterface.saveAction(sql);
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Does a check to compare player's balance state against the desired state.");
		player.tell("Who: " + what.toString() + " Where: " + where.toString());
	}
}
