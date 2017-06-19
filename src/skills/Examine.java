package skills;

import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;

public class Examine extends Skills {
	
	private Holdable possibleItem;
	private Mobile possibleMob;
	private String toExamine;
	
	public Examine() {
		super.name = "examine";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}

	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			if (possibleItem == null) {
				messageSelf(possibleMob.getDescription());
				return;
			} else {
				messageSelf(possibleItem.getExamine());
			}
		} else {
			messageSelf("You can't find that to examine it."); 
		}
	}

	private boolean searchForExamine() {
		possibleItem = null;
		//check inv
		possibleItem = currentPlayer.getHoldableFromString(toExamine);
		//check location
		if (possibleItem == null) {possibleItem = currentPlayer.getContainer().getHoldableFromString(toExamine);}
		//if not an item, try a mob
		if (possibleItem == null) {
			possibleMob = null;
			possibleMob = currentPlayer.getContainer().getMobileFromString(toExamine);
			if (possibleMob == null) { return false; }
		}
		return true;
	}

	@Override
	protected boolean preSkillChecks() {
		toExamine = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (toExamine.equals("")) {
			messageSelf("Examine what?");
			return false;
		}
		if (!searchForExamine()) {return false;}
		return true;
	}
}

