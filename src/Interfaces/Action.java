package interfaces;

import java.util.HashMap;

import processes.Skill;

public abstract class Action {
	
	protected int id;

	public abstract boolean activate(Skill s, String fullCommand, Mobile currentPlayer);
	
	public boolean save(int position) {	
		HashMap<String, Object> blockView = selectOneself(position);
		if (blockView.isEmpty()) {
			insertOneself(position);
			blockView = selectOneself(position);
		}
		if (blockView.isEmpty()) {
			return false;			
		} else {
			this.id = (int) blockView.get("BLOCKID");
		}
		return true;
	}
	
	protected abstract void insertOneself(int position);
	public abstract HashMap<String, Object> selectOneself(int position);
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public abstract Action newBlock(Mobile player);
	
	public abstract void explainOneself(Mobile player);
	
	
}
