package actions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import TargettingStrategies.*;
import processes.SQLInterface;
import processes.Skills;
import processes.Skills.Syntax;
import processes.WorldServer;
import interfaces.*;

public class Look extends Action {
	
	private WhereStrategyInterface where;
	
	public Look() {
		this(new WhereStrategyHere());
	}
	
	public Look(WhereStrategyInterface where) {
		this.where = where;
	}

	@Override
	public boolean activate(Skills s, String fullCommand, Mobile currentPlayer) {
		String dir = s.getStringInfo(Syntax.DIRECTION, fullCommand);		
		if (!dir.equals("")) {
			List<Container> locs = where.findWhere(s, fullCommand, currentPlayer);
			if (locs.isEmpty()) {
				return false;
			}
			for (Container c : locs) {
				if (c != null) {
					c.look(currentPlayer);
				}
			}
		} else {
			for (Container c : new WhereStrategyHere().findWhere(s, fullCommand, currentPlayer)) {
				c.look(currentPlayer);
			}
		}
		return true;
	}
	@Override
	public Action newBlock(Mobile player) {
		WhereFactory whereFactory = new WhereFactory();
		WhereStrategyInterface newWhere = where;
		try {
			newWhere = whereFactory.parse((Godcreate.askQuestion("Which locations will be looked at? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new Look(newWhere);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='LOOK' AND BLOCKPOS=" + position
				+ " AND TARGETWHERE='" + where.toString() + "';";
		return WorldServer.databaseInterface.returnBlockView(blockQuery);
	}
	@Override
	public void insertOneself(int position) {
		if (selectOneself(position).isEmpty()) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, TARGETWHERE) VALUES ('LOOK', " 
					+ position + ", '" + where.toString() + "');";
			WorldServer.databaseInterface.saveAction(sql);
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Displays the information of a location, typically used for locations, needs refactoring for bags etc.");
		player.tell("Where: " + where.toString());
	}
}
