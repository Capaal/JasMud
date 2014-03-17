package actions;

import java.sql.SQLException;
import java.util.HashMap;

import interfaces.*;
import processes.SQLInterface;
import processes.Skill;
import processes.Skill.Syntax;

public class Drop extends Action {
	
	private final Who who;
	private final Where where;
	private final Where finalLoc;
	
	public Drop() {
		this(Who.SELF, Where.HERE, Where.HERE);
	}

	public Drop(Who who, Where where, Where finalLoc) {
		this.who = who;
		this.where = where;
		this.finalLoc = finalLoc;
	}	
	
	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		String toDrop = s.getStringInfo(Syntax.ITEM, fullCommand);
		for (Mobile m : who.findTarget(s, fullCommand, currentPlayer, where.findLoc(s, fullCommand, currentPlayer))) {
			Holdable item = m.getHoldableFromString(toDrop);
			if (item != null) {				
				moveItem(item, item.getContainer(), m.getContainer());
				m.tell("You drop " + item.getShortDescription());
			}
		}
		return true;
	}
	
	private void moveItem(Holdable movingItem, Container startingLocation, Container finalLocation) {
		startingLocation.removeItemFromLocation(movingItem);
		finalLocation.acceptItem(movingItem);
		movingItem.setContainer(finalLocation);
	}
	
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='DROP' AND ENDWHERE='" + finalLoc.toString() + "' AND BLOCKPOS=" + position
				+ " AND TARGETWHO='" + who.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	@Override
	protected void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, ENDWHERE, TARGETWHO, TARGETWHERE) VALUES ('DROP', " 
					+ position + ", '" +  finalLoc.toString() + "', '" + who.toString() + "', '" + where.toString() + "');";
			try {
				SQLInterface.saveAction(sql);
			} catch (SQLException e) {
				System.out.println("Drop failed to save via sql : " + sql);
				e.printStackTrace();
			}
		}
	}
	@Override
	public Action newBlock(Mobile player) {
		Who newWho = who;
		Where newWhere = where;
		Where newFinal = finalLoc;
		try {
			newWho = Who.valueOf((Godcreate.askQuestion("Who do you want to drop something (this is using Syntax).", player)).toUpperCase());
			newWhere = Where.valueOf((Godcreate.askQuestion("Where must this target be? (this is using Syntax).", player)).toUpperCase());
			newFinal = Where.valueOf((Godcreate.askQuestion("Where will this item end up?", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new Drop(newWho, newWhere, newFinal);
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Takes an item from a container and puts it on the ground, in actuallity it is a lot like get.");
		player.tell("Nothing here, needs to be refactored, probably combined with get.");
	}
}
