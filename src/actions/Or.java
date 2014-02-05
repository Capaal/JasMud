package actions;

import java.util.HashMap;

import processes.SQLInterface;
import processes.Skill;
import interfaces.Action;

// Using OR at the moment makes fail messages not work right. You get a fail message but still succeed on a different action.
public class Or extends Action {
	
	private final Action actionOne;
	private final Action actionTwo;
	
	public Or(Action aOne, Action aTwo) {
		this.actionOne = aOne;
		this.actionTwo = aTwo;
	}

	@Override
	public boolean activate(Skill s) {
		if (actionOne.activate(s)) {
			return true;
		} else if (actionTwo.activate(s)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean save(int position) {	
		if (!actionOne.save(0) || !actionTwo.save(0)) {
			return false;
		}
		return super.save(position);		
	}
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='OR' AND BLOCKPOS=" + position
				+ " AND BLOCKPOINTERONE=" + actionOne.getId() + " AND BLOCKPOINTERTWO=" + actionTwo.getId() + ";";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, BLOCKPOINTERONE, BLOCKPOINTERTWO) VALUES ('OR', " 
				+ position + ", " + actionOne.getId() + ", " + actionTwo.getId() + ");";
		SQLInterface.saveAction(sql);
	}
}
