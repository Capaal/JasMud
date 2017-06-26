package skills;

import java.util.Arrays;

import effects.PassiveCondition;
import interfaces.Mobile;
import processes.InductionSkill;
import processes.Skills;
import processes.Type;

public class LightningWhip extends InductionSkill {
	
	private int intensity = 10;
	private String possibleTarg;
	private Mobile finalTarget;
	
	public LightningWhip(Mobile currentPlayer, String fullCommand) {
		super("whip", "Powerful magical whip attack.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
	}
	

	public class InnerLightningWhip extends InnerSkill {
		
		public InnerLightningWhip(Mobile currentPlayer, String fullCommand) {
			super(currentPlayer, fullCommand);
		}

		@Override
		public void performSkill() {
			findTarget(); // Performs finding target again to ensure target is valid.
			if (finalTarget == null) {
				messageSelf("There is no \"" + possibleTarg + "\" for you to attack.");
				return;
			}	
			if (isBlocking(finalTarget)) {  // Probably not complete still
				messageSelf(finalTarget.getName() + " blocks a whip strike.");
				return;
			}		
			finalTarget.takeDamage(Type.COLD, calculateDamage());
			messageSelf("Your lightning whip hits " + finalTarget.getNameColored() + ".");
			messageTarget(currentPlayer.getNameColored() + " lightning lashes you.", Arrays.asList(finalTarget));
			messageOthers(currentPlayer.getNameColored() + " whips " + finalTarget.getNameColored(), Arrays.asList(currentPlayer, finalTarget));	
		}

		@Override
		public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
			return new InnerLightningWhip(currentPlayer, fullCommand);
		}
	}
	
	@Override
	public InnerSkill getInnerSkill(Mobile currentPlayer, String fullCommand) {
		return new InnerLightningWhip(currentPlayer, fullCommand);
	}
	
	// Deals damage to a single target in currentPlayer's location 
	// Target MUST be targettable BOTH at the START and at the END of induction.
	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			scheduleInduction(4, 2500, 1500); // Triggers this skill's "run()" in 1.5 seconds. and ticks every 2.5 seconds. Interruptible.
			currentPlayer.setInduction(this);
			messageSelf("You begin whipping " + finalTarget.getName() + ".");
			messageTarget(currentPlayer.getName() + " begins whipping you with lightning.", Arrays.asList(finalTarget));
		}
	}		
		
	private int calculateDamage() {
		return intensity;
	}
	
	private void findTarget() {
		possibleTarg = Syntax.TARGET.getStringInfo(fullCommand, this);
		if (possibleTarg == "") {
			messageSelf("Who are you trying to headshot?");
		
		}	
		finalTarget = currentPlayer.getContainer().getMobileFromString(possibleTarg);
		if (finalTarget == null) {
			messageSelf("There is no \"" + possibleTarg + "\" for you to attack.");
			
		}	
	}

	@Override
	public void inductionKilled() {
		messageSelf("You stop whipping " + possibleTarg + ".");
		inductionEnded();
	}	
	
	private int calculateBalance() {
	//	double damageMult = item.getBalanceMult();  Item not required yet.
	//	return (int) (damageMult * intensity);
		return 3000;
	}

	@Override
	protected void inductionEnded() {
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, calculateBalance());		
	}

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}	
		findTarget();		
		if (finalTarget == null) {return false;}
		return true;
	}

	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new LightningWhip(currentPlayer, fullCommand);
	}
}
