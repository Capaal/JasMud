package skills;

import interfaces.Holdable;
import interfaces.Mobile;
import processes.Equipment.EquipmentSlot;
import processes.Skills;

public class Unwield extends Skills {

	private EquipmentSlot wantSlot;
	private Holdable itemToUnwield;
	
	public Unwield (Mobile currentPlayer, String fullCommand) {
		super("unwield", "Unwielding items in your hands.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.ITEM);
		super.syntaxList.add(Syntax.SLOT);
	}
	
	@Override
	protected void performSkill() {
		if(!preSkillChecks()) {return;}
		currentPlayer.unEquip(wantSlot);
		messageSelf("You unwield a " + itemToUnwield.getName().toString() + " from your " + wantSlot.toString().toLowerCase() + ".");	
	}
	
	@Override
	protected boolean preSkillChecks() {
		String item = Syntax.ITEM.getStringInfo(fullCommand, this);
		wantSlot = EquipmentSlot.fromString((Syntax.SLOT.getStringInfo(fullCommand, this)));
		if (wantSlot == null) {
			wantSlot = EquipmentSlot.RIGHTHAND;
		}
		itemToUnwield = currentPlayer.getEquipmentInSlot(wantSlot);
		if (itemToUnwield == null || !itemToUnwield.getName().toLowerCase().startsWith(item.toLowerCase())) {
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