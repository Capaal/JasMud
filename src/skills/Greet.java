package skills;

import interfaces.Holdable;
import interfaces.Mobile;
import processes.Location;

import java.util.Arrays;

import Quests.Quest;
import processes.Skills;

public class Greet extends Skills {
	
	public Greet(Mobile currentPlayer, String fullCommand) {
		super("greet", "Greeting others, an invitation to chat.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}
	
	private Mobile target;

	@Override
	public void performSkill() {
		target = setTarget();
		if (preSkillChecks()) {
			if (canTalk()) {
				messageSelf("You greet " + target.getName() + " in a friendly manner.");
				messageOthers(currentPlayer.getName() + " greets " + target.getName() + " in a friendly manner.", Arrays.asList(currentPlayer, target));
				messageTarget(currentPlayer.getName() + " greets you in a friendly manner.", Arrays.asList(target));
				((Location)currentPlayer.getContainer()).notifyQuest(Quest.Trigger.GREETS);
			} else {
				messageSelf("You can't talk.");
			}
		}
	}
	
	private Mobile setTarget() {
		Mobile h = currentPlayer.getContainer().getMobileFromString(Syntax.TARGET.getStringInfo(fullCommand, this));
		return h;
	}
	
	//checks for dumb, silence, etc
	public boolean canTalk() {
		return true;
	}

	@Override
	protected boolean preSkillChecks() {
		if (target == null) {
			messageSelf("Whom do you wish to greet?");
			return false;
		}
		return true;
	}
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Greet(currentPlayer, fullCommand);
	}
}
