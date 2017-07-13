package skills;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.Mobile;
import items.StdItem;
import processes.Skills;
import processes.Equipment.EquipmentSlot;

public class Wield extends Skills {

	private EquipmentSlot wantSlot;
	private StdItem itemToWield;
	
	public Wield(Mobile currentPlayer, String fullCommand) {
		super("wield", "Wielding items.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.SLOT);
	}
	
	@Override
	protected void performSkill() {
		if (preSkillChecks()) {		
			currentPlayer.equip(wantSlot, itemToWield);
			messageSelf("You are now wielding the " + itemToWield.getName().toString() + " in your " + wantSlot.toString().toLowerCase() + ".");
			messageOthers(currentPlayer.getNameColored() + " is now wielding a " + itemToWield.getName() + 
					" in their " + wantSlot.toString().toLowerCase() + ".", Arrays.asList(currentPlayer));
			return;	
		}
	}

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		wantSlot = EquipmentSlot.fromString((Syntax.SLOT.getStringInfo(fullCommand, this)));
		if (wantSlot == null) {
			wantSlot = EquipmentSlot.RIGHTHAND;
		}
		//find if item exists in inv
		itemToWield = (StdItem)currentPlayer.getHoldableFromString(Syntax.ITEM.getStringInfo(fullCommand, this));
		if (itemToWield == null) {
			messageSelf("You can't find that item.");
			return false;
		}		
		if (!(itemToWield instanceof StdItem)) {
			messageSelf("You can't wield that. Not StdItem.");
			return false;
		}
		//find if item can be wielded in slot specified
		if (!itemToWield.getAllowedEquipSlots().contains(wantSlot)) {
			messageSelf("You can't wield that there.");
			return false;
		}
		//is bodypart broken/wieldable
		if (wantSlot.equals(EquipmentSlot.RIGHTHAND)) {
			if (currentPlayer.hasCondition(PassiveCondition.BROKENRIGHTARM)) {
				messageSelf("Your arm is broken, you can't wield right now.");
				return false;
			}
		} else if (wantSlot.equals(EquipmentSlot.LEFTHAND)) {
			if (currentPlayer.hasCondition(PassiveCondition.BROKENLEFTARM)) {
				messageSelf("Your arm is broken, you can't wield right now.");
				return false;
			}
		}
		return true;
	}
}
