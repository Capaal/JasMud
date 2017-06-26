package skills;

import interfaces.Mobile;

import java.util.Arrays;

import Quests.Quest;
import processes.Location;
import processes.Skills;

public class Nod extends Skills {
	
	public Nod(Mobile currentPlayer, String fullCommand) {
		super("nod", "Nodding to others, to encourage more details.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}
	
	private Mobile target;

	@Override
	protected void performSkill() {
		target = setTarget();
		if (target == null) {
			messageSelf("You nod agreeably.");
			messageOthers(currentPlayer.getName() + " nods in agreement.", Arrays.asList(currentPlayer));
			return;
		}
		messageSelf("You nod to " + target.getNameColored() + " in agreement.");
		messageOthers(currentPlayer.getNameColored() + " nods to " + target.getNameColored() + ".", Arrays.asList(currentPlayer, target));
		messageTarget(currentPlayer.getNameColored() + " nods to you in agreement.", Arrays.asList(target));
		((Location)currentPlayer.getContainer()).notifyQuest(Quest.Trigger.NODS);	
	}
	
	private Mobile setTarget() {
		Mobile h = currentPlayer.getContainer().getMobileFromString(Syntax.TARGET.getStringInfo(fullCommand, this));
		if (h != null) {
			return h;
		}			
		return null;
	}

	@Override
	protected boolean preSkillChecks() {
		return true;
	}
	
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Nod(currentPlayer, fullCommand);
	}
}
