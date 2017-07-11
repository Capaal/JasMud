package processes;

import java.util.NavigableMap;
import java.util.TreeMap;

public class SkillBook {

	protected NavigableMap<String, Skills> skillList;
	protected final String name;
	protected final int id;
	
	public SkillBook(String name, int id) {
		skillList = new TreeMap<String, Skills>();
		this.name = name;
		this.id = id;
	}
	
	public void addSkill(Skills newSkill) {
		synchronized (skillList) {
			skillList.put(newSkill.name.toLowerCase(), newSkill);
		}
	}	
	
	public Skills getSkill(String skill) {
		if (skill == null || skill.equals("")) {
			return null;
		}
		return findSkill(skill.toLowerCase());
	}
	
	// TODO change to find via name via how moveHoldable works.
	private Skills findSkill(String skill) {
		synchronized (skillList) {
			String s = skillList.ceilingKey(skill);
			if (s != null && s.startsWith(skill)) {
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
