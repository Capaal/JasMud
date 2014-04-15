package actions;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

import processes.SQLInterface;
import processes.Skill;
import processes.WorldServer;
import interfaces.Action;
import interfaces.Mobile;

public class Chance extends Action {
	
	private final int chance;
	private final Action action;
	private final Random ran = new Random();
	
	public Chance() {
		this(0, new Damage());
	}
	
	public Chance(int incChance, Action action) {
		if (action == null) {
			throw new IllegalArgumentException("The action may not be null.");
		}
		if (incChance > 100) {
			incChance = 100;
		}
		if (incChance < 0) {
			incChance = 0;
		}
		this.chance = incChance;
		this.action = action;
	}

	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		if (s == null) {
			throw new IllegalStateException("Skill is null.");
		}
		int roll = ran.nextInt(101);
		if (roll <= chance) {
			return action.activate(s, fullCommand, currentPlayer);
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
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		//HashMap<String, Object> chanceActionView = action.selectOneself(0);
		String blockQuery = "SELECT BLOCK.*, BLOCKPOINTERTABLE.BLOCKPOINTER FROM BLOCK LEFT JOIN BLOCKPOINTERTABLE"
				+ " ON BLOCK.BLOCKID = BLOCKPOINTERTABLE.BLOCKID WHERE BLOCKTYPE='CHANCE'"
				+ " AND INTVALUE=" + chance + " AND BLOCKPOS=" + position + " AND BLOCKPOINTER=" + action.getId() + ";";

	//	String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='CHANCE' AND INTVALUE=" + chance + " AND BLOCKPOS=" + position
	//			+ " AND BLOCKPOINTERONE='" + chanceActionView.get("BLOCKID") + "';";
		return WorldServer.databaseInterface.returnBlockView(blockQuery);
	}
	@Override
	public void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {			
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, INTVALUE) VALUES ('CHANCE', " 
					+ position + ", " +  chance + ");";
			WorldServer.databaseInterface.saveAction(sql);
			sql = "insert IGNORE into BLOCKPOINTERTABLE(BLOCKID, BLOCKPOINTER) VALUES "
					+ "(" + this.getId() + ", " + action.getId() + ");";
			WorldServer.databaseInterface.saveAction(sql);
		}
	}
	@Override
	public Action newBlock(Mobile player) {
		int newChance = chance;
		Action newAction = action;
		try {
			newChance = Integer.parseInt(Godcreate.askQuestion("What percent chance should this action trigger?", player));
		} catch (NumberFormatException e) {
			player.tell("That value of intensity is invalid, keep it to integers. (i.e. 10)");
			return this.newBlock(player);
		}
		Godcreate.displayActions(player);		
		newAction = Godcreate.selectAction(player);
		return new Chance(newChance, newAction);
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("A percent chance to trigger a different block.");
		player.tellLine("Chance to trigger: " + chance + " Block to trigger: ");
		action.explainOneself(player);
	}
}
