package skills;

import java.util.Arrays;
import java.util.Random;

import effects.PassiveCondition;
import interfaces.Mobile;
import processes.InductionSkill;
import processes.Skills;


public class Wake extends InductionSkill {
	
	private Random r = new Random();
	
	public Wake(Mobile currentPlayer, String fullCommand) {
		super("wake", "Waking from sleep.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}
	
	public class InnerWake extends InnerSkill {		
		public InnerWake(Mobile currentPlayer, String fullCommand) {
			super(currentPlayer, fullCommand);
		}

		@Override
		public void performSkill() {
			int n = r.nextInt(5);
			if (n > 2) {  //20% change success
				currentPlayer.removeAllConditions(PassiveCondition.SLEEP);
				messageSelf("You wake up.");
				messageOthers(currentPlayer.getName() + " wakes up.", Arrays.asList(currentPlayer));
				endSuccessfully();
			} else {
				messageSelf("You continue trying to wake up.");
			}	
		}

		@Override
		public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
			return new InnerWake(currentPlayer, fullCommand);
		}
	}
	
	@Override
	public InnerSkill getInnerSkill(Mobile currentPlayer, String fullCommand) {
		return new InnerWake(currentPlayer, description);
	}

	@Override
	protected void performSkill() {
		String target = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (target.equals("")) {
			if (currentPlayer.hasAllConditions(PassiveCondition.SLEEP)) {
				messageSelf("You start trying to wake yourself up.");
				scheduleInduction(50, 2500);
				currentPlayer.setInduction(this);
			} else {
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
	public void inductionKilled() {
		messageSelf("You settle back into sleep.");		
	}

	@Override
	protected void inductionEnded() {
		// TODO Auto-generated method stub		
	}

	@Override
	protected boolean preSkillChecks() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Wake(currentPlayer, fullCommand);
	}
}