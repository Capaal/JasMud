package skills;

import java.util.Arrays;
import java.util.Random;

import effects.PassiveCondition;
import processes.InductionSkill;

// Does sleep regen life, exhaustion, mana, etc?
// Is sleep woken quickly or slowly like struggle? Does damage instantly wake?
public class Wake extends InductionSkill {
	
	Random r = new Random();
	
	public Wake() {
		super.name = "wake";
		super.syntaxList.add(Syntax.SKILL);
	}

	@Override
	protected void performSkill() {
		if (currentPlayer.hasAllConditions(PassiveCondition.SLEEP)) {
			messageSelf("You start trying to wake yourself up.");
			scheduleSkillRepeatNTimesOverXMilliseconds(1, 2000);
			currentPlayer.setInduction(this);
		} else {
			currentPlayer.addAllConditions(PassiveCondition.SLEEP);
			messageSelf("You're not asleep.");
		}
		
	}
	
	@Override
	public void run() {
		int n = r.nextInt(5);
		if (n > 2) {  //20% change success
			currentPlayer.removeAllConditions(PassiveCondition.SLEEP);
			messageSelf("You wake up.");
			messageOthers(currentPlayer.getName() + " wakes up.", Arrays.asList(currentPlayer));
			return;
		}
		messageSelf("You continue trying to wake up.");
		scheduleSkillRepeatNTimesOverXMilliseconds(1, 2000);  //effectively recursion since this calls run()
		currentPlayer.setInduction(this);
	}

	@Override
	public void inductionKilled() {
		messageSelf("You settle back into sleep.");
		
	}

}