package skills;

import java.util.Arrays;

import interfaces.Holdable;
import processes.Skills;

public class Drop extends Skills {
	
	public Drop() {
		super.name = "drop";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}
	
	// Moves a HOLDABLE from the CURRENTPLAYER's INVENTORY to the LOCATION they are in.
	// Requires balance, syntax = "drop dagger" or "drop sword1532"
	@Override
	public void performSkill() {
		if (!hasBalance()) {
			return;
		}
		Holdable itemToMove = currentPlayer.getHoldableFromString(Syntax.ITEM.getStringInfo(fullCommand, this));
		if (itemToMove == null) {
			messageSelf("You can't find that item.");
			return;
		}
		moveHoldable(itemToMove, currentPlayer.getContainer());
		messageSelf("You drop " + itemToMove.getName() + ".");
		messageOthers(currentPlayer.getName() + " drops " + itemToMove.getName() + ".", Arrays.asList(currentPlayer));
	}
}
