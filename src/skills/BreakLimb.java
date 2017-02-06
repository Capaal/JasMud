package skills;

import java.util.Arrays;

import effects.Balance;
import interfaces.Holdable;
import interfaces.Mobile;
import items.StdItem;
import processes.Equipment;
import processes.Skills;
import processes.Type;
import processes.Equipment.EquipmentEnum;
import processes.Skills.Syntax;

public class BreakLimb extends Skills {

	private String targetName;
	private Mobile finalTarget;
	private EquipmentEnum slot;
	private final int intensity = 8;
	
	public BreakLimb() {
		super.name = "break";
		super.description = "Break an arm or leg.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.SLOT);
	}	
	
	@Override
	protected void performSkill() {
		targetName = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (!hasBalance()) {return;}
		setTarget();
		if (finalTarget == null) {
			messageSelf("There is no " + targetName + " here for you to attack.");
			return;
		}
		if (isBlocking(finalTarget)) {  // Probably not complete still
			return;
		}
		if (!findSlotAndWeapon()) {
			messageSelf("What are you trying to break?");
			messageSelf("Syntax: BREAK <TARGET> <LEFTHAND/RIGHTHAND/LEG>");
			return;
		}
			finalTarget.unEquip(slot);
			messageTarget("Your arm breaks and you can no longer wield a weapon.", Arrays.asList(finalTarget));
			//make slot unwieldable - later
		
		finalTarget.informLastAggressor(currentPlayer);
		finalTarget.takeDamage(Type.BLUNT, calculateDamage());
		currentPlayer.addEffect(new Balance(), 3000);
		messageSelf("You target a specific limb and hit " + finalTarget.getName() + " really hard.");
		messageTarget(currentPlayer.getName() + " hits you with a targetted punch.", Arrays.asList(finalTarget));
		messageOthers(currentPlayer.getName() + " punches " + finalTarget.getName() + " harder than usual.", Arrays.asList(currentPlayer, finalTarget));
	}
	
	private void setTarget() {
		finalTarget = null;
		Holdable h = currentPlayer.getContainer().getHoldableFromString(targetName);
		if (h != null && h instanceof Mobile) {
			finalTarget = (Mobile)h;
		}			
	}
	
	private boolean findSlotAndWeapon() {
		String toBreak = Syntax.SLOT.getStringInfo(fullCommand, this);
		slot = EquipmentEnum.fromString(toBreak);
		if (slot == null) {
			return false;
		}
		if (!slot.equals(EquipmentEnum.LEFTHAND) && !slot.equals(EquipmentEnum.RIGHTHAND)) {
			return false;
		}
				
		return true;
	}
	
	private int calculateDamage() {
		return intensity;
	}

}