package skills;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.Mobile;
import processes.Skills;
import processes.Type;

public class Heal extends Skills {
	
	private final int intensity = -30;
	private Mobile finalTarget;
	
	public Heal(Mobile currentPlayer, String fullCommand) {
		super("heal", "A little bit of healing.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}	
	
	// Deals damage to a single target in currentPlayer's location
	@Override
	protected void performSkill() {
		String targetName = Syntax.TARGET.getStringInfo(fullCommand, this).toLowerCase();
		if (!hasBalance()) {return;}
		finalTarget = setTarget(targetName);
		if (preSkillChecks()) {
			finalTarget.takeDamage(Type.BLUNT, calculateDamage());
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
			if (finalTarget == currentPlayer) {
				messageSelf("You heal yourself a bit.");
				messageOthers(currentPlayer.getName() + " heals a bit.", Arrays.asList(currentPlayer, finalTarget));
			} else {
				messageSelf("You heal " + finalTarget.getName());
				messageTarget(currentPlayer.getName() + " heals you.", Arrays.asList(finalTarget));
				messageOthers(currentPlayer.getName() + " heals " + finalTarget.getName() + ".", Arrays.asList(currentPlayer, finalTarget));
			}
		}
	}

	private int calculateDamage() {
		return intensity;
	}
	
	private Mobile setTarget(String targetName) {
		if (targetName.equals("") || targetName.equals(currentPlayer.getName().toLowerCase())) {
			return currentPlayer;
		}
		Mobile h = currentPlayer.getContainer().getMobileFromString(targetName);
		return h;
	}

	@Override
	protected boolean preSkillChecks() {
		if (finalTarget == null) {
			messageSelf("You can't heal that.");
			return false;
		}
		if (finalTarget.isDead()) {
			messageSelf("You can't heal dead people.");
			return false;
		}		
		return true;
	}
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Heal(currentPlayer, fullCommand);
	}
}
