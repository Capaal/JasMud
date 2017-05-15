package skills;

import java.util.Arrays;

import effects.Levitate;
import effects.PassiveCondition;
import interfaces.Mobile;
import processes.Skills;
import processes.Skills.Syntax;

public class VineTrip extends Skills{
	
	private Mobile finalTarget;
	
	public VineTrip() {
		super.name = "vinetrip";
		super.description = "Unbalance your target briefly.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}	

	@Override
	protected void performSkill() {
		if (!hasBalance()) {return;}
		if (!setTarget()) {return;}
		
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 2000);
		finalTarget.addPassiveCondition(PassiveCondition.BALANCE, 1700);
		messageSelf("You encourage a vine to get in the way.");
		messageTarget("A vine shoots out by your feet and trips you.", Arrays.asList(finalTarget));
		messageOthers("A vine trips " + finalTarget + ".", Arrays.asList(currentPlayer, finalTarget));
		
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

}
