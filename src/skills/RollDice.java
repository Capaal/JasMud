package skills;

import java.util.Arrays;

import interfaces.Mobile;
import processes.Skills;
import processes.UsefulCommands;
import processes.Skills.Syntax;

public class RollDice extends Skills {
	
	String diceWanted;
	int numOfDice;
	int sidesOfDice;

	public RollDice(Mobile currentPlayer, String fullCommand) {
		super("roll", "Roll a die or two.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}

	@Override
	protected void performSkill() {
		diceWanted = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (preSkillChecks()) {
			for (int i=1;i<=numOfDice;i++) {
				int roll = (int) (Math.random() * sidesOfDice) + 1;
				messageSelf("You roll a " + diceWanted.toUpperCase() +  " dice and it comes to: " +  roll);
				messageOthers(currentPlayer.getName() + "rolls and " + diceWanted.toUpperCase() + " dice and it comes to: " +  roll, Arrays.asList(currentPlayer));
			}
		}
	}


	String failMsg = "Syntax: ROLL #D# (Example: Roll 2D20 or Roll D20)";
	
	@Override
	protected boolean preSkillChecks() {
		//checks for extra characters other than numbers and 'D' - example false for D2O0
		if (diceWanted.equals("")) {
			messageSelf(failMsg);
			return false;
		}
		numOfDice = 1;
		String[] splitWords = diceWanted.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		if (splitWords.length==3 && UsefulCommands.isInteger(splitWords[0]) && splitWords[1].equalsIgnoreCase("d") && UsefulCommands.isInteger(splitWords[2])) {
			numOfDice = Integer.parseInt(splitWords[0]);
			sidesOfDice = Integer.parseInt(splitWords[2]);
		} else if (splitWords.length==2 && splitWords[0].equalsIgnoreCase("d") && UsefulCommands.isInteger(splitWords[1])) {
			sidesOfDice = Integer.parseInt(splitWords[1]);
		} else {
			messageSelf(failMsg);
			return false;
		}
		
	//	if (!diceWanted.substring(0, 1).equalsIgnoreCase("d") || !(diceWanted.length()>1) || (UsefulCommands.getOnlyNumerics(diceWanted).length()+1)<diceWanted.length()) { //yeah that's ugly
	//		messageSelf("Specify # of sides on the die. Syntax: ROLL D# (optional # of dice)");
	//		return false;
	//	}
		return true;
	}

	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new RollDice(currentPlayer, fullCommand);
	}

}
