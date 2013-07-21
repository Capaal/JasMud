package skills;

import processes.Command;
import processes.PlayerPrompt;

public class Icebolt implements Command {
	
	public Icebolt() {
		
	}
	
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
		playerPrompt.getSendBack().printMessage("Cast Icebolt.");
		System.out.println("Accessed icebolt");
	}
}
