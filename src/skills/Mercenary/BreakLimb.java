package skills.Mercenary;

import java.util.Arrays;
import effects.PassiveCondition;
import interfaces.Mobile;
import processes.Skills;
import processes.Type;
import processes.Equipment.EquipmentEnum;

public class BreakLimb extends Skills {

	private Mobile finalTarget;
	private EquipmentEnum slot;
	private final int intensity = 8;
	
	//check for: limb already broken, is item wielded, unwield item
	public BreakLimb() {
		super.name = "break";
		super.description = "Break an arm or leg.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.SLOT);
	}	
	
	@Override
	protected void performSkill() {
		String targetName = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (!hasBalance()) {return;}
		finalTarget = setTarget(targetName);
		if (finalTarget == null) {
			messageSelf("There is no " + targetName + " here for you to attack.");
			return;
		}
		if (isBlocking(finalTarget)) {return;}  // Probably not complete still
		if (!findSlotAndWeapon()) {
			messageSelf("What are you trying to break?");
			messageSelf("Syntax: BREAK <TARGET> <LEFTHAND/RIGHTHAND/LEG>");
			return;
		}
		//checks if slot specified is a hand, then checks if hand is already broken
		//sets limb to broken, unwield if hand
		if (slot.equals(EquipmentEnum.LEFTHAND)) {
			breakarm(PassiveCondition.BROKENLEFTARM);
		} else if (slot.equals(EquipmentEnum.RIGHTHAND)) {
			breakarm(PassiveCondition.BROKENRIGHTARM);
		} else if (slot.equals(EquipmentEnum.LEGS)) {
		//	messageTarget("Your legs are broken.", Arrays.asList(finalTarget));
			finalTarget.addAllConditions(PassiveCondition.BROKENLEGS);
		}
		//the regular stuff a damaging atk does
		finalTarget.informLastAggressor(currentPlayer);
		finalTarget.takeDamage(Type.BLUNT, calculateDamage());
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
		messageSelf("You target a specific limb and hit " + finalTarget.getName() + " really hard.");
		messageTarget(currentPlayer.getName() + " hits you with a targetted punch.", Arrays.asList(finalTarget));
		messageOthers(currentPlayer.getName() + " punches " + finalTarget.getName() + " harder than usual.", Arrays.asList(currentPlayer, finalTarget));
	}
	
	private Mobile setTarget(String targetName) {
		return currentPlayer.getContainer().getMobileFromString(targetName);
	}
	
	private boolean findSlotAndWeapon() {
		String toBreak = Syntax.SLOT.getStringInfo(fullCommand, this);
		slot = EquipmentEnum.fromString(toBreak);
		if (slot == null) {
			return false;
		}
		if (!slot.equals(EquipmentEnum.LEFTHAND) && !slot.equals(EquipmentEnum.RIGHTHAND) && !slot.equals(EquipmentEnum.LEGS)) {
			return false;
		}				
		return true;
	}
	
	private void breakarm(PassiveCondition brokenHand) {
		String hand = slot.toString().toLowerCase();
		if (finalTarget.hasAllConditions(brokenHand)) {
			messageSelf("That players " + hand + " is already broken.");
		} else {
			finalTarget.unEquip(slot);
			messageTarget("Your arm breaks and you can no longer wield a weapon.", Arrays.asList(finalTarget));
			finalTarget.addAllConditions(brokenHand);
		}
	}
	
	private int calculateDamage() {
		return intensity;
	}

}
