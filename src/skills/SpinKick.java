package skills;

import java.util.Arrays;
import effects.Balance;
import effects.ConditionsEnum;
import interfaces.Mobile;
import processes.Skills;
import processes.Type;

public class SpinKick extends Skills {

	private String targetName;
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
		targetName = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (!hasBalance()) {return;}
		finalTarget = setTarget();
		if (finalTarget == null) {
			messageSelf("There is no " + targetName + " here for you to attack.");
			return;
		}
		if (isBlocking(finalTarget)) {  // Probably not complete still
			return;
		}
		
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
	
	private Mobile setTarget() {
		Mobile h = currentPlayer.getContainer().getMobileFromString(targetName);
		if (h != null) {
			return h;
		}	
		return null;
	}
	
	private int calculateDamage() {
		return intensity;
	}

}
