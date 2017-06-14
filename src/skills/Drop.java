package skills;

import java.util.Arrays;

import interfaces.Holdable;
import processes.Skills;

public class Drop extends Skills {
	
	int quantity;
	Holdable itemToMove;
	
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
		if (!hasBalance()) {return;}
		itemToMove = currentPlayer.getHoldableFromString(Syntax.ITEM.getStringInfo(fullCommand, this));
		if (itemToMove == null) {
			messageSelf("You can't find that item.");
			return;
		}	
		dropItem(itemToMove);
	}	
	
	private void dropItem(Holdable itemToDrop) {
		String quantityToMove = Syntax.QUANTITY.getStringInfo(fullCommand, this);
		quantity = 1;
		if (!quantityToMove.isEmpty()) {
			quantity = Integer.parseInt(quantityToMove);
			if (quantity > itemToDrop.getQuantity()) {
				quantity = itemToDrop.getQuantity();
			}
		}
		itemToDrop.moveHoldable(currentPlayer.getContainer(), quantity);
		displayMessages();		
	}
	
	private void displayMessages() {	
		displaySelf();
		displayOthers();
	
	}
	
	private void displaySelf() {
		StringBuilder selfSB = new StringBuilder();
		selfSB.append("You drop ");
		if (quantity == 1) {
			selfSB.append("a ");
			selfSB.append(itemToMove.getName());
		} else {
			selfSB.append(quantity);
			selfSB.append(" ");
			selfSB.append(itemToMove.getName());
			selfSB.append("s");
		}		
		selfSB.append(".");	
		messageSelf(selfSB.toString());
	}
	
	private void displayOthers() {
		StringBuilder sb = new StringBuilder();
		sb.append(currentPlayer.getName());
		sb.append(" drops ");
		if (quantity == 1) {
			sb.append("a ");
			sb.append(itemToMove.getName());
		} else {
			sb.append(quantity);
			sb.append(" ");
			sb.append(itemToMove.getName());
			sb.append("s");
		}
		sb.append(".");	
		messageOthers(sb.toString(), Arrays.asList(currentPlayer));	
	}
}
