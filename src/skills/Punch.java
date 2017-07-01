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
	private final int BASEBALANCE = 3000;
	
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
			System.out.println(calculateBalance());
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, calculateBalance());
			messageSelf("You punch " + finalTarget.getName());
			messageTarget(currentPlayer.getNameColored() + " punches you.", Arrays.asList(finalTarget));
			messageOthers(currentPlayer.getNameColored() + " punches " + finalTarget.getNameColored(), Arrays.asList(currentPlayer, finalTarget));
		}
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
		return (int) (BASEBALANCE * getPlayerBalanceMult());
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
