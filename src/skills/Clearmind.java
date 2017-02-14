package skills;

import effects.PassiveCondition;
import effects.Fear;
import processes.Skills;

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
		if (currentPlayer.hasAllConditions(PassiveCondition.DIZZY)) {
			currentPlayer.removeAllConditions(PassiveCondition.DIZZY);
			messageSelf("Dizzy cured.");
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
		} else if (currentPlayer.hasCondition(new Fear(currentPlayer))) {
			currentPlayer.removeCondition(new Fear(currentPlayer));
			messageSelf("Fear cured.");
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
		} else {
			messageSelf("Your mind is already clear.");
		}

	}

}
