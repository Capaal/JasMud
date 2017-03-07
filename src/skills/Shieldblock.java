package skills;

import java.util.Arrays;
import effects.PassiveCondition;
import processes.Equipment;
import processes.InductionSkill;

public class Shieldblock extends InductionSkill {
	
	public Shieldblock() {
		super.name = "shieldblock";
		super.syntaxList.add(Syntax.SKILL);
	}
	
	// Runs POST INDUCTION.
	@Override
	public void run() {		
		// TURN OFF DEFENDING
		changeBlocking(false);	
	}

	// On activation, begins blocking.
	// Requires a shield and balance
	// Inturruption by action, or opponent's action stops channeling block
	@Override
	protected void performSkill() {
		if (!hasBalance()) {return;}
		if (!weaponWielded()) {return;}		
		scheduleSkillRepeatNTimesOverXMilliseconds(1, 2000); // Triggers this skill's "run()" in 2 seconds. Interruptible.
		currentPlayer.setInduction(this);
		changeBlocking(true);
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, calculateBalance());
	}
	
	// TODO Needs to find a SHIELD, which we can't handle yet.
	private boolean weaponWielded() {
		if (currentPlayer.getEquipmentInSlot(Equipment.EquipmentEnum.LEFTHAND) == null && currentPlayer.getEquipmentInSlot(Equipment.EquipmentEnum.RIGHTHAND) == null) {
			messageSelf("You are not wielding a weapon.");
			return false;
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
}
