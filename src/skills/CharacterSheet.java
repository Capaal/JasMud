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
		sb.append("Name: ");
		sb.append(currentPlayer.getName());
		sb.append("\n");
		sb.append("Health: ");
		sb.append(currentPlayer.getCurrentHp());
		sb.append("/");
		sb.append(currentPlayer.getMaxHp());
		sb.append("\n");
		sb.append("Class: ");
		sb.append(currentPlayer.getClassName());
		sb.append("\n");
		sb.append("Description: ");
		sb.append(currentPlayer.getDescription());
		sb.append("\n");
		sb.append("More info: [SKILLS] [INVENTORY]");
		messageSelf(sb.toString());
	}

	@Override
	protected boolean preSkillChecks() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new CharacterSheet(currentPlayer, fullCommand);
	}

}
