package checks;

import java.sql.SQLException;
import java.util.HashMap;

import actions.Damage;
import actions.Godcreate;
import processes.*;
import processes.Location.GroundType;
import interfaces.*;
import interfaces.Action.Where;
import interfaces.Action.Who;

public class MoveCheck extends Action {
	
	private final Who who;
	private final Where where;
	private final Where finalLoc;
	private final GroundType moveType;
	
	public MoveCheck() {
		this(GroundType.GROUND, Who.SELF, Where.HERE, Where.HERE);
	}
		
	public MoveCheck(GroundType moveType, Who who, Where where, Where finalLoc) {
		this.who = who;
		this.where = where;
		this.finalLoc = finalLoc;
		this.moveType = moveType;
	}

	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		for (Mobile m : who.findTarget(s, fullCommand, currentPlayer,  where.findLoc(s, fullCommand, currentPlayer))) {
			for (Container c : finalLoc.findLoc(s, fullCommand, currentPlayer)) {
				if (c == null) { 
					return false;
				}
				if ((m.getContainer().getGroundType() != moveType && c.getGroundType() != moveType)) {
					return false;
				}
				if (moveType == GroundType.GROUND && m.getContainer().getGroundType() != c.getGroundType()) {
					return false;
				}
			}
		}		
		return true;
	}
	@Override
	public Action newBlock(Mobile player) {
		GroundType newGround = moveType;
		Who newWho = who;
		Where newWhere = where;
		Where newFinal = finalLoc;
		try {
			newGround = GroundType.valueOf(Godcreate.askQuestion("", player).toUpperCase());
			newWho = Who.valueOf((Godcreate.askQuestion("Who do you want to check if they can move? (this is using Syntax).", player)).toUpperCase());
			newWhere = Where.valueOf((Godcreate.askQuestion("Where is this target now? (this is using Syntax).", player)).toUpperCase());
			newFinal = Where.valueOf((Godcreate.askQuestion("Where will they end up? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE, GROUND)");
			return this.newBlock(player);
		}
		return new MoveCheck(newGround, newWho, newWhere, newFinal);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='MOVECHECK' AND BLOCKPOS=" + position + " AND GROUNDTYPE='" + moveType.toString()
				+ "' AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "' AND ENDWHERE='" + finalLoc.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	@Override
	protected void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, GROUNDTYPE, TARGETWHO, TARGETWHERE, ENDWHERE) VALUES ('MOVECHECK', " 
					+ position + ", '" + moveType.toString() + "', '" + who.toString() + "', '" + where.toString() + "', '" + finalLoc.toString() + "');";
			try {
				SQLInterface.saveAction(sql);
			} catch (SQLException e) {
				System.out.println("Movecheck failed to save via sql : " + sql);
				e.printStackTrace();
			}
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Does a check to ensure that a standard move by a player is legal.");
		player.tell("Who: " + who.toString() + " Where from: " + where.toString() + " Where to: " + finalLoc.toString() 
			+ " Move type: " + moveType.toString());
	}
}