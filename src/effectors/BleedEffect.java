package effectors;

import java.sql.SQLException;
import java.util.HashMap;

import actions.Damage;
import actions.Godcreate;
import processes.SQLInterface;
import processes.Skill;
import processes.Type;
import effects.Bleed;
import interfaces.*;
import interfaces.Action.Where;
import interfaces.Action.Who;

public class BleedEffect extends Action {
	
	private final int duration;
	private final Who who;
	private final Where where;
	
	public BleedEffect() {
		this(0, Who.SELF, Where.HERE);
	}
	
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
	@Override
	public Action newBlock(Mobile player) {
		int newDuration = duration;
		Who newWho = who;
		Where newWhere = where;
		try {
			newDuration = Integer.parseInt(Godcreate.askQuestion("How long should the bleed last?", player));
		} catch (NumberFormatException e) {
			player.tell("That value of duration is invalid, keep it to integers. (i.e. 10)");
			return this.newBlock(player);
		}
		try {
			newWho = Who.valueOf((Godcreate.askQuestion("Who do you want to target (this is using Syntax).", player)).toUpperCase());
			newWhere = Where.valueOf((Godcreate.askQuestion("Where must this target be? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new BleedEffect(newDuration, newWho, newWhere);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='BLEEDEFFECT' AND BLOCKPOS=" + position + " AND INTVALUE=" + duration
				+ " AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	@Override
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) VALUES ('BLEEDEFFECT', " 
				 + position + ", " + duration + ", '" + who.toString() + "', '" + where.toString() + "');";
		try {
			SQLInterface.saveAction(sql);
		} catch (SQLException e) {
			System.out.println("BleedEffect failed to save via sql : " + sql);
			e.printStackTrace();
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Applies a bleed effect on target of a particular duration.");
		player.tell("Duration: " + duration + " Who: " + who.toString() + " Where: " + where.toString());
	}
}
