package skills;

import interfaces.Mobile;

import java.util.Map;

import processes.SkillBook;
import processes.Skills;
import processes.UsefulCommands;

public class SkillList extends Skills {

	public SkillList(Mobile currentPlayer, String fullCommand) {
		super("skills", "Inventory of your knowledge.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
	}
	
	@Override
	protected void performSkill() {
		messageSelf(UsefulCommands.ANSI.MAGENTA + "Skills: " + UsefulCommands.ANSI.SANE);
		Map<SkillBook, Integer> books = currentPlayer.viewSkillBooks();
		for (SkillBook b : books.keySet()) {
			messageSelf(b.getName() + ": ");
			for (Skills s : b.viewSkills().values()) {
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
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new SkillList(currentPlayer, fullCommand);
	}
}
