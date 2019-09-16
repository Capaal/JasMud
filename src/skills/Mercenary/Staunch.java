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
		Bleed bleedChange = new Bleed(currentPlayer, -1);
		if (currentPlayer.hasCondition(bleedChange)) {
			currentPlayer.addActiveCondition(bleedChange, 1); // Times arbitrary, because bleed doesn't care.
			if (currentPlayer.hasCondition(bleedChange)) {
				messageSelf("You manage to stop some of your bleeding.");
			} else {
				messageSelf("Bleeding cured.");
			}
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 1000);
		} else {
			messageSelf("You're not bleeding.");
		}
	}

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		return true;
	}
}
