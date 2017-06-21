package skills;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;
import processes.Type;

public class Punch extends Skills {
	
	private final int intensity = 10;
	private String targetName;
	private Mobile finalTarget;
	
	public Punch(Mobile currentPlayer, String fullCommand) {
		super("punch", "Throwing a quick punch.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}	
	
	// Deals damage to a single target in currentPlayer's location
	// ADD CHECK FOR ISDEAD()
	@Override
	protected void performSkill() {
		targetName = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (preSkillChecks()) {
			finalTarget.informLastAggressor(currentPlayer);
			finalTarget.takeDamage(Type.BLUNT, calculateDamage());
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
			messageSelf("You punch " + finalTarget.getName());
			messageTarget(currentPlayer.getName() + " punches you.", Arrays.asList(finalTarget));
			messageOthers(currentPlayer.getName() + " punches " + finalTarget.getName(), Arrays.asList(currentPlayer, finalTarget));
		}
	}

	private int calculateDamage() {
		return intensity;
	}
	
	private boolean brokenArms() {
		if (currentPlayer.hasAllConditions(PassiveCondition.BROKENLEFTARM) && currentPlayer.hasAllConditions(PassiveCondition.BROKENRIGHTARM)) {
			messageSelf("Your arms are broken!");
			return true;
		} 
		return false;
	}
	
	private Mobile setTarget() {
		Mobile h = currentPlayer.getContainer().getMobileFromString(targetName);
		if (h != null) {
			return h;
		}			
		return null;
	}

	@Override
	protected boolean preSkillChecks() {
		if (targetName.equals("")) {
			messageSelf("Specify target.");
			return false;
		}
		if (!hasBalance()) {return false;}
		if (brokenArms()) {return false;}
		finalTarget = setTarget();
		if (finalTarget == null) {
			messageSelf("Invalid target.");
			return false;
		}
		if (isBlocking(finalTarget)) {  // Probably not complete still
			return false;
		}
		return true;
	}
	
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Punch(currentPlayer, fullCommand);
	}
}
