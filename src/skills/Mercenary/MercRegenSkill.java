package skills.Mercenary;

import effects.MercRegen;
import effects.PassiveCondition;
import interfaces.Mobile;
import processes.Skills;

public class MercRegenSkill extends Skills {
	
	public MercRegenSkill(Mobile currentPlayer, String fullCommand) {
		super("regen", "Resisting damage.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
	}

	@Override
	protected void performSkill() {
		currentPlayer.addActiveCondition(new MercRegen(currentPlayer, -5), 5); // Heals for 5 percent every tick, and ticks 5 times. (25% total).
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
		messageSelf("You gain regeneration.");	
	}

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		return true;
	}
}

