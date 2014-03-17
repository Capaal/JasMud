package actions;

import java.sql.SQLException;
import java.util.HashMap;
import processes.SQLInterface;
import processes.Skill;
import interfaces.Action;
import interfaces.Mobile;

// Using OR at the moment makes fail messages not work right. You get a fail message but still succeed on a different action.
// The above comment still the case?
public class Or extends Action {
	
	private final Action actionOne;
	private final Action actionTwo;
	
	public Or() {
		this(new Damage(), new Damage());
	}
	
	public Or(Action aOne, Action aTwo) {
		this.actionOne = aOne;
		this.actionTwo = aTwo;
	}

	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		if (actionOne.activate(s, fullCommand, currentPlayer)) {
			return true;
		} else if (actionTwo.activate(s, fullCommand, currentPlayer)) {
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
	@Override
	public Action newBlock(Mobile player) {
		Godcreate.displayActions(player);		
		Action newActionOne = Godcreate.selectAction(player);
		Action newActionTwo = Godcreate.selectAction(player);
		return new Or(newActionOne, newActionTwo);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='OR' AND BLOCKPOS=" + position
				+ " AND BLOCKPOINTERONE=" + actionOne.getId() + " AND BLOCKPOINTERTWO=" + actionTwo.getId() + ";";
		return SQLInterface.returnBlockView(blockQuery);
	}
	@Override
	protected void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, BLOCKPOINTERONE, BLOCKPOINTERTWO) VALUES ('OR', " 
					+ position + ", " + actionOne.getId() + ", " + actionTwo.getId() + ");";
			try {
				SQLInterface.saveAction(sql);
			} catch (SQLException e) {
				System.out.println("Or failed to save via sql : " + sql);
				e.printStackTrace();
			}
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Allows several blocks to be possible without breaking skill and preventing further blocks.");
		player.tellLine("Try first: ");
		actionOne.explainOneself(player);
		player.tellLine("Then try: ");
		actionTwo.explainOneself(player);
	}
}
