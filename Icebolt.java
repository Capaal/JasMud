
public class Icebolt implements Command {
	public void execute(CommandProcess com) {
		com.sendBack.printMessage("Cast Icebolt.");
		System.out.println("Accessed icebolt");
	}
}
