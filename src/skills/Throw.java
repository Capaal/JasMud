package skills;

import java.util.Arrays;

import effects.Balance;
import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Location;
import processes.Skills;
import processes.Type;

public class Throw extends Skills {
	
	private int intensity = 8;
	
	public Throw() {
		super.name = "throw";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.DIRECTION);
	}
	
	// Deals throw damage to a single target in currentPlayer's location or designated direction
	@Override
	public void performSkill() {
		if (!hasBalance()) {
			return;
		}
		//checks if player specified an item to throw
		String possibleItem = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (possibleItem == "") {
			messageSelf("What are you trying to throw?");
			messageSelf("Syntax: throw <ITEM> <TARGET> (direction)");
			return;
		}
		//checks if player has the item
		Holdable itemToThrow = currentPlayer.getHoldableFromString(possibleItem);
		if (itemToThrow == null) {
			messageSelf("You do not have a \"" + possibleItem + "\".");
			return;
		}
		//checks for a direction. must be done before comparing if target is in the same location as intended direction. 
		//defaults to here
		Location finalLoc = (Location)(currentPlayer.getContainer());
		String dir = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		if (!dir.equals("")) {
			finalLoc = getLoc(dir, fullCommand, currentPlayer);
			if (finalLoc == null) {
				messageSelf("There isn't a location that way.");
				return;
			}
		} 
		//checks if a target is specified
		String possibleTarg = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (possibleTarg == "") {
			messageSelf("What are you trying to throw the " + possibleItem + " at?");
			messageSelf("Syntax: throw <ITEM> <TARGET> (direction)");
			return;
		}
		//checks if the target is in the same location as intended throw end location
		Mobile finalTarget = getTarget(finalLoc, fullCommand, possibleTarg);
		if (finalTarget == null) {
			messageSelf("There is no \"" + possibleTarg + "\" for you to attack.");
			return;
		}	
		if (isBlocking(finalTarget)) {  // Probably not complete still
			return;
		}		
		
		//what the skill actually does:
		finalTarget.takeDamage(Type.SHARP, calculateDamage(itemToThrow.getDamageMult()));
		moveItem(itemToThrow, finalLoc);
		currentPlayer.addEffect(new Balance(), 3000);
		messageSelf("You throw " + itemToThrow.getName() + " at " + finalTarget.getName() + ".");
		messageTarget(currentPlayer.getName() + " throws " + itemToThrow.getName() + " at you.", Arrays.asList(finalTarget));
		messageOthers(currentPlayer.getName() + " throws " + itemToThrow.getName() + " at " + finalTarget.getName(), Arrays.asList(currentPlayer, finalTarget));
	}		
		

	
	private int calculateDamage(double dmgMult) {
		return (int) (dmgMult*intensity);
	}
	
	private void moveItem(Holdable itemToThrow, Location finalLoc) {
		itemToThrow.getContainer().removeItemFromLocation(itemToThrow);
		finalLoc.acceptItem(itemToThrow);
	}
	
	private Mobile getTarget(Location finalLoc, String fullCommand, String possibleTarg) {
		Holdable h = finalLoc.getHoldableFromString(possibleTarg);
			if (h != null && h instanceof Mobile) {
				return (Mobile)h;
			}
		return null;
	}
	
	private Location getLoc(String dir, String fullCommand, Mobile currentPlayer) {
		Container mobLocation = currentPlayer.getContainer();
		if (mobLocation instanceof Location) {
			return ((Location)mobLocation).getContainer(dir);
		}
		return null;
	}
}