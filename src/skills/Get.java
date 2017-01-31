package skills;

import java.util.Arrays;

import interfaces.Container;
import interfaces.Holdable;
import processes.Skills;

public class Get extends Skills {
	
	public Get() {
		super.name = "get";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}

	// Moves a HOLDABLE from the LOCATION of the CURRENTPLAYER into their INVENTORY.
	// Requires Balance, Syntax = "get sword" or "get dagger1234"
	@Override
	public void performSkill() {
		if (!hasBalance()) {
			return;
		}
		Container here = currentPlayer.getContainer();
		Holdable itemToMove = here.getHoldableFromString(Syntax.ITEM.getStringInfo(fullCommand, this));
		if (itemToMove == null) {
			messageSelf("You can't find that item.");
			return;
		}		
		itemToMove.moveHoldable(currentPlayer);
		messageSelf("You get " + itemToMove.getName() + ".");
		messageOthers(currentPlayer.getName() + " picks up " + itemToMove.getName() + ".", Arrays.asList(currentPlayer));
	}
}
