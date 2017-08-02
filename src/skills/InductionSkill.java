package skills;

import interfaces.Mobile;
import processes.Skills;
import processes.WorldServer;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

// Defines a skill that triggers a second part after an initial wait that is interruptible.
// Nearly any skill will interrupt the wait (by calling killInduction()).
public abstract class InductionSkill extends Skills {
	
	protected InductionWrapper wrapper;
		
	public InductionSkill(String name, String description, Mobile currentPlayer, String fullCommand) {
		super(name, description, currentPlayer, fullCommand);
	}
	/**
	 * Call if induction ends successfully, Usually to add in a message.
	 */
	protected abstract void inductionEnded();
	/**
	 * Call if induction ends early, usually to add in a message.
	 */
	protected abstract void inductionKilled();
	
	/**
	 * Schedules induction ticking, defaults InitialWait to interval.
	 * @param skill InnerSkill that you want to trigger.
	 * @param times How many times it should try to trigger.
	 * @param interval Amount of time between triggering.
	 */
	public void scheduleInduction(InnerSkill skill, int times, int interval) {
		scheduleInduction(skill, times, interval, interval);
	}
	
	/**
	 * Schedules induction ticking.
	 * @param skill InnerSkill that you want to trigger.
	 * @param times How many times it should try to trigger.
	 * @param interval Amount of time between triggering.
	 * @param initialWait How long to wait before the first tick.
	 * @throws IllegalArgumentException times != 0, interval !<= 50 milliseconds, skill != null, initialWait !< 0 millseconds.
	 */
	public void scheduleInduction(InnerSkill skill, int times, int interval, int initialWait) {
		if (times == 0 || interval <= 50 || skill == null || initialWait < 0) {
			throw new IllegalArgumentException("Invalid duration or times: " + interval + " " + times + " " + initialWait);
		}
		wrapper = new InductionWrapper(skill, times);
		ScheduledFuture<?> future = WorldServer.getGameState().getEffectExecutor().scheduleWithFixedDelay(wrapper, initialWait, interval, TimeUnit.MILLISECONDS);
		wrapper.setOwnFuture(future);			
	}
	
	/**
	 * Interrupts an induction skill. Stops future ticks, removes current induction skill from player's current, and calls inductionKilled()
	 */
	public void interrupt() {
		wrapper.interrupt();	
		currentPlayer.setInduction(null);
		inductionKilled();
	}	
	
	// Defines the second portion of skill that triggers after the wait.
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
	
	// Specialized wrapper that can run x number of times and is interruptible.
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
				WorldServer.getGameState().addToQueue(wrappedInnerSkill); 
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
}
