package skills;

import java.util.Set;

import effects.ConditionsEnum;
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
			messageSelf("You can't find that item. Failed step 1.");
			return;
		}		
		if (!(itemToWield instanceof StdItem)) {
			messageSelf("You can't wield that. Failed step 2.");
			return;
		}
		//find if item can be wielded in slot specified
		Set<Equipment.EquipmentEnum> equippableSlots = itemToWield.getAllowedEquipSlots();
		if (equippableSlots == null) {
			messageSelf("You can't wield that. Failed step 3.");
			return;
		}
		//is bodypart broken/wieldable
		if (wantSlot.equals("righthand")) {
			if (currentPlayer.hasAllConditions(ConditionsEnum.BROKENRIGHTARM)) {
				messageSelf("Your arm is broken, you can't wield right now.");
				return;
			}
		} else if (wantSlot.equals("lefthand")) {
			if (currentPlayer.hasAllConditions(ConditionsEnum.BROKENLEFTARM)) {
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
