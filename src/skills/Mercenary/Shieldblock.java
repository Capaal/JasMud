package skills.Mercenary;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Equipment.EquipmentSlot;
import skills.InductionSkill;

public class Shieldblock extends InductionSkill {
	
	public Shieldblock(Mobile currentPlayer, String fullCommand) {
		super("shieldblock", "Careful guard against attacks.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
	}
	
	public class InnerShieldBlock extends InnerSkill {		
		public InnerShieldBlock(Mobile currentPlayer, String fullCommand) {
			super(currentPlayer, fullCommand);
		}

		@Override
		public void performSkill() {
			changeBlocking(false);	
		}
	}

	// On activation, begins blocking.
	// Requires a shield and balance
	// Interruption by action, or opponent's action stops channeling block
	// should be interrupted if shield unwielded (or force unwielded/dropped) TODO
	@Override
	protected void performSkill() {
		scheduleInduction(new InnerShieldBlock(currentPlayer, fullCommand), 1, 4000); // Triggers this skill's "run()" in 4 seconds. Interruptible.
		currentPlayer.setInduction(this);
		changeBlocking(true);
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, calculateBalance());		
	}
	
	private boolean weaponWielded() {
		//tests left hand first
		Holdable possShield = currentPlayer.getEquipmentInSlot(EquipmentSlot.LEFTHAND);
		if (possShield == null || (!possShield.getName().equals("shield"))) {
			possShield = currentPlayer.getEquipmentInSlot(EquipmentSlot.RIGHTHAND);
			if (possShield == null || (!possShield.getName().equals("shield"))) {
				messageSelf("You are not wielding a shield.");
				return false;
			}
		}
		return true;
	}

	@Override
	public void inductionKilled() {
		changeBlocking(false);			
	}	
	
	private void changeBlocking(boolean b) {
		currentPlayer.changeBlocking(b);
		if (b) {
			messageSelf("You raise your shield, defending against incoming attacks.");
			messageOthers(currentPlayer.getName() + " raises their shield in defence.", Arrays.asList(currentPlayer));						
		} else {
			messageSelf("You lower your shield, no longer defending.");
			messageOthers(currentPlayer.getName() + " lowers their shield.", Arrays.asList(currentPlayer));
		}
	}

	private int calculateBalance() {
	//	double balanceMult = item.getBalanceMult();  Item not required yet.
		return 2500;
	}

	@Override
	protected void inductionEnded() {
		// TODO Auto-generated method stub		
	}

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		if (!weaponWielded()) {return false;}	
		return true;
	}
}
