package actions;

import java.sql.SQLException;
import java.util.HashMap;
import interfaces.*;
import processes.SQLInterface;
import processes.Skill;
import processes.Skill.Syntax;

public class Get extends Action {
	
	private final Who who;
	private final Where where;
	
	public Get() {
		this(Who.SELF, Where.HERE);
	}
	
	public Get(Who who, Where where) {
		this.who = who;
		this.where = where;
	}	
	
	// need to write a transfer ownership method, so that I don't keep forgetting steps.
	// Forcing 1 locations right now, need a better way to ensure no duplications of item.
	// TODO right now it isn't versatile enough, needs to move from any container to any other container.
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
	@Override
	public Action newBlock(Mobile player) {
		Who newWho = who;
		Where newWhere = where;
		try {
			newWho = Who.valueOf((Godcreate.askQuestion("Who do you want to get the item? (this is using Syntax).", player)).toUpperCase());
			newWhere = Where.valueOf((Godcreate.askQuestion("Where must this person be? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new Get(newWho, newWhere);
	}
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='GET' AND BLOCKPOS=" + position
				+ " AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, TARGETWHO, TARGETWHERE) VALUES ('DAMAGE', " 
				+ position + ", '" + who.toString() + "', '" + where.toString() + "');";
		try {
			SQLInterface.saveAction(sql);
		} catch (SQLException e) {
			System.out.println("Get failed to save via sql : " + sql);
			e.printStackTrace();
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Takes an item from a container and puts it into a different container.");
		player.tell("This is empty becase the skill needs to be reworked.");
	}
}
