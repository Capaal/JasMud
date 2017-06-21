package skills.Mercenary;

import interfaces.Mobile;
import effects.PassiveCondition;
import effects.Fear;
import processes.InductionSkill;
import processes.Skills;
import skills.Sleep;

public class Clearmind extends InductionSkill {
	
	public Clearmind(Mobile currentPlayer, String fullCommand) {
		super("clearmind", "Cures mental afflictions", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
	}	
	
	
	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			if (currentPlayer.hasAllConditions(PassiveCondition.DIZZY)) {
				currentPlayer.removeAllConditions(PassiveCondition.DIZZY);
				messageSelf("Dizzy cured.");
				currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 1000);
				triggerCooldown(5000);
			} else if (currentPlayer.hasCondition(new Fear(currentPlayer))) {
				currentPlayer.removeCondition(new Fear(currentPlayer));
				messageSelf("Fear cured.");
				currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 1000);
				triggerCooldown(5000);
			} else {
				messageSelf("Your mind is already clear.");
			}
		}
	}
	
	@Override protected void setOffCooldown() { 
		super.setOffCooldown();
		messageSelf("You are again able to clear your mind.");
	}
	
	@Override
	public void inductionKilled() {
		throw new IllegalStateException("Cooldown skills should not have induction called.");		
	}

	@Override
	protected void inductionEnded() {
		throw new IllegalStateException("Cooldown skills should not have induction called.");	
	}



	@Override
	public InnerSkill getInnerSkill(Mobile currentPlayer, String fullCommand) {
		return null;
	}



	@Override
	protected boolean preSkillChecks() {
		if (!offCooldown) {
			messageSelf("You must wait a moment to clear your mind again.");
			return false;
		}
		if (!hasBalance()) {return false;}
		return true;
	}
	
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Clearmind(currentPlayer, fullCommand);
	}
}
