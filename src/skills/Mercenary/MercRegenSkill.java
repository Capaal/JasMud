package skills.Mercenary;

import java.util.Arrays;

import effects.MercRegen;
import effects.PassiveCondition;
import interfaces.Mobile;
import processes.Skills;

public class MercRegenSkill extends Skills {
	
	public MercRegenSkill() {
		super.name = "regen";
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


}

