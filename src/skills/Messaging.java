package skills;

import java.util.Set;

import interfaces.Holdable;
import processes.PlayerPrompt;
import processes.Skills;
import processes.Skills.Syntax;
import processes.WorldServer;

public class Messaging extends Skills {
	
	public Messaging() {
		super.name = "message";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.LIST);
	}

	//player needs to see that they have messages when logging in
	@Override
	protected void performSkill() {
		String player = Syntax.TARGET.getStringInfo(fullCommand, this);
		//need list of all existing players
		//can't have list of existing players without save states

	}

}
