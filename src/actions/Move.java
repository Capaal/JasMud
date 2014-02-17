package actions;

import java.sql.SQLException;
import java.util.HashMap;

import processes.SQLInterface;
import processes.Skill;
import interfaces.*;

public class Move extends Action {
		
	private final Who who;
	private final Where where;
	private final Where finalLoc;
	
	public Move(Who who, Where where, Where finalLoc) {
		this.who = who;
		this.where = where;
		this.finalLoc = finalLoc;
	}
	
	// Currently gets 1 location poorly, need a better Where ENUM to handle 1 location away. Or something else.
	public boolean activate(Skill s) {	
		for (Mobile m : who.findTarget(s, where.findLoc(s))) {
			Container c = finalLoc.findLoc(s).get(0);		
			m.getContainer().removeItemFromLocation(m);
			m.setContainer(c);
			c.acceptItem(m);
			c.look(m);
		}
		return true;
	}
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='MOVE' AND BLOCKPOS=" + position
				+ " AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "' AND ENDWHERE='" + finalLoc.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, TARGETWHO, TARGETWHERE, ENDWHERE) VALUES ('MOVE', " 
				+ position + ", '" + who.toString() + "', '" + where.toString() + "', '" + finalLoc.toString() + "');";
		try {
			SQLInterface.saveAction(sql);
		} catch (SQLException e) {
			System.out.println("Move failed to save via sql : " + sql);
			e.printStackTrace();
		}
	}
}
	