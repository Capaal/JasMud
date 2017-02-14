package skills;

import java.util.Arrays;

import effects.Balance;
import effects.Fear;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;
import processes.Skills.Syntax;

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

		mobileToFear.addTickingEffect(new Fear(mobileToFear), 10000, 4);
		messageSelf("You fear someone.");
		messageTarget("Someone fears you.", Arrays.asList(mobileToFear));
	}

	
	private boolean setTarget() {
		targetName = Syntax.TARGET.getStringInfo(fullCommand, this);
		mobileToFear = null;
		Holdable h = currentPlayer.getContainer().getHoldableFromString(targetName);
		if (h != null && h instanceof Mobile) {
			mobileToFear = (Mobile)h;
			return true;
		} else if (!(h instanceof Mobile)) {
			messageSelf("The " +  targetName + " does not find you intimidating.");
			return false;
		}
		if (mobileToFear == null) {
			messageSelf("There is no " + targetName + " here for you to intimidate.");
			return false;
		}
		return false;
	}
}

