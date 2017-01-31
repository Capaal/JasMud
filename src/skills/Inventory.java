package skills;

import interfaces.Container;
import processes.Location;
import processes.Skills;

public class Inventory extends Skills {

	public Inventory() {
		super.name = "inventory";
		super.syntaxList.add(Syntax.SKILL);
	}
	
	@Override
	protected void performSkill() {
		Container inventory = currentPlayer;
		inventory.look(currentPlayer);
	}
}
