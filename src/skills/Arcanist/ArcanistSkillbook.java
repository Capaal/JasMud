package skills.Arcanist;

import java.util.NavigableMap;

import interfaces.Mobile;
import processes.SkillBook;
import processes.Skills;

public class ArcanistSkillbook extends SkillBook {
	
	private ArcanistBuilder build;
	
	@Override
	public void addSkill(Skills newSkill) {
		if (newSkill instanceof ArcanistSkill) {
			super.addSkill(newSkill);
		} else {
			throw new IllegalArgumentException("ArcanistSkillBooks accepts ONLY arcanist skills!");
		}
		
	}

	public ArcanistSkillbook(String name, int id) {
		super(name, id);		
	}
	
	public void setBuilder(ArcanistBuilder b) {
		this.build = b;
	}

	public ArcanistBuilder getCurrentSkillBuilder() {
		return build;
	}
	
	public static ArcanistSkillbook getCurrentBook(Mobile currentPlayer) {
		for (SkillBook s : currentPlayer.viewSkillBooks().keySet()) {
			if (s instanceof ArcanistSkillbook) {
				return (ArcanistSkillbook)s;
			}
		}
		return null;
	}

	public void remove(ArcanistSkill spell) {		
		skillList.remove(spell.getName().toLowerCase());
	}
}
