package actions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import actions.Message.msgStrings;
import processes.SQLInterface;
import processes.Skill;
import interfaces.*;
import interfaces.Action.Where;
import interfaces.Action.Who;

public class Move extends Action {
		
	private final Who who;
	private final Where where;
	private final Where finalLoc;
	
	public Move() {
		this(Who.SELF, Where.HERE, Where.HERE);
	}
	
	public Move(Who who, Where where, Where finalLoc) {
		this.who = who;
		this.where = where;
		this.finalLoc = finalLoc;
	}
	
	// Currently gets 1 location poorly, need a better Where ENUM to handle 1 location away. Or something else.
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {	
		for (Mobile m : who.findTarget(s, fullCommand, currentPlayer, where.findLoc(s, fullCommand, currentPlayer))) {
			Container c = finalLoc.findLoc(s, fullCommand, currentPlayer).get(0);		
			m.getContainer().removeItemFromLocation(m);
			m.setContainer(c);
			c.acceptItem(m);
			c.look(m);
		}
		return true;
	}
	@Override
	public Action newBlock(Mobile player) {
		Who newWho = who;
		Where newWhere = where;
		Where newFinal = finalLoc;
		try {
			newWho = Who.valueOf((Godcreate.askQuestion("Who do you want to move? (this is using Syntax).", player)).toUpperCase());
			newWhere = Where.valueOf((Godcreate.askQuestion("Where is this target? (this is using Syntax).", player)).toUpperCase());
			newFinal = Where.valueOf((Godcreate.askQuestion("Where should this target end up? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new Move(newWho, newWhere, newFinal);
	}
	
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='MOVE' AND BLOCKPOS=" + position
				+ " AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "' AND ENDWHERE='" + finalLoc.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	
	protected void insertOneself(int position) {
		String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, TARGETWHO, TARGETWHERE, ENDWHERE) VALUES ('MOVE', " 
				+ position + ", '" + who.toString() + "', '" + where.toString() + "', '" + finalLoc.toString() + "');";
		try {
			SQLInterface.saveAction(sql);
		} catch (SQLException e) {
			System.out.println("Move failed to save via sql : " + sql);
			e.printStackTrace();
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Moves a mobile from one container to another container.");
		player.tell("Who: " + who.toString() + " From where: " + where.toString() + " to where: " + finalLoc.toString());
	}
}
	