package skills.Mercenary;

import interfaces.Cooldown;
import interfaces.Mobile;
import effects.PassiveCondition;

import java.util.concurrent.TimeUnit;

import effects.Fear;
import processes.InductionSkill;
import processes.Skills;
import processes.WorldServer;
import skills.Sleep;

public class Clearmind extends Skills implements Cooldown {
	
	private final int COOLDOWNLENGTH = 10000;
	
	public Clearmind(Mobile currentPlayer, String fullCommand) {
		super("clearmind", "Cures mental afflictions", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
	}	
	
	
	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			if (currentPlayer.hasCondition(PassiveCondition.DIZZY)) {
				currentPlayer.removeCondition(PassiveCondition.DIZZY);
				messageSelf("Dizzy cured.");
				currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 1000);
				addCooldown(currentPlayer);
				WorldServer.getGameState().getEffectExecutor().schedule(() -> setOffCooldown(), COOLDOWNLENGTH, TimeUnit.MILLISECONDS);	
		//		offCooldownIn(length);
		//		triggerCooldown(5000);
			} else if (currentPlayer.hasCondition(new Fear(currentPlayer))) {
				currentPlayer.removeCondition(new Fear(currentPlayer));
				messageSelf("Fear cured.");
				currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 1000);
				addCooldown(currentPlayer);
				WorldServer.getGameState().getEffectExecutor().schedule(() -> setOffCooldown(), COOLDOWNLENGTH, TimeUnit.MILLISECONDS);		
		//		offCooldownIn(length);
		//		triggerCooldown(5000);
			} else {
				messageSelf("Your mind is already clear.");
			}
		}
	}
	
	
	private void setOffCooldown() { 
	//	super.setOffCooldown();
		removeCooldown(currentPlayer);
		messageSelf("You are again able to clear your mind.");
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
