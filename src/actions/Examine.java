package actions;

import java.sql.SQLException;
import java.util.HashMap;

import interfaces.*;
import processes.*;

public class Examine extends Action {
	
	private final Where where;

	public Examine(Where where) {
		this.where = where;
	}
	
	@Override
	public boolean activate(Skill s) {
		String toExamine = UsefulCommands.returnTarget(s.getFullCommand());			
		for (Container c : where.findLoc(s)) {
			for (Holdable h : c.getInventory()) {
				if (h.getName().equals(toExamine) || (h.getName() + h.getId()).equals(toExamine)) {
					s.getCurrentPlayer().tell(h.getDescription());
					return true;
				}
			}
		}
//		s.getCurrentPlayer().tell("You search around for " + toExamine + " but find nothing to examine.");
		return false;	
	}	

	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='EXAMINE' AND BLOCKPOS=" + position
				+ " AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, TARGETWHERE) VALUES ('EXAMINE', " 
				+ position + ", '" + where.toString() + "');";
		try {
			SQLInterface.saveAction(sql);
		} catch (SQLException e) {
			System.out.println("Examine failed to save via sql : " + sql);
			e.printStackTrace();
		}
	}
}
