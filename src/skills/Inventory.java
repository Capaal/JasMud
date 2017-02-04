package skills;

import java.util.Iterator;
import java.util.Set;

import interfaces.Container;
import interfaces.Holdable;
import processes.Location;
import processes.Skills;
import processes.UsefulCommands;

public class Inventory extends Skills {

	public Inventory() {
		super.name = "inventory";
		super.syntaxList.add(Syntax.SKILL);
	}
	
	@Override
	protected void performSkill() {
		StringBuilder inventoryMsg = new StringBuilder();
		inventoryMsg.append("You are holding: ");
		Set inv = currentPlayer.getInventory().keySet();
		Iterator i = inv.iterator();
		String firstItem = (String)i.next();
		inventoryMsg.append(firstItem);
		while (i.hasNext()) {
			inventoryMsg.append(", " + (String)i.next());
		}
		inventoryMsg.append(".");
		messageSelf(inventoryMsg.toString());
		
		//should do short descriptions
	}
}
