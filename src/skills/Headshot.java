package skills;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import effects.Balance;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.InductionSkill;
import processes.Location;
import processes.Type;

public class Headshot extends InductionSkill {
	
	private int intensity = (8*3);
	private Set<Location> allLocations;
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
		allLocations = new HashSet<Location>();
		possibleTarg = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (possibleTarg == "") {
			messageSelf("What are you trying to headshot?");
			return;
		}
		if (!hasBalance()) {
			return;
		}	
		findAllLocations();				
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
	
	private void findAllLocations() {
		Location currentPlayerLocation = (Location)(currentPlayer.getContainer());		
		allLocations.add(currentPlayerLocation);		
		String dir = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		if (!dir.equals("")) {
			Location nextLocation = currentPlayerLocation.getContainer(dir);
			if (nextLocation == null) {
				messageSelf("There isn't a location that way.");
				return;
			} else {
				allLocations.add(nextLocation);
				getNextLocation(dir, nextLocation);
			}
		}
	}
	
	private void getNextLocation(String dir, Location currentLocation) {
		Location anotherLocation = currentLocation.getContainer(dir);
		if (anotherLocation != null && !allLocations.contains(anotherLocation)) {
			allLocations.add(anotherLocation);
			getNextLocation(dir, anotherLocation);
		}
	}
	
	private Mobile getTarget() {
		for (Location l : allLocations) {		
			Holdable t = l.getHoldableFromString(possibleTarg);
				if (t != null && t instanceof Mobile) {
					return (Mobile)t;
				}
		}
		return null;
	}

	@Override
	public void inductionKilled() {
		messageSelf("You stop aiming at " + possibleTarg);
	}

	
}
