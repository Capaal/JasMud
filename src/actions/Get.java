package actions;

import java.util.ArrayList;
import java.util.HashMap;

import interfaces.*;
import processes.SQLInterface;
import processes.Skill;
import processes.Skill.Syntax;
import processes.UsefulCommands;

public class Get extends Action {
	
	private final Who who;
	private final Where where;
	
	public Get(Who who, Where where) {
		this.who = who;
		this.where = where;
	}	
	
	// need to write a transfer ownership method, so that I don't keep forgetting steps.
	// Forcing 1 locations right now, need a better way to ensure no duplications of item.
	@Override
	public boolean activate(Skill s) {
		String toGet = s.getStringInfo(Syntax.ITEM);
		for (Container c : where.findLoc(s)) {
			Holdable item = c.getHoldableFromString(toGet);
			if (item != null) { 
				c.removeItemFromLocation(item);
				Mobile m = who.findTarget(s, where.findLoc(s)).get(0);
				m.acceptItem(item);
				item.setContainer(m);
				m.tell("You pick up a " + item.getShortDescription() + ".");
				return true;
			} 
		}
		return false;
	}
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='GET' AND BLOCKPOS=" + position
				+ " AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, TARGETWHO, TARGETWHERE) VALUES ('DAMAGE', " 
				+ position + ", '" + who.toString() + "', '" + where.toString() + "');";
		SQLInterface.saveAction(sql);
	}
}
