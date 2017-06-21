package skills;

import java.util.Collection;
import java.util.Iterator;

import interfaces.Holdable;
import interfaces.Mobile;
import processes.Equipment;
import processes.Equipment.EquipmentEnum;
import processes.Skills;

public class Inventory extends Skills {

	public Inventory(Mobile currentPlayer, String fullCommand) {
		super("inventory", "Viewing your items.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
	}
	
	@Override
	protected void performSkill() {
		StringBuilder inventoryMsg = new StringBuilder();
	/*	Collection<Holdable> equippedItems = currentPlayer.getEquipment().getValToKeyMap().keySet();
		Iterator<Holdable> i = equippedItems.iterator();
		inventoryMsg.append("\n");
		inventoryMsg.append("You are wielding: ");
		inventoryMsg.append("\n");
		//grab the 1st item for fenceposting
		if (i.hasNext()) {
			Holdable item = i.next();
			if (item != null) {
				inventoryMsg.append(currentPlayer.getEquipment().getKey(item).toString().toLowerCase());
				inventoryMsg.append(" - ");
				inventoryMsg.append(item.getName());
				while (i.hasNext()) {
					item = i.next();
					if (item != null) {
						inventoryMsg.append(", "); //all I wanted was this comma -.-
						inventoryMsg.append(currentPlayer.getEquipment().getKey(item).toString().toLowerCase());
						inventoryMsg.append(" - ");
						inventoryMsg.append(item.getName());
					}
				}
			}
		} */

		inventoryMsg.append("You are holding: ");
		Collection<Holdable> inv = currentPlayer.getInventory().values();
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
