package processes;

import java.util.NavigableMap;
import java.util.TreeMap;

// Organizes list of skills to control order of unlocking, and simplifying given them out to particular classes.
public class SkillBook {

	protected NavigableMap<String, Skills> skillList;
	protected final String name;
	protected final int id;
	
	public SkillBook(String name, int id) {
		skillList = new TreeMap<String, Skills>();
		this.name = name;
		this.id = id;
	}
	
	// Add a skill to this skill book.
	public void addSkill(Skills newSkill) {
		synchronized (skillList) {
			skillList.put(newSkill.name.toLowerCase(), newSkill);
		}
	}	
	
	// Finds a particular skill in this skillbook, or null if unable.
	// Uses startsWith.
	public Skills getSkill(String skill) {
		if (skill == null || skill.equals("")) {
			return null;
		}
		synchronized (skillList) {
			String s = skillList.ceilingKey(skill.toLowerCase());
			if (s != null && s.startsWith(skill.toLowerCase())) {
				return skillList.get(s);
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
	
	public NavigableMap<String, Skills> viewSkills() {
		return new TreeMap<String, Skills>(skillList);
	}
	
	protected SkillBook duplicate() {
		SkillBook newSkillBook = new SkillBook(name, id);
		newSkillBook.setSkillList(copySkillList());
		return newSkillBook;
	}

	protected NavigableMap<String, Skills> copySkillList() {
		NavigableMap<String, Skills> newList = new TreeMap<String, Skills>();
		for (Skills s : skillList.values()) {
			newList.put(s.name, s.getNewInstance(null, null));
		}
		return newList;
	}

	public void setSkillList(NavigableMap<String, Skills> copySkillList) {
		this.skillList = copySkillList;
	}
}
