package skills;

import effects.Balance;
import effects.ConditionsEnum;
import interfaces.Mobile;
import processes.Skills;
import processes.Skills.Syntax;

public class Straighten extends Skills {
	
	String slot;
	ConditionsEnum broken;
	
	public Straighten() {
		super.name = "straighten";
		super.description = "Fix a broken arm or leg.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.SLOT);
	}	
	
	@Override
	protected void performSkill() {
		slot = Syntax.SLOT.getStringInfo(fullCommand, this);
		if (slot.equals("")) {
			messageSelf("What you trying to straighten?");
			return;
		}
		if (!hasBalance()) {return;}

		if (slot.equals("righthand")) {
			broken = ConditionsEnum.BROKENRIGHTARM;
			checkBroken(broken);
		} else if (slot.equals("lefthand")) {
			broken = ConditionsEnum.BROKENLEFTARM;
			checkBroken(broken);
		} else if (slot.equals("legs")) {
			broken = ConditionsEnum.BROKENLEGS;
			checkBroken(broken);
		} else {
			messageSelf("You can't straighten that.");
		}

	}
	
	private void checkBroken(ConditionsEnum broken) {
		if (currentPlayer.hasAllConditions(broken)) {
			currentPlayer.removeAllConditions(broken);
			messageSelf("You straighten your broken " + slot + " by force of will.");
			currentPlayer.addEffect(new Balance(), 3000);
			return;
		} 
		messageSelf("Your " + slot + " is not broken.");
	}

}
