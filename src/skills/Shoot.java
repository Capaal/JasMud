package skills;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import effects.PassiveCondition;
import interfaces.Holdable;
import interfaces.Mobile;
import items.StdItem;
import processes.Equipment;
import processes.Equipment.EquipmentSlot;
import processes.Location;
import processes.Location.Direction;
import processes.Skills;
import processes.Type;

public class Shoot extends Skills {
	
	private int intensity = 20;
	private Set<Location> allLocations;
	private String possibleTarg;
	private Mobile finalTarget;
	private StdItem possBow;
	
	public Shoot(Mobile currentPlayer, String fullCommand) {
		super("shoot", "A Quick shot or throw.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.DIRECTION);
	}
	
	// Deals damage to a single target in currentPlayer's location or infinitely away in one direction
	// REQUIRES ranged weapon (eventually)
	// Direction is OPTIONAL
	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			messageSelf("You shoot " + finalTarget.getName() + ".");
			messageTarget(currentPlayer.getNameColored() + " shoots you.", Arrays.asList(finalTarget));
			messageOthers(currentPlayer.getNameColored() + " shoots " + finalTarget.getNameColored(), Arrays.asList(currentPlayer, finalTarget));
			finalTarget.informLastAggressor(currentPlayer);
			finalTarget.takeDamage(Type.SHARP, calculateDamage());
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
		}
	}		
		
	private int calculateDamage() {
		return (int) (possBow.getDamageMult() * intensity);
	}
	
	private void findAllLocations() {
		Location currentPlayerLocation = (currentPlayer.getContainer());		
		allLocations.add(currentPlayerLocation);		
		String dir = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		if (!dir.equals("")) {
			Location nextLocation = currentPlayerLocation.getContainer(dir);
			if (isDoorBlocking(currentPlayerLocation, Direction.getDirectionName(dir))) {
				messageSelf("There is a door blocking your way.");
				return;
			}
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
		if (!isDoorBlocking(currentLocation, Direction.getDirectionName(dir))) {
			if (anotherLocation != null && !allLocations.contains(anotherLocation)) {
				allLocations.add(anotherLocation);
				getNextLocation(dir, anotherLocation);
			}
		}		
	}
	
	private Mobile getTarget() {
		for (Location l : allLocations) {		
			Mobile t = l.getMobileFromString(possibleTarg);
				if (t != null) {
					return t;
				}
		}
		return null;
	}

	@Override
	protected boolean preSkillChecks() {
		allLocations = new HashSet<Location>();
		possibleTarg = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (possibleTarg == "") {
			messageSelf("What are you trying to shoot?");
			return false;
		}
		if (!hasBalance()) {
			return false;
		}	
		if(!checkWeapon()) {return false;}
		findAllLocations();				
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
	
	private boolean checkWeapon() {
        //righthand primary for now
    	Holdable possBowLeft = currentPlayer.getEquipmentInSlot(EquipmentSlot.LEFTHAND);
    	Holdable possBowRight = currentPlayer.getEquipmentInSlot(EquipmentSlot.RIGHTHAND);
    	if ((possBowLeft != null && possBowLeft.getName().equals("bow")) || (possBowRight != null && possBowRight.getName().equals("bow"))) {
    		return true;
    	}
    	return false;
    	
    	
	/*	if ((possBowLeft == null || (!possBow.getName().equals("bow")))) {
			possBow = (StdItem) currentPlayer.getEquipmentInSlot(EquipmentSlot.RIGHTHAND);
			if (possBow == null || (!possBow.getName().equals("bow"))) {
				messageSelf("You are not wielding a bow.");
				return false;
			}
		}
		return true;*/
	}
	
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Shoot(currentPlayer, fullCommand);
	}
}
