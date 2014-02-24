package actions;

import java.sql.SQLException;
import java.util.HashMap;

import processes.SQLInterface;
import processes.Skill;
import processes.UsefulCommands;
import interfaces.*;
import interfaces.Action.Where;
import interfaces.Action.Who;

public class Look extends Action {
	
	private Where where;
	
	public Look() {
		this(Where.HERE);
	}
	
	public Look(Where where) {
		this.where = where;
	}

	@Override
	public boolean activate(Skill s) {
		String dir = UsefulCommands.getSecondWord(s.getFullCommand());
		if (dir != null) {
			for (Container c : where.findLoc(s)) {
				c.look(s.getCurrentPlayer());
			}
		} else {
			for (Container c : Where.HERE.findLoc(s)) {
				c.look(s.getCurrentPlayer());
			}
		}
		return true;
	}
	@Override
	public Action newBlock(Mobile player) {
		Where newWhere = where;
		try {
			newWhere = Where.valueOf((Godcreate.askQuestion("Which locations will be looked at? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new Look(newWhere);
	}
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='LOOK' AND BLOCKPOS=" + position
				+ " AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, TARGETWHERE) VALUES ('LOOK', " 
				+ position + ", '" + where.toString() + "');";
		try {
			SQLInterface.saveAction(sql);
		} catch (SQLException e) {
			System.out.println("Look failed to save via sql : " + sql);
			e.printStackTrace();
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Displays the information of a location, typically used for locations, needs refactoring for bags etc.");
		player.tell("Where: " + where.toString());
	}
}
