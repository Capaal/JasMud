package skills;

import java.util.Arrays;
import effects.Balance;
import interfaces.Holdable;
import interfaces.Mobile;
import processes.Skills;
import processes.Type;

public class Heal extends Skills {
	
	private final int intensity = -30;
	private String targetName;
	private Mobile finalTarget;
	
	public Heal() {
		super.name = "heal";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}	
	
	// Deals damage to a single target in currentPlayer's location
	@Override
	protected void performSkill() {
		targetName = Syntax.TARGET.getStringInfo(fullCommand, this).toLowerCase();
		if (!hasBalance()) {return;}
		setTarget();
		if (!(finalTarget instanceof Mobile)) {
			messageSelf("You can't heal that.");
			return;
		}
		if (finalTarget.isDead()) {
			messageSelf("You can't heal dead people.");
			return;
		}
		
		finalTarget.takeDamage(Type.BLUNT, calculateDamage());
		currentPlayer.addEffect(new Balance(), 3000);
		if (finalTarget == currentPlayer) {
			messageSelf("You heal yourself a bit.");
			messageOthers(currentPlayer.getName() + " heals a bit.", Arrays.asList(currentPlayer, finalTarget));
		} else {
			messageSelf("You heal " + finalTarget.getName());
			messageTarget(currentPlayer.getName() + " heals you.", Arrays.asList(finalTarget));
			messageOthers(currentPlayer.getName() + " heals " + finalTarget.getName() + ".", Arrays.asList(currentPlayer, finalTarget));
		}
	}

	private int calculateDamage() {
		return intensity;
	}
	
	private void setTarget() {
		finalTarget = null;
		if (targetName.equals("") || targetName.equals(currentPlayer.getName().toLowerCase())) {
			finalTarget = currentPlayer;
			return;
		}
		Holdable h = currentPlayer.getContainer().getHoldableFromString(targetName);
		if (h != null && h instanceof Mobile) {
			finalTarget = (Mobile)h;
		} 
	}
}
