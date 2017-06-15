package skills;

import java.util.Arrays;
import java.util.Random;

import effects.Fear;
import effects.PassiveCondition;
import processes.InductionSkill;
import processes.Skills;
import processes.Skills.Syntax;

//interruptible, repeating induction skill
public class Struggle extends InductionSkill {
	
	Random r = new Random();
	boolean emote = true;
	
	public Struggle() {
		super.name = "struggle";
		super.description = "Struggling out of bonds.";
		super.syntaxList.add(Syntax.SKILL);
	}

	@Override
	protected void performSkill() {
		
		if (currentPlayer.hasAllConditions(PassiveCondition.ROOT)) {
			emote = false;
			messageSelf("You being struggling against the bonds holding you.");
			scheduleInduction(1, 2000);
			currentPlayer.setInduction(this);
		}
		
		if (emote) {
			messageSelf("You struggle against the futility of the world.");
		}
		
	}

	@Override
	public void run() {
		int n = r.nextInt(5);
		if (n > 2) {  //20% change success
			currentPlayer.removeAllConditions(PassiveCondition.ROOT);
			messageSelf("You are free to move about again.");
			messageOthers(currentPlayer.getName() + " is no longer bound.", Arrays.asList(currentPlayer));
			return;
		}
		messageSelf("You continue to writhe against the bonds holding you.");
		scheduleInduction(1, 2000);  //effectively recursion since this calls run()
		currentPlayer.setInduction(this);
	}

	@Override
	public void inductionKilled() {
		messageSelf("You stop struggling.");
		
	}

}
