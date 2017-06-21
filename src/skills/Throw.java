package skills;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.Holdable;
import interfaces.Mobile;
import items.StdItem;
import processes.Location;
import processes.Skills;
import processes.Type;
import processes.Location.Direction;

public class Throw extends Skills {
	
	private final int BASEDAMAGE = 8;
	private final int BASEBALANCE = 3000;
	private Holdable itemToThrow;
	private Mobile finalTarget;
	private Location finalLoc;
	private String possibleItem;
	
	public Throw(Mobile currentPlayer, String fullCommand) {
		super("throw", "Throwing items.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.DIRECTION);
	}
	
	// Deals throw damage to a single target in currentPlayer's location or designated direction
	@Override
	protected void performSkill() {
		if (!preSkillChecks()) {return;};		
		finalTarget.takeDamage(Type.SHARP, calculateDamage());
		itemToThrow.moveHoldable(finalLoc);
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, calculateBalance());
		messageSelf("You throw " + itemToThrow.getName() + " at " + finalTarget.getName() + ".");
		messageTarget(currentPlayer.getName() + " throws " + itemToThrow.getName() + " at you.", Arrays.asList(finalTarget));
		messageOthers(currentPlayer.getName() + " throws " + itemToThrow.getName() + " at " + finalTarget.getName(), Arrays.asList(currentPlayer, finalTarget));
		messageOthersAway(finalTarget.getName() + "is hit with a thrown " +  itemToThrow.getName() + ".", Arrays.asList(finalTarget), finalLoc);
	}	
	
	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		if (!findThrownItem()) {return false;}				
		if (!findLocation()) {return false;}
		if (!findTarget()) {return false;}
		return true;			
	}
		
	private boolean findThrownItem() {
		possibleItem = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (possibleItem == "") {
			messageSelf("What are you trying to throw?");
			messageSelf("Syntax: throw <ITEM> <TARGET> (direction)");
			return false;
		}
		itemToThrow = currentPlayer.getHoldableFromString(possibleItem);
		if (itemToThrow == null) {
			messageSelf("You do not have a \"" + possibleItem + "\".");
			return false;
		}
		if (!(itemToThrow instanceof StdItem)) {
			messageSelf("You cannot throw " + possibleItem + ".");
			return false;
		}
		return true;
	}
	
	//defaults to here, but if the OPTIONAL DIRECTION is given, then it MUST be valid.
	private boolean findLocation() {
		finalLoc = currentPlayer.getContainer();
		String dir = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		if (!dir.equals("")) {
			finalLoc = getLoc(dir);			
			if (finalLoc == null) {
				messageSelf("There isn't a location that way.");
				return false;
			}
			if (isDoorBlocking(finalLoc, Direction.getDirectionName(dir))) {
				messageSelf("There is a door blocking your way.");
				return false;
			}
		} 
		return true;
	}
	
	private boolean findTarget() {
		String possibleTarg = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (possibleTarg == "") {
			messageSelf("What are you trying to throw the " + possibleItem + " at?");
			messageSelf("Syntax: throw <ITEM> <TARGET> (direction)");
			return false;
		}
		//checks if the target is in the same location as intended throw end location
		finalTarget = getTarget(possibleTarg);
		if (finalTarget == null) {
			messageSelf("There is no \"" + possibleTarg + "\" for you to attack.");
			return false;
		}	
		if (isBlocking(finalTarget)) {  // Probably not complete still
			return false;
		}
		return true;
	}
	
	private int calculateBalance() {
		return (int) (BASEBALANCE * ((StdItem)itemToThrow).getBalanceMult());
	}
	
	private int calculateDamage() {
		double damageMult = ((StdItem)itemToThrow).getDamageMult(); 
		return (int) (damageMult * BASEDAMAGE);
	}
	
	private Mobile getTarget(String possibleTarg) {
		Mobile h = finalLoc.getMobileFromString(possibleTarg);
		if (h != null && h instanceof Mobile) {
			return (Mobile)h;
		}
		return null;
	}
	
	private Location getLoc(String dir) {
		Location mobLocation = currentPlayer.getContainer();
		return mobLocation.getContainer(dir);
	}
	
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Throw(currentPlayer, fullCommand);
	}
}