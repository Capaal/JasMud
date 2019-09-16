package skills;

import interfaces.Mobile;

import java.util.Arrays;

import processes.Skills;

public class Say extends Skills {
	
	private String words;
	
	public Say(Mobile currentPlayer, String fullCommand) {
		super("say", "Talking to others nearby.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.MESSAGE);
	}

	@Override
	protected void performSkill() {
		messageSelf("You say: \"" + words + "\".");
		messageOthers(currentPlayer.getNameColored() + " says, \"" + words + "\".", Arrays.asList(currentPlayer));
	}
	
	//checks for dumb, silence, etc
	public boolean canTalk() {
		return true;
	}

	@Override
	protected boolean preSkillChecks() {
		words = Syntax.MESSAGE.getStringInfo(fullCommand, this);
		if (!canTalk()) {
			messageSelf("You can't talk.");
			return false;
		}
		words = Syntax.MESSAGE.getStringInfo(fullCommand, this);
		if (words.equals("")) {
			messageSelf("You utter nothing.");
			return false;
		}
		return true;
	}
}
