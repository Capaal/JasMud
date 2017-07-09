package skills.Arcanist.Commands;

import interfaces.Mobile;
import processes.Skills;
import processes.Skills.Syntax;
import skills.Arcanist.ArcanistSkill;
import skills.Arcanist.ArcanistSkillbook;

public class ArcanistDelete extends Skills {
	
	ArcanistSkill spell;
	ArcanistSkillbook currentBook;

	public ArcanistDelete(Mobile currentPlayer, String fullCommand) {
		super("delete", "Regaining space in your spellbook.", currentPlayer, fullCommand);
		syntaxList.add(Syntax.SKILL);
		syntaxList.add(Syntax.TARGET);
	}

	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			currentBook.remove(spell);
			messageSelf("Spell successfully forgotten.");
		}
	}

	@Override
	protected boolean preSkillChecks() {
		String spellName = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (spellName.equals("")) {
			messageSelf("What spell would you like to delete?");
			return false;
		}
		currentBook = ArcanistSkillbook.getCurrentBook(currentPlayer);
		if (currentBook == null) {
			messageSelf("You have no book!");
			System.out.println("Major bug, player missing book but delete");
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
		return new ArcanistDelete(currentPlayer, fullCommand);
	}

}
