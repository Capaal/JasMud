package skills;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;
import processes.Skills.Syntax;

public class Drop extends Skills {
	
	public Drop() {
		super.name = "drop";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}

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
	}
}
