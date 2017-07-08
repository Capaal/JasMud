package skills.Arcanist.Commands;

import interfaces.Mobile;
import processes.Skills;
import skills.Arcanist.Commands.ArcanistAlter.ArcanistComponentsFactory;

public class ArcanistComponents extends Skills {

	public ArcanistComponents(Mobile currentPlayer, String fullCommand) {
		super("components", "Displays available components and their workings", currentPlayer, fullCommand);
	}

	@Override
	protected void performSkill() {
		StringBuilder sb = new StringBuilder();
		for (ArcanistComponentsFactory c : ArcanistComponentsFactory.values()) {
			sb.append(c.describeYourself());
			sb.append(System.lineSeparator());
		}
		messageSelf(sb.toString());
	}

	@Override
	protected boolean preSkillChecks() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		// TODO Auto-generated method stub
		return new ArcanistComponents(currentPlayer, fullCommand);
	}

}
