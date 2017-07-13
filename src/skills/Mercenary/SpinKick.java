package skills.Mercenary;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.InformsAggro;
import interfaces.Mobile;
import processes.Skills;
import skills.Sleep;

public class SpinKick extends Skills implements InformsAggro {

	private Mobile finalTarget;
	private final int intensity = 8;
	
	//check for: limb already broken, is item wielded, unwield item
	public SpinKick(Mobile currentPlayer, String fullCommand) {
		super("spinkick", "Dizzy causing kick.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}	
	
	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			if (!(finalTarget.hasCondition(PassiveCondition.DIZZY))) {
				finalTarget.addPassiveCondition((PassiveCondition.DIZZY),-1);
				messageTarget("You feel dizzy.", Arrays.asList(finalTarget));
			}
			
			finalTarget.informLastAggressor(currentPlayer);
			finalTarget.takeDamage(calculateDamage());
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
			messageSelf("You spin around really fast and kick " + finalTarget.getNameColored() + ".");
			messageTarget(currentPlayer.getNameColored() + " makes you stumble with a dizzying kick.", Arrays.asList(finalTarget));
			messageOthers(currentPlayer.getNameColored() + " spins and kicks " + finalTarget.getNameColored() + ".", Arrays.asList(currentPlayer, finalTarget));
			informLastAggressor(currentPlayer, finalTarget);
		}
	}
	
	private boolean setTarget() {
		String targetName = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (targetName.equals("")) {
			messageSelf("Specify target.");
			return false;
		}
		finalTarget = currentPlayer.getContainer().getMobileFromString(targetName);
		if (finalTarget != null) {
			return true;
		} else {
			messageSelf("There is no " + targetName + " here for you to attack.");
			return false;
		}
	}
	
	private int calculateDamage() {
		return intensity;
	}

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		if (!setTarget()) {return false;}
		if (isBlocking(finalTarget)) {return false;}  // Probably not complete still
		
		return true;
	}
}
