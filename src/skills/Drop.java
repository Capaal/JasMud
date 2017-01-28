package skills;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;

public class Drop extends Skills {
	
	public Drop() {
		super.name = "drop";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}

	@Override
	public void perform(String fullCommand, Mobile currentPlayer) {
		super.perform(fullCommand, currentPlayer);
		if (!hasBalance(currentPlayer)) {
			return;
		}
		Container inventory = currentPlayer;
		Holdable itemToMove = inventory.getHoldableFromString(this.getStringInfo(Syntax.ITEM, fullCommand));
		if (itemToMove == null) {
			messageSelf("You can't find that item.", currentPlayer);
			return;
		}
		moveHoldable(itemToMove, inventory);
		messageSelf("You drop " + itemToMove.getName() + ".", currentPlayer);
	}
}
