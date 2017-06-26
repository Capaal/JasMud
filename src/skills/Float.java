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
		if (preSkillChecks()) {
			if (currentPlayer.hasCondition(new Levitate(currentPlayer))) {
				messageSelf("You are already levitating.");
				return;
			}
	
			currentPlayer.addActiveCondition(new Levitate(currentPlayer), 4);
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
		//	messageSelf("You float up a foot above the ground."); //done in levitate effect
			messageOthers(currentPlayer.getNameColored() + "floats into the air.", Arrays.asList(currentPlayer));
		}
	}

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		return true;
	}
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Float(currentPlayer, fullCommand);
	}
	
}

