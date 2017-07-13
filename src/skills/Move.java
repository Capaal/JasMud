package skills;

import interfaces.Mobile;

import java.util.Arrays;
import java.util.Random;

import effects.PassiveCondition;
import processes.Location;
import processes.Location.Direction;
import processes.Skills;

public class Move extends Skills {	
	
	public Move(Mobile currentPlayer, String fullCommand) {
		super("move", "Move around.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.DIRECTION);
	}
	
	protected String dir; // Not Direction enum?
	protected Direction directionEnum;
	protected Location startContainer;
	protected Location endContainer;
	
	
	@Override
	protected void performSkill() {
		stopFollowing();	
		if (endContainer == null) {
			messageSelf("You can't go that way!");
			onFail();
			return;
		}
		if (isDirectionBlocked(startContainer, directionEnum)) {
			//no messageSelf? TODO
			onFail();
			return;
		}
		
		displayLeaveMsg();
		currentPlayer.moveHoldable(endContainer);
		displayEnterMsg();
		
		Look look = new Look(currentPlayer, "");
		look.run();
		moveFollowers();
		stopFollowing();
		
		// I removed the below because it doesn't work with the new preSkillCheks() check done in skills, but might break moveFollow or fearFollow
	//	} else {
	//		onFail();
	//	}
	}
	
	@Override
	protected void displayPrompt() {
	}
	
	protected void onFail() {
		stopFollowing();
	}
	
	protected void moveFollowers() {
		currentPlayer.moveFollowers(fullCommand);
	}
	
	protected void stopFollowing() {
		currentPlayer.stopFollowing();
	}
	
	protected boolean findDirection() {
		dir = Syntax.DIRECTION.getStringInfo(fullCommand, this); // Why not convert to enum?
		ifConfused(); //if confused, sets the opposite direction, ok to run into walls
		ifDizzy(); //if dizzy, sets a new random direction, ok to run into walls
		if (!dir.equals("")) {
			endContainer = startContainer.getLocation(dir);
			directionEnum = Direction.getDirectionName(dir);
			return true;
		} else {
			messageSelf("You need to say which way you want to go!");
			return false;
		}
	}
	
	protected void displayLeaveMsg() {
		messageOthers(currentPlayer.getNameColored() + " leaves to the " + dir.toLowerCase() + ".", Arrays.asList(currentPlayer));
	}
	
	protected void displayEnterMsg() {
		messageOthers(currentPlayer.getNameColored() + " arrives from the " + Location.Direction.getDirectionName(dir).getOpp().toLowerCase() + ".", Arrays.asList(currentPlayer));
		//this opposite direction is not always correct?
	}
	
	protected boolean legsOk() {
		if(currentPlayer.hasCondition(PassiveCondition.BROKENLEGS)) {
			messageSelf("Oh no your legs are broken.");
			return false;
		}
		return true;
	}
	
	protected void ifConfused() {
		if (currentPlayer.hasCondition(PassiveCondition.CONFUSED)) {
			dir = Direction.getDirectionName(dir).getOpp();
			messageSelf("In your confused state, you try to go the other way.");
		}
	}
	
	protected void ifDizzy() {
		if (currentPlayer.hasCondition(PassiveCondition.DIZZY)) {
			String[] d = {"n", "s", "e", "w", "nw", "se", "ne", "sw", "in", "out", "up", "down"};
			Random r = new Random();
			dir = d[r.nextInt(d.length)];		
			messageSelf("You stumble around in confusion and move a random direction.");
		}
	}
	
	protected boolean isRooted() {
		if (currentPlayer.hasCondition(PassiveCondition.ROOT)) {
			messageSelf("You are rooted and cannot move.");
			return true;
		}
		return false;
	}

	// The onFail() hack allows moveFollow and others to stop following when failing to follow.
	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {onFail();return false;}
		if (!legsOk()) {onFail();return false;}
		if (isRooted()) {onFail();return false;};
		startContainer = currentPlayer.getContainer();
		endContainer = null;
		if (!findDirection()) {onFail();return false;}
		return true;
	}
	
	@Override
	public String displaySyntax() {
		return "MOVE [DIR] or just [DIR]";
	}
}
