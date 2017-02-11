package skills;

import java.util.Arrays;

import effects.ConditionsEnum;
import interfaces.Container;
import processes.Location;
import processes.Skills;
import processes.StdMob;

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
	protected void performSkill() {
		if (!hasBalance()) {
			return;
		}
		if (!canMove()) {
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
		currentPlayer.moveHoldable(endContainer);
		messageOthers(currentPlayer.getName() + " arrives from the " + Location.Direction.getDirectionName(dir).getOpp() + ".", Arrays.asList(currentPlayer));
		Look look = new Look();
		look.perform("", currentPlayer);
	}
	
	private boolean canMove() {
		if(currentPlayer.hasAllConditions(ConditionsEnum.BROKENLEGS)) {
			messageSelf("Oh no your legs are broken.");
			return false;
		}
		return true;
	}
}
