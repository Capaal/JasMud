package effectors;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import TargettingStrategies.*;
import actions.Godcreate;
import processes.SQLInterface;
import processes.Skill;
import processes.WorldServer;
import effects.Bleed;
import interfaces.*;

public class BleedEffect extends Action {
	
	private final int duration;
	private final int times;
	private final WhatTargettingStrategy what;
	private final WhereTargettingStrategy where;	
	
	public BleedEffect() {
		this(100, 0,  new TargetSelfWhatStrategy(),  new TargetHereWhereStrategy());
	}
	
	public BleedEffect(int duration, int times, WhatTargettingStrategy what, WhereTargettingStrategy where) {
		this.duration = duration;
		this.what = what;
		this.where = where;
		this.times = times;
	}

	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		List<Holdable> mobs = what.findWhat(s, fullCommand, currentPlayer,  where.findWhere(s, fullCommand, currentPlayer));
		if (mobs.isEmpty()) {
			return false;
		}
		for (Holdable m : mobs) {
			if (m instanceof Mobile) {
				((Mobile)m).addTickingEffect(new Bleed((Mobile) m), duration, times);
			}
		}
		return true;
	}
	@Override
	public Action newBlock(Mobile player) {
		WhatTargettingFactory whatFactory = new WhatTargettingFactory();
		WhereTargettingFactory whereFactory = new WhereTargettingFactory();
		int newDuration = duration;
		int newTimes = times;
		WhatTargettingStrategy newWho = what;
		WhereTargettingStrategy newWhere = where;
		try {
			newDuration = Integer.parseInt(Godcreate.askQuestion("How long should the bleed last?", player));
			newTimes = Integer.parseInt(Godcreate.askQuestion("How many times should the bleed trigger?", player));
		} catch (NumberFormatException e) {
			player.tell("That value of duration is invalid, keep it to integers. (i.e. 10)");
			return this.newBlock(player);
		}
		try {
			newWho = whatFactory.parse((Godcreate.askQuestion("Who do you want to target (this is using Syntax).", player)).toUpperCase());
			newWhere = whereFactory.parse((Godcreate.askQuestion("Where must this target be? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new BleedEffect(newDuration, newTimes, newWho, newWhere);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='BLEEDEFFECT' AND BLOCKPOS=" + position + " AND INTVALUE=" + duration
				+ " AND TARGETWHO='" + what.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return WorldServer.databaseInterface.returnBlockView(blockQuery);
	}
	@Override
	public void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) VALUES ('BLEEDEFFECT', " 
					 + position + ", " + duration + ", '" + what.toString() + "', '" + where.toString() + "');";
			WorldServer.databaseInterface.saveAction(sql);
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Applies a bleed effect on target of a particular duration.");
		player.tell("Duration: " + duration + " Who: " + what.toString() + " Where: " + where.toString());
	}
}
