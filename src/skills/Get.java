package skills;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;
import processes.WorldServer;

public class Get extends Skills {
	
	public Get() {
		super.id = 3;
		super.name = "get";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}

	@Override
	public void perform(String fullCommand, Mobile currentPlayer) {
		if (!hasBalance(currentPlayer)) {
			return;
		}
		Container here = currentPlayer.getContainer();
		Holdable itemToMove = here.getHoldableFromString(this.getStringInfo(Syntax.ITEM, fullCommand));
		if (itemToMove == null) {
			messageSelf("You can't find that item.", currentPlayer);
			return;
		}
		itemToMove.getContainer().removeItemFromLocation(itemToMove);
		currentPlayer.acceptItem(itemToMove);
		messageSelf("You get " + itemToMove.getName() + ".", currentPlayer);
	}

}
