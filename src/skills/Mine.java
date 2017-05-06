package skills;

import java.util.Arrays;

import interfaces.Holdable;
import processes.InductionSkill;
import items.Mineable;

public class Mine extends InductionSkill {
	
	private String oreName;
	private Holdable oreItem;
	Mineable rock;

	public Mine() {
		super.name = "mine";
		super.description = "Mining ores and maybe other things.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}	
	
	@Override
	protected void performSkill() {
		oreName = Syntax.ITEM.getStringInfo(fullCommand, this);
		//check if already mining
		if (oreName.equals("")) {
			messageSelf("What are you trying to mine?");
			return;
		}
		//find the item
		oreItem = currentPlayer.getContainer().getHoldableFromString(oreName);
		if (oreItem == null) {
			messageSelf("You don't see a \"" + oreName + "\".");
			return;
		}
		//check if the item is a rock
		if (!(oreItem instanceof Mineable)) { 
			messageSelf("You cannot mine " + oreName + ".");
			return;
		}
		rock = (Mineable) oreItem;		
		scheduleSkillRepeatNTimesOverXMilliseconds(1, 5000); // Triggers this skill's "run()" in 5 seconds. Interruptible.
		currentPlayer.setInduction(this);
		messageSelf("You begin mining.");
		messageOthers(currentPlayer.getName() + " begins mining.", Arrays.asList(currentPlayer));
		
	}

	@Override
	public void run() {
		if(!rock.changeOres(1)) {
			messageSelf("There are no more ores left in this rock.");
			return;
		}
		messageSelf(rock.mine(currentPlayer));
	}

	@Override
	public void inductionKilled() {
		messageSelf("You suddenly look up from your labors.");
		
	}

}
