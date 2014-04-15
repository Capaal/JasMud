package actions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import TargettingStrategies.*;
import processes.SQLInterface;
import processes.Skill;
import processes.WorldServer;
import interfaces.*;

public class Move extends Action {
		
	private final WhatTargettingStrategy what;
	private final WhereTargettingStrategy where;
	private final WhereTargettingStrategy finalLoc;
	
	public Move() {
		this(new TargetSelfWhatStrategy(), new TargetHereWhereStrategy(), new TargetHereWhereStrategy());
	}
	
	public Move(WhatTargettingStrategy what, WhereTargettingStrategy where, WhereTargettingStrategy finalLoc) {
		this.what = what;
		this.where = where;
		this.finalLoc = finalLoc;
	}
	
	// Currently gets 1 location poorly, need a better Where ENUM to handle 1 location away. Or something else.
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {	
		for (Holdable m : what.findWhat(s, fullCommand, currentPlayer, where.findWhere(s, fullCommand, currentPlayer))) {
			List<Container> locs = finalLoc.findWhere(s, fullCommand, currentPlayer);
			if (locs.isEmpty()) {
				return false;
			}
			Container c = locs.get(0);		
			m.getContainer().removeItemFromLocation(m);
			m.setContainer(c);
			c.acceptItem(m);
			if (m instanceof Mobile) {
				c.look((Mobile) m);
			}
		}
		return true;
	}
	@Override
	public Action newBlock(Mobile player) {
		WhatTargettingFactory whatFactory = new WhatTargettingFactory();
		WhereTargettingFactory whereFactory = new WhereTargettingFactory();
		WhatTargettingStrategy newWho = what;
		WhereTargettingStrategy newWhere = where;
		WhereTargettingStrategy newFinal = finalLoc;
		try {
			newWho = whatFactory.parse((Godcreate.askQuestion("Who do you want to move? (this is using Syntax).", player)).toUpperCase());
			newWhere = whereFactory.parse((Godcreate.askQuestion("Where is this target? (this is using Syntax).", player)).toUpperCase());
			newFinal = whereFactory.parse((Godcreate.askQuestion("Where should this target end up? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new Move(newWho, newWhere, newFinal);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='MOVE' AND BLOCKPOS=" + position
				+ " AND TARGETWHO='" + what.toString() + "' AND TARGETWHERE='" + where.toString() + "' AND ENDWHERE='" + finalLoc.toString() + "';";
		return WorldServer.databaseInterface.returnBlockView(blockQuery);
	}
	@Override
	public void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, TARGETWHO, TARGETWHERE, ENDWHERE) VALUES ('MOVE', " 
					+ position + ", '" + what.toString() + "', '" + where.toString() + "', '" + finalLoc.toString() + "');";
			WorldServer.databaseInterface.saveAction(sql);
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Moves a mobile from one container to another container.");
		player.tell("Who: " + what.toString() + " From where: " + where.toString() + " to where: " + finalLoc.toString());
	}
}
	