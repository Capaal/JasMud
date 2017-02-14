package skills;

import java.util.Arrays;
import effects.Balance;
import effects.ConditionsEnum;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;
import processes.Type;

public class Punch extends Skills {
	
	private final int intensity = 10;
	private String targetName;
	private Mobile finalTarget;
	
	public Punch() {
		super.name = "punch";
		super.description = "Punching things.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}	
	
	// Deals damage to a single target in currentPlayer's location
	// ADD CHECK FOR ISDEAD()
	@Override
	protected void performSkill() {
		targetName = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (!hasBalance()) {return;}
		if (brokenArms()) {return;}
		finalTarget = setTarget();
		if (finalTarget == null) {
			messageSelf("There is no " + targetName + " here for you to punch.");
			return;
		}
		if (isBlocking(finalTarget)) {  // Probably not complete still
			return;
		}		
		finalTarget.informLastAggressor(currentPlayer);
		finalTarget.takeDamage(Type.BLUNT, calculateDamage());
		currentPlayer.addEffect(new Balance(), 3000);
		messageSelf("You punch " + finalTarget.getName());
		messageTarget(currentPlayer.getName() + " punches you.", Arrays.asList(finalTarget));
		messageOthers(currentPlayer.getName() + " punches " + finalTarget.getName(), Arrays.asList(currentPlayer, finalTarget));
	}

	private int calculateDamage() {
		return intensity;
	}
	
	private boolean brokenArms() {
		if (currentPlayer.hasAllConditions(ConditionsEnum.BROKENLEFTARM) && currentPlayer.hasAllConditions(ConditionsEnum.BROKENRIGHTARM)) {
			messageSelf("Your arms are broken!");
			return true;
		} 
		return false;
	}
	
	private Mobile setTarget() {
		Mobile h = currentPlayer.getContainer().getMobileFromString(targetName);
		if (h != null) {
			return h;
		}			
		return null;
	}
}
