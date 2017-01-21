package actions;


import java.util.HashMap;
import processes.Skill;
import interfaces.Action;
import interfaces.Mobile;

public class And extends Action {
	
	private final Action actionOne;
	private final Action actionTwo;
	
	public And() {
		this(new Damage(), new Damage());
	}
	
	public And(Action aOne, Action aTwo) {
		this.actionOne = aOne;
		this.actionTwo = aTwo;
	}

	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		if (actionOne.activate(s, fullCommand, currentPlayer) && actionTwo.activate(s, fullCommand, currentPlayer)) {
			return true;
		}
		return false;
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
	/*	String blockQuery = "SELECT BLOCK.BLOCKID, BLOCK.BLOCKPOS, BLOCK.BLOCKTYPE, BLOCKPOINTERTABLE.BLOCKPOINTER"
				+ " FROM BLOCK LEFT JOIN BLOCKPOINTERTABLE"
				+ " ON BLOCK.BLOCKID = BLOCKPOINTERTABLE.BLOCKID WHERE BLOCKTYPE='OR' AND BLOCKPOS=" + position + ";";		
	//	String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='OR' AND BLOCKPOS=" + position
	//			+ " AND BLOCKPOINTERONE=" + actionOne.getId() + " AND BLOCKPOINTERTWO=" + actionTwo.getId() + ";";*/
	//	return WorldServer.databaseInterface.returnBlockView(blockQuery);
		return null;
	}
	@Override
	public void insertOneself(int position) {
/*		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS) VALUES ('OR', " 
					+ position + ");";
			WorldServer.databaseInterface.saveAction(sql);
			sql = "insert ignore into blockpointertable (BLOCKID, BLOCKPOINTER) values (" + getId() + ", " + actionOne.getId() + ");";
			WorldServer.databaseInterface.saveAction(sql);
			sql = "insert ignore into blockpointertable (BLOCKID, BLOCKPOINTER) values (" + getId() + ", " + actionTwo.getId() + ");";
			WorldServer.databaseInterface.saveAction(sql);
		}*/
	}
	@Override
	public void explainOneself(Mobile player) {
/*		player.tell("Allows several blocks to be possible without breaking skill and preventing further blocks.");
		player.tellLine("Try first: ");
		actionOne.explainOneself(player);
		player.tellLine("Then try: ");
		actionTwo.explainOneself(player);*/
	}
}
