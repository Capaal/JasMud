package actions;

import interfaces.*;

import java.sql.SQLException;
import java.util.*;

import processes.*;

//TODO Should damage contain TYPE, so that devs can specify what type that damage is, rather than relying on skill knowing?
public class Damage extends Action {
	
	
	private final int intensity;
	private final Who who;
	private final Where where;

	public Damage() {
		this(0, Who.SELF, Where.HERE);
	}
	
	public Damage(int intensity, Who who, Where where) {
		this.intensity = intensity;
		this.who = who;
		this.where = where;
	}	
	
	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		ArrayList<Container> loc = where.findLoc(s, fullCommand, currentPlayer);
		ArrayList<Mobile> target = who.findTarget(s, fullCommand, currentPlayer, loc);
		if (target.isEmpty()) {
			return false;
		}
		for (Mobile m : target) {
			m.takeDamage(s.getTypes(), intensity);
		}
		return true;
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
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='DAMAGE' AND INTVALUE=" + intensity + " AND BLOCKPOS=" + position
				+ " AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	@Override
	protected void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO BLOCK (BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) VALUES ('DAMAGE', " 
					+ position + ", " +  intensity + ", '" + who.toString() + "', '" + where.toString() + "');";
			try {
				SQLInterface.saveAction(sql);
			} catch (SQLException e) {
				System.out.println("Damage failed to save via sql : " + sql);
				e.printStackTrace();
			}
		}
	}
	@Override
	public Action newBlock(Mobile player) {
		int newIntensity = intensity;
		Who newWho = who;
		Where newWhere = where;
		try {
			newIntensity = Integer.parseInt(Godcreate.askQuestion("How much damage would you like to cause? Negative is ok.", player));
		} catch (NumberFormatException e) {
			player.tell("That value of intensity is invalid, keep it to integers. (i.e. 10)");
			return this.newBlock(player);
		}
		try {
			newWho = Who.valueOf((Godcreate.askQuestion("Who do you want to target (this is using Syntax).", player)).toUpperCase());
			newWhere = Where.valueOf((Godcreate.askQuestion("Where must this target be? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new Damage(newIntensity, newWho, newWhere);
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Affects hp in a positive or negative way.");
		player.tell("Intensity: " + intensity + " Who: " + who.toString() + " Where: " + where.toString());
	}

}
