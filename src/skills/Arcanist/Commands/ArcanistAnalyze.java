package skills.Arcanist.Commands;

import interfaces.Mobile;
import processes.SkillBook;
import processes.Skills;
import processes.UsefulCommands;
import skills.Arcanist.ArcanistBuilder;
import skills.Arcanist.ArcanistSkillbook;

public class ArcanistAnalyze extends Skills {
	
	private ArcanistBuilder currentSkill;

	public ArcanistAnalyze(Mobile currentPlayer, String fullCommand) {
		super("analyze", "Referencing the current state of your spell-to-be.", currentPlayer, fullCommand);
		syntaxList.add(Syntax.SKILL);
	}

	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			StringBuilder sb = new StringBuilder();
			sb.append(currentSkill.getName());
			sb.append(": ");
			sb.append(currentSkill.getDescrption());
			sb = currentSkill.getDamageBlock().describeOneself(sb);
			sb = currentSkill.getSpeedBlock().describeOneself(sb);
			sb = currentSkill.getTargettingBlock().describeOneself(sb);
			sb.append(System.lineSeparator() + "Mana required: " + currentSkill.getMana());
			sb.append(". Costing: ");
			sb.append(currentSkill.calculateManaCost());
			sb.append(System.lineSeparator() + "Syntax: " + currentSkill.getSyntax());
			if (currentSkill.getCost() >= 0) {
				sb.append(UsefulCommands.ANSI.GREEN);
			} else {
				sb.append(UsefulCommands.ANSI.RED);
			}
			sb.append(System.lineSeparator() + "Build status: " + currentSkill.getCost());
			sb.append(UsefulCommands.ANSI.SANE);
			if (currentSkill.isValid()) {
				sb.append(UsefulCommands.ANSI.GREEN);
				sb.append(System.lineSeparator() + "is valid for completion.");
				sb.append(UsefulCommands.ANSI.SANE);
			} else {
				sb.append(UsefulCommands.ANSI.RED);
				sb.append(System.lineSeparator() + "is NOT valid for completion.");
				sb.append(UsefulCommands.ANSI.SANE);
			}
			messageSelf(sb.toString());
		}
	}

	@Override
	protected boolean preSkillChecks() {
		ArcanistSkillbook currentBook = getCurrentBook();
		if (currentBook == null) {
			messageSelf("But you have no book for which to scribe!");
			System.out.println("Serious bug, player missing ArcanistSkillbook but used ALTER.");
			return false;
		}
		currentSkill = currentBook.getCurrentSkillBuilder();
		if (currentSkill == null) {
			messageSelf("You have not yet begun to CREATE.");
			return false;
		}
		return true;
	}
	
	private ArcanistSkillbook getCurrentBook() {
		for (SkillBook s : currentPlayer.viewSkillBooks().keySet()) {
			if (s instanceof ArcanistSkillbook) {
				return (ArcanistSkillbook)s;
			}
		}
		return null;
	}

	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new ArcanistAnalyze(currentPlayer, fullCommand);
	}

}
