package skills;

import java.util.Arrays;
import java.util.Random;

import effects.PassiveCondition;
import interfaces.Mobile;
import processes.InductionSkill;
import processes.Skills.Syntax;


public class Wake extends InductionSkill {
	
	Random r = new Random();
	
	public Wake() {
		super.name = "wake";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}

	@Override
	protected void performSkill() {
		String target = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (target.equals("")) {
			if (currentPlayer.hasAllConditions(PassiveCondition.SLEEP)) {
				messageSelf("You start trying to wake yourself up.");
				scheduleInduction(50, 100000);
				currentPlayer.setInduction(this);
			} else {
				currentPlayer.addAllConditions(PassiveCondition.SLEEP);
				messageSelf("You're not asleep.");
			}
		} else {
			Mobile mob = currentPlayer.getContainer().getMobileFromString(target);
			if (mob == null) {
				messageSelf("You don't see a \"" + target + "\".");
				return;
			} 
			//test if rooted, paralyzed?
			if (mob.hasAllConditions(PassiveCondition.SLEEP)) {
				mob.removeAllConditions(PassiveCondition.SLEEP);
				messageSelf("You shake " + mob.getName() + " awake.");
				messageTarget(currentPlayer.getName() + " wakes you up.", Arrays.asList(mob));
				messageOthers(currentPlayer.getName() + " shakes " + mob.getName() + " awake.", Arrays.asList(currentPlayer, mob));
			}
		}
		

	}
	
	@Override
	public void run() {
		int n = r.nextInt(5);
		if (n > 2) {  //20% change success
			currentPlayer.removeAllConditions(PassiveCondition.SLEEP);
			messageSelf("You wake up.");
			messageOthers(currentPlayer.getName() + " wakes up.", Arrays.asList(currentPlayer));
			wrapper.endSuccess();
			return;
		}
		messageSelf("You continue trying to wake up.");
	}

	@Override
	public void inductionKilled() {
		messageSelf("You settle back into sleep.");
		
	}

	@Override
	protected void inductionEnded() {
		// TODO Auto-generated method stub
		
	}

}