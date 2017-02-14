package skills;

import java.util.Arrays;

import interfaces.Holdable;
import items.StackableItem;
import processes.Skills;

public class Drop extends Skills {
	
	public Drop() {
		super.name = "drop";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.QUANTITY);
	}
	
	// Moves a HOLDABLE from the CURRENTPLAYER's INVENTORY to the LOCATION they are in.
	// Requires balance, syntax = "drop dagger" or "drop sword1532"
	@Override
	protected void performSkill() {
		if (!hasBalance()) {
			return;
		}
		Holdable itemToMove = currentPlayer.getHoldableFromString(Syntax.ITEM.getStringInfo(fullCommand, this));
		if (itemToMove == null) {
			messageSelf("You can't find that item.");
			return;
		}		
		if (itemToMove instanceof StackableItem) {	
			moveStackableItem((StackableItem)itemToMove);			
		} else {
			standardDropItem(itemToMove);
		}
	}
	
	//TODO does stackable not handle some of these cases?
	private void moveStackableItem(StackableItem itemToMove) {
		String quantityToMove = Syntax.QUANTITY.getStringInfo(fullCommand, this);
		if (!quantityToMove.isEmpty()) {
			int quantity = Integer.parseInt(quantityToMove);
			if (quantity > itemToMove.getQuantity()) {
				quantity = itemToMove.getQuantity();
			}
			((StackableItem)itemToMove).moveHoldable(currentPlayer.getContainer(), quantity);
			messageSelf("You drop " + quantity + " " + itemToMove.getName() + ".");
			messageOthers(currentPlayer.getName() + " drops " + quantity + " " + itemToMove.getName() + ".", Arrays.asList(currentPlayer));
		} else {
			standardDropItem(itemToMove);
		}
	}
	
	private void standardDropItem(Holdable itemToMove) {
		itemToMove.moveHoldable(currentPlayer.getContainer());
		messageSelf("You drop " + itemToMove.getName() + ".");
		messageOthers(currentPlayer.getName() + " drops " + itemToMove.getName() + ".", Arrays.asList(currentPlayer));
	}
}
