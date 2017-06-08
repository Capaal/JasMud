package skills.Mercenary;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Location;
import processes.Skills;
import processes.Location.Direction;
import processes.Skills.Syntax;

public class Shove extends Skills {

	
	public Shove() {
		super.name = "shove";
		super.description = "Push someone forcefully into another location.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.DIRECTION);
	}

	private String dir;
	private Location startContainer;
	private Location endContainer;
	private String targetName;
	private Mobile finalTarget;
	
	@Override
	protected void performSkill() {
		if (!hasBalance()) {return;}
		startContainer = currentPlayer.getContainer();
		targetName = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (!setTarget()) {return;}
		if (!setDirection()) {return;}
		
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
		messageSelf("You shove " + finalTarget.getName() + " to the " + dir + ".");
		messageTarget(currentPlayer.getName() + " shoves you away.", Arrays.asList(finalTarget));
		messageOthers(currentPlayer.getName() + " shoves " + finalTarget.getName() + " away.", Arrays.asList(currentPlayer, finalTarget));
		finalTarget.moveHoldable(endContainer);
		messageOthersAway(targetName + "is suddenly shoved into this location.", Arrays.asList(finalTarget), endContainer);
		
	}
	
	private boolean setTarget() {
		finalTarget = null;
		if (targetName.equals("")) {
			messageSelf("Who are you trying to shove?");
			return false;
		}
		finalTarget = startContainer.getMobileFromString(targetName);		
		if (finalTarget == null) {
			messageSelf("There is no " + targetName + " here for you to shove.");
			return false;
		}
		return true;
	}
	
	private boolean setDirection() {
		dir = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		if (!dir.equals("")) {
			endContainer = startContainer.getContainer(dir);
		} else {
			messageSelf("Specify direction to shove.");
			return false;
		}
		if (endContainer == null) {
			messageSelf("There is no location in that direction.");
			return false;
		}
		if (isDoorBlocking(startContainer, Direction.getDirectionName(dir))) {
			messageSelf("You can't shove through closed doors.");
			return false;
		}
		return true;
	}

}
