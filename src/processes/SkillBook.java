package processes;

import interfaces.Mobile;

import java.util.ArrayList;

// Effectively a spellbook for basic skills.
public class SkillBook {

	private ArrayList<Skill> skillList;
	private String name;
	private final int id;
	public boolean toBeSaved = false;
	
	public SkillBook(String name, int id) {
		skillList = new ArrayList<Skill>();
		this.name = name;
		this.id = id;
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
	
	public ArrayList<Skill> getSkills() {
		return new ArrayList<Skill>(skillList);
	}
	
	public SkillBook copySkillBook(Mobile newPlayer) {
		SkillBook newSkillList = new SkillBook(name, id);
		ArrayList<Skill> newSkills = copySkills(newPlayer);
	//	for (Skill s : newSkills) {
	//		s.changePlayer(newPlayer);
	//	}
		newSkillList.setSkillList(newSkills);
		return newSkillList;
	}
	
	private ArrayList<Skill> copySkills(Mobile newPlayer) {
		ArrayList<Skill> newSkillList = new ArrayList<Skill>();
		for (Skill s : skillList) {
			newSkillList.add(s.copy(newPlayer));
		}
		return newSkillList;
	}
	
	private void setSkillList(ArrayList<Skill> newSkills) {
		skillList = newSkills;
	}
	
	public boolean save() {
		if (toBeSaved) {
			for (Skill s : skillList) {
				if (!s.save()) {
					return false;
				}
			}
		}
		return true;
	}
}
