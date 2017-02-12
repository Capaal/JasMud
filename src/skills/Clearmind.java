package skills;

import effects.Balance;
import effects.ConditionsEnum;
import effects.Fear;
import interfaces.Mobile;
import processes.Skills;
import processes.Skills.Syntax;

public class Clearmind extends Skills {
	
	//check for: limb already broken, is item wielded, unwield item
	public Clearmind() {
		super.name = "clearmind";
		super.description = "Cures mental afflictions.";
		super.syntaxList.add(Syntax.SKILL);
	}	
	
	@Override
	protected void performSkill() {
		if (!hasBalance()) {return;}
		if (currentPlayer.hasAllConditions(ConditionsEnum.DIZZY)) {
			currentPlayer.removeAllConditions(ConditionsEnum.DIZZY);
			messageSelf("Dizzy cured.");
			currentPlayer.addEffect(new Balance(), 3000);
		} else if (currentPlayer.hasEffect(new Fear(currentPlayer))) {
			currentPlayer.removeEffect(new Fear(currentPlayer));
			messageSelf("Fear cured.");
			currentPlayer.addEffect(new Balance(), 3000);
		} else {
			messageSelf("Your mind is already clear.");
		}

	}

}
