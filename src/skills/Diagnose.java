package skills;

import java.util.Set;

import effects.PassiveCondition;
import interfaces.Mobile;
import interfaces.TickingEffect;
import processes.Skills;

public class Diagnose extends Skills {
	
	public Diagnose(Mobile currentPlayer, String fullCommand) {
		super("diagnose", "Detect Diseases.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
	}	

	@Override
	protected void performSkill() {
		Set<PassiveCondition> passiveC = currentPlayer.getAllPassiveEffects();
		for (PassiveCondition p : passiveC) {
			messageSelf(p.name().toLowerCase());
		}
		Set<TickingEffect> activeC = currentPlayer.getAllActiveConditions();
		for (TickingEffect t : activeC) {
			messageSelf(t.getClass().getSimpleName() + " "+ t.getInfo());
		}
		if (passiveC.isEmpty() && activeC.isEmpty()) {
			messageSelf("You have no conditions.");
		}
		
	}

	@Override
	protected boolean preSkillChecks() {
		return true;
	}
}
