package skills;

import java.util.Collection;
import java.util.Iterator;

import interfaces.Holdable;
import interfaces.Mobile;
import processes.Equipment;
import processes.Equipment.EquipmentSlot;
import processes.Skills;

public class Inventory extends Skills {

	public Inventory(Mobile currentPlayer, String fullCommand) {
		super("inventory", "Viewing your items.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
	}
	
	@Override
	protected void performSkill() {
		//wielded
		StringBuilder inventoryMsg = new StringBuilder();
		inventoryMsg.append("You are wielding: ");
		inventoryMsg.append(System.lineSeparator());
		Holdable weapon = currentPlayer.getEquipmentInSlot(EquipmentSlot.LEFTHAND);
		if (weapon != null) {
			inventoryMsg.append("Lefthand: " + weapon.getName());
		} else { inventoryMsg.append("Lefthand: empty. "); }
		weapon = currentPlayer.getEquipmentInSlot(EquipmentSlot.RIGHTHAND);
		if (weapon != null) {
			inventoryMsg.append("Righthand: " + weapon.getName());
		} else {  inventoryMsg.append("Righthand: empty. "); }
		inventoryMsg.append("\n");
		//inventory
		inventoryMsg.append("You are holding: ");
		Collection<Holdable> inv = currentPlayer.viewInventoryWithoutEquipment().values();
		Iterator<Holdable> i = inv.iterator();
		if (!i.hasNext()) {
			inventoryMsg.append("nothing!");
			messageSelf(inventoryMsg.toString());
			return;
		}
		Holdable firstItem = i.next();
		inventoryMsg.append(firstItem.getShortDesc());
		while (i.hasNext()) {
			inventoryMsg.append(", " + i.next().getShortDesc());
		}
		inventoryMsg.append(".");
		messageSelf(inventoryMsg.toString());
		
		//should do short descriptions?
	}

	@Override
	protected boolean preSkillChecks() {
		return true;
	}
	
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Inventory(currentPlayer, fullCommand);
	}
}
