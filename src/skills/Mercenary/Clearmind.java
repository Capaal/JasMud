package skills.Mercenary;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import effects.PassiveCondition;
import effects.Fear;
import processes.Skills;
import processes.WorldServer;

// TODO move to induction skills to handle cooldowns
public class Clearmind extends Skills {
	
	private boolean offCooldown = true;
	
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
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
			triggerCooldown();
		} else if (currentPlayer.hasCondition(new Fear(currentPlayer))) {
			currentPlayer.removeCondition(new Fear(currentPlayer));
			messageSelf("Fear cured.");
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 3000);
			triggerCooldown();
		} else {
			messageSelf("Your mind is already clear.");
		}
	}
	
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
		schedule(duration);				
	}
	
	public void shutDown() {
		WorldServer.shutdownAndAwaitTermination(effectExecutor);
		WorldServer.shutdownAndAwaitTermination(wrapperExecutor);
	}
	
	private void schedule(int duration) {
		SkillWrapper wrapper = new SkillWrapper(this);
		effectExecutor.schedule(wrapper, duration, TimeUnit.MILLISECONDS);
	}
	
	private class SkillWrapper implements Runnable {		
		Clearmind skill;		
		public SkillWrapper(Clearmind s) {
			skill = s;
		}
		
		public void run() {
			skill.setOffCooldown();			
		}
	}
}
