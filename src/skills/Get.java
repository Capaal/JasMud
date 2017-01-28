package skills;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;
import processes.WorldServer;
import processes.Skills.Syntax;

public class Get extends Skills {
	
	public Get() {
		super.name = "get";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}

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
		
		moveHoldable(itemToMove, currentPlayer);
		messageSelf("You get " + itemToMove.getName() + ".");
	}

}
