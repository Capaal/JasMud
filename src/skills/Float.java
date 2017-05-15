package skills;

import java.util.Arrays;

import effects.Levitate;
import effects.PassiveCondition;
import interfaces.Mobile;
import processes.Skills;

public class Float extends Skills {
	
	public Float() {
		super.name = "float";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);	
	}

	@Override
	protected void performSkill() {
		if (!hasBalance()) {return;}
		if (currentPlayer.hasCondition(new Levitate(currentPlayer))) {
			messageSelf("You are already levitating.");
			return;
		}

		currentPlayer.addActiveCondition(new Levitate(currentPlayer), 4);
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
	//	messageSelf("You float up a foot above the ground."); //done in levitate effect
		messageOthers(currentPlayer.getName() + "floats into the air.", Arrays.asList(currentPlayer));
	}

	
}

