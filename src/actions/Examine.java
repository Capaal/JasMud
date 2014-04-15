package actions;

import java.sql.SQLException;
import java.util.HashMap;

import TargettingStrategies.*;
import interfaces.*;
import processes.*;

public class Examine extends Action {
	
	private final WhereTargettingStrategy where;

	public Examine() {
		this(new TargetHereWhereStrategy());
	}
	
	public Examine(WhereTargettingStrategy where) {
		this.where = where;
	}
	
	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		String toExamine = UsefulCommands.returnTarget(fullCommand);			
		for (Container c : where.findWhere(s, fullCommand, currentPlayer)) {
			for (Holdable h : c.getInventory()) {
				if (h.getName().equals(toExamine) || (h.getName() + h.getId()).equals(toExamine)) {
					currentPlayer.tell(h.getDescription());
					return true;
				}
			}
		}
		return false;	
	}	
	@Override
	public Action newBlock(Mobile player) {
		WhereTargettingFactory whereFactory = new WhereTargettingFactory();
		WhereTargettingStrategy newWhere = where;
		try {
			newWhere = whereFactory.parse((Godcreate.askQuestion("Where will they look for the item? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new Examine(newWhere);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='EXAMINE' AND BLOCKPOS=" + position
				+ " AND TARGETWHERE='" + where.toString() + "';";
		return WorldServer.databaseInterface.returnBlockView(blockQuery);
	}
	@Override
	public void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, TARGETWHERE) VALUES ('EXAMINE', " 
					+ position + ", '" + where.toString() + "');";
			WorldServer.databaseInterface.saveAction(sql);
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Examines an item in a particular spot, indicated by where.");
		player.tell("Where: " + where.toString());
	}
}
