package processes;

public interface Command {
	public void execute(PlayerPrompt playerPrompt, String fullCommand);
}
