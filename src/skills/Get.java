package skills;

import java.util.Arrays;

import effects.ConditionsEnum;
import interfaces.Container;
import interfaces.Holdable;
import items.StackableItem;
import processes.Location;
import processes.Skills;
import processes.UsefulCommands;

public class Get extends Skills {
	
	public Get() {
		super.name = "get";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.QUANTITY);
	}

	String possItem;
	
	// Moves a HOLDABLE from the LOCATION of the CURRENTPLAYER into their INVENTORY.
	// Requires Balance, Syntax = "get sword" or "get dagger1234" or "get gold 26"
	@Override
	protected void performSkill() {
		possItem = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (possItem.equals("")) {
			messageSelf("Get what?");
			return;
		}
		if (!hasBalance()) {return;}
		if (brokenArms()) {return;}
		Location here = currentPlayer.getContainer();
		Holdable itemToMove = here.getHoldableFromString(possItem);
		if (itemToMove == null) {
			messageSelf("You can't find that item.");
			return;
		}		
		if (itemToMove instanceof StackableItem) {	
			moveStackableItem((StackableItem)itemToMove);			
		} else {
			standardGetItem(itemToMove);
		}
	}
	
	private boolean brokenArms() {
		if (currentPlayer.hasAllConditions(ConditionsEnum.BROKENLEFTARM) && currentPlayer.hasAllConditions(ConditionsEnum.BROKENRIGHTARM)) {
			messageSelf("Your arms are broken!");
			return true;
		} 
		return false;
	}
	
	// TODO Doesn't stackable item handle most of these cases?
	private void moveStackableItem(StackableItem itemToMove) {
		String quantityToMove = Syntax.QUANTITY.getStringInfo(fullCommand, this);
		if (!quantityToMove.isEmpty()) {
			int quantity = Integer.parseInt(quantityToMove);
			if (quantity > itemToMove.getQuantity()) {
				quantity = itemToMove.getQuantity();
			}
			((StackableItem)itemToMove).moveHoldable(currentPlayer, quantity);
			messageSelf("You get " + quantity + " " + itemToMove.getName() + ".");
			messageOthers(currentPlayer.getName() + " picks up " + quantity + " " + itemToMove.getName() + ".", Arrays.asList(currentPlayer));
		} else {
			standardGetItem(itemToMove);
		}
	}
	
	private void standardGetItem(Holdable itemToMove) {
		itemToMove.moveHoldable(currentPlayer);
		messageSelf("You get " + itemToMove.getName() + ".");
		messageOthers(currentPlayer.getName() + " picks up " + itemToMove.getName() + ".", Arrays.asList(currentPlayer));
	}
}
