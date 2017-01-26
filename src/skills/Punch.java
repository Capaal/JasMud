package skills;

import java.util.Arrays;

import effects.Balance;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;
import processes.Type;
import processes.WorldServer;

public class Punch extends Skills {
	
	private final int intensity = 10;
	
	public Punch() {
		super.name = "punch";
		super.id = 1;
		super.description = "Punching things.";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		WorldServer.gameState.getBook(1).addSkill(this);
	}	
	
	// Deals damage to a single target in currentPlayer's location
	@Override
	public void perform(String fullCommand, Mobile currentPlayer) {
		Boolean continueRunning = true;
		continueRunning = hasBalance(currentPlayer);		
		Mobile finalTarget = getTarget(fullCommand, currentPlayer);
		continueRunning = !isBlocking(finalTarget);  // Probably not complete still
		if (finalTarget == null) {
			continueRunning = false;
			messageSelf("There is no " + this.getStringInfo(Syntax.TARGET, fullCommand) + " here for you to punch.", currentPlayer);
		}
		if (continueRunning) {
			finalTarget.takeDamage(Type.SHARP, calculateDamage());
			currentPlayer.addEffect(new Balance(), 3000);
			messageSelf("You punch " + finalTarget.getName(), currentPlayer);
			messageTarget(currentPlayer.getName() + " punches you.", Arrays.asList(finalTarget));
			messageOthers(currentPlayer.getName() + " punches " + finalTarget.getName(), currentPlayer, Arrays.asList(currentPlayer, finalTarget));
		}		
	}

	private int calculateDamage() {
		return intensity;
	}
	
	private Mobile getTarget(String fullCommand, Mobile currentPlayer) {
		Holdable h = currentPlayer.getContainer().getHoldableFromString(this.getStringInfo(Syntax.TARGET, fullCommand));
			if (h != null && h instanceof Mobile) {
				return (Mobile)h;
			}
		return null;
	}
}
