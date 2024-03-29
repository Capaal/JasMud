package skills.Mercenary;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import effects.PassiveCondition;
import interfaces.InformsAggro;
import interfaces.Mobile;
import processes.Equipment.EquipmentSlot;
import processes.Location;
import processes.Location.Direction;
import skills.InductionSkill;

public class Headshot extends InductionSkill implements InformsAggro {
	
	private int intensity = 30;
	private Set<Location> allLocations;
	private String possibleTarg;
	private Mobile finalTarget;
	
	public Headshot(Mobile currentPlayer, String fullCommand) {
		super("headshot", "Careful aim with a ranged weapon.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.DIRECTION);
	}
	
	public class InnerHeadshot extends InnerSkill {	
		public InnerHeadshot(Mobile currentPlayer, String fullCommand) {
			super(currentPlayer, fullCommand);
		}

		@Override
		protected void performSkill() {
			finalTarget = getTarget(); // Performs finding target again to ensure target is valid.
			if (finalTarget == null) {
				messageSelf("There is no \"" + possibleTarg + "\" for you to attack.");
				return;
			}	
			if (isBlocking(finalTarget)) { 
				return;
			}		
			finalTarget.takeDamage(calculateDamage());
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, calculateBalance());
			messageSelf("You headshot " + finalTarget.getNameColored() + ".");
			messageTarget(currentPlayer.getNameColored() + " headshots you.", Arrays.asList(finalTarget));
			messageOthers(currentPlayer.getNameColored() + " headshots " + finalTarget.getNameColored(), Arrays.asList(currentPlayer, finalTarget));	
			messageOthersAway(currentPlayer.getNameColored() + " headshots " + finalTarget.getNameColored(), Arrays.asList(currentPlayer, finalTarget), finalTarget.getContainer());	
			informLastAggressor(currentPlayer, finalTarget);
		}		
	}
	
	// Deals damage to a single target in currentPlayer's location or infinitately away in one direction.
	// Target MUST be targettable BOTH at the START and at the END of induction.
	// REQUIRES ranged weapon (eventually)
	// Direction is OPTIONAL, defaults to currentPlayer's location
	@Override
	protected void performSkill() {		
		scheduleInduction(new InnerHeadshot(currentPlayer, fullCommand), 1, 2000); // Triggers this skill's "run()" in 2 seconds 1 time. Interruptible.
		currentPlayer.setInduction(this);
		messageSelf("You begin aiming at " + finalTarget.getName() + ".");
		messageTarget(currentPlayer.getName() + " begins aiming at your head.", Arrays.asList(finalTarget));
	}		
		
	private int calculateDamage() {
		return intensity;
	}
	
	private boolean weaponWielded() {
		if (currentPlayer.getEquipmentInSlot(EquipmentSlot.LEFTHAND) == null && currentPlayer.getEquipmentInSlot(EquipmentSlot.RIGHTHAND) == null) {
			messageSelf("You are not wielding a weapon.");
			return false;
		}
		return true;
	}
	
	private boolean findAllLocations() {
		Location currentPlayerLocation = currentPlayer.getContainer();		
		allLocations.add(currentPlayerLocation);		
		String dir = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		if (!dir.equals("")) {
			Location nextLocation = currentPlayerLocation.getLocation(dir);
			if (nextLocation == null) {
				messageSelf("There isn't a location that way.");
				return false;
			} else if (isDirectionBlocked(currentPlayerLocation, Direction.getDirectionName(dir))) {
				return false;
			} else {
				allLocations.add(nextLocation);
				getNextLocation(dir, nextLocation);
			}
		}
		return true;
	}
	
	private void getNextLocation(String dir, Location currentLocation) {
		Location anotherLocation = currentLocation.getLocation(dir);
		if (!isDirectionBlocked(currentLocation, Direction.getDirectionName(dir))) {		
			if (anotherLocation != null && !allLocations.contains(anotherLocation)) {
				allLocations.add(anotherLocation);
				getNextLocation(dir, anotherLocation);
			}
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
			Mobile t = l.getMobileFromString(possibleTarg);
			if (t != null) {
				return t;
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

	@Override
	protected void inductionEnded() {
		// TODO Auto-generated method stub
		// Nothing extra, run handles
	}

	@Override
	protected boolean preSkillChecks() {
		allLocations = new HashSet<Location>();
		if (!hasBalance()) {return false;}
		if (!weaponWielded()) {return false;}
		if (!findAllLocations()) {return false;}		
		if (!findTarget()) {return false;}	
		return true;
	}
	
	@Override
	public String displaySyntax() {
		return "HEADSHOT [TARGET] (DIR)";
	}
}
