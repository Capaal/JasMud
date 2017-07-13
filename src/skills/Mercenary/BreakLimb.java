package skills.Mercenary;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.InformsAggro;
import interfaces.Mobile;
import processes.Equipment.EquipmentSlot;
import processes.Skills;

//TODO only breaks arms, legs disabled.
public class BreakLimb extends Skills implements InformsAggro {

	private Mobile finalTarget;
	private EquipmentSlot slot;
	private final int intensity = 8;
	private String targetName;
	
	//check for: limb already broken, is item wielded, unwield item
	public BreakLimb(Mobile currentPlayer, String fullCommand) {
		super("break", "Break an arm or leg.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.SLOT);
	}	
	
	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			//checks if slot specified is a hand, then checks if hand is already broken
			//sets limb to broken, unwield if hand
			if (slot.equals(EquipmentSlot.LEFTHAND)) {
				breakarm(PassiveCondition.BROKENLEFTARM);
			} else if (slot.equals(EquipmentSlot.RIGHTHAND)) {
				breakarm(PassiveCondition.BROKENRIGHTARM);
	//		} else if (slot.equals(EquipmentSlot.LEGS)) {
			//	messageTarget("Your legs are broken.", Arrays.asList(finalTarget));
//				finalTarget.addPassiveCondition((PassiveCondition.BROKENLEGS),-1);
			}
			//the regular stuff a damaging atk does
			finalTarget.takeDamage(calculateDamage());
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
			messageSelf("You target a specific limb and hit " + finalTarget.getNameColored() + " really hard.");
		//	messageTarget(currentPlayer.getName() + " hits you with a targetted punch.", Arrays.asList(finalTarget));
			messageOthers(currentPlayer.getNameColored() + " punches " + finalTarget.getNameColored() + " harder than usual.", Arrays.asList(currentPlayer, finalTarget));
			informLastAggressor(currentPlayer, finalTarget);
		}
	}
	
	private Mobile setTarget(String targetName) {
		return currentPlayer.getContainer().getMobileFromString(targetName);
	}
	
	private boolean findSlotAndWeapon() {
		String toBreak = Syntax.SLOT.getStringInfo(fullCommand, this);
		slot = EquipmentSlot.fromString(toBreak);
		if (slot == null) {
			return false;
		}
		if (!slot.equals(EquipmentSlot.LEFTHAND) && !slot.equals(EquipmentSlot.RIGHTHAND) /*&& !slot.equals(EquipmentSlot.LEGS)*/) {
			return false;
		}				
		return true;
	}
	
	private void breakarm(PassiveCondition brokenHand) {
		String hand = slot.toString().toLowerCase();
		if (finalTarget.hasCondition(brokenHand)) {
			messageSelf("That players " + hand + " is already broken.");
		} else {
			finalTarget.unEquip(slot);
			messageTarget("Your arm breaks and you can no longer wield a weapon.", Arrays.asList(finalTarget));
			finalTarget.addPassiveCondition((brokenHand),-1);
		}
	}
	
	private int calculateDamage() {
		return intensity;
	}

	@Override
	protected boolean preSkillChecks() {
		targetName = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (targetName.equals("")) {
			messageSelf("What are you trying to attack?");
			return false;
		}
		if (!hasBalance()) {return false;}
		finalTarget = setTarget(targetName);
		if (finalTarget == null) {
			messageSelf("There is no " + targetName + " here for you to attack.");
			return false;
		}
		if (isBlocking(finalTarget)) {return false;}  // Probably not complete still
		if (!findSlotAndWeapon()) {
			messageSelf("What are you trying to break?");
			messageSelf("Syntax: BREAK <TARGET> <LEFTHAND/RIGHTHAND/LEG>");
			return false;
		}
		return true;
	}
}
