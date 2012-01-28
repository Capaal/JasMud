package processes;

public interface Command {
	public void execute(CommandProcess com, String fullCommand);
}
