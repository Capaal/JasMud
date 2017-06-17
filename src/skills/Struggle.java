package skills;

import java.util.Arrays;
import java.util.Random;

import effects.PassiveCondition;
import processes.InductionSkill;

//interruptible, repeating induction skill
public class Struggle extends InductionSkill {
	
	private Random r = new Random();
	
	public Struggle() {
		super.name = "struggle";
		super.description = "Struggling out of bonds.";
		super.syntaxList.add(Syntax.SKILL);
	}
	
	public class InnerStruggle extends InnerSkill {
		
		@Override
		public void performSkill() {
			int n = r.nextInt(5);
			if (n > 2) {  //20% change success
				currentPlayer.removeAllConditions(PassiveCondition.ROOT);
				messageSelf("You are free to move about again.");
				messageOthers(currentPlayer.getName() + " is no longer bound.", Arrays.asList(currentPlayer));
				endSuccessfully();
			} else {
				messageSelf("You continue to writhe against the bonds holding you.");
			}	
		}
	}
	
	@Override
	public InnerSkill getInnerSkill() {
		return new InnerStruggle();
	}

	@Override
	protected void performSkill() {		
		if (currentPlayer.hasAllConditions(PassiveCondition.ROOT)) {
			messageSelf("You being struggling against the bonds holding you.");
			scheduleInduction(50, 2500);
			currentPlayer.setInduction(this);
		} else {
			messageSelf("You struggle against the futility of the world.");
		}
		
	}

	@Override
	public void inductionKilled() {
		messageSelf("You stop struggling.");		
	}

	@Override
	protected void inductionEnded() {
		// TODO Auto-generated method stub		
	}

	@Override
	protected boolean preSkillChecks() {
		return true;
	}

}
