package skills;

import interfaces.Mobile;

import java.util.Random;

import effects.PassiveCondition;
import processes.InductionSkill;

//interruptible, repeating induction skill
public class Struggle extends InductionSkill {
	
	private Random r = new Random();
	
	public Struggle(Mobile currentPlayer, String fullCommand) {
		super("struggle", "Struggling out of bonds.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
	}
	
	public class InnerStruggle extends InnerSkill {
		
		public InnerStruggle(Mobile currentPlayer, String fullCommand) {
			super(currentPlayer, fullCommand);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void performSkill() {
			int n = r.nextInt(5);
			if (n > 2) {  //20% change success
				currentPlayer.removeCondition(PassiveCondition.ROOT);
			//	messageSelf("You are free to move about again."); // this is in passivecondition doOnDestruction instead
			//	messageOthers(currentPlayer.getNameColored() + " is no longer bound.", Arrays.asList(currentPlayer));
				endSuccessfully();
			} else {
				messageSelf("You continue to writhe against the bonds holding you.");
			}	
		}
	}

	@Override
	protected void performSkill() {		
		if (currentPlayer.hasCondition(PassiveCondition.ROOT)) {
			messageSelf("You being struggling against the bonds holding you.");
			scheduleInduction(new InnerStruggle(currentPlayer, fullCommand), 50, 2500);
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
