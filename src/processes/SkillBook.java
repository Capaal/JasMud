package processes;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class SkillBook {

	private NavigableMap<String, Skills> skillList;
	private final String name;
	private final int id;
	
	public SkillBook(String name, int id) {
		skillList = new TreeMap<String, Skills>();
		this.name = name;
		this.id = id;
	}
	
	public void addSkill(Skills newSkill) {
		synchronized (skillList) {
			skillList.put(newSkill.name, newSkill);
		}
	}	
	
	public Skills getSkill(String skill) {
		if (skill == null || skill.equals("")) {
			return null;
		}
		return findSkill(skill);
	}
	
	// TODO change to find via name via how moveHoldable works.
	private Skills findSkill(String skill) {
		synchronized (skillList) {
			String s = skillList.ceilingKey(skill);
			if (s != null && s.startsWith(skill)) {
				return skillList.get(s);
			}
	//		for (Skills s : skillList) {
	//			if (s.getName().toLowerCase().startsWith(skill.toLowerCase())) {			
	//				return s;
	//			}
	//		}
		}
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public Map<String, Skills> viewSkills() {
		return new TreeMap<String, Skills>(skillList);
	}
	
	protected SkillBook duplicate() {
		SkillBook newSkillBook = new SkillBook(name, id);
		newSkillBook.skillList = copySkillList();
		return newSkillBook;
	}

	private NavigableMap<String, Skills> copySkillList() {
		NavigableMap<String, Skills> newList = new TreeMap<String, Skills>();
		for (Skills s : skillList.values()) {
			newList.put(s.name, s.getNewInstance(null, null));
		}
		return newList;
	}
}
