package skills;

import processes.Skills;
import processes.Skills.Syntax;

public class SkillList extends Skills {

	public SkillList() {
		super.name = "skills";
		super.syntaxList.add(Syntax.SKILL);
	}
	
	@Override
	protected void performSkill() {
		//StdMob needs a way to send its skillbooks.

	}

}
