package skills;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import effects.Balance;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Location;
import processes.Skills;
import processes.Type;

public class Shoot extends Skills {
	
	private int intensity = 8;
	private Set<Location> allLocations;
	private String possibleTarg;
	private Mobile finalTarget;
	
	public Shoot() {
		super.name = "shoot";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.DIRECTION);
	}
	
	// Deals damage to a single target in currentPlayer's location or One Away
	// REQUIRES ranged weapon (eventually)
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
		finalTarget.takeDamage(Type.SHARP, calculateDamage());
		currentPlayer.addEffect(new Balance(), 3000);
		messageSelf("You shoot " + finalTarget.getName() + ".");
		messageTarget(currentPlayer.getName() + " shoots you.", Arrays.asList(finalTarget));
		messageOthers(currentPlayer.getName() + " shoots " + finalTarget.getName(), Arrays.asList(currentPlayer, finalTarget));	
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
}
