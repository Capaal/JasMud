package skills.Arcanist.Commands;

import interfaces.Mobile;
import processes.Skills;
import skills.Arcanist.ArcanistBuilder;
import skills.Arcanist.ArcanistSkill;
import skills.Arcanist.ArcanistSkillbook;

public class ArcanistModify extends Skills {
	
	private ArcanistSkill spell;
	private ArcanistSkillbook currentBook;

	public ArcanistModify(Mobile currentPlayer, String fullCommand) {
		super("modify", "Prepares a spell to be altered.", currentPlayer, fullCommand);
		syntaxList.add(Syntax.SKILL);
		syntaxList.add(Syntax.TARGET);
	}

	@Override
	protected void performSkill() {
		ArcanistBuilder modifyBuild = spell.getNewBuilder();
		currentBook.setBuilder(modifyBuild);
		messageSelf("Spell ready to be altered!");
	}

	@Override
	protected boolean preSkillChecks() {
		String spellName = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (spellName.equals("")) {
			messageSelf("What spell would you like to modify?");
			return false;
		}
		currentBook = ArcanistSkillbook.getCurrentBook(currentPlayer);
		if (currentBook == null) {
			messageSelf("You have no book!");
			System.out.println("Major bug, player missing book but modify");
			return false;
		}		
		spell = (ArcanistSkill) currentBook.getSkill(spellName);
		if (spell == null) {
			messageSelf("You know no spell by that name!");
			return false;
		}
		return true;
	}

	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new ArcanistModify(currentPlayer, fullCommand);
	}
}
