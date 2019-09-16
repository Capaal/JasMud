package skills;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.Mobile;
import processes.Skills;

public class Root extends Skills implements interfaces.InformsAggro {

	private Mobile finalTarget;
	
	public Root(Mobile currentPlayer, String fullCommand) {
		super("root", "Roots the target in place.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}
	
	@Override
	protected void performSkill() {
		finalTarget.addPassiveCondition(PassiveCondition.ROOT, -1);
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 2000);
		messageSelf("Strong, ropey vines curl out of your hands towards " + finalTarget.getNameColored() + " and wrap themselves tightly around " + "their feet."); //finalTarget.getGenderHimHer()
		messageTarget("Vines suddenly encircle your feet and root you to the spot.", Arrays.asList(finalTarget));
		messageOthers("Living vines fly out from " + currentPlayer.getNameColored() + "'s hands and firmly binds " + finalTarget.getNameColored() + " to the ground.", Arrays.asList(currentPlayer, finalTarget)); 
		informLastAggressor(currentPlayer, finalTarget);		
	}
	
	@Override
	protected boolean preSkillChecks() {
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
		if (finalTarget.hasCondition(PassiveCondition.ROOT)) {
			messageSelf("There are already roots surrounding " + finalTarget.getName() + ".");
			return true;
		}
		return false;
	}
}
