package skills;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;
import processes.Skills.Syntax;

public class Get extends Skills {
	
	public Get() {
		super.id = 3;
		super.name = "get";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}

	@Override
	public void perform(String fullCommand, Mobile currentPlayer) {
		Container here = currentPlayer.getContainer();
		Holdable itemToMove = here.getHoldableFromString(this.getStringInfo(Syntax.ITEM, fullCommand));
		itemToMove.getContainer().removeItemFromLocation(itemToMove);
		currentPlayer.acceptItem(itemToMove);
	}

}
