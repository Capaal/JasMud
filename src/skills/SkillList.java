package skills;

import java.util.Map;
import processes.SkillBook;
import processes.Skills;

public class SkillList extends Skills {

	public SkillList() {
		super.name = "skills";
		super.syntaxList.add(Syntax.SKILL);
	}
	
	@Override
	protected void performSkill() {
		messageSelf("Skills: ");
		Map<SkillBook, Integer> books = currentPlayer.viewSkillBooks();
		for (SkillBook b : books.keySet()) {
			for (Skills s : b.viewSkills()) {
				if (s.getName() != "skills") {
					messageSelf(s.getName());
				}
			}
		}

	}

	@Override
	protected boolean preSkillChecks() {
		return true;
	}

}
