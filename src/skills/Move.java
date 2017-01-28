package skills;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.List;

import processes.Location;
import processes.Skills;
import processes.Skills.Syntax;

public class Move extends Skills {
	
	public Move() {
		super.name = "move";
		super.description = "Move around.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.DIRECTION);
	}
	
	@Override
	public void perform(String fullCommand, Mobile currentPlayer) {
		if (!hasBalance(currentPlayer)) {
			return;
		}
		Container startLocation = currentPlayer.getContainer();
		Location futureLocation = null;
		String dir = getStringInfo(Syntax.DIRECTION, fullCommand);
		if (!dir.equals("")) {
			if (startLocation instanceof Location) {
				futureLocation = ((Location)startLocation).getContainer(dir);
			}
		} else {
			messageSelf("You need to say which way you want to go!", currentPlayer);
			return;
		}
		if (futureLocation == null) {
			messageSelf("You can't go that way!", currentPlayer);
			return;
		}
		moveHoldable(currentPlayer, futureLocation);
		currentPlayer.getContainer().look(currentPlayer);
	}
}
