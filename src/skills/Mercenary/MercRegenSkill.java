package skills.Mercenary;

import java.util.Arrays;

import effects.MercRegen;
import effects.PassiveCondition;
import interfaces.Mobile;
import processes.Skills;
import skills.Sleep;

public class MercRegenSkill extends Skills {
	
	public MercRegenSkill(Mobile currentPlayer, String fullCommand) {
		super("regen", "Resisting damage.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
	}

	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			currentPlayer.addActiveCondition(new MercRegen(currentPlayer, -5), 5);
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
			messageSelf("You gain regeneration.");
		}
	}

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		return true;
	}

	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new MercRegenSkill(currentPlayer, fullCommand);
	}
}

