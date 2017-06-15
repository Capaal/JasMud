package skills;

import java.util.Arrays;

import Quests.Quest;
import Quests.Quest.Trigger;
import effects.PassiveCondition;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;
import processes.Skills.Syntax;

public class Give extends Skills {
	
	public Give() {
		super.name = "give";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.ITEM);	
		super.syntaxList.add(Syntax.QUANTITY);
	}
	
	String possItem;
	
	// Moves a HOLDABLE from the CURRENTPLAYER's INVENTORY to the MOBILE who is in currentPlayer's LOCATION.
	// Requires balance, syntax = "give goblin2334 dagger " or "give andrew sword1532 "
	@Override
	protected void performSkill() {
		possItem = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (possItem.equals("")) {
			messageSelf("Give what to who? Syntax: GIVE [item] [player]");
			return;
		}
		if (!hasBalance()) {return;}
		if (brokenArms()) {return;}
		Holdable itemToMove = currentPlayer.getHoldableFromString(possItem);
		if (itemToMove == null) {
			messageSelf("You can't find that item.");
			return;
		}
		Mobile mobileToGive = currentPlayer.getContainer().getMobileFromString(Syntax.TARGET.getStringInfo(fullCommand, this));
		if (mobileToGive == null) {
			messageSelf("You can't find that person to give " + itemToMove.getName() + ".");
			return;
		}
		int qty = -1;
		if (!Syntax.QUANTITY.getStringInfo(fullCommand, this).equals("")) {			
			try {
				qty = Integer.parseInt(Syntax.QUANTITY.getStringInfo(fullCommand, this)); 
			} catch (NumberFormatException fail) {
				System.out.println("User error: 'Give' optional qty not a number. Optional ignored.");
			}
		}
		if (qty == -1) { // defaults to full stack
			itemToMove.moveHoldable((Mobile)mobileToGive);
			messageSelf("You give " + itemToMove.getName() + " to " + mobileToGive.getName() + ".");
			messageTarget(currentPlayer.getName() + " gives you " + itemToMove + ".", Arrays.asList((Mobile)mobileToGive));
			messageOthers(currentPlayer.getName() + " gives " + itemToMove.getName() + " to " + (Mobile)mobileToGive + ".", Arrays.asList(currentPlayer));
		} else {
			itemToMove.moveHoldable((Mobile)mobileToGive, qty);
			messageSelf("You give " + qty + " " + itemToMove.getName() + " to " + mobileToGive.getName() + ".");
			messageTarget(currentPlayer.getName() + " gives you " + qty + " " + itemToMove + ".", Arrays.asList((Mobile)mobileToGive));
			messageOthers(currentPlayer.getName() + " gives " + itemToMove.getName() + " to " + (Mobile)mobileToGive + ".", Arrays.asList(currentPlayer));
		}		
		questCares(itemToMove, (Mobile)mobileToGive);		
	}
	
	private boolean brokenArms() {
		if (currentPlayer.hasAllConditions(PassiveCondition.BROKENLEFTARM) && currentPlayer.hasAllConditions(PassiveCondition.BROKENRIGHTARM)) {
			messageSelf("Your arms are broken!");
			return true;
		} 
		return false;
	}
	
	private void questCares(Holdable item, Mobile mobile) {
		Quest locationQuest = currentPlayer.getContainer().getQuest();
		if (locationQuest != null) {
			if (locationQuest.stateRequiresItem(item, mobile)) {
				locationQuest.triggered(Trigger.GIVES);	
			}
		}
	}
}
