package processes;

import java.util.ArrayList;

//import processes.Command;

// Effectively a spellbook for basic skills.
public class SkillBook {

	ArrayList<Skill> skillList;
	
	public SkillBook() {
		skillList = new ArrayList<Skill>();
	}
	

	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		Skill toCast = getSkill(UsefulCommands.getFirstWord(fullCommand));
		if (toCast != null) {
			toCast.perform(fullCommand);
		}
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
