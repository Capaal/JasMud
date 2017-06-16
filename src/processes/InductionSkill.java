package processes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class InductionSkill extends Skills implements Runnable {
		
	protected static ScheduledExecutorService effectExecutor = Executors.newScheduledThreadPool(1);
	protected static ExecutorService wrapperExecutor = Executors.newCachedThreadPool();
	protected InductionWrapper wrapper;
	
	protected boolean offCooldown = true;
	
	@Override
	public abstract void run();
	
	public void shutDown() {
		WorldServer.shutdownAndAwaitTermination(effectExecutor);
		WorldServer.shutdownAndAwaitTermination(wrapperExecutor);
	}
	
	protected abstract void inductionEnded(); // Induction ends successfully.
	protected abstract void inductionKilled(); // Induction stopped early.
	
	public void scheduleInduction(int times, int interval) {
		scheduleInduction(times, interval, interval);
	}
	
	public void scheduleInduction(int times, int interval, int initialWait) {
		if (times == 0 || interval / times <= 50 || interval <= 0 || initialWait < 0) {
			throw new IllegalArgumentException("Invalid duration or times: " + interval + " " + times + " " + initialWait);
		}
		wrapper = new InductionWrapper(this, times);
		ScheduledFuture<?> future = effectExecutor.scheduleWithFixedDelay(wrapper, initialWait, interval, TimeUnit.MILLISECONDS);
		wrapper.setOwnFuture(future);			
	}
	
	public void interrupt() {
		wrapper.interrupt();	
		currentPlayer.setInduction(null);
		inductionKilled();
	}
	
	public void endSuccessfully() {
		wrapper.interrupt();
		currentPlayer.setInduction(null);
		inductionEnded();
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
					future.cancel(true);
					wrappedSkill.endSuccessfully();
				}
			} else {
				future.cancel(true);
				wrappedSkill.endSuccessfully();
			}
		}		
		public void setOwnFuture(Future<?> future) {
			this.future = future;
		}
		
		public void interrupt() {
			future.cancel(true);
			totalTimesRan = timesToRun;			
		}
	}
	
	// Called to start cooldown period.
	protected void triggerCooldown(int length) {
		offCooldown = false;
		offCooldownIn(length);
	}
	
	// Called when cooldown period ends. Override to add messages. But call super.setOffCooldown()
	protected void setOffCooldown() {
		offCooldown = true;
	}

	private void offCooldownIn(int duration) {
		if (duration <= 0) {
			throw new IllegalArgumentException("Invalid duration " + duration);
		}
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
}
