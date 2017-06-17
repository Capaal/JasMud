package skills;

import java.util.Arrays;

import interfaces.Holdable;
import processes.InductionSkill;
import items.Harvestable;
import items.Harvestable.HarvestType;

public class Mine extends InductionSkill {
	
	private String oreName;
	private Holdable oreItem;
	private Harvestable rock;

	public Mine() {
		super.name = "mine";
		super.description = "Mining ores and maybe other things.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}	
	
	public class InnerMine extends InnerSkill {
		
		@Override
		public void performSkill() {
			if(!rock.changeRemaining(1)) {
				messageSelf("There are no more ores left in this rock.");
				return;
			}
			rock.harvest(currentPlayer);	
		}
	}
	
	@Override
	public InnerSkill getInnerSkill() {
		return new InnerMine();
	}
	
	@Override
	protected void performSkill() {
		oreName = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (preSkillChecks()) {
			rock = (Harvestable) oreItem;		
			scheduleInduction(1, 5000); // Triggers this skill's "run()" in 5 seconds. Interruptible.
			currentPlayer.setInduction(this);
			messageSelf("You begin mining.");
			messageOthers(currentPlayer.getName() + " begins mining.", Arrays.asList(currentPlayer));
		}
		
	}

	@Override
	public void inductionKilled() {
		messageSelf("You suddenly look up from your labors.");
		
	}

	@Override
	protected void inductionEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean preSkillChecks() {
		//check if already mining
		if (oreName.equals("")) {
			messageSelf("What are you trying to mine?");
			return false;
		}
		//find the item
		oreItem = currentPlayer.getContainer().getHoldableFromString(oreName);
		if (oreItem == null) {
			messageSelf("You don't see a \"" + oreName + "\".");
			return false;
		}
		//check if the item is a rock
		if (oreItem instanceof Harvestable) { 
			Harvestable item = (Harvestable) oreItem;
			if (!(item.getType().equals(HarvestType.IRON))) {
				messageSelf("You cannot mine " + oreName + ".");
				return false;
			}
		}
		return true;
	}

}
