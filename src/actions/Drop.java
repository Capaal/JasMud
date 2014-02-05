package actions;

import java.util.ArrayList;
import java.util.HashMap;

import interfaces.*;
import processes.Location;
import processes.SQLInterface;
import processes.Skill;
import processes.Skill.Syntax;
import processes.UsefulCommands;

public class Drop extends Action {
	
	private final Who who;
	private final Where where;
	private final Where finalLoc;

	public Drop(Who who, Where where, Where finalLoc) {
		this.who = who;
		this.where = where;
		this.finalLoc = finalLoc;
	}	
	
	@Override
	public boolean activate(Skill s) {
		String toDrop = (s.getStringInfo(Syntax.ITEM)).toLowerCase();
		if (toDrop.equals("")) {
			return false;
		}
		Boolean success = false;
		for (Mobile m : who.findTarget(s, where.findLoc(s))) {
			ArrayList<Holdable> inv = m.getInventory();
			Holdable item = UsefulCommands.stringToHoldable(toDrop, inv);
			if (item != null && item instanceof Holdable) { 
				m.removeItem(item);
				Container loc = finalLoc.findLoc(s).get(0);
				item.setContainer(loc);
				/// currently drops in first location for finalLoc, to ensure one location gets item, but better way is better.
				loc.acceptItem(item); 
				success = true;
			} else {
				return false;
			}
		}
		return success;
	}
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='DROP' AND ENDWHERE='" + finalLoc.toString() + "' AND BLOCKPOS=" + position
				+ " AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, ENDWHERE, TARGETWHO, TARGETWHERE) VALUES ('DROP', " 
				+ position + ", '" +  finalLoc.toString() + "', '" + who.toString() + "', '" + where.toString() + "');";
		SQLInterface.saveAction(sql);
	}
}
