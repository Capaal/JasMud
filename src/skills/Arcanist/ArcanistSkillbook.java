package skills.Arcanist;

import processes.SkillBook;

public class ArcanistSkillbook extends SkillBook {
	
	private ArcanistBuilder build;

	public ArcanistSkillbook(String name, int id) {
		super(name, id);		
	}
	
	public void setBuilder(ArcanistBuilder b) {
		this.build = b;
	}

	public ArcanistBuilder getCurrentSkillBuilder() {
		return build;
	}
}
