package skills;

import java.util.Arrays;

import effects.Balance;
import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Location;
import processes.Skills;
import processes.Type;
import processes.WorldServer;
import processes.Skills.Syntax;

public class Throw extends Skills {
	
	private Boolean failed;
	private int intensity = 8;
	
	public Throw() {
		super.id = 2;
		super.name = "throw";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.DIRECTION);
	}
	
	// Deals damage to a single target in currentPlayer's location
	@Override
	public void perform(String fullCommand, Mobile currentPlayer) {
		if (!hasBalance(currentPlayer)) {
			return;
		}
		Holdable itemToThrow = currentPlayer.getHoldableFromString(this.getStringInfo(Syntax.ITEM, fullCommand));
		if (itemToThrow == null) {
			messageSelf("You do not have a \"" + this.getStringInfo(Syntax.ITEM, fullCommand) + "\" .", currentPlayer);
			return;
		}
		Mobile finalTarget = getTarget(fullCommand, currentPlayer);
		if (finalTarget == null) {
			messageSelf("You can't find a \"" + this.getStringInfo(Syntax.TARGET, fullCommand) + "\" to attack.", currentPlayer);
			return;
		}	
		if (isBlocking(finalTarget)) {  // Probably not complete still
			return;
		}		
		Location targetLoc = (Location)(finalTarget.getContainer());
		Location finalLoc = (Location)(currentPlayer.getContainer());
		String dir = this.getStringInfo(Syntax.DIRECTION, fullCommand);
		if (!dir.equals("")) {
			finalLoc = getLoc(dir, fullCommand, currentPlayer);
		} 
		if (targetLoc != finalLoc) {
			messageSelf("You can't find a \"" + this.getStringInfo(Syntax.TARGET, fullCommand) + "\" to attack.", currentPlayer);
			return;
		}
		finalTarget.takeDamage(Type.SHARP, calculateDamage());
		moveItem(itemToThrow, finalLoc);
		currentPlayer.addEffect(new Balance(), 3000);
		messageSelf("You throw " + itemToThrow.getName() + " at " + finalTarget.getName() + ".", currentPlayer);
		messageTarget(currentPlayer.getName() + " throws " + itemToThrow.getName() + " at you.", Arrays.asList(finalTarget));
		messageOthers(currentPlayer.getName() + " throws " + itemToThrow.getName() + " at " + finalTarget.getName(), currentPlayer, Arrays.asList(currentPlayer, finalTarget));
	}		
		

	
	private int calculateDamage() {
		return intensity;
	}
	
	private void moveItem(Holdable itemToThrow, Location finalLoc) {
		itemToThrow.getContainer().removeItemFromLocation(itemToThrow);
		finalLoc.acceptItem(itemToThrow);
	}
	
	private Mobile getTarget(String fullCommand, Mobile currentPlayer) {
		Holdable h = currentPlayer.getContainer().getHoldableFromString(this.getStringInfo(Syntax.TARGET, fullCommand));
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
