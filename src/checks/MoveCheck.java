package checks;

import java.util.HashMap;

import processes.*;
import processes.Location.GroundType;
import interfaces.*;

public class MoveCheck extends Action {
	
	private final Who who;
	private final Where where;
	private final Where finalLoc;
	private final GroundType moveType;
		
	public MoveCheck(GroundType moveType, Who who, Where where, Where finalLoc) {
		this.who = who;
		this.where = where;
		this.finalLoc = finalLoc;
		this.moveType = moveType;
	}

	@Override
	public boolean activate(Skill s) {
		for (Mobile m : who.findTarget(s,  where.findLoc(s))) {
			for (Container c : finalLoc.findLoc(s)) {
				if (c == null) { 
					return false;
				}
				if ((m.getContainer().getGroundType() != moveType && c.getGroundType() != moveType)) {
					return false;
				}
				if (moveType == GroundType.GROUND && m.getContainer().getGroundType() != c.getGroundType()) {
					return false;
				}
			}
		}		
		return true;
	}
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='MOVECHECK' AND BLOCKPOS=" + position + " AND GROUNDTYPE='" + moveType.toString()
				+ "' AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "' AND ENDWHERE='" + finalLoc.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, GROUNDTYPE, TARGETWHO, TARGETWHERE, ENDWHERE) VALUES ('MOVECHECK', " 
				+ position + ", '" + moveType.toString() + "', '" + who.toString() + "', '" + where.toString() + "', '" + finalLoc.toString() + "');";
		SQLInterface.saveAction(sql);
	}
}