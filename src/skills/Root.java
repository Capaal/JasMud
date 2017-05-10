package skills;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.Mobile;
import processes.Skills;
import processes.Skills.Syntax;

public class Root extends Skills{

	private Mobile finalTarget;
	private String possibleTarg;
	
	public Root() {
		super.name = "root";
		super.description = "Roots the target in place.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}
	
	@Override
	protected void performSkill() {
		if(!preSkillChecks()) {return;}
		
		finalTarget.addAllConditions(PassiveCondition.ROOT);
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 2000);
		messageSelf("Strong, ropey vines curl out of your hands towards " + finalTarget.getName() + " and wrap themselves tightly around " + "their feet."); //finalTarget.getGenderHimHer()
		messageTarget("Vines suddenly encircle your feet and root you to the spot.", Arrays.asList(finalTarget));
		messageOthers("Living vines fly out from " + currentPlayer.getName() + "'s hands and firmly binds " + finalTarget.getName() + " to the ground.", Arrays.asList(currentPlayer, finalTarget)); 
		
	}
	
	private boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		if (!findTarget()) {return false;}
		if (hasCondition()) {return false;}
		return true;
	}
	
	private boolean findTarget() {
		String targetName  = Syntax.TARGET.getStringInfo(fullCommand, this);
		finalTarget = null;
		if (targetName.equals("")) {
			messageSelf("Who are you trying to root?");
			return false;
		}
		finalTarget = currentPlayer.getContainer().getMobileFromString(targetName);		
		if (finalTarget == null) {
			messageSelf("There is no " + targetName + " here.");
			return false;
		}
		return true;
	}
	
	private boolean hasCondition() {
		if (finalTarget.hasAllConditions(PassiveCondition.ROOT)) {
			messageSelf("There are already roots surrounding " + finalTarget.getName() + ".");
			return true;
		}
		return false;
	}

}
