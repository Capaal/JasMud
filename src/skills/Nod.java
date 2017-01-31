package skills;

import interfaces.Holdable;
import interfaces.Mobile;

import java.util.Arrays;

import Quests.Quest;
import processes.Location;
import processes.Skills;

public class Nod extends Skills {
	
	public Nod() {
		super.name = "nod";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}
	
	private Mobile target;

	@Override
	public void performSkill() {
		if (!setTarget()) {
			messageSelf("Whom do you wish to nod to?");
			return;
		}
		messageSelf("You nod to " + target.getName() + " in agreement.");
		messageOthers(currentPlayer.getName() + " nods to " + target.getName() + " .", Arrays.asList(currentPlayer, target));
		messageTarget(currentPlayer.getName() + " nods to you in agreement.", Arrays.asList(target));
		((Location)currentPlayer.getContainer()).notifyQuest(Quest.Trigger.NODS);	
	}
	
	private boolean setTarget() {
		target = null;
		Holdable h = currentPlayer.getContainer().getHoldableFromString(Syntax.TARGET.getStringInfo(fullCommand, this));
		if (h != null && h instanceof Mobile) {
			target = (Mobile)h;
			return true;
		}			
		return false;
	}
}
