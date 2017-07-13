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
		if (preSkillChecks() ) {
			if (target == null) {
				messageSelf("You nod agreeably.");
				messageOthers(currentPlayer.getName() + " nods in agreement.", Arrays.asList(currentPlayer));
			} else {
				messageSelf("You nod to " + target.getNameColored() + " in agreement.");
				messageOthers(currentPlayer.getNameColored() + " nods to " + target.getNameColored() + ".", Arrays.asList(currentPlayer, target));
				messageTarget(currentPlayer.getNameColored() + " nods to you in agreement.", Arrays.asList(target));
			}
		}
		((Location)currentPlayer.getContainer()).notifyQuest(Quest.Trigger.NODS);	
	}

	@Override
	protected boolean preSkillChecks() {
		String targetName = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (targetName.equals("") || targetName.equals(currentPlayer.getName())) {
			target = null;
		} else {
			target = currentPlayer.getContainer().getMobileFromString(targetName);
		}		
		return true;
	}
}
