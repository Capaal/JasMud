package skills;

import java.util.Collection;
import java.util.Iterator;
import interfaces.Holdable;
import processes.Skills;

public class Inventory extends Skills {

	public Inventory() {
		super.name = "inventory";
		super.syntaxList.add(Syntax.SKILL);
	}
	
	@Override
	protected void performSkill() {
		StringBuilder inventoryMsg = new StringBuilder();
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
		
		//should do short descriptions
	}
}
