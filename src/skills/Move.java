package skills;

import java.util.Arrays;

import interfaces.Container;
import processes.Location;
import processes.Skills;

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
		startContainer = currentPlayer.getContainer();
		endContainer = null;
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
		messageOthers(currentPlayer.getName() + " leaves to the " + dir + ".", Arrays.asList(currentPlayer));
		moveHoldable(currentPlayer, endContainer);
		messageOthers(currentPlayer.getName() + " arrives from the " + Location.Direction.getDirectionName(dir).getOpp() + ".", Arrays.asList(currentPlayer));
		currentPlayer.getContainer().look(currentPlayer);
	}
}
