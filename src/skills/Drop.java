package skills;

import java.util.Arrays;

import interfaces.Holdable;
import interfaces.Mobile;
import processes.ContainerErrors;
import processes.Equipment.EquipmentEnum;
import processes.Skills;

public class Drop extends Skills {
	
	private int quantity;
	private Holdable itemToMove;
	
	public Drop(Mobile currentPlayer, String fullCommand) {
		super("drop", "Dropping Items.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.QUANTITY);
	}
	
	// Moves a HOLDABLE from the CURRENTPLAYER's INVENTORY to the LOCATION they are in.
	// Requires balance, syntax = "drop dagger" or "drop sword1532"
	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			EquipmentEnum slot = null;
			itemToMove = currentPlayer.getHoldableFromString(Syntax.ITEM.getStringInfo(fullCommand, this));
			if (itemToMove == null) {
				itemToMove = currentPlayer.getEquipmentInSlot(EquipmentEnum.RIGHTHAND);
				slot = EquipmentEnum.RIGHTHAND;
				if (itemToMove == null) {
					itemToMove = currentPlayer.getEquipmentInSlot(EquipmentEnum.LEFTHAND);
					slot = EquipmentEnum.LEFTHAND;
					if (itemToMove == null) {
						messageSelf("You can't find that item.");
						return;	
					}
				}
			}	
			dropItem(itemToMove, slot);
		}
	}	
	
	private void dropItem(Holdable itemToDrop, EquipmentEnum slot) {
		String quantityToMove = Syntax.QUANTITY.getStringInfo(fullCommand, this);
		quantity = 1;
		if (!quantityToMove.isEmpty()) {
			quantity = Integer.parseInt(quantityToMove);
			if (quantity > itemToDrop.getQuantity()) {
				quantity = itemToDrop.getQuantity();
			}
		}
		if (slot == null) {
			ContainerErrors err = itemToDrop.moveHoldable(currentPlayer.getContainer(), quantity);
			if (err == null) {
				displayMessages();	
			} else {
				System.out.println(currentPlayer.getName() + " is holding an item that can't be dropped: " + itemToDrop.getName());
				messageSelf(err.display(itemToDrop.getName()));
			}
		} else {
			currentPlayer.getEquipment().unEquip(slot);
			displayMessages();
		}
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

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		return true;
	}
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Drop(currentPlayer, fullCommand);
	}
}
