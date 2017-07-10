package skills;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.InformsAggro;
import interfaces.Mobile;
import processes.Skills;

public class VineTrip extends Skills implements InformsAggro {
	
	private Mobile finalTarget;
	private final int BASEBALANCE = 2000;
	
	public VineTrip(Mobile currentPlayer, String fullCommand) {
		super("vinetrip", "Unbalance your target briefly.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}	

	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, calculateBalance());
			finalTarget.addPassiveCondition(PassiveCondition.BALANCE, 1700);
			finalTarget.killInduction();
			messageSelf("You encourage a vine to get in the way.");
			messageTarget("A vine shoots out by your feet and trips you.", Arrays.asList(finalTarget));
			messageOthers("A vine trips " + finalTarget.getNameColored() + ".", Arrays.asList(currentPlayer, finalTarget));
			informLastAggressor();
		}
	}
	
	private boolean setTarget() {
		String targetName = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (targetName.equals("")) {
			messageSelf("Specify target.");
			return false;
		}
		finalTarget = currentPlayer.getContainer().getMobileFromString(targetName);
		if (finalTarget != null) {
			return true;
		} else {
			messageSelf("There is no " + targetName + " here.");
			return false;
		}
	}
	
	private int calculateBalance() {
		return (int) (BASEBALANCE * getPlayerBalanceMult());
	}

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		if (!setTarget()) {return false;}
		return true;
	}
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new VineTrip(currentPlayer, fullCommand);
	}
	
	@Override
	public void informLastAggressor() {
		finalTarget.informLastAggressor(currentPlayer);
	}
}
