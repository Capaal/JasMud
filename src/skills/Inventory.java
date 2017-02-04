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
		Set <String> inv = currentPlayer.getInventory().keySet();
		Iterator <String> i = inv.iterator();
		if (!i.hasNext()) {
			inventoryMsg.append("nothing!");
			messageSelf(inventoryMsg.toString());
			return;
		}
		String firstItem = i.next();
		inventoryMsg.append(UsefulCommands.getOnlyStrings(firstItem));
		while (i.hasNext()) {
			inventoryMsg.append(", " + UsefulCommands.getOnlyStrings(i.next()));
		}
		inventoryMsg.append(".");
		messageSelf(inventoryMsg.toString());
		
		//should do short descriptions
	}
}
