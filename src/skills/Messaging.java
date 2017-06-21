package skills;

import java.util.Set;

import interfaces.Holdable;
import interfaces.Mobile;
import processes.PlayerPrompt;
import processes.Skills;
import processes.Skills.Syntax;
import processes.WorldServer;

public class Messaging extends Skills {
	
	public Messaging(Mobile currentPlayer, String fullCommand) {
		super("message", "Long distance communication.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.LIST);
	}

	//player needs to see that they have messages when logging in
	@Override
	protected void performSkill() {
		String player = Syntax.TARGET.getStringInfo(fullCommand, this);
		//code/internal system to hold messages that only a specific player can access
		//need list of all existing players
		//can't have list of existing players without save states

	}

	@Override
	protected boolean preSkillChecks() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Messaging(currentPlayer, fullCommand);
	}
}
