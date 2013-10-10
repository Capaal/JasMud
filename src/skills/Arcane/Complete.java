package skills.Arcane;

import processes.Command;
import processes.PlayerPrompt;

public class Complete implements Command {

	@Override
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		((ArcaneBuilder)playerPrompt.getCurrentPlayer().getCommand("design")).complete();

	}

}
