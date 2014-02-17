package effectors;

import java.sql.SQLException;
import java.util.HashMap;

import processes.SQLInterface;
import processes.Skill;
import processes.Type;
import effects.Defence;
import interfaces.*;

public class DefenceEffect extends Action {
	
	private final int duration;
	private final Who who;
	private final Where where;
	
	private final Type type;
	
	public DefenceEffect(int duration, Type type, Who who, Where where) {
		this.duration = duration;
		this.who = who;
		this.where = where;
		this.type = type;
	}

	@Override
	public boolean activate(Skill s) {
		for (Mobile m : who.findTarget(s,  where.findLoc(s))) {
			m.addEffect(new Defence(m, duration, type));
		}
		return true;
	}
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='DEFENCEEFFECT' AND BLOCKPOS=" + position + " AND INTVALUE=" + duration
				+ " AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) VALUES ('DEFENCEEFFECT', "
				+ position + ", " + duration + ", '" + who.toString() + "', '" + where.toString() + "');";
		try {
			SQLInterface.saveAction(sql);
		} catch (SQLException e) {
			System.out.println("DefenceEffect failed to save via sql : " + sql);
			e.printStackTrace();
		}
	}
}
