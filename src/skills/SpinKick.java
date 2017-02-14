package skills;

import java.util.Arrays;
import effects.Balance;
import effects.ConditionsEnum;
import interfaces.Mobile;
import processes.Skills;
import processes.Type;

public class SpinKick extends Skills {

	private Mobile finalTarget;
	private final int intensity = 8;
	
	//check for: limb already broken, is item wielded, unwield item
	public SpinKick() {
		super.name = "spinkick";
		super.description = "Make someone dizzy.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}	
	
	@Override
	protected void performSkill() {
		if (!hasBalance()) {return;}
		if (!setTarget()) {return;}
		if (isBlocking(finalTarget)) {return;}  // Probably not complete still
		
		if (!(finalTarget.hasAllConditions(ConditionsEnum.DIZZY))) {
			finalTarget.addAllConditions(ConditionsEnum.DIZZY);
			messageTarget("You feel dizzy.", Arrays.asList(finalTarget));
		}
		
		finalTarget.informLastAggressor(currentPlayer);
		finalTarget.takeDamage(Type.BLUNT, calculateDamage());
		currentPlayer.addEffect(new Balance(), 3000);
		messageSelf("You spin around really fast and kick " + finalTarget.getName() + ".");
		messageTarget(currentPlayer.getName() + " makes you stumble with a dizzying kick.", Arrays.asList(finalTarget));
		messageOthers(currentPlayer.getName() + " spins and kicks " + finalTarget.getName() + ".", Arrays.asList(currentPlayer, finalTarget));
	}
	
	private boolean setTarget() {
		String targetName = Syntax.TARGET.getStringInfo(fullCommand, this);
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

}
