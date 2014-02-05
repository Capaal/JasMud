package effectors;

import java.util.HashMap;

import processes.SQLInterface;
import processes.Skill;
import processes.Type;
import effects.Bleed;
import interfaces.*;

public class BleedEffect extends Action {
	
	private final int duration;
	private final Who who;
	private final Where where;
	
	public BleedEffect(int duration, Who who, Where where) {
		this.duration = duration;
		this.who = who;
		this.where = where;
	}

	@Override
	public boolean activate(Skill s) {
		for (Mobile m : who.findTarget(s,  where.findLoc(s))) {
			m.addEffect(new Bleed(m, duration));
		}
		return true;
	}
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='BLEEDEFFECT' AND BLOCKPOS=" + position + " AND INTVALUE=" + duration
				+ " AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) VALUES ('BLEEDEFFECT', " 
				 + position + ", " + duration + ", '" + who.toString() + "', '" + where.toString() + "');";
		SQLInterface.saveAction(sql);
	}
}
