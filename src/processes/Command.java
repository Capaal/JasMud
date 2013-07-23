package processes;

public interface Command {
	
	public String defaultName = "DefaultCommand";
	
	public void execute(PlayerPrompt playerPrompt, String fullCommand);
}
