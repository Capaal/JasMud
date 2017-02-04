package skills;

import java.util.Arrays;
import effects.Balance;
import interfaces.Holdable;
import interfaces.Mobile;
import interfaces.Weapon;
import processes.Location;
import processes.Skills;
import processes.Type;

public class Throw extends Skills {
	
	private int intensity = 8;
	private Holdable itemToThrow;
	private Mobile finalTarget;
	private Location finalLoc;
	private String possibleItem;
	
	public Throw() {
		super.name = "throw";
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
		currentPlayer.addEffect(new Balance(), calculateBalance());
		messageSelf("You throw " + itemToThrow.getName() + " at " + finalTarget.getName() + ".");
		messageTarget(currentPlayer.getName() + " throws " + itemToThrow.getName() + " at you.", Arrays.asList(finalTarget));
		messageOthers(currentPlayer.getName() + " throws " + itemToThrow.getName() + " at " + finalTarget.getName(), Arrays.asList(currentPlayer, finalTarget));
	}	
	
	private boolean preSkillChecks() {
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
		if (!(itemToThrow instanceof Weapon)) {
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
		return (int) (3000 * ((Weapon)itemToThrow).getBalanceMult());
	}
	
	private int calculateDamage() {
		double damageMult = ((Weapon)itemToThrow).getBalanceMult();
		return (int) (damageMult * intensity);
	}
	
	private Mobile getTarget(String possibleTarg) {
		Holdable h = finalLoc.getHoldableFromString(possibleTarg);
		if (h != null && h instanceof Mobile) {
			return (Mobile)h;
		}
		return null;
	}
	
	private Location getLoc(String dir) {
		Location mobLocation = currentPlayer.getContainer();
		return mobLocation.getContainer(dir);
	}
}