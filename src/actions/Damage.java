package actions;

import interfaces.*;

import java.sql.SQLException;
import java.util.*;

import processes.*;

public class Damage extends Action {
	
	private int id;
	private final int intensity;
	private final Who who;
	private final Where where;

	public Damage(int intensity, Who who, Where where) {
		this.intensity = intensity;
		this.who = who;
		this.where = where;
	}	
	
	@Override
	public boolean activate(Skill s) {
		ArrayList<Container> loc = where.findLoc(s);
		ArrayList<Mobile> target = who.findTarget(s, loc);
		if (loc != null && target != null) {
			for (Mobile m : target) {
				m.takeDamage(s.getTypes(), intensity);
			}
			return true;			
		}
		return false;
	}
	
	/*@Override
	public boolean save(int position) {	
		HashMap<String, Object> blockView = selectOneself(position);
		if (blockView == null) {
			insertOneself(position);
			blockView = selectOneself(position);
		}
		this.id = (int) blockView.get("BLOCKID");
		return true;
	}*/
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='DAMAGE' AND INTVALUE=" + intensity + " AND BLOCKPOS=" + position
				+ " AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO BLOCK (BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) VALUES ('DAMAGE', " 
				+ position + ", " +  intensity + ", '" + who.toString() + "', '" + where.toString() + "');";
		try {
			SQLInterface.saveAction(sql);
		} catch (SQLException e) {
			System.out.println("Damage failed to save via sql : " + sql);
			e.printStackTrace();
		}
	}
	
/*	@Override
	public int getId() {
		return id;
	}*/

}
