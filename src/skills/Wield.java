package skills;

import java.util.Set;

import effects.PassiveCondition;
import interfaces.Container;
import interfaces.Mobile;
import items.StdItem;
import processes.Equipment;
import processes.Skills;


public class Wield extends Skills {

	private String wantSlot;
	private StdItem itemToWield;
	private Set<Equipment.EquipmentEnum> equippableSlots;
	
	public Wield(Mobile currentPlayer, String fullCommand) {
		super("wield", "Wielding items.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.SLOT);
	}
	
	@Override
	protected void performSkill() {
		wantSlot = Syntax.SLOT.getStringInfo(fullCommand, this);
		if (preSkillChecks()) {		
			for (Equipment.EquipmentEnum s : equippableSlots) {
				if (wantSlot.equalsIgnoreCase(s.toString())) {
					currentPlayer.equip(s, itemToWield);
					messageSelf("You are now wielding the " + itemToWield.getName() + " in your " + s.toString().toLowerCase() + ".");
					return;
				}
			}
			messageSelf("You can't wield that item there.");
		}
	//	Equipment.valueof(Syntax.SLOT.getStringInfo(fullCommand, this));
	}

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		if(wantSlot.equals("")) {wantSlot = "righthand";} //defaulting righthand
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
		equippableSlots = itemToWield.getAllowedEquipSlots();
		if (equippableSlots == null) {
			messageSelf("You can't wield that. Not equippable.");
			return false;
		}
		//is bodypart broken/wieldable
		if (wantSlot.equals("righthand")) {
			if (currentPlayer.hasCondition(PassiveCondition.BROKENRIGHTARM)) {
				messageSelf("Your arm is broken, you can't wield right now.");
				return false;
			}
		} else if (wantSlot.equals("lefthand")) {
			if (currentPlayer.hasCondition(PassiveCondition.BROKENLEFTARM)) {
				messageSelf("Your arm is broken, you can't wield right now.");
				return false;
			}
		}
		return true;
	}
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Wield(currentPlayer, fullCommand);
	}
}
