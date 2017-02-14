package skills;

import java.util.Arrays;
import processes.Skills;

public class Say extends Skills {
	
	public Say() {
		super.name = "say";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.LIST);
	}

	@Override
	protected void performSkill() {
		if (canTalk()) {
			String words = Syntax.LIST.getStringInfo(fullCommand, this);
			if(!words.equals("")) {
				messageSelf("You say: \"" + words + "\".");
				messageOthers(currentPlayer.getName() + " says, \"" + Syntax.LIST.getStringInfo(fullCommand, this) + "\".", Arrays.asList(currentPlayer));
			}
		} else {
			messageSelf("You can't talk.");
		}
	}
	
	//checks for dumb, silence, etc
	public boolean canTalk() {
		return true;
	}

}
