package skills;

import interfaces.Mobile;

import java.util.Arrays;

import effects.Levitate;
import effects.PassiveCondition;
import processes.Skills;

public class Float extends Skills {
	
	public Float(Mobile currentPlayer, String fullCommand) {
		super("float", "Floating above the ground.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);	
	}

	@Override
	protected void performSkill() {
		if (currentPlayer.hasCondition(new Levitate(currentPlayer))) { // could let stackedInstance() handle this, either resetting time or whatever.
			messageSelf("You are already levitating.");
			return;
		}
		currentPlayer.addActiveCondition(new Levitate(currentPlayer), 4);
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
		messageOthers(currentPlayer.getNameColored() + "floats into the air.", Arrays.asList(currentPlayer));
	}

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		return true;
	}
}

