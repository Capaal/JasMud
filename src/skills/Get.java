package skills;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.Holdable;
import items.StackableItem;
import processes.ContainerErrors;
import processes.Location;
import processes.Skills;

public class Get extends Skills {
	
	private String possItem;
	private int quantity;
	private Holdable itemToMove;
	
	public Get() {
		super.name = "get";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.QUANTITY);
	}	
	
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
		itemToMove = here.getHoldableFromString(possItem);
		if (itemToMove == null) {
			messageSelf("You can't find that item.");
			return;
		}	
		getItem();
	}
	
	private void getItem() {
		String quantityToMove = Syntax.QUANTITY.getStringInfo(fullCommand, this);
		quantity = 1;
		if (!quantityToMove.isEmpty()) {
			quantity = Integer.parseInt(quantityToMove); 
			if (quantity > itemToMove.getQuantity()) {
				quantity = itemToMove.getQuantity();
			}
		}		
		ContainerErrors error = itemToMove.moveHoldable(currentPlayer, quantity);
		if (error != null) {
			messageSelf(error.display(currentPlayer.getName()));
		} else {
			messageSelf();
			messageOthers();
		}
	}
	
	private void messageSelf() {
		StringBuilder selfSB = new StringBuilder();
		selfSB.append("You get ");
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
	
	private void messageOthers() {
		StringBuilder sb = new StringBuilder();
		sb.append(currentPlayer.getName());
		sb.append(" gets ");
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
	
	private boolean brokenArms() {
		if (currentPlayer.hasAllConditions(PassiveCondition.BROKENLEFTARM) && currentPlayer.hasAllConditions(PassiveCondition.BROKENRIGHTARM)) {
			messageSelf("Your arms are broken!");
			return true;
		} 
		return false;
	}
}
