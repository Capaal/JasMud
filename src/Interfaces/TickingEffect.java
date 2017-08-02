package interfaces;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import processes.EffectManager;
import processes.WorldServer;

// Modified set of Wrappers for implementing scheduled events to occur a set number of times
// Designed specifically for ticking conditions and to interact with the Mobile's EffectManager.
// Interval is defined by the Ticking Effects, Times is defined by the the particular skill's implementation.
public abstract class TickingEffect implements Runnable {
	
	protected final int interval; // Time between ticks, in Milliseconds.
	protected final Mobile currentPlayer; // Mobile on which to affect.
	
	protected ConditionWrapper wrapper;	
	protected EffectManager linkedManager; // Pointer to the currentPlayer's EffectManager.
	
	/**
	 * 
	 * @param currentPlayer Mobile on which ticking should affect.
	 * @param interval Time between ticks, in Milliseconds.
	 */
	public TickingEffect(Mobile currentPlayer, int interval) {
		this.currentPlayer = currentPlayer;
		this.interval = interval;
	}
	
	// Ticking Effects are equal if they are the same class, specific implementations (times ticked/remaining) are ignored.
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other.getClass() == this.getClass()) {
			return true;
		}
		return false;
	}
	
	// Overrides Runnable, trigger each tick.
	@Override
	public abstract void run();	
		
	// When first attached to Mobile, runs doOnCreation(),
	public abstract void doOnCreation();
	
	// When removed from Mobile either via ticking full duration or being removed early, runs doOnDestruction().
	public abstract void doOnDestruction();
	

	/**
	 * If a TickingEffect is applied to Mobile who ALREADY has the same instance, then triggers stackedInstance
	 * INSTEAD of creating a new instance of self and applying to mobile. See EQUALS (implementations are equal).
	 * @param stackedInstance TickingEffect to be applied.
	 * @param times Integer number of times to tick the given TickingEffect.
	 */
	public abstract void stackedInstance(TickingEffect stackedInstance, int times);
	
	/**
	 * Ran by EffectManager once properly attached to Mobile.
	 * @see doOnCreation Runs when triggered.
	 * @param times Integer times to tick.
	 * @param manager Manager of currentPlayer. 
	 */
	public void startTicking(int times, EffectManager manager) {
		doOnCreation();
		this.linkedManager = manager;
		buildWrapper(times);		
	}
	
	// Preps counting wrapper, starts it ticking. Default INITIATL wait = interval.
	private void buildWrapper(int times) {
		wrapper = new ConditionWrapper(this, times);
		ScheduledFuture<?> future = WorldServer.getGameState().getEffectExecutor().scheduleWithFixedDelay(wrapper, this.interval, this.interval, TimeUnit.MILLISECONDS);
		wrapper.setOwnFuture(future);
	}
	// Returns time between ticks, in Milliseconds.
	public int getInterval() {
		return interval;
	}
	
	// Ends the wrapper's ticking of the current TickingEffect, prematurely.
	public synchronized void kill() {
		if (wrapper != null) {
			wrapper.kill();
		}
	}
	
	// Speciallized wrapper to handle ticking runnables a defined number of times.
	// Also allows for pre-mature cancelling of future scheduled ticks.
	protected class ConditionWrapper implements Runnable {
		private final TickingEffect wrappedEffect;	
		private final int timesToRun;
		private int totalTimesRan = 0;
		private Future<?> future;
		
		public ConditionWrapper(TickingEffect effect, int times) {
			this.wrappedEffect = effect;
			this.timesToRun = times;
		}	
		
		// Triggers the wrapped TickingEffect's "run()" method, then increments count of times ran.
		public void run() {
			if (totalTimesRan < timesToRun) {
				WorldServer.getGameState().addToQueue(wrappedEffect);
				totalTimesRan ++;
			} else {
				kill();
			}
		}
		
		// Allows modification of currently running instance, usually via stackedInstance().
		public synchronized void modifyTimesRan(int newTimes) {
			totalTimesRan = newTimes;
		}	
		
		public void setOwnFuture(Future<?> future) {
			this.future = future;
		}
		
		public void kill() {
			future.cancel(true);
			linkedManager.unRegisterActiveCondition(wrappedEffect);
		}
		
		public int remainingTime() {
			return (timesToRun-totalTimesRan)*interval;
		}
	}

	public String getInfo() {
		return "" + wrapper.remainingTime();
	}
}
