package skills;

import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;
import processes.Type;
import processes.WorldServer;

public class Punch extends Skills {
	
	private Boolean failed;
	private int intensity = 10;
	public String name = "punch";
	
	public Punch() {
		this.name = "punch";
		this.syntax.add(Syntax.SKILL);
		this.syntax.add(Syntax.TARGET);
		WorldServer.gameState.getBook(1).addSkill(this);
	}
	
	private Mobile finalTarget;
	
	// Deals damage to a single target in currentPlayer's location
	@Override
	public void perform(String fullCommand, Mobile currentPlayer) {
		failed = false;
		checkForBalance();
		checkForBlock();
		finalTarget = getTarget(fullCommand, currentPlayer);
		if (finalTarget == null) {
			failed(1);
		}
		if (!failed) {
			int finalDamage = calculateDamage();
			finalTarget.takeDamage(Type.SHARP, finalDamage);
//			messageSelf("You punch " + finalTarget.getName());
//			messageTarget(currentPlayer.getName() + " punches you.");
//			messageOthers(currentPlayer.getName() + " punches " + finalTarget.getName());
		}		
	}
	
	private int calculateDamage() {
		return intensity;
	}

	private void failed(int stage) {
		failed = true;
		switch (stage) {
		case 1:
//			messageSelf("There is no " + target + " here for you to punch.");
			break;
		case 2:
//			messageSelf("You are off balance and cannot punch.");
			break;
		default:
			break;
		}
	}
	
	@Override
	public void checkForBalance() {
//		if this fails then
//		failed(2);
	}
	
	private void checkForBlock() {
//		if block is active
//		do something like change final damage, or set a boolean, or fail out
	}
	
	private Mobile getTarget(String fullCommand, Mobile currentPlayer) {
		Holdable h = currentPlayer.getContainer().getHoldableFromString(this.getStringInfo(Syntax.TARGET, fullCommand));
			if (h != null && h instanceof Mobile) {
				return (Mobile)h;
			}
		return null;
	}
}
