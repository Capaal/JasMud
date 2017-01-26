package actions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import processes.Skills;
import processes.WorldServer;
import interfaces.Action;
import interfaces.Mobile;

public class Charge extends Action {
	
	private int duration;
	private Queue<Action> actionQueue;
	
	public Charge() {
		this(0, new LinkedList());
	}
	
	public Charge(int duration, Queue actionQueue) {
		if (duration < 0) {
			duration = 0;
		}
		this.duration = duration;
		this.actionQueue = actionQueue;
	}

	@Override
	public boolean activate(Skills s, String fullCommand, Mobile currentPlayer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO BLOCK (BLOCKTYPE, BLOCKPOS, INTVALUE) VALUES ('CHARGE', " 
					+ position + ", " +  duration + ");";
			WorldServer.databaseInterface.saveAction(sql);
			for (Action a : actionQueue) {
				sql = "insert ignore into blockpointertable (BLOCKID, BLOCKPOINTER) values (" + this.getId() + ", "
						+ a.getId() + ");";
				WorldServer.databaseInterface.saveAction(sql);
			}
		}		
	}

	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "select block.blockid, block.blocktype, block.intvalue, block.blockpos, blockpointertable.blockpointer"
				+ " FROM block inner join blockpointertable on block.blockid = blockpointertable.blockid"
				+ " where block.blockpos=" + position + " AND block.intvalue=" + duration + " AND block.blocktype='CHARGE';";
		return WorldServer.databaseInterface.returnBlockView(blockQuery);
	}

	@Override
	public Action newBlock(Mobile player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void explainOneself(Mobile player) {
		// TODO Auto-generated method stub
		
	}

}
