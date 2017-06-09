package skills.Mercenary;

import java.util.Arrays;

import effects.Fear;
import effects.PassiveCondition;
import interfaces.Mobile;
import processes.Skills;

public class Intimidate extends Skills {
	
	private Mobile mobileToFear;
	private String targetName;
	
	public Intimidate() {
		super.name = "intimidate";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);	
	}

	@Override
	protected void performSkill() {
		if (!hasBalance()) {return;}
		if (!setTarget()) {return;}

		mobileToFear.addActiveCondition(new Fear(mobileToFear), 5);
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
		messageSelf("You fear someone.");
		messageTarget("Someone fears you.", Arrays.asList(mobileToFear));
	}

	
	private boolean setTarget() {
		targetName = Syntax.TARGET.getStringInfo(fullCommand, this);
		mobileToFear = currentPlayer.getContainer().getMobileFromString(targetName);
		if (mobileToFear == null) {
			messageSelf("There is no \"" + targetName + "\" here for you to intimidate.");	
			return false;
		}
		return true;
	}
}

