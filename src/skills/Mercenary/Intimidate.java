package skills.Mercenary;

import java.util.Arrays;

import effects.Fear;
import effects.PassiveCondition;
import interfaces.Mobile;
import processes.Skills;

public class Intimidate extends Skills {
	
	private Mobile mobileToFear;
	private String targetName;
	
	public Intimidate(Mobile currentPlayer, String fullCommand) {
		super("intimidate", "Scaring others into fleeing.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);	
	}

	@Override
	protected void performSkill() {
		mobileToFear.addActiveCondition(new Fear(mobileToFear), 20); // Adds fear, ticks 20 times.
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 2000);
		messageTarget(currentPlayer.getNameColored() + " fears you.", Arrays.asList(mobileToFear));
		messageSelf("You fear " + mobileToFear.getNameColored());
		messageOthers(mobileToFear.getNameColored() + " suddenly looks around with a panicked expression.", Arrays.asList(mobileToFear,currentPlayer));	
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

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		if (!setTarget()) {return false;}
		return true;
	}
}

