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
	
	public Headshot() {
		super.name = "headshot";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.DIRECTION);
	}
	
	// Runs POST INDUCTION.
	@Override
	public void run() {		
		finalTarget = getTarget(); // Performs finding target again to ensure target is valid.
		if (finalTarget == null) {
			messageSelf("There is no \"" + possibleTarg + "\" for you to attack.");
			return;
		}	
		if (isBlocking(finalTarget)) {  // Probably not complete still
			return;
		}		
		finalTarget.takeDamage(Type.SHARP, calculateDamage());
		currentPlayer.addEffect(new Balance(), calculateBalance());
		messageSelf("You headshot " + finalTarget.getName() + ".");
		messageTarget(currentPlayer.getName() + " headshots you.", Arrays.asList(finalTarget));
		messageOthers(currentPlayer.getName() + " headshots " + finalTarget.getName(), Arrays.asList(currentPlayer, finalTarget));		
	}
	
	// Deals damage to a single target in currentPlayer's location or infinitately away in one direction.
	// Target MUST be targettable BOTH at the START and at the END of induction.
	// REQUIRES ranged weapon (eventually)
	// Direction is OPTIONAL, defaults to currentPlayer's location
	@Override
	protected void performSkill() {
		allLocations = new HashSet<Location>();
		if (!hasBalance()) {return;}
		if (!findAllLocations()) {return;}		
		if (!findTarget()) {return;}	
		scheduleSkillRepeatNTimesOverXMilliseconds(1, 2000); // Triggers this skill's "run()" in 2 seconds. Interruptible.
		currentPlayer.setInduction(this);
		messageSelf("You begin aiming at " + finalTarget.getName() + ".");
		messageTarget(currentPlayer.getName() + " begins aiming at your head.", Arrays.asList(finalTarget));
	}		
		
	private int calculateDamage() {
		return intensity;
	}
	
	private boolean findAllLocations() {
		Location currentPlayerLocation = currentPlayer.getContainer();		
		allLocations.add(currentPlayerLocation);		
		String dir = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		if (!dir.equals("")) {
			Location nextLocation = currentPlayerLocation.getContainer(dir);
			if (nextLocation == null) {
				messageSelf("There isn't a location that way.");
				return false;
			} else {
				allLocations.add(nextLocation);
				getNextLocation(dir, nextLocation);
			}
		}
		return true;
	}
	
	private void getNextLocation(String dir, Location currentLocation) {
		Location anotherLocation = currentLocation.getContainer(dir);
		if (anotherLocation != null && !allLocations.contains(anotherLocation)) {
			allLocations.add(anotherLocation);
			getNextLocation(dir, anotherLocation);
		}
	}
	
	private boolean findTarget() {
		possibleTarg = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (possibleTarg == "") {
			messageSelf("Who are you trying to headshot?");
			return false;
		}							
		finalTarget = getTarget();
		if (finalTarget == null) {
			messageSelf("There is no \"" + possibleTarg + "\" for you to attack.");
			return false;
		}	
		if (isBlocking(finalTarget)) {  // Probably not complete still
			return false;
		}
		return true;
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
	
	private int calculateBalance() {
	//	double damageMult = item.getBalanceMult();  Item not required yet.
	//	return (int) (damageMult * intensity);
		return 3000;
	}
}
