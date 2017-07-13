package skills.Mercenary;

import interfaces.Mobile;
import effects.Bleed;
import effects.PassiveCondition;
import processes.Skills;

public class Staunch extends Skills { 
	
	//check for: limb already broken, is item wielded, unwield item
	public Staunch(Mobile currentPlayer, String fullCommand) {
		super("staunch", "Cures bleeding, a little at a time.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
	}	
	
	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
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

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		return true;
	}
}
