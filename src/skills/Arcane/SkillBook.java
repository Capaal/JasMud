package skills.Arcane;

import java.util.ArrayList;
import processes.Command;
import processes.PlayerPrompt;
import processes.UsefulCommands;

// Effectively a spellbook for basic skills.
public class SkillBook implements Command {

	ArrayList<Skill> skillList;
	
	public SkillBook() {
		skillList = new ArrayList<Skill>();
	}
	
	@Override
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		String info = UsefulCommands.returnTarget(fullCommand);
		String skill = UsefulCommands.getFirstWord(fullCommand);
		String target = UsefulCommands.getSecondWord(fullCommand);
		Skill toCast = getSkill(skill);
		toCast.perform(fullCommand);
	}
	
	public void addSkill(Skill newSpell) {
		skillList.add(newSpell);
	}
	
	public Skill getSkill(String spell) {
		int spellIndex = findSkill(spell);
		if (spellIndex != -1) {
			return skillList.get(spellIndex);
		}
		return null;
	}
	
	private int findSkill(String skill) {
		for (int i = 0; i < skillList.size(); i++) {
			if (skillList.get(i).getName().equals(skill)) {
				return i;
			}
		}
		return -1;
	}
}
