package skills;

import processes.Command;
import processes.CommandProcess;

public class Icebolt implements Command {
	
	public Icebolt() {
		
	}
	
	public void execute(CommandProcess com, String fullCommand) {
		com.getSendBack().printMessage("Cast Icebolt.");
		System.out.println("Accessed icebolt");
	}
}
