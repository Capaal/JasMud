package skills.Arcanist.Commands;

import interfaces.Mobile;
import processes.SkillBook;
import processes.Skills;
import processes.UsefulCommands;
import skills.Arcanist.ArcanistBuilder;
import skills.Arcanist.ArcanistSkillbook;

public class ArcanistCreate extends Skills {
	
	private String spellName;
	private ArcanistSkillbook currentBook;

	public ArcanistCreate(Mobile currentPlayer, String fullCommand) {
		super("create", "Start building a new spell: create [spellname]", currentPlayer, fullCommand);
		syntaxList.add(Syntax.SKILL);
		syntaxList.add(Syntax.TARGET);		
	}

	@Override
	protected void performSkill() {
		spellName = Syntax.TARGET.getStringInfo(fullCommand, this);
		currentBook = getCurrentBook();
		if (preSkillChecks()) {
			if (currentBook.getCurrentSkillBuilder() != null) {
				messageSelf("Previous scriblings have been forgotten.");
			}
			currentBook.setBuilder(new ArcanistBuilder(spellName));
			messageSelf(spellName + " scribing began. ALTERations necessary for completion.");
		}
	}

	@Override
	protected boolean preSkillChecks() {
		if (currentBook == null) {
			messageSelf("But you have no book for which to scribe!");
			System.out.println("Serious bug, player missing ArcanistSkillbook but used CREATE.");
			return false;
		}
		if (spellName == null || spellName.equals("")) {
			messageSelf("You need to name your spell: create [spellname].");
			return false;
		}
		if(doesSkillExist()) {
			messageSelf("That spell already exists, either MODIFY that spell, choose a new spellname, or FORGET it.");
			return false;
		}
		if (improperName()) {
			messageSelf(spellName + " is not a valid name.");
			return false;
		}
		return true;
	}

	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new ArcanistCreate(currentPlayer, fullCommand);
	}

	private boolean doesSkillExist() {
		for (String s : currentBook.viewSkills().keySet()) {
			if (s.equalsIgnoreCase(spellName)) {
				return true;
			}
		}
		return false; 
	}
	
	private ArcanistSkillbook getCurrentBook() {
		for (SkillBook s : currentPlayer.viewSkillBooks().keySet()) {
			if (s instanceof ArcanistSkillbook) {
				return (ArcanistSkillbook)s;
			}
		}
		return null;
	}
	
	private boolean improperName() {
		return !UsefulCommands.checkIfValidCharacters(spellName);
	}
}