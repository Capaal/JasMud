package actions;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

import processes.SQLInterface;
import processes.Skill;
import interfaces.Action;

public class Chance extends Action {
	
	private final int chance;
	private final Action action;
	
	public Chance(int chance, Action action) {
		this.chance = chance;
		this.action = action;
	}

	@Override
	public boolean activate(Skill s) {
		Random ran = new Random();
		int roll = ran.nextInt(101);
		if (roll <= chance) {
			return action.activate(s);
		}
		return true;
	}
	
	@Override
	public boolean save(int position) {
		if (!action.save(0) || !super.save(position)) {
			return false;
		}
		return true;
	}
	
	public HashMap<String, Object> selectOneself(int position) {
		HashMap<String, Object> chanceActionView = action.selectOneself(0);
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='CHANCE' AND INTVALUE=" + chance + " AND BLOCKPOS=" + position
				+ " AND BLOCKPOINTERONE='" + chanceActionView.get("BLOCKID") + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, INTVALUE, BLOCKPOINTERONE) VALUES ('CHANCE', " 
				+ position + ", " +  chance + ", " + action.getId() + ");";
		try {
			SQLInterface.saveAction(sql);
		} catch (SQLException e) {
			System.out.println("Chance failed to save via sql : " + sql);
			e.printStackTrace();
		}
	}
}
