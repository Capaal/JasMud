package effectors;

import java.sql.SQLException;
import java.util.HashMap;

import TargettingStrategies.*;
import actions.Godcreate;
import processes.SQLInterface;
import processes.Skill;
import processes.WorldServer;
import effects.Balance;
import interfaces.*;

public class BalanceEffect extends Action {
	
	private final WhatTargettingStrategy what;
	private final WhereTargettingStrategy where;
	private final int duration;
	
	public BalanceEffect() {
		this(0, new TargetSelfWhatStrategy(), new TargetHereWhereStrategy());
	}
	
	public BalanceEffect(int duration, WhatTargettingStrategy what, WhereTargettingStrategy where) {
		this.what = what;
		this.where = where;
		this.duration = duration;
	}

	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		for (Holdable m : what.findWhat(s, fullCommand, currentPlayer, where.findWhere(s, fullCommand, currentPlayer))) {
			if (m instanceof Mobile) {
				((Mobile) m).addEffect(new Balance(), duration);
			}
		}
		return true;
	}
	@Override
	public Action newBlock(Mobile player) {
		WhatTargettingFactory whatFactory = new WhatTargettingFactory();
		WhereTargettingFactory whereFactory = new WhereTargettingFactory();
		int newDuration = duration;
		WhatTargettingStrategy newWho = what;
		WhereTargettingStrategy newWhere = where;
		try {
			newDuration = Integer.parseInt(Godcreate.askQuestion("How long should they be put off balance? In milliseconds", player));
		} catch (NumberFormatException e) {
			player.tell("That was not in the format of a number, like 1000");
			return this.newBlock(player);
		}
		try {
			newWho = whatFactory.parse((Godcreate.askQuestion("Who's balance is being changed? (this is using Syntax).", player)).toUpperCase());
			newWhere = whereFactory.parse((Godcreate.askQuestion("Where is this target? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new BalanceEffect(newDuration, newWho, newWhere);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='BALANCECOST' AND BLOCKPOS=" + position + " AND INTVALUE='" + duration
				+ "' AND TARGETWHO='" + what.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return WorldServer.databaseInterface.returnBlockView(blockQuery);
	}
	@Override
	public void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT INTO block (BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) VALUES ('BALANCECOST', " 
					+ position + ", '" + duration + "', '" + what.toString() + "', '" + where.toString() + "');";
			WorldServer.databaseInterface.saveAction(sql);
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Gives the unbalanced effect. DOES run a balancecheck first to ensure the switch will occur.");
		player.tell("Who: " + what.toString() + " Where: " + where.toString() + " Duration: " + duration);
	}
	
}
