package skills.Mercenary;

import effects.PassiveCondition;
import effects.Fear;
import processes.InductionSkill;

public class Clearmind extends InductionSkill {
	
	public Clearmind() {
		super.name = "clearmind";
		super.description = "Cures mental afflictions.";
		super.syntaxList.add(Syntax.SKILL);
	}	
	
	@Override
	protected void performSkill() {
		if (!offCooldown) {
			messageSelf("You must wait a moment to clear your mind again.");
			return;
		}
		if (!hasBalance()) {return;}
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
	public void run() {
		throw new IllegalStateException("Does not have a ticking effect.");
	}
}
