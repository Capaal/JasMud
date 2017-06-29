package processes;

import interfaces.Mobile;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class InductionSkill extends Skills {
		
	public InductionSkill(String name, String description, Mobile currentPlayer, String fullCommand) {
		super(name, description, currentPlayer, fullCommand);
	}

	protected static ScheduledExecutorService effectExecutor = Executors.newScheduledThreadPool(1);
	protected InductionWrapper wrapper;
	
//	protected boolean offCooldown = true;
	
	public void shutDown() {
		WorldServer.shutdownAndAwaitTermination(effectExecutor);
	}
	
	protected abstract void inductionEnded(); // Induction ends successfully.
	protected abstract void inductionKilled(); // Induction stopped early.
	
	public void scheduleInduction(int times, int interval) {
		scheduleInduction(times, interval, interval);
	}
	
	public abstract InnerSkill getInnerSkill(Mobile currentPlayer, String fullCommand);
	
	public void scheduleInduction(int times, int interval, int initialWait) {
		if (times == 0 || interval <= 50 || interval <= 0 || initialWait < 0) {
			throw new IllegalArgumentException("Invalid duration or times: " + interval + " " + times + " " + initialWait);
		}
		InnerSkill innerSkill = getInnerSkill(currentPlayer, fullCommand);
		wrapper = new InductionWrapper(innerSkill, times);
		ScheduledFuture<?> future = effectExecutor.scheduleWithFixedDelay(wrapper, initialWait, interval, TimeUnit.MILLISECONDS);
		wrapper.setOwnFuture(future);			
	}
	
	public void interrupt() {
		wrapper.interrupt();	
		currentPlayer.setInduction(null);
		inductionKilled();
	}	
	
	protected abstract class InnerSkill extends Skills {		
		public InnerSkill(Mobile currentPlayer, String fullCommand) {
			super("", "", currentPlayer, fullCommand);
		}
		@Override
		protected void testForInduction() {
			// Do not want to test for induction.
		}
		@Override
		protected boolean preSkillChecks() { return true; }
		@Override
		protected abstract void performSkill();
		
		public void endSuccessfully() {
			wrapper.interrupt();
			currentPlayer.setInduction(null);
			inductionEnded();
		}
	}
	
	protected class InductionWrapper implements Runnable {
		private final InnerSkill wrappedInnerSkill;	
		private int timesToRun;
		private int totalTimesRan = 0;
		private Future<?> future;
		
		public InductionWrapper(InnerSkill skill, int times) {
			this.wrappedInnerSkill = skill;
			this.timesToRun = times;
		}
		
		public void run() {
			if (totalTimesRan < timesToRun) {
				WorldServer.getGameState().addToQueue(wrappedInnerSkill.getNewInstance(currentPlayer, "")); 
		//		wrapperExecutor.execute(wrappedSkill);
		//		wrappedSkill.secondaryRun();
				totalTimesRan ++;
				if (totalTimesRan == timesToRun) {
					future.cancel(true);
					wrappedInnerSkill.endSuccessfully();
				}
			} else {
				future.cancel(true);
				wrappedInnerSkill.endSuccessfully();
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
	
/*	// Called to start cooldown period.
	protected  void triggerCooldown(int length) {
		offCooldown = false;
		offCooldownIn(length);
	}
	
	// Called when cooldown period ends. Override to add messages. But call super.setOffCooldown()
	protected  void setOffCooldown() {
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
		}	*/		
}
