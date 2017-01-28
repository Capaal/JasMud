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
	
	private Container startContainer;
	private Container endContainer;
	
	@Override
	public void performSkill() {
		if (!hasBalance()) {
			return;
		}
		Container startContainer = currentPlayer.getContainer();
		Location endContainer = null;
		String dir = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		if (!dir.equals("")) {
			if (startContainer instanceof Location) {
				endContainer = ((Location)startContainer).getContainer(dir);
			}
		} else {
			messageSelf("You need to say which way you want to go!");
			return;
		}
		if (endContainer == null) {
			messageSelf("You can't go that way!");
			return;
		}
		moveHoldable(currentPlayer, endContainer);
		currentPlayer.getContainer().look(currentPlayer);
	}
}
