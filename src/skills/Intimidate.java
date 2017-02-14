package skills;

import java.util.Arrays;

import effects.Balance;
import effects.Fear;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;
import processes.Skills.Syntax;

public class Intimidate extends Skills {
	
	Mobile mobileToFear;
	
	public Intimidate() {
		super.name = "intimidate";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);	
	}

	@Override
	protected void performSkill() {
		if (!hasBalance()) {
			return;
		}
		String mobName = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (mobName.equals("")) {
			messageSelf("Specify a target.");
			return;
		}
		mobileToFear = (currentPlayer.getContainer().getMobileFromString(mobName));
		if (mobileToFear == null) {
			messageSelf("You can't find a person called " + mobileToFear.getName() + ".");
			return;
		}
		mobileToFear.addTickingEffect(new Fear(mobileToFear), 10000, 4);
		messageSelf("You fear someone.");
		messageTarget("Someone fears you.", Arrays.asList(mobileToFear));
	}

}

