package skills;

import java.util.Arrays;

import interfaces.Holdable;
import processes.InductionSkill;
import items.Harvestable;
import items.Harvestable.HarvestType;

public class Chop extends InductionSkill {
	
	private String treeName;
	private Holdable treeItem;
	private Harvestable tree;

	public Chop() {
		super.name = "chop";
		super.description = "Chopping trees and maybe other things.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}	
	
	@Override
	protected void performSkill() {
		treeName = Syntax.ITEM.getStringInfo(fullCommand, this);
		//check if already doing
		if (treeName.equals("")) {
			messageSelf("What are you trying to chop?");
			return;
		}
		//find the item
		treeItem = currentPlayer.getContainer().getHoldableFromString(treeName);
		if (treeItem == null) {
			messageSelf("You don't see a \"" + treeName + "\".");
			return;
		}
		//check if the item is a tree
		if (treeItem instanceof Harvestable) { 
			Harvestable item = (Harvestable) treeItem;
			if (!(item.getType().equals(HarvestType.WOOD))) {
				messageSelf("You cannot chop \"" + treeName + "\".");
				return;
			}
		}
		
		tree = (Harvestable) treeItem;		
		scheduleInduction(1, 5000); // Triggers this skill's "run()" in 5 seconds. Interruptible.
		currentPlayer.setInduction(this);
		messageSelf("You begin chopping.");
		messageOthers(currentPlayer.getName() + " begins chopping.", Arrays.asList(currentPlayer));
		
	}

	@Override
	public void run() {
		if(!tree.changeRemaining(1)) {
			messageSelf("The tree needs time to regrow.");
			return;
		}
		tree.harvest(currentPlayer);
	}

	@Override
	public void inductionKilled() {
		messageSelf("You suddenly look up from your labors.");
		
	}

	@Override
	protected void inductionEnded() {
		// TODO Auto-generated method stub
		
	}

}
