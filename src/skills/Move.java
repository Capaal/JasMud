package skills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import effects.PassiveCondition;
import processes.Location;
import processes.Location.Direction;
import processes.Skills;

public class Move extends Skills {	
	
	public Move() {
		super.name = "move";
		super.description = "Move around.";
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
		if (!hasBalance()) {return;}
		if (!legsOk()) {return;}
		startContainer = currentPlayer.getContainer();
		endContainer = null;
		dir = Syntax.DIRECTION.getStringInfo(fullCommand, this); // Why not convert to enum?
		
		if(isRooted()) {return;};
		ifDizzy(); //if dizzy, sets a new random direction, ok to run into walls
		if (!dir.equals("")) {
			endContainer = startContainer.getContainer(dir);
			directionEnum = Direction.getDirectionName(dir);
		} else {
			messageSelf("You need to say which way you want to go!");
			return;
		}
		if (endContainer == null) {
			messageSelf("You can't go that way!");
			return;
		}
		if (isDoorBlocking(startContainer, directionEnum)) {
			messageSelf("The door is closed before you.");
			return;
		}
		displayMessages();
		
		Look look = new Look();
		look.perform("", currentPlayer);
		moveFollowers();
		stopFollowing();
	}
	
	protected void displayMessages() {
		messageOthers(currentPlayer.getName() + " leaves to the " + dir.toLowerCase() + ".", Arrays.asList(currentPlayer));
		currentPlayer.moveHoldable(endContainer);		
		//this opposite direction is not always correct?
		messageOthers(currentPlayer.getName() + " arrives from the " + Location.Direction.getDirectionName(dir).getOpp().toLowerCase() + ".", Arrays.asList(currentPlayer));
		
	}
	
	private boolean legsOk() {
		if(currentPlayer.hasAllConditions(PassiveCondition.BROKENLEGS)) {
			messageSelf("Oh no your legs are broken.");
			return false;
		}
		return true;
	}
	
	private void ifDizzy() {
		if (currentPlayer.hasAllConditions(PassiveCondition.DIZZY)) {
			String[] d = {"n", "s", "e", "w", "nw", "se", "ne", "sw", "in", "out", "up", "down"};
			Random r = new Random();
			dir = d[r.nextInt(d.length)];		
			messageSelf("You stumble around in confusion and move a random direction.");
		}
	}
	
	private boolean isRooted() {
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
	
	public void removeFollower(Follow follow) {
		followers.remove(follow);
	}
}
