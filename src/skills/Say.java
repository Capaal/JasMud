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
	public void performSkill() {
		if (canTalk()) {
			messageSelf("You say: " + Syntax.LIST.getStringInfo(fullCommand, this) + ".");
			messageOthers(currentPlayer.getName() + " says, " + Syntax.LIST.getStringInfo(fullCommand, this) + ".", Arrays.asList(currentPlayer));
		} else {
			messageSelf("You can't talk.");
		}
	}
	
	//checks for dumb, silence, etc
	public boolean canTalk() {
		return true;
	}

}
