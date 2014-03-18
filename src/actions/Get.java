package actions;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import TargettingStrategies.*;
import interfaces.*;
import processes.SQLInterface;
import processes.Skill;
import processes.Skill.Syntax;

public class Get extends Action {
	
	private final WhatTargettingStrategy what;
	private final WhereTargettingStrategy where;
	
	public Get() {
		this(new TargetSelfWhatStrategy(), new TargetHereWhereStrategy());
	}
	
	public Get(WhatTargettingStrategy what, WhereTargettingStrategy where) {
		this.what = what;
		this.where = where;
	}	
	
	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		String toGet = s.getStringInfo(Syntax.ITEM, fullCommand);
		List<Container> possibleContainers = where.findWhere(s, fullCommand, currentPlayer);
		for (Container c : possibleContainers) {
			Holdable item = c.getHoldableFromString(toGet);
			if (item != null) {
				moveItem(item, item.getContainer(), currentPlayer);
				currentPlayer.tell("You pick up " + item.getShortDescription() + ".");
				return true;
			} 
		}
		return false;
	}
	
	private void moveItem(Holdable movingItem, Container startingLocation, Container finalLocation) {
		startingLocation.removeItemFromLocation(movingItem);
		finalLocation.acceptItem(movingItem);
		movingItem.setContainer(finalLocation);
	}
	
	@Override
	public Action newBlock(Mobile player) {
		WhatTargettingStrategy newWhat = what;
		WhereTargettingStrategy newWhere = where;
		WhatTargettingFactory whatFactory = new WhatTargettingFactory();
		WhereTargettingFactory whereFactory = new WhereTargettingFactory();
		try {
			newWhat = whatFactory.parse((Godcreate.askQuestion("Who do you want to get the item? (this is using Syntax).", player)).toUpperCase());
			newWhere = whereFactory.parse((Godcreate.askQuestion("Where must this person be? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
		return new Get(newWhat, newWhere);
	}
	@Override
	public HashMap<String, Object> selectOneself(int position) {
		String blockQuery = "SELECT * FROM BLOCK WHERE BLOCKTYPE='GET' AND BLOCKPOS=" + position
				+ " AND TARGETWHO='" + what.toString() + "' AND TARGETWHERE='" + where.toString() + "';";
		return SQLInterface.returnBlockView(blockQuery);
	}
	@Override
	protected void insertOneself(int position) {
		if (selectOneself(position).get("BLOCKID") == null) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, TARGETWHO, TARGETWHERE) VALUES ('DAMAGE', " 
					+ position + ", '" + what.toString() + "', '" + where.toString() + "');";
			try {
				SQLInterface.saveAction(sql);
			} catch (SQLException e) {
				System.out.println("Get failed to save via sql : " + sql);
				e.printStackTrace();
			}
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Takes an item from a container and puts it into a different container.");
		player.tell("This is empty becase the skill needs to be reworked.");
	}
}
