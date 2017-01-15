package actions;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import TargettingStrategies.*;
import interfaces.Action;
import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skill;
import processes.Skill.Syntax;

public class genericMoveItem extends Action {

	//Item location is based on player.
//	private final WhatStrategyInterface whatItem; // Not necessary? We are assume it is an item.
//	private final WhatStrategyInterface endTarget; // Not necessary? end target SHOULD be a container (i.e. a where)
	private final WhereStrategyInterface whereTarget;
	private final WhereStrategyInterface whereItem;
	
	//move item from one player to another holdable (mobiles, stditem)
	public genericMoveItem() {
		this(new WhereStrategyHere(), new WhereStrategyHere());
	}
	
	// Expects 
	public genericMoveItem(WhereStrategyInterface whereItem, WhereStrategyInterface whereTarget) {
//		this.whatItem = whatItem; // See above.
//		this.endTarget = endTarget; // See above.
		this.whereTarget = whereTarget;
		this.whereItem = whereItem;
	}
	
	@Override
	//True=player is holding the item. Action successful. False=Player does not have the item (but is state now invalid?).
	// Expects SKILL to indicate a Syntax.ITEM
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		//find item STRING to move
		String toMove = s.getStringInfo(Syntax.ITEM, fullCommand);
		//identifies current location of item
		List<Container> possibleContainers = whereItem.findWhere(s, fullCommand, currentPlayer);
		//searches for item in location
		for (Container h : possibleContainers) {
			Holdable item = h.getHoldableFromString(toMove);
			String tempItemName = item.getName().toLowerCase();
			if (tempItemName.equals(toMove) || (tempItemName + item.getId()).equals(toMove)) {
				//If item found, searches for where/what to move target to
				if (h != null) {
					//Figures out what endTarget is (mob, location, etc) and where endTarget is
		//			String endTarg = s.getStringInfo(Syntax.TARGET, fullCommand);
					List<Container> endTargLoc = whereTarget.findWhere(s, fullCommand, currentPlayer);
					for (Container a : endTargLoc) {
		//				String tempTargName = a.getName().toLowerCase();
		//				if (tempTargName.equals(endTarg) || (tempTargName + a.getId()).equals(endTarg)) {
							if (a != null) {
								moveItem(item, a); 
								currentPlayer.tell(h.getName() + " is moved."); // This should be a message block in the skill instead.
								return true;
							}
		//				}
					}

				} 
			}
		}
	//item not found or target not found, should there be a message here or elsewhere?
		currentPlayer.tell("You don't have: " + s.getStringInfo(Syntax.ITEM, fullCommand) + "."); // atm, skill can accept a "fail message" also, not true
		return false;
	}

	//change item's location, remove from prev-player-item's location, add to new-player-item's location
	private void moveItem(Holdable itemToMove, Container finalLocation) {
//		startingLocation.removeItemFromLocation(itemToMove); 
		itemToMove.getContainer().removeItemFromLocation(itemToMove);
		finalLocation.acceptItem(itemToMove); // acceptItem should do the below as well.
		itemToMove.setContainer(finalLocation);
	}

	@Override
	public void insertOneself(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public HashMap<String, Object> selectOneself(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Action newBlock(Mobile player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void explainOneself(Mobile player) {
		// TODO Auto-generated method stub

	}

}
