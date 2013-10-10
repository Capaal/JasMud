package skills.Arcane;

import processes.Command;
import processes.PlayerPrompt;
import processes.UsefulCommands;

public class Adjust implements Command {

	@Override
	public void execute(PlayerPrompt playerPrompt, String fullCommand) {
	//	String toCast = UsefulCommands.getSecondWord(fullCommand);
		ArcaneBuilder spell = ((ArcaneBuilder)playerPrompt.getCurrentPlayer().getCommand("design"));
		String toChange = UsefulCommands.getSecondWord(fullCommand);
		String theChange = UsefulCommands.getThirdWord(fullCommand);
		switch(toChange) {
			case "damage":
				spell.setDamage(theChange);
				break;
			case "mana":
				spell.setMana(theChange);
				break;
			case "life":
				spell.setLife(theChange);
				break;
			case "speed":
				spell.setSpeed(theChange);
				break;
			case "cost":
				spell.setCost(theChange);
				break;
			case "effect":
				spell.setEffect(theChange);
		}
	}
}
