package skills;

import java.util.Arrays;

import interfaces.Holdable;
import interfaces.Mobile;
import items.Plant;
import items.Weapon;
import processes.Skills;
import processes.Skills.Syntax;

public class Apply extends Skills {
	
	private String itemToApplyName;
	private Holdable itemToApply;
	private Holdable target;
	
	public Apply(Mobile currentPlayer, String fullCommand) {
		super("apply", "Applying herbs and the such.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.FILLER);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.QUANTITY);
	}

	@Override
	protected void performSkill() {
		if(!preSkillChecks()) {return;}
		if (!(itemToApply instanceof Plant)) {
			messageSelf("You can't apply that.");
			return;
		}
		if (!(target instanceof Weapon)) {
			messageSelf("Applying the " + itemToApplyName + "won't have any affect.");
			return;
		}
		
		String quantityToApply = Syntax.QUANTITY.getStringInfo(fullCommand, this);
		int quantity = 1;
		if (!quantityToApply.isEmpty()) {
			quantity = Integer.parseInt(quantityToApply);
			if (quantity > itemToApply.getQuantity()) {
				quantity = itemToApply.getQuantity();
			}
		}
		
		Plant plant = (Plant) itemToApply;
		for (int i=0; i<=quantity; i++) {
			((Weapon)target).getAppliedList().add(plant);
		}
		plant.removeFromStack(quantity);
		messageSelf("You carefully apply the " + itemToApply.getName() + " to your " + target.getName() + ".");
		messageOthers(currentPlayer.getNameColored() + " applies a " + itemToApply.getName() + " to a " + target.getName() + ".", Arrays.asList(currentPlayer));
		
	}
	
	//checks all syntax and sets all starting variables
	protected boolean preSkillChecks() {
		itemToApplyName = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (itemToApplyName.equals("")) {
			messageSelf("Apply what?");
			return false;
		}
		//find the item
		if (!checkItem()) {
			messageSelf("You do not have a \"" + itemToApplyName + "\".");
			System.out.println("Apply precheck: item not in inv.");
			return false;
		} 
		//check filler word
		String in = Syntax.FILLER.getStringInfo(fullCommand, this);
		if (!in.equalsIgnoreCase("on")) {
			messageSelf("Syntax: APPLY (ITEM) ON (WEAPON).");
			System.out.println("Apply precheck: invalid filler (on).");
			return false;
		}
		//check target name
		String possibleTarget = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (possibleTarget.equals("")) {
			messageSelf("What are you trying to apply that to?");
			System.out.println("Apply precheck: no target weapon.");
			return false;
		} 		
		//find target weapon
		target = currentPlayer.getHoldableFromString(possibleTarget);
		if (target == null) {
			messageSelf("You don't see a \"" + possibleTarget + "\".");
			return false;
		}
		
		return true;
	}
	
	
	private boolean checkItem() {
		itemToApply = currentPlayer.getHoldableFromString(itemToApplyName);
		if (itemToApply == null) {
			return false;
		}
		return true;
	}

	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Apply(currentPlayer, fullCommand);
	}

}
