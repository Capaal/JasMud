package skills;

import java.util.Arrays;
import java.util.Random;

import effects.ConditionsEnum;
import interfaces.Container;
import processes.Location;
import processes.Location.Direction;
import processes.Skills;
import processes.StdMob;

public class Move extends Skills {
	
	private String dir;
	
	public Move() {
		super.name = "move";
		super.description = "Move around.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.DIRECTION);
	}
	
	private Location startContainer;
	private Location endContainer;
	
	@Override
	protected void performSkill() {
		if (!hasBalance()) {return;}
		if (!canMove()) {return;}
		startContainer = currentPlayer.getContainer();
		endContainer = null;
		dir = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		ifDizzy(); //if dizzy, sets a new random direction, ok to run into walls
		if (!dir.equals("")) {
			endContainer = startContainer.getContainer(dir);
		} else {
			messageSelf("You need to say which way you want to go!");
			return;
		}
		if (endContainer == null) {
			messageSelf("You can't go that way!");
			return;
		}
		messageOthers(currentPlayer.getName() + " leaves to the " + dir.toLowerCase() + ".", Arrays.asList(currentPlayer));
		currentPlayer.moveHoldable(endContainer);
		//this opposite direction is not always correct?
		messageOthers(currentPlayer.getName() + " arrives from the " + Location.Direction.getDirectionName(dir).getOpp().toLowerCase() + ".", Arrays.asList(currentPlayer));
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
	
	private void ifDizzy() {
		if (currentPlayer.hasAllConditions(ConditionsEnum.DIZZY)) {
			String[] d = {"n", "s", "e", "w", "nw", "se", "ne", "sw", "in", "out", "up", "down"};
			Random r = new Random();
			dir = d[r.nextInt(d.length)];		
			messageSelf("You stumble around in confusion and move a random direction.");
		}
	}
}
