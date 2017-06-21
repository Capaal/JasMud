package skills;

import interfaces.Mobile;

import java.util.ArrayList;
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
	protected ArrayList<Follow> followers = new ArrayList<Follow>();
	protected Follow follow = null;
	
	@Override
	protected void performSkill() {
		if (follow == null) {
			follow = (Follow) currentPlayer.getCommand("follow");
		}
		if (preSkillChecks()) {

			ifDizzy(); //if dizzy, sets a new random direction, ok to run into walls
	
			if (endContainer == null) {
				messageSelf("You can't go that way!");
				return;
			}
			if (!checkDoor()) {return;}
			
			displayLeaveMsg();
			currentPlayer.moveHoldable(endContainer);
			displayEnterMsg();
			
			Look look = new Look(currentPlayer, "");
			look.run();
	//		moveFollowers();
	//		stopFollowing();
		}
	}
	
	protected boolean findDirection() {
		dir = Syntax.DIRECTION.getStringInfo(fullCommand, this); // Why not convert to enum?
		if (!dir.equals("")) {
			endContainer = startContainer.getContainer(dir);
			directionEnum = Direction.getDirectionName(dir);
			return true;
		} else {
			messageSelf("You need to say which way you want to go!");
			return false;
		}
	}
	
	protected boolean checkDoor() { //might crash if directionEnum is null TODO
		if (isDoorBlocking(startContainer, directionEnum)) {
			messageSelf("The door is closed before you.");
			return false;
		}
		return true;
	}
	
	protected void displayLeaveMsg() {
		messageOthers(currentPlayer.getName() + " leaves to the " + dir.toLowerCase() + ".", Arrays.asList(currentPlayer));
	}
	
	protected void displayEnterMsg() {
		messageOthers(currentPlayer.getName() + " arrives from the " + Location.Direction.getDirectionName(dir).getOpp().toLowerCase() + ".", Arrays.asList(currentPlayer));
		//this opposite direction is not always correct?
	}
	
	protected boolean legsOk() {
		if(currentPlayer.hasAllConditions(PassiveCondition.BROKENLEGS)) {
			messageSelf("Oh no your legs are broken.");
			return false;
		}
		return true;
	}
	
	protected void ifDizzy() {
		if (currentPlayer.hasAllConditions(PassiveCondition.DIZZY)) {
			String[] d = {"n", "s", "e", "w", "nw", "se", "ne", "sw", "in", "out", "up", "down"};
			Random r = new Random();
			dir = d[r.nextInt(d.length)];		
			messageSelf("You stumble around in confusion and move a random direction.");
		}
	}
	
	protected boolean isRooted() {
		if (currentPlayer.hasAllConditions(PassiveCondition.ROOT)) {
			messageSelf("You are rooted and cannot move.");
			return true;
		}
		return false;
	}
	
	protected void moveFollowers() {
		for (Follow f : followers) {
			f.move(fullCommand);
		}
	}
	
	protected void stopFollowing() {
		follow.stopFollowing();
	}
	
	protected void loseFollowers() {
		followers.clear();
	}
	
	protected void addFollower(Follow follow) {
		followers.add(follow);
	}
	
	//A follows B, A stops following B, B removes A here - wtf?
	public void removeFollower(Follow follow) {
		followers.remove(follow);
	}

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		if (!legsOk()) {return false;}
		if (isRooted()) {return false;};
		startContainer = currentPlayer.getContainer();
		endContainer = null;
		if (!findDirection()) {return false;}
		
		return true;
	}
	
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Move(currentPlayer, fullCommand);
	}
}
