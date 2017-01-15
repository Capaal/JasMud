//USE MOVEITEM

/*
 * package actions;
 

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import TargettingStrategies.*;
import interfaces.*;
import processes.SQLInterface;
import processes.Skill;
import processes.Skill.Syntax;
import processes.WorldServer;

public class Get extends Action {
	
	// How to get GET to work
	//  delete it
	 
	
	private final WhatStrategyInterface what;
	private final WhereStrategyInterface where;
	
	public Get() {
		this(new WhatStrategySelf(), new WhereStrategyHere());
	}
	
	public Get(WhatStrategyInterface what, WhereStrategyInterface where) {
		this.what = what;
		this.where = where;
	}	
	
	@Override
	//Checks if the location (here) has the item in question, then moves it from (here) to the player.
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		//identifies item from fullcommand
		String toGet = s.getStringInfo(Syntax.ITEM, fullCommand);
		//identifies list of items in location (here)
		List<Container> possibleContainers = where.findWhere(s, fullCommand, currentPlayer);
		//searches list for the item
		for (Container c : possibleContainers) {
			Holdable item = c.getHoldableFromString(toGet);
			//If item found, moves the item.
			if (item != null) {
				moveItem(item, item.getContainer(), currentPlayer);
				currentPlayer.tell("You pick up " + item.getName() + ".");
				return true;
			} 
		}
		//item not found, should there be a message here or elsewhere?
		return false;
	}
	
	private void moveItem(Holdable movingItem, Container startingLocation, Container finalLocation) {
		startingLocation.removeItemFromLocation(movingItem);
		finalLocation.acceptItem(movingItem);
		movingItem.setContainer(finalLocation);
	}
	
	@Override
	public Action newBlock(Mobile player) {
		WhatStrategyInterface newWhat = what;
		WhereStrategyInterface newWhere = where;
		WhatFactory whatFactory = new WhatFactory();
		WhereFactory whereFactory = new WhereFactory();
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
		return WorldServer.databaseInterface.returnBlockView(blockQuery);
	}
	@Override
	public void insertOneself(int position) {
		if (selectOneself(position).get("BLOCKID") == null) {
			String sql = "INSERT IGNORE INTO block (BLOCKTYPE, BLOCKPOS, TARGETWHO, TARGETWHERE) VALUES ('DAMAGE', " 
					+ position + ", '" + what.toString() + "', '" + where.toString() + "');";
			WorldServer.databaseInterface.saveAction(sql);
		}
	}
	@Override
	public void explainOneself(Mobile player) {
		player.tell("Takes an item from a container and puts it into a different container.");
		player.tell("This is empty becase the skill needs to be reworked.");
	}
}

*/
