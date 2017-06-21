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
	
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Diagnose(currentPlayer, fullCommand);
	}
}
