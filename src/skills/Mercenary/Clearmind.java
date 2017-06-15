package skills.Mercenary;

import effects.PassiveCondition;
import effects.Fear;
import processes.InductionSkill;

// TODO move to induction skills to handle cooldowns
public class Clearmind extends InductionSkill {
	
//	private boolean offCooldown = true;
	
	public Clearmind() {
		super.name = "clearmind";
		super.description = "Cures mental afflictions.";
		super.syntaxList.add(Syntax.SKILL);
	}	
	
	@Override
	protected void performSkill() {
		if (!offCooldown) {
			messageSelf("You must wait a moment to clear your mind again.");
			return;
		}
		if (!hasBalance()) {return;}
		if (currentPlayer.hasAllConditions(PassiveCondition.DIZZY)) {
			currentPlayer.removeAllConditions(PassiveCondition.DIZZY);
			messageSelf("Dizzy cured.");
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 1000);
			triggerCooldown(5000);
		} else if (currentPlayer.hasCondition(new Fear(currentPlayer))) {
			currentPlayer.removeCondition(new Fear(currentPlayer));
			messageSelf("Fear cured.");
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 1000);
			triggerCooldown(5000);
		} else {
			messageSelf("Your mind is already clear.");
		}
	}
	
	@Override protected void setOffCooldown() { 
		super.setOffCooldown();
		messageSelf("You are again able to clear your mind.");
	}
	/*
	private void triggerCooldown() {
		offCooldown = false;
		offCooldownIn(5000);
	}
	
	public void setOffCooldown() {
		offCooldown = true;
		messageSelf("You are again able to clear your mind.");
	}
	
	private ScheduledExecutorService effectExecutor = Executors.newScheduledThreadPool(1);
	private ExecutorService wrapperExecutor = Executors.newCachedThreadPool();
	
	public void offCooldownIn(int duration) {
		if (duration <= 0) {
			throw new IllegalArgumentException("Invalid duration " + duration);
		}
		scheduleOnce(duration);				
	}
	
	private void scheduleOnce(int duration) {
		CooldownWrapper wrapper = new CooldownWrapper(this);
		effectExecutor.schedule(wrapper, duration, TimeUnit.MILLISECONDS);
	}
	
	private class CooldownWrapper implements Runnable {		
		InductionSkill wrappedSkill;		
		public CooldownWrapper(InductionSkill s) {
			wrappedSkill = s;
		}
		
		public void run() {
			wrappedSkill.setOffCooldown();			
		}
	}*/

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inductionKilled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void inductionEnded() {
		// TODO Auto-generated method stub
		// Shouldn't happen, is not induction skill
		System.out.println("Induction ended called on a cooldown skill. Should not happen.");
	}
}
