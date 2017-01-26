package processes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SkillBook {

	private Set<Skills> skillList;
	private final String name;
	private final int id;
	
	public SkillBook(String name, int id) {
		skillList = new HashSet<Skills>();
		this.name = name;
		this.id = id;
	}
	
	public void addSkill(Skills newSpell) {
		synchronized (skillList) {
			skillList.add(newSpell);
		}
	}	
	
	public Skills getSkill(String skill) {
		if (skill == null || skill.equals("")) {
			return null;
		}
		return findSkill(skill);
	}
	
	private Skills findSkill(String skill) {
		synchronized (skillList) {
			for (Skills s : skillList) {
				if (s.getName().equals(skill)) {
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
	
	public void save() {
		synchronized (skillList) {
			for (Skills s : skillList) {	
				saveSkillBookLink(s);
			}
		}
	}
	
	private void saveSkillBookLink(Skills s) {
/*		String skillSelect = "SELECT * FROM skilltable WHERE SKILLID='" + s.getId() + "';";
		HashMap<String, Object> skillView = WorldServer.databaseInterface.returnBlockView(skillSelect);
		String skillInsert;
		if (skillView.get("SKILLID") == null) {
			skillInsert = "INSERT INTO skilltable (SKILLBOOKID, SKILLID) VALUES (" + this.id + ", " + s.getId() + ");";		
			WorldServer.databaseInterface.saveAction(skillInsert);
		}*/
	}
	
	protected SkillBook duplicate(SkillBook toBeCopied) {
		SkillBook newSkillBook = new SkillBook(toBeCopied.name, toBeCopied.id);
		newSkillBook.skillList = toBeCopied.skillList;
		return newSkillBook;
	}
}
