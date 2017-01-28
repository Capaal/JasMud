package skills;

import java.util.Arrays;

import effects.Balance;
import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.InductionSkill;
import processes.Location;
import processes.Skills;
import processes.Type;

public class Headshot extends InductionSkill {
	
	private int intensity = (8*3);
	private String fullCommand;
	private Mobile currentPlayer;
	private Location finalLoc;
	private String possibleTarg;
	
	public Headshot() {
		super.name = "headshot";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.DIRECTION);
	}
	
	@Override
	public void run() {		
		Mobile finalTarget = getTarget(finalLoc, fullCommand, possibleTarg);
		if (finalTarget == null) {
			messageSelf("There is no \"" + possibleTarg + "\" for you to attack.", currentPlayer);
			return;
		}	
		if (isBlocking(finalTarget)) {  // Probably not complete still
			return;
		}		
		finalTarget.takeDamage(Type.SHARP, calculateDamage());
		currentPlayer.addEffect(new Balance(), 3000);
		messageSelf("You headshot " + finalTarget.getName() + ".", currentPlayer);
		messageTarget(currentPlayer.getName() + " headshots you.", Arrays.asList(finalTarget));
		messageOthers(currentPlayer.getName() + " headshots " + finalTarget.getName(), currentPlayer, Arrays.asList(currentPlayer, finalTarget));		
	}
	
	// Deals damage to a single target in currentPlayer's location or One Away
	// REQUIRES ranged weapon
	// Direction is OPTIONAL
	@Override
	public void perform(String fullCommand, Mobile currentPlayer) {
		super.perform(fullCommand, currentPlayer);
		this.fullCommand = fullCommand;
		this.currentPlayer = currentPlayer;
		if (!hasBalance(currentPlayer)) {
			return;
		}
		finalLoc = (Location)(currentPlayer.getContainer());
		String dir = this.getStringInfo(Syntax.DIRECTION, fullCommand);
		if (!dir.equals("")) {
			Location possibleLoc = getLoc(dir, fullCommand, currentPlayer);
			if (possibleLoc == null) {
				messageSelf("There isn't a location that way.", currentPlayer);
				return;
			} else {
				finalLoc = possibleLoc;
			}
		} 
		possibleTarg = getStringInfo(Syntax.TARGET, fullCommand);
		if (possibleTarg == "") {
			messageSelf("What are you trying to headshot?", currentPlayer);
			return;
		}
		Mobile finalTarget = getTarget(finalLoc, fullCommand, possibleTarg);
		if (finalTarget == null) {
			messageSelf("There is no \"" + possibleTarg + "\" for you to attack.", currentPlayer);
			return;
		}	
		if (isBlocking(finalTarget)) {  // Probably not complete still
			return;
		}	
		scheduleSkillRepeatNTimesOverXMilliseconds(1, 2000);
		currentPlayer.setInduction(this);
		messageSelf("You begin aiming at " + finalTarget.getName() + ".", currentPlayer);
		messageTarget(currentPlayer.getName() + " begins aiming at your head.", Arrays.asList(finalTarget));
	}		
		

	
	private int calculateDamage() {
		return intensity;
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

	@Override
	public void inductionKilled() {
		messageSelf("You stop aiming at " + possibleTarg, currentPlayer);
	}
}
