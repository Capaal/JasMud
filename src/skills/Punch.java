package skills;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.InformsAggro;
import interfaces.Mobile;
import processes.Skills;

public class Punch extends Skills implements InformsAggro {
	
	private final int intensity = 10;
	private String targetName;
	private Mobile finalTarget;
	private final int BASEBALANCE = 3000;
	
	public Punch(Mobile currentPlayer, String fullCommand) {
		super("punch", "Throwing a quick punch.", currentPlayer, fullCommand);
		syntaxList.add(Syntax.SKILL);
		syntaxList.add(Syntax.TARGET);
	}	
	
	// Deals damage to a single target in currentPlayer's location
	@Override
	protected void performSkill() {
		informLastAggressor(currentPlayer, finalTarget);
		finalTarget.takeDamage(calculateDamage());
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, calculateBalance());
		messageSelf("You punch " + finalTarget.getName());
		messageTarget(currentPlayer.getNameColored() + " punches you.", Arrays.asList(finalTarget));
		messageOthers(currentPlayer.getNameColored() + " punches " + finalTarget.getNameColored(), Arrays.asList(currentPlayer, finalTarget));
		
	}

	private int calculateDamage() {
		return intensity;
	}
	
	private boolean brokenArms() {
		if (currentPlayer.hasCondition(PassiveCondition.BROKENLEFTARM) && currentPlayer.hasCondition(PassiveCondition.BROKENRIGHTARM)) {
			messageSelf("Your arms are broken!");
			return true;
		} 
		return false;
	}
	
	private int calculateBalance() {
		return (int) (BASEBALANCE * currentPlayer.getBalanceMult());
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
		targetName = Syntax.TARGET.getStringInfo(fullCommand, this);
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
}
