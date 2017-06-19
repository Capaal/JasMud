package skills;

import java.util.Set;

import effects.PassiveCondition;
import interfaces.TickingEffect;
import processes.Skills;

public class Diagnose extends Skills {
	
	public Diagnose() {
		super.name = "diagnose";
		super.description = "";
		super.syntaxList.add(Syntax.SKILL);
	}	

	@Override
	protected void performSkill() {
		Set<PassiveCondition> passiveC = currentPlayer.getAllConditions();
		for (PassiveCondition p : passiveC) {
			messageSelf(p.name().toLowerCase());
		}
		Set<TickingEffect> activeC = currentPlayer.getAllActiveConditions();
		for (TickingEffect t : activeC) {
			messageSelf(t.getClass().getSimpleName() + " "+ t.getInfo());
		}
		
	}

	@Override
	protected boolean preSkillChecks() {
		// TODO Auto-generated method stub
		return true;
	}

}
