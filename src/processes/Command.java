package processes;

// Command is used to designate anything that might be ran by the user. So whenever they type something it will always contain 1 command keyword.
// It then grabs that command and executes, oftentimes taking into account any extra details supplied by user.

// Example: say hello!   This will grab the command say and execute, which then display "hello!" to all other users in that location.

public interface Command {
	
	public void execute(PlayerPrompt playerPrompt, String fullCommand);
}
