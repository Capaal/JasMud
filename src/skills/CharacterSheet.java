package skills;

import interfaces.Mobile;
import processes.Skills;
import processes.Skills.Syntax;

public class CharacterSheet extends Skills {
	
	public CharacterSheet(Mobile currentPlayer, String fullCommand) {
		super("character", "Your character's info.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
	}

	@Override
	protected void performSkill() {
		StringBuilder sb = new StringBuilder();
		sb.append("***********************************");
		sb.append("\n");
		sb.append("Character Info");
		sb.append("\n");
		sb.append("Name: ");
		sb.append(currentPlayer.getNameColored());
		sb.append("\n");
		sb.append("Health: ");
		sb.append(currentPlayer.getCurrentHp());
		sb.append("/");
		sb.append(currentPlayer.getMaxHp());
		sb.append("\n");
		sb.append("Mana: ");
		sb.append(currentPlayer.getCurrentMana());
		sb.append("/");
		sb.append("105"); // max mana?
		sb.append("\n");
		sb.append("Class: ");
		sb.append(currentPlayer.getClassName());
		sb.append("\n");
		sb.append("Description: ");
		sb.append(currentPlayer.getDescription());
		sb.append("\n");
		sb.append("***********************************");
		sb.append("\n");
		sb.append("More info: [SKILLS] [INVENTORY]");
		messageSelf(sb.toString());
		//add age, city?, race?
	}

	@Override
	protected boolean preSkillChecks() {
		return true;
	}

}
