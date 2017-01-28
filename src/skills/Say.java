package skills;

import java.util.Arrays;

import interfaces.Mobile;
import processes.Skills;
import processes.Skills.Syntax;

public class Say extends Skills {
	
	public Say() {
		super.name = "say";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.LIST);
	}

	@Override
	public void perform(String fullCommand, Mobile currentPlayer) {
		messageSelf("You say: " + getStringInfo(Syntax.LIST, fullCommand) + ".", currentPlayer);
		messageOthers(currentPlayer.getName() + " says, " + getStringInfo(Syntax.LIST, fullCommand) + ".", currentPlayer, Arrays.asList(currentPlayer));
	}

}
