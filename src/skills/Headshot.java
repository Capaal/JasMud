package skills;

import java.util.Arrays;

import effects.Balance;
import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.InductionSkill;
import processes.Location;
import processes.Type;

public class Headshot extends InductionSkill {
	
	private int intensity = (8*3);
	private Location finalLoc;
	private String possibleTarg;
	private Mobile finalTarget;
	
	
	// THOSE THINGS ABOVE ARE SET, WHAT IF TWO PEOPLE HEADSHOT AROUND THE SAME TIME?
	// PANIC!!! It totally is messed up.
	public Headshot() {
		super.name = "headshot";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.DIRECTION);
	}
	
	@Override
	public void run() {		
		finalTarget = getTarget();
		if (finalTarget == null) {
			messageSelf("There is no \"" + possibleTarg + "\" for you to attack.");
			return;
		}	
		if (isBlocking(finalTarget)) {  // Probably not complete still
			return;
		}		
		finalTarget.takeDamage(Type.SHARP, calculateDamage());
		currentPlayer.addEffect(new Balance(), 3000);
		messageSelf("You headshot " + finalTarget.getName() + ".");
		messageTarget(currentPlayer.getName() + " headshots you.", Arrays.asList(finalTarget));
		messageOthers(currentPlayer.getName() + " headshots " + finalTarget.getName(), Arrays.asList(currentPlayer, finalTarget));		
	}
	
	// Deals damage to a single target in currentPlayer's location or One Away
	// REQUIRES ranged weapon
	// Direction is OPTIONAL
	@Override
	public void performSkill() {
		if (!hasBalance()) {
			return;
		}
		finalLoc = (Location)(currentPlayer.getContainer());
		String dir = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		if (!dir.equals("")) {
			finalLoc = getLoc(dir);
			if (finalLoc == null) {
				messageSelf("There isn't a location that way.");
				return;
			}
		} 
		possibleTarg = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (possibleTarg == "") {
			messageSelf("What are you trying to headshot?");
			return;
		}
		finalTarget = getTarget();
		if (finalTarget == null) {
			messageSelf("There is no \"" + possibleTarg + "\" for you to attack.");
			return;
		}	
		if (isBlocking(finalTarget)) {  // Probably not complete still
			return;
		}	
		scheduleSkillRepeatNTimesOverXMilliseconds(1, 2000);
		currentPlayer.setInduction(this);
		messageSelf("You begin aiming at " + finalTarget.getName() + ".");
		messageTarget(currentPlayer.getName() + " begins aiming at your head.", Arrays.asList(finalTarget));
	}		
		
	private int calculateDamage() {
		return intensity;
	}
	
	private Mobile getTarget() {
		Holdable h = finalLoc.getHoldableFromString(possibleTarg);
			if (h != null && h instanceof Mobile) {
				return (Mobile)h;
			}
		return null;
	}
	
	private Location getLoc(String dir) {
		Container mobLocation = currentPlayer.getContainer();
		if (mobLocation instanceof Location) {
			return ((Location)mobLocation).getContainer(dir);
		}
		return null;
	}

	@Override
	public void inductionKilled() {
		messageSelf("You stop aiming at " + possibleTarg);
	}

	
}
