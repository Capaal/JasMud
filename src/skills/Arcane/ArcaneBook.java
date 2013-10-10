package skills.Arcane;

import java.util.ArrayList;
import processes.Command;
import processes.PlayerPrompt;
import processes.UsefulCommands;


// Effectively a spellbook for the arcane class.
public class ArcaneBook extends SkillBook implements Command {

	ArrayList<ArcaneSpell> skillList;
	
	public ArcaneBook() {
		super();
		skillList = new ArrayList<ArcaneSpell>();
	}
	
	@Override
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		super.execute(playerPrompt, fullCommand);
	}
	
//	public void addSpell(ArcaneSpell newSpell) {
//		spellList.add(newSpell);
//	}
	
//	public ArcaneSpell getSpell(String spell) {
//		int spellIndex = findSpell(spell);
//		if (spellIndex != -1) {
//			return spellList.get(spellIndex);
//		}
//		return null;
//	}
	
//	private int findSpell(String spell) {
//		for (int i = 0; i < spellList.size(); i++) {
//			if (spellList.get(i).getName().equals(spell)) {
//				return i;
//			}
//		}
//		return -1;
//	}

}
