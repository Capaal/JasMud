package skills.Mercenary;

import interfaces.Cooldown;
import interfaces.Mobile;
import effects.PassiveCondition;

import effects.Fear;
import processes.Skills;

public class Clearmind extends Skills implements Cooldown {
	
	private final int COOLDOWNLENGTH = 5000;
	
	public Clearmind(Mobile currentPlayer, String fullCommand) {
		super("clearmind", "Cures mental afflictions", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
	}		
	
	@Override
	protected void performSkill() {
		//can do switch/case here? TODO
		if (currentPlayer.hasCondition(PassiveCondition.DIZZY)) {
			currentPlayer.removeCondition(PassiveCondition.DIZZY);
			messageSelf("Dizzy cured.");
			startCooldown();
		} else if (currentPlayer.hasCondition(new Fear(currentPlayer))) {
			currentPlayer.removeCondition(new Fear(currentPlayer));
			messageSelf("Fear cured.");
			startCooldown();
		} else if (currentPlayer.hasCondition(PassiveCondition.CONFUSED)) {
			currentPlayer.removeCondition(PassiveCondition.CONFUSED);
			messageSelf("Confusion cured.");
			startCooldown();
		} else {
			messageSelf("Your mind is already clear.");
		}		
	}
	
	//can do this like Straighten instead? TODO
	private void startCooldown() {
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 1000);
		addCooldown(currentPlayer, COOLDOWNLENGTH);
	}	
	
	@Override
	protected boolean preSkillChecks() {
		if (isOnCooldown(currentPlayer)) {
			messageSelf("You must wait a moment to clear your mind again.");
			return false;
		}
		if (!hasBalance()) {return false;}
		return true;
	}

	@Override
	public String coolDownRemovedMessages() {
		return "You are again able to clear your mind.";
	}
}
