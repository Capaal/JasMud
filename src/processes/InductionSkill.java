package processes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class InductionSkill extends Skills implements Runnable {
		
	protected ScheduledExecutorService effectExecutor = Executors.newScheduledThreadPool(1);
	protected static ExecutorService wrapperExecutor = Executors.newCachedThreadPool();
	protected InductionWrapper wrapper;
	
	protected boolean offCooldown = true;
	
	public void scheduleSkillRepeatNTimesOverXMilliseconds(int times, int duration) {
		if (times == 0 || duration / times <= 50 || duration <= 0) {
			throw new IllegalArgumentException("Invalid duration or times: " + duration + " " + times);
		}
		schedule(times, duration);	
			
	}
	
	public void kill() {
		wrapper.kill();	
		currentPlayer.setInduction(null);
	}
	
	public void endInduction() {
		currentPlayer.setInduction(null);
	}
	
	@Override
	public abstract void run();
	
	public void shutDown() {
		WorldServer.shutdownAndAwaitTermination(effectExecutor);
		WorldServer.shutdownAndAwaitTermination(wrapperExecutor);
	}
	
	protected void schedule(int times, int duration) {
		int timeGaps = duration / times;
		wrapper = new InductionWrapper(this, times);
		ScheduledFuture<?> future = effectExecutor.scheduleWithFixedDelay(wrapper, timeGaps, timeGaps, TimeUnit.MILLISECONDS);
		wrapper.setOwnFuture(future);
	}
	
	protected class InductionWrapper implements Runnable {
		private final InductionSkill wrappedSkill;	
		private int timesToRun;
		private int totalTimesRan = 0;
		private Future<?> future;
		
		public InductionWrapper(InductionSkill skill, int times) {
			this.wrappedSkill = skill;
			this.timesToRun = times;
		}
		
		public void run() {
			if (totalTimesRan < timesToRun) {
				wrapperExecutor.execute(wrappedSkill);
				totalTimesRan ++;
				if (totalTimesRan == timesToRun) {
					wrappedSkill.endInduction();
				}
			} else {
				future.cancel(true);
				wrappedSkill.endInduction();
			}
		}		
		public void setOwnFuture(Future<?> future) {
			this.future = future;
		}
		
		public void kill() {
			totalTimesRan = timesToRun;
			wrappedSkill.inductionKilled();
		}
	}

	public abstract void inductionKilled();


	public void offCooldownIn(int duration) {
		if (duration <= 0) {
			throw new IllegalArgumentException("Invalid duration " + duration);
		}
		scheduleOnce(duration);				
	}
	
	protected void scheduleOnce(int duration) {
		CooldownWrapper wrapper = new CooldownWrapper(this);
		effectExecutor.schedule(wrapper, duration, TimeUnit.MILLISECONDS);
	}
	
	protected class CooldownWrapper implements Runnable {		
		InductionSkill wrappedSkill;		
		public CooldownWrapper(InductionSkill s) {
			wrappedSkill = s;
		}
		
		public void run() {
			wrappedSkill.setOffCooldown();			
		}
	}
	
	protected void triggerCooldown() {
		offCooldown = false;
		offCooldownIn(5000);
	}
	
	protected void setOffCooldown() {
		offCooldown = true;
		messageSelf("You are again able to clear your mind.");
	}
}
