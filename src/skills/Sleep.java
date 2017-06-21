package skills;

import interfaces.Mobile;
import effects.PassiveCondition;
import processes.Skills;
import processes.Skills.Syntax;

// Does sleep regen life, exhaustion, mana, etc? Periodically snore?
// Is sleep woken quickly or slowly like struggle? Does damage instantly wake?
// Display to others in 'look' that you are asleep?
public class Sleep extends Skills {
	
	public Sleep(Mobile currentPlayer, String fullCommand) {
		super("sleep", "Sleeping.", currentPlayer, fullCommand);
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

	@Override
	protected boolean preSkillChecks() {
		return true;
	}
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Sleep(currentPlayer, fullCommand);
	}
}
