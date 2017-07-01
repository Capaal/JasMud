package skills;

import java.util.Arrays;

import interfaces.Mobile;
import processes.Skills;
import processes.UsefulCommands;
import processes.Skills.Syntax;

public class RollDice extends Skills {
	
	String sides;

	public RollDice(Mobile currentPlayer, String fullCommand) {
		super("roll", "Roll a die or two.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.QUANTITY);
	}

	@Override
	protected void performSkill() {
		sides = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (preSkillChecks()) {
			String quantityDice = Syntax.QUANTITY.getStringInfo(fullCommand, this);
			int qty = 1;
			if (UsefulCommands.isInteger(quantityDice)) {
				qty = Integer.parseInt(quantityDice);
			}
			for (int i=1;i<=qty;i++) {
				int roll = (int) (Math.random() * Integer.parseInt(UsefulCommands.getOnlyNumerics(sides))) + 1;
				messageSelf("You roll a " + sides.toUpperCase() +  " dice and it comes to: " +  roll);
				messageOthers(currentPlayer.getName() + "rolls and " + sides.toUpperCase() + " dice and it comes to: " +  roll, Arrays.asList(currentPlayer));
			}
		}
		
	}

	@Override
	protected boolean preSkillChecks() {
		//checks for extra characters other than numbers and 'D' - example false for D2O0
		if (sides.equals("")) {
			messageSelf("Syntax: ROLL D# (optional # of dice)");
			return false;
		}
		if (!sides.substring(0, 1).equalsIgnoreCase("d") || !(sides.length()>1) || (UsefulCommands.getOnlyNumerics(sides).length()+1)<sides.length()) { //yeah that's ugly
			messageSelf("Specify # of sides on the die. Syntax: ROLL D# (optional # of dice)");
			return false;
		}
		return true;
	}

	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new RollDice(currentPlayer, fullCommand);
	}

}
