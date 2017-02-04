package skills;

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
		StringBuilder inventory = new StringBuilder();
		inventory.append("You are holding: ");

		for (String i : currentPlayer.getInventory().keySet()) {
			inventory.append(UsefulCommands.getOnlyStrings(i) + ", ");
		}
		messageSelf(inventory.toString());
	}
}
