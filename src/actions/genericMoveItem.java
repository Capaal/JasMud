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
	private final WhereStrategyInterface whereTarget;
	private final WhereStrategyInterface whereItem;
	
	//move item from one player to another holdable (mobiles, stditem)
	public genericMoveItem() {
		this(new WhereStrategyHere(), new WhereStrategyHere());
	}
	
	// Expects where/where
	public genericMoveItem(WhereStrategyInterface whereItem, WhereStrategyInterface whereTarget) {
		this.whereTarget = whereTarget;
		this.whereItem = whereItem;
	}
	
	@Override
	//True=player is holding the item. Action successful. False=Player does not have the item (but is state now invalid?).
	// Expects SKILL to indicate a Syntax.ITEM
	// Need to handle TARGET and DIRECTION, both option. Throw dagger north, throw dagger at player north, throw dagger at player (here)
	public boolean activate(Skill s, String fullCommand, Mobile currentPlayer) {
		//find item STRING to move
		String toMove = s.getStringInfo(Syntax.ITEM, fullCommand);
		//identifies current location of item
		List<Container> possibleContainers = whereItem.findWhere(s, fullCommand, currentPlayer);
		//searches for item in location
		for (Container h : possibleContainers) {
			Holdable item = h.getHoldableFromString(toMove);
			String tempItemName = ""; //why not use toMove?
			tempItemName = item.getName().toLowerCase();
			if (tempItemName.equals(toMove) || (tempItemName + item.getId()).equals(toMove)) {
				//If item found, searches for where/what to move target to
				if (h != null) {
					//Figures out what endTarget is (mob, location, etc) and where endTarget is
					List<Container> endTargLoc = whereTarget.findWhere(s, fullCommand, currentPlayer);
					for (Container a : endTargLoc) {
						if (a != null) {
							String locName = a.getName();
							String playerLoc = currentPlayer.getName();
							if (toMove.equalsIgnoreCase(currentPlayer.getName()) && locName.equalsIgnoreCase(playerLoc)) {
								currentPlayer.tell("You pick yourself up off the ground and keep going.");
								currentPlayer.tell("(You can't pick yourself up.)");
							} else {						
								moveItem(item, a); 
								currentPlayer.tell(h.getName() + " is moved."); // This should be a message block in the skill instead.
								return true;
							}
						}
					}

				} 
			}
		}
	//item not found or target not found, should there be a message here or elsewhere?
		currentPlayer.tell("Can't move: " + s.getStringInfo(Syntax.ITEM, fullCommand) + "."); // atm, skill can accept a "fail message" also, not true
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
		return new genericMoveItem(newWhereTarget, newWhereItem);
	}

	@Override
	public void explainOneself(Mobile player) {
		player.tell("Takes an item from a container and puts it into a different container.");
	}

}
