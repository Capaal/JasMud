package processes;

import java.sql.SQLException;
import java.util.HashMap;
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
	
	public boolean save() {
		if (toBeSaved) {			
			for (Skill s : skillList) {				
				if (!s.save()) {
					return false;
				}
				saveSkillBookLink(s);
			}
			toBeSaved = false;
		}		
		return true;
	}
	
	private void saveSkillBookLink(Skill s) {
		String skillSelect = "SELECT * FROM skilltable WHERE SKILLID='" + s.getId() + "';";
		HashMap<String, Object> skillView = SQLInterface.returnBlockView(skillSelect);
		String skillInsert;
		if (skillView.get("SKILLID") == null) {
			skillInsert = "INSERT INTO skilltable (SKILLBOOKID, SKILLID) VALUES (" + this.id + ", " + s.getId() + ");";		
			try {
				SQLInterface.saveAction(skillInsert);
			} catch (SQLException e) {
				System.out.println("Attempt to save skillLink to book: " + skillInsert);
				e.printStackTrace();
			}
		}
	}
}
