package skills;

import interfaces.Mobile;

import java.util.Arrays;

import processes.Skills;

public class Say extends Skills {
	
	private String words;
	
	public Say(Mobile currentPlayer, String fullCommand) {
		super("say", "Talking to others nearby.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.LIST);
	}

	@Override
	protected void performSkill() {
		words = Syntax.LIST.getStringInfo(fullCommand, this);
		if (preSkillChecks()) {
			messageSelf("You say: \"" + words + "\".");
			messageOthers(currentPlayer.getName() + " says, \"" + Syntax.LIST.getStringInfo(fullCommand, this) + "\".", Arrays.asList(currentPlayer));
		}
	}
	
	//checks for dumb, silence, etc
	public boolean canTalk() {
		return true;
	}

	@Override
	protected boolean preSkillChecks() {
		if (!canTalk()) {
			messageSelf("You can't talk.");
			return false;
		}
		words = Syntax.LIST.getStringInfo(fullCommand, this);
		if (words.equals("")) {
			messageSelf("You utter nothing.");
			return false;
		}
		return true;
	}
	
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Say(currentPlayer, fullCommand);
	}

}
