package skills;

import interfaces.Mobile;
import processes.Skills;
import processes.Equipment.EquipmentEnum;

public class Unwield extends Skills {

	private String wantSlot;
	
	public Unwield (Mobile currentPlayer, String fullCommand) {
		super("unwield", "Unwielding items in your hands.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.SLOT);
	}
	
	@Override
	protected void performSkill() {
		if(!preSkillChecks()) {return;}
		
		//this doesn't work right now, just deletes the equipped thing
		if (wantSlot.equals("")) { 
			wantSlot = "righthand"; 
			if (currentPlayer.getEquipment().getValue(EquipmentEnum.RIGHTHAND) == null) {
				tryLeft();
			} else {
				tryRight();
			}
		} else if (wantSlot.equals("righthand")) {
			if (currentPlayer.getEquipment().getValue(EquipmentEnum.RIGHTHAND) != null) {
				tryRight();
			} else {
				messageSelf("You are not wielding anything in your right hand.");
				return;
			}
		} else if (wantSlot.equals("lefthand")) {
			if (currentPlayer.getEquipment().getValue(EquipmentEnum.LEFTHAND) != null) {
				tryLeft();
			} else {
				messageSelf("You are not wielding anything in your left hand.");
				return;
			}
		}
	
	}
	
	private void tryRight() {
		messageSelf("You unwield a " + currentPlayer.getEquipment().getValue(EquipmentEnum.RIGHTHAND).toString() + ".");
		currentPlayer.unEquip(EquipmentEnum.RIGHTHAND);
	}
	
	private void tryLeft() {
		messageSelf("You unwield a " + currentPlayer.getEquipment().getValue(EquipmentEnum.LEFTHAND).toString() + ".");
		currentPlayer.unEquip(EquipmentEnum.LEFTHAND);
	}
	
	@Override
	protected boolean preSkillChecks() {
		wantSlot = Syntax.SLOT.getStringInfo(fullCommand, this);
		//if (!hasBalance()) {return;} - don't need balance to unwield, shrug
		if (currentPlayer.getEquipment().getValue(EquipmentEnum.RIGHTHAND) == null && currentPlayer.getEquipment().getValue(EquipmentEnum.LEFTHAND) == null) {
			messageSelf("You're not wielding anything.");
			return false;
		}
		return true;
	}
	
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Unwield(currentPlayer, fullCommand);
	}
}