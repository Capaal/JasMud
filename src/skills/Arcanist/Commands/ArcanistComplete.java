package skills.Arcanist.Commands;

import interfaces.Mobile;
import processes.SkillBook;
import processes.Skills;
import processes.Skills.Syntax;
import skills.Arcanist.ArcanistBuilder;
import skills.Arcanist.ArcanistSkill;
import skills.Arcanist.ArcanistSkillbook;

public class ArcanistComplete extends Skills {
	
	ArcanistBuilder currentSkill;
	ArcanistSkillbook currentBook;

	public ArcanistComplete(Mobile currentPlayer, String fullCommand) {
		super("complete", "Finishing a new spell you've been scribing via CREAT and ALTER.", currentPlayer, fullCommand);
		syntaxList.add(Syntax.SKILL);
	}

	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			ArcanistSkill newSkill = currentSkill.complete();
			if (newSkill != null) {
				currentBook.addSkill(newSkill);
				messageSelf("Spell scribed successfully!");
			} else {
				throw new IllegalStateException("Complete should never have failed this far!");
			}
		}		
	}

	@Override
	protected boolean preSkillChecks() {
		currentBook = ArcanistSkillbook.getCurrentBook(currentPlayer);
		if (currentBook == null) {
			messageSelf("But you have no book for which to scribe!");
			System.out.println("Serious bug, player missing ArcanistSkillbook but used ALTER.");
			return false;
		}
		currentSkill = currentBook.getCurrentSkillBuilder();
		if (currentSkill == null) {
			messageSelf("You have not yet began to CREATE a new spell.");
			return false;
		}
		if (!currentSkill.isValid()) {
			messageSelf("But you can see for yourself the spell is not ready! Perhaps a few more ALTERations..");
			return false;
		}
		return true;
	}

	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new ArcanistComplete(currentPlayer, fullCommand);
	}

}
