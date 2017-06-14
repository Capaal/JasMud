package skills;

import effects.PassiveCondition;
import processes.Skills;
import processes.Skills.Syntax;

// Does sleep regen life, exhaustion, mana, etc?
// Is sleep woken quickly or slowly like struggle? Does damage instantly wake?
public class Sleep extends Skills {
	
	public Sleep() {
		super.name = "sleep";
		super.syntaxList.add(Syntax.SKILL);
	}

	@Override
	protected void performSkill() {
		if (currentPlayer.hasAllConditions(PassiveCondition.SLEEP)) {
			messageSelf("You are already asleep.");
		} else {
			currentPlayer.addAllConditions(PassiveCondition.SLEEP);
			messageSelf("You fall asleep.");
		}
		
	}

}