package actions;

import java.util.HashMap;
import java.util.Set;

import TargettingStrategies.*;
import interfaces.Action;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skill;
import processes.Skill.Syntax;

public class genericMoveItem extends Action {

	//Item location is based on player.
	private final WhatTargettingStrategy whatItem;
	private final WhatTargettingStrategy endTarget;
	private final WhereTargettingStrategy whereTarget;
	
	//move item from one player to another holdable (mobiles, stditem)
	public genericMoveItem() {
		this(new TargetTargetWhatStrategy(), new TargetTargetWhatStrategy(), new WhereTargettingStrategy());
	}
	
	public genericMoveItem(WhatTargettingStrategy whatItem, WhatTargettingStrategy endTarget, 
			WhereTargettingStrategy whereItemStarts, WhereTargettingStrategy whereTarget) {
		this.whatItem = whatItem;
		this.endTarget = endTarget;
		this.whereTarget = whereTarget;
	}
	

	@Override
	//True=player is holding the item. Action successful. False=Player does not have the item.
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		//find item to move
		String toMove = s.getStringInfo(Syntax.ITEM, fullCommand);
		//identifies list of items in player's inventory
		Set<Holdable> possibleHoldables = currentPlayer.getInventory();
		//searches list for the item
		for (Holdable h : possibleHoldables) {
			String tempItemName = h.getName().toLowerCase();
			if (tempItemName.equals(toMove) || (tempItemName + h.getId()).equals(toMove)) {
				//If item found, moves the item.
				if (h != null) {
					moveItem(h, h.getContainer(), endTarget);
					currentPlayer.tell(h.getName() + " is moved.");
					return true;
				} 
			}
		}
	//item not found, should there be a message here or elsewhere?
		currentPlayer.tell("You don't have: " + item.getName() + ".");
		return false;
	}

	//change item's location, remove from prev-player-item's location, add to new-player-item's location
	private void moveItem(Holdable movingItem, Container startingLocation, Container finalLocation) {
		startingLocation.removeItemFromLocation(movingItem);
		finalLocation.acceptItem(movingItem);
		movingItem.setContainer(finalLocation);
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
