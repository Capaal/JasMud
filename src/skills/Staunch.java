package skills;

import effects.Bleed;
import effects.Fear;
import effects.PassiveCondition;
import processes.Skills;
import processes.Skills.Syntax;

public class Staunch extends Skills { 
	
	//check for: limb already broken, is item wielded, unwield item
	public Staunch() {
		super.name = "staunch";
		super.description = "Cures bleeding, 1 \"stack\" at a time.";
		super.syntaxList.add(Syntax.SKILL);
	}	
	
	@Override
	protected void performSkill() {
		if (!hasBalance()) {return;}
		if (currentPlayer.hasCondition(new Bleed(currentPlayer, 1))) {
			currentPlayer.addActiveCondition(new Bleed(currentPlayer, -5), 10);
			if (currentPlayer.hasCondition(new Bleed(currentPlayer, 1))) {
				messageSelf("You manage to stop some of your bleeding.");
			} else {
				messageSelf("Bleeding cured.");
			}
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
		} else {
			messageSelf("You're not bleeding.");
		}

	}
}
