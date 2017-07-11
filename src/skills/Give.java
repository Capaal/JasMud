package skills;

import java.util.Arrays;

import Quests.Quest;
import Quests.Quest.Trigger;
import effects.PassiveCondition;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.ContainerErrors;
import processes.Skills;

public class Give extends Skills {
	
	private Holdable itemToMove;
	private Mobile mobileToGive;
	
	public Give(Mobile currentPlayer, String fullCommand) {
		super("give", "Giving items to others.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);	
		//add filler "to"? TODO
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.QUANTITY);
	}
	
	private String possItem;
	
	// Moves a HOLDABLE from the CURRENTPLAYER's INVENTORY to the MOBILE who is in currentPlayer's LOCATION.
	// Requires balance, syntax = "give goblin2334 dagger " or "give andrew sword1532 "
	@Override
	protected void performSkill() {
		possItem = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (preSkillChecks()) {
			int qty = -1;
			if (!Syntax.QUANTITY.getStringInfo(fullCommand, this).equals("")) {			
				try {
					qty = Integer.parseInt(Syntax.QUANTITY.getStringInfo(fullCommand, this)); 
				} catch (NumberFormatException fail) {
					System.out.println("User error: 'Give' optional qty not a number. Optional ignored.");
				}
			}
			if (qty == -1) { // defaults to full stack
				ContainerErrors err = itemToMove.moveHoldable((Mobile)mobileToGive);
				if (err == null) {
					messageSelf("You give " + itemToMove.getName() + " to " + mobileToGive.getNameColored() + ".");
					messageTarget(currentPlayer.getNameColored() + " gives you " + itemToMove + ".", Arrays.asList(mobileToGive));
					messageOthers(currentPlayer.getNameColored() + " gives " + itemToMove.getName() + " to " + mobileToGive.getNameColored() + ".", Arrays.asList(currentPlayer));
				} else {
					System.out.println(currentPlayer.getName() + " is holding an item that can't be dropped: " + itemToMove.getName());
					messageSelf(err.display(itemToMove.getName()));
				}
			} else {
				ContainerErrors err = itemToMove.moveHoldable((Mobile)mobileToGive, qty);
				if (err == null) {
					messageSelf("You give " + qty + " " + itemToMove.getName() + " to " + mobileToGive.getNameColored() + ".");
					messageTarget(currentPlayer.getNameColored() + " gives you " + qty + " " + itemToMove + ".", Arrays.asList(mobileToGive));
					messageOthers(currentPlayer.getNameColored() + " gives " + itemToMove.getName() + " to " + mobileToGive.getNameColored() + ".", Arrays.asList(currentPlayer));
				} else {
					System.out.println(currentPlayer.getName() + " is holding an item that can't be dropped: " + itemToMove.getName());
					messageSelf(err.display(itemToMove.getName()));
				}
			}		
			questCares(itemToMove, (Mobile)mobileToGive);		
		}
	}
	
	private boolean brokenArms() {
		if (currentPlayer.hasCondition(PassiveCondition.BROKENLEFTARM) && currentPlayer.hasCondition(PassiveCondition.BROKENRIGHTARM)) {
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

	@Override
	protected boolean preSkillChecks() {
		if (possItem.equals("")) {
			messageSelf("Give what to who? Syntax: GIVE [item] [player]");
			return false;
		}
		if (!hasBalance()) {return false;}
		if (brokenArms()) {return false;}
		itemToMove = currentPlayer.getHoldableFromString(possItem);
		if (itemToMove == null) {
			messageSelf("You can't find that item.");
			return false;
		}
		mobileToGive = currentPlayer.getContainer().getMobileFromString(Syntax.TARGET.getStringInfo(fullCommand, this));
		if (mobileToGive == null) {
			messageSelf("You can't find that person to give " + itemToMove.getName() + ".");
			return false;
		}
		return true;
	}
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Give(currentPlayer, fullCommand);
	}
	
	@Override
	public String displaySyntax() {
		return "GIVE [ITEM] [TARGET] (QTY)";
	}
}
