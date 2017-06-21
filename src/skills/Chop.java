package skills;

import java.util.Arrays;

import interfaces.Holdable;
import interfaces.Mobile;
import processes.InductionSkill;
import processes.Skills;
import items.Harvestable;
import items.Harvestable.HarvestType;

public class Chop extends InductionSkill {
	
	private String treeName;
	private Holdable treeItem;
	private Harvestable tree;

	public Chop(Mobile currentPlayer, String fullCommand) {
		super("chop", "Chopping trees and maybe other things.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
	}	
	
	public class InnerChop extends InnerSkill {
		
		public InnerChop(Mobile currentPlayer, String fullCommand) {
			super(currentPlayer, fullCommand);
		}

		@Override
		public void performSkill() {
			if(!tree.changeRemaining(1)) {
				messageSelf("The tree needs time to regrow.");
				return;
			}
			tree.harvest(currentPlayer);
		}

		@Override
		public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
			return new InnerChop(currentPlayer, fullCommand);
		}
		
	}
	
	@Override
	protected void performSkill() {
		treeName = Syntax.ITEM.getStringInfo(fullCommand, this);
		//check if already doing?
		if (preSkillChecks()) {
			tree = (Harvestable) treeItem;		
			scheduleInduction(1, 5000); // Triggers this skill's "run()" in 5 seconds. Interruptible.
			currentPlayer.setInduction(this);
			messageSelf("You begin chopping.");
			messageOthers(currentPlayer.getName() + " begins chopping.", Arrays.asList(currentPlayer));
		}
	}
	
	@Override
	protected boolean preSkillChecks() {
		//check if already doing?
		if (treeName.equals("")) {
			messageSelf("What are you trying to chop?");
			return false;
		}
		//find the item
		treeItem = currentPlayer.getContainer().getHoldableFromString(treeName);
		if (treeItem == null) {
			messageSelf("You don't see a \"" + treeName + "\".");
			return false;
		}
		//check if the item is a tree
		if (treeItem instanceof Harvestable) { 
			Harvestable item = (Harvestable) treeItem;
			if (!(item.getType().equals(HarvestType.WOOD))) {
				messageSelf("You cannot chop \"" + treeName + "\".");
				return false;
			}
		}
		return true;
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
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Chop(currentPlayer, fullCommand);
	}

	@Override
	public InnerSkill getInnerSkill(Mobile currentPlayer, String fullCommand) {
		return new InnerChop(currentPlayer, fullCommand);
	}

}
