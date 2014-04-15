package actions;


import java.util.HashMap;
import processes.Skill;
import processes.WorldServer;
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
		String blockQuery = "SELECT BLOCK.BLOCKID, BLOCK.BLOCKPOS, BLOCK.BLOCKTYPE, BLOCKPOINTERTABLE.BLOCKPOINTER"
				+ " FROM BLOCK LEFT JOIN BLOCKPOINTERTABLE"
				+ " ON BLOCK.BLOCKID = BLOCKPOINTERTABLE.BLOCKID WHERE BLOCKTYPE='OR' AND BLOCKPOS=" + position + ";";		
	//	String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='OR' AND BLOCKPOS=" + position
	//			+ " AND BLOCKPOINTERONE=" + actionOne.getId() + " AND BLOCKPOINTERTWO=" + actionTwo.getId() + ";";
		return WorldServer.databaseInterface.returnBlockView(blockQuery);
	}
	@Override
	public void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS) VALUES ('OR', " 
					+ position + ");";
			WorldServer.databaseInterface.saveAction(sql);
			sql = "insert ignore into blockpointertable (BLOCKID, BLOCKPOINTER) values (" + getId() + ", " + actionOne.getId() + ");";
			WorldServer.databaseInterface.saveAction(sql);
			sql = "insert ignore into blockpointertable (BLOCKID, BLOCKPOINTER) values (" + getId() + ", " + actionTwo.getId() + ");";
			WorldServer.databaseInterface.saveAction(sql);
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
