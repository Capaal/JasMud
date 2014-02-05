package actions;

import java.util.HashMap;

import processes.SQLInterface;
import processes.Skill;
import processes.UsefulCommands;
import interfaces.*;

public class Look extends Action {
	
	private Where where;
	
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
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='LOOK' AND BLOCKPOS=" + position
				+ " AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, TARGETWHERE) VALUES ('LOOK', " 
				+ position + ", '" + where.toString() + "');";
		SQLInterface.saveAction(sql);
	}
}
