package skills.Mercenary;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Location;
import processes.Skills;
import processes.Location.Direction;
import processes.Skills.Syntax;
import skills.MoveShove;

public class Shove extends Skills {
	
	private String dir;
	private Location startContainer;
	private Location endContainer;
	private String targetName;
	private Mobile finalTarget;
	private MoveShove move;
	
	public Shove() {
		super.name = "shove";
		super.description = "Push someone forcefully into another location.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.DIRECTION);
		this.move = new MoveShove();
	}

	
	@Override
	protected void performSkill() {
	
		if (preSkillChecks()) {
		//	messageSelf("You shove " + finalTarget.getName() + " to the " + dir + ".");
		//	messageTarget(currentPlayer.getName() + " shoves you away.", Arrays.asList(finalTarget));
		//	messageOthers(currentPlayer.getName() + " shoves " + finalTarget.getName() + " away.", Arrays.asList(currentPlayer, finalTarget));
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
			move.setShover(currentPlayer);
			move.perform("move " + dir + " " + currentPlayer.getName(), finalTarget);
		//	messageOthersAway(targetName + "is suddenly shoved into this location.", Arrays.asList(finalTarget), endContainer);
		}
		
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


	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		startContainer = currentPlayer.getContainer();
		targetName = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (!setTarget()) {return false;}
		if (!setDirection()) {return false;}
		return true;
	}

}
