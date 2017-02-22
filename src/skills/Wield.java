package skills;

import java.util.Set;

import effects.PassiveCondition;
import interfaces.Container;
import interfaces.Holdable;
import items.StdItem;
import processes.Equipment;
import processes.Skills;
import processes.Equipment.EquipmentEnum;
import processes.Skills.Syntax;

public class Wield extends Skills {

	private String wantSlot;
	
	public Wield() {
		super.name = "wield";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.SLOT);
	}
	
	@Override
	protected void performSkill() {
		wantSlot = Syntax.SLOT.getStringInfo(fullCommand, this);
		if (!hasBalance()) {return;}
		if(wantSlot.equals("")) {messageSelf("Specify where to wield."); return;}
		//find if item exists in inv
		Container here = currentPlayer;
		StdItem itemToWield = (StdItem)here.getHoldableFromString(Syntax.ITEM.getStringInfo(fullCommand, this));
		if (itemToWield == null) {
			messageSelf("You can't find that item.");
			return;
		}		
		if (!(itemToWield instanceof StdItem)) {
			messageSelf("You can't wield that. Not StdItem.");
			return;
		}
		//find if item can be wielded in slot specified
		Set<Equipment.EquipmentEnum> equippableSlots = itemToWield.getAllowedEquipSlots();
		if (equippableSlots == null) {
			messageSelf("You can't wield that. Not equippable.");
			return;
		}
		//is bodypart broken/wieldable
		if (wantSlot.equals("righthand")) {
			if (currentPlayer.hasAllConditions(PassiveCondition.BROKENRIGHTARM)) {
				messageSelf("Your arm is broken, you can't wield right now.");
				return;
			}
		} else if (wantSlot.equals("lefthand")) {
			if (currentPlayer.hasAllConditions(PassiveCondition.BROKENLEFTARM)) {
				messageSelf("Your arm is broken, you can't wield right now.");
				return;
			}
		}
		
		for (Equipment.EquipmentEnum s : equippableSlots) {
			if (wantSlot.equalsIgnoreCase(s.toString())) {
				currentPlayer.equip(s, itemToWield);
				messageSelf("You are now wielding the " + itemToWield.getName() + " in your " + s.toString().toLowerCase() + ".");
				return;
			}
		}
		messageSelf("You can't wield that item there.");
		
	//	Equipment.valueof(Syntax.SLOT.getStringInfo(fullCommand, this));
	}

}
