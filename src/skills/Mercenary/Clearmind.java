package skills.Mercenary;

import interfaces.Cooldown;
import interfaces.Mobile;
import effects.PassiveCondition;

import java.util.concurrent.TimeUnit;

import effects.Fear;
import processes.Skills;
import processes.WorldServer;

public class Clearmind extends Skills implements Cooldown {
	
	private final int COOLDOWNLENGTH = 5000;
	
	public Clearmind(Mobile currentPlayer, String fullCommand) {
		super("clearmind", "Cures mental afflictions", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
	}	
	
	
	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			//can do switch/case here? TODO
			if (currentPlayer.hasCondition(PassiveCondition.DIZZY)) {
				currentPlayer.removeCondition(PassiveCondition.DIZZY);
				messageSelf("Dizzy cured.");
				cureCooldown();
			} else if (currentPlayer.hasCondition(new Fear(currentPlayer))) {
				currentPlayer.removeCondition(new Fear(currentPlayer));
				messageSelf("Fear cured.");
				cureCooldown();
			} else if (currentPlayer.hasCondition(PassiveCondition.CONFUSED)) {
				currentPlayer.removeCondition(PassiveCondition.CONFUSED);
				messageSelf("Confusion cured.");
				cureCooldown();
			} else {
				messageSelf("Your mind is already clear.");
			}
		}
	}
	
	//can do this like Straighten instead? TODO
	private void cureCooldown() {
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 1000);
		addCooldown(currentPlayer);
		WorldServer.getGameState().getEffectExecutor().schedule(() -> setOffCooldown(), COOLDOWNLENGTH, TimeUnit.MILLISECONDS);	
	}
	
	
	private void setOffCooldown() { 
		messageSelf("You are again able to clear your mind.");
		removeCooldown(currentPlayer);
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
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Clearmind(currentPlayer, fullCommand);
	}


	@Override
	public boolean isOnCooldown(Mobile currentPlayer) {
		return currentPlayer.isOnCooldown(this);
	}


	@Override
	public void addCooldown(Mobile currentPlayer) {
		currentPlayer.addCooldown(this);
	}


	@Override
	public void removeCooldown(Mobile currentPlayer) {
		currentPlayer.removeCooldown(this);
	}
}
