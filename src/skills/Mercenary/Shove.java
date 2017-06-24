package skills.Mercenary;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Location;
import processes.Skills;
import processes.WorldServer;
import processes.Location.Direction;
import processes.Skills.Syntax;
import skills.MoveShove;
import skills.Sleep;

public class Shove extends Skills {
	
	private String dir;
	private Location startContainer;
	private Location endContainer;
	private String targetName;
	private Mobile finalTarget;
	
	public Shove(Mobile currentPlayer, String fullCommand) {
		super("shove", "Push someone forcefully into another location.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.DIRECTION);
	}

	
	@Override
	protected void performSkill() {
	
		if (preSkillChecks()) {
		//	messageSelf("You shove " + finalTarget.getName() + " to the " + dir + ".");
		//	messageTarget(currentPlayer.getName() + " shoves you away.", Arrays.asList(finalTarget));
		//	messageOthers(currentPlayer.getName() + " shoves " + finalTarget.getName() + " away.", Arrays.asList(currentPlayer, finalTarget));
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
			MoveShove move = new MoveShove(finalTarget, "move " + dir + " " + currentPlayer.getName());
			move.setShover(currentPlayer);
			WorldServer.getGameState().addToQueue(move);
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
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Shove(currentPlayer, fullCommand);
	}
}
