package actions;

import java.util.HashMap;
import java.util.List;
import TargettingStrategies.*;
import interfaces.Action;
import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skill;

public class MoveHoldable extends Action {

	private final WhatStrategyInterface whatObject;
	private final WhereStrategyInterface whereContainer;
	private final WhereStrategyInterface whereObject;
	private final WhatStrategyInterface whatOptionalTarget;
	
	public MoveHoldable() {
		this(new WhatStrategyTarget(), new WhereStrategyHere(), new WhereStrategyHere());
	}
	
	public MoveHoldable(WhatStrategyInterface whatItem, WhereStrategyInterface whereItem, WhereStrategyInterface whereTarget) {
		this(whatItem, whereItem, whereTarget, null);
	}
	
	
	public MoveHoldable(WhatStrategyInterface whatItem, WhereStrategyInterface whereItem, WhereStrategyInterface whereTarget, WhatStrategyInterface optionalTarget) {
		this.whatObject = whatItem;
		this.whereContainer = whereTarget;
		this.whereObject = whereItem;
		this.whatOptionalTarget = optionalTarget;
	}
	
	@Override
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		List<Container> possibleContainersOfHoldable = whereObject.findWhere(s, fullCommand, currentPlayer);
		List<Holdable> possibleHoldables = whatObject.findWhat(s, fullCommand, currentPlayer, possibleContainersOfHoldable);
		List<Container> possibleEndContainers = whereContainer.findWhere(s, fullCommand, currentPlayer);
		if (possibleEndContainers.size() < 1) {
			return false;
		}
		Container finalLocation;
		if (whatOptionalTarget != null) {
			List<Holdable> finalTargets = whatOptionalTarget.findWhat(s, fullCommand, currentPlayer, possibleEndContainers);
			if (finalTargets.size() < 1) {
				return false;
			}
			finalLocation = (Container)finalTargets.get(0);
		} else {
			finalLocation = possibleEndContainers.get(0);
		}
		if (possibleHoldables.size() < 1 || possibleEndContainers.size() < 1) {
			return false;
		}
		Holdable finalHoldable = possibleHoldables.get(0);
		if (!checkLegality(finalLocation, finalHoldable)) {
			return false;
		}
		moveHoldable(finalHoldable, finalLocation);
		return true;
	}

	private boolean checkLegality(Container finalLocation, Holdable finalHoldable) {
		if (finalHoldable instanceof Mobile && finalLocation instanceof Mobile) { // Checks for Mobiles put in a mobiles inventory
			return false;
		}
		if (finalLocation == null || finalHoldable == null) {
			return false;
		}
		return true;
	}

	//change item's location, remove from prev-player-item's location, add to new-player-item's location
	private void moveHoldable(Holdable itemToMove, Container finalLocation) {
		itemToMove.getContainer().removeItemFromLocation(itemToMove);
		finalLocation.acceptItem(itemToMove); // LAZY, AT LEAST CHECK FOR 0 OR FOR > 1 AND RETURN FALSE
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
		WhereStrategyInterface newWhereTarget;
		WhereStrategyInterface newWhereItem;
		WhereFactory whereFactoryTarget = new WhereFactory();
		WhereFactory whereFactoryItem = new WhereFactory();
		try {
			newWhereTarget = whereFactoryTarget.parse((Godcreate.askQuestion("Where did the item start? (this is using Syntax).", player)).toUpperCase());
			newWhereItem = whereFactoryItem.parse((Godcreate.askQuestion("Where is the item going? (this is using Syntax).", player)).toUpperCase());
		} catch (IllegalArgumentException e) {
			player.tell("That wasn't a valid enum choice for syntax, please refer to syntax for options. (i.e. SELF, HERE)");
			return this.newBlock(player);
		}
//		return new genericMoveItem(newWhereTarget, newWhereItem);
		return null;
	}

	@Override
	public void explainOneself(Mobile player) {
		player.tell("Takes an item from a container and puts it into a different container.");
	}

}
