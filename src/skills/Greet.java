package skills;

import interfaces.Holdable;
import interfaces.Mobile;

import java.util.Arrays;

import Quests.Quest;
import processes.Skills;

public class Greet extends Skills {
	
	public Greet() {
		super.name = "greet";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}
	
	private Mobile target;

	@Override
	public void performSkill() {
		if (!setTarget()) {
			messageSelf("Whom do you wish to greet?");
			return;
		}
		if (canTalk()) {
			messageSelf("You greet " + target.getName() + " in a friendly manner.");
			messageOthers(currentPlayer.getName() + " greets " + target.getName() + " in a friendly manner.", Arrays.asList(currentPlayer, target));
			messageTarget(currentPlayer.getName() + " greets you in a friendly manner.", Arrays.asList(target));
			currentPlayer.getContainer().notifyQuest(Quest.Trigger.GREETS);
		} else {
			messageSelf("You can't talk.");
		}
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
	
	//checks for dumb, silence, etc
	public boolean canTalk() {
		return true;
	}

}
