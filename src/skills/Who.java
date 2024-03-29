package skills;

import interfaces.Mobile;
import processes.PlayerPrompt;
import processes.Skills;
import processes.UsefulCommands;
import processes.WorldServer;

public class Who extends Skills {
	public Who(Mobile currentPlayer, String fullCommand) {
		super("who", "Shows logged in players.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
	}
	
	@Override
	protected void performSkill() {
		StringBuilder sb = new StringBuilder();
		sb.append("All players online: \n");
		for (PlayerPrompt m : WorldServer.getGameState().viewActiveClients()) {
			sb.append(m.getPlayer().getNameColored());
			sb.append(" at: ");
			sb.append(UsefulCommands.ANSI.MAGENTA + m.getPlayer().getContainer().getName() + UsefulCommands.ANSI.SANE);
			sb.append("\n");
		}
		messageSelf(sb.toString());
	}

	@Override
	protected boolean preSkillChecks() {
		return true;
	}
}
