package processes;

import interfaces.Mobile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

// Effectively a spellbook for basic skills.
public class SkillBook {

	private Set<Skill> skillList;
	private final String name;
	private final int id;
	private boolean toBeSaved = false;
	
	public SkillBook(String name, int id) {
		skillList = new HashSet<Skill>();
		this.name = name;
		this.id = id;
	}
	
	public void addSkill(Skill newSpell) {
		skillList.add(newSpell);
	}
	
	public boolean getToBeSave() {
		return toBeSaved;
	}
	
	public void setToBeSaved(boolean save) {
		this.toBeSaved = save;
	}
	
	public Skill getSkill(String skill) {
		if (skill == null || skill.equals("")) {
			return null;
		}
		return findSkill(skill);
	}
	
	private Skill findSkill(String skill) {
		for (Skill s : skillList) {
			if (s.getName().equals(skill)) {
				return s;
			}
		}
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public boolean save(Mobile currentPlayer) {
		if (toBeSaved) {			
			for (Skill s : skillList) {
				if (!s.save()) {
					return false;
				}
			}
			toBeSaved = false;
		}		
		return true;
	}
	
}
