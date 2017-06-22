package processes;

import java.util.HashSet;
import java.util.Set;

import skills.Move;

public class SkillBook {

	private Set<Skills> skillList;
	private final String name;
	private final int id;
	
	public SkillBook(String name, int id) {
		skillList = new HashSet<Skills>();
		this.name = name;
		this.id = id;
	}
	
	public void addSkill(Skills newSkill) {
		synchronized (skillList) {
			skillList.add(newSkill);
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
			for (Skills s : skillList) {
				if (s.getName().toLowerCase().startsWith(skill.toLowerCase())) {			
					return s;
				}
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
	
	public Set<Skills> viewSkills() {
		return new HashSet<Skills>(skillList);
	}
	
	protected SkillBook duplicate() {
		SkillBook newSkillBook = new SkillBook(name, id);
		newSkillBook.skillList = copySkillList();
		return newSkillBook;
	}

	private Set<Skills> copySkillList() {
		Set<Skills> newList = new HashSet<Skills>();
		for (Skills s : skillList) {
			try {
				newList.add(s.getClass().newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return newList;
	}
}
