package actions;

import java.sql.SQLException;
import java.util.HashMap;

import TargettingStrategies.*;
import interfaces.*;
import processes.SQLInterface;
import processes.Skill;
import processes.Skill.Syntax;
import processes.WorldServer;

public class Drop extends Action {
	
	private final WhatTargettingStrategy what;
	private final WhereTargettingStrategy where;
	private final WhereTargettingStrategy finalLoc;
	
	public Drop() {
		this(new TargetSelfWhatStrategy(), new TargetHereWhereStrategy(), new TargetHereWhereStrategy());
	}

	public Drop(WhatTargettingStrategy what, WhereTargettingStrategy where, WhereTargettingStrategy finalLoc) {
		this.what = what;
		this.where = where;
		this.finalLoc = finalLoc;
	}	
	
	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		String toDrop = s.getStringInfo(Syntax.ITEM, fullCommand);
		for (Holdable m : what.findWhat(s, fullCommand, currentPlayer, where.findWhere(s, fullCommand, currentPlayer))) {
			if (m instanceof Mobile) {
				Holdable item = ((Mobile) m).getHoldableFromString(toDrop);
				if (item != null) {				
					moveItem(item, item.getContainer(), m.getContainer());
					((Mobile) m).tell("You drop " + item.getName());
				}
			}
		}
		return true;
	}
	
	private void moveItem(Holdable movingItem, Container startingLocation, Container finalLocation) {
		startingLocation.removeItemFromLocation(movingItem);
		finalLocation.acceptItem(movingItem);
		movingItem.setContainer(finalLocation);
	}
	
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='DROP' AND ENDWHERE='" + finalLoc.toString() + "' AND BLOCKPOS=" + position
				+ " AND TARGETWHO='" + what.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return WorldServer.databaseInterface.returnBlockView(blockQuery);
	}
	@Override
	public void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, ENDWHERE, TARGETWHO, TARGETWHERE) VALUES ('DROP', " 
					+ position + ", '" +  finalLoc.toString() + "', '" + what.toString() + "', '" + where.toString() + "');";
			WorldServer.databaseInterface.saveAction(sql);
		}
	}
	@Override
	public Action newBlock(Mobile player) {
		WhatTargettingStrategy newWhat = what;
		WhereTargettingStrategy newWhere = where;
		WhereTargettingStrategy newFinal = finalLoc;
		WhatTargettingFactory whatFactory = new WhatTargettingFactory();
		WhereTargettingFactory whereFactory = new WhereTargettingFactory();
		try {
			newWhat = whatFactory.parse((Godcreate.askQuestion("what do you want to drop something (this is using Syntax).", player)).toUpperCase());
			newWhere = whereFactory.parse((Godcreate.askQuestion("WhereTargettingStrategy must this target be? (this is using Syntax).", player)).toUpperCase());
			newFinal = whereFactory.parse((Godcreate.askQuestion("WhereTargettingStrategy will this item end up?", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new Drop(newWhat, newWhere, newFinal);
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Takes an item from a container and puts it on the ground, in actuallity it is a lot like get.");
		player.tell("Nothing here, needs to be refactored, probably combined with get.");
	}
}
