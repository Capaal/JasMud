package interfaces;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import processes.EffectManager;

public abstract class TickingEffect implements Runnable {
	
	protected static ScheduledExecutorService effectExecutor = Executors.newScheduledThreadPool(1);
	
	protected final int interval;
	protected final Mobile currentPlayer;
	
	protected ConditionWrapper wrapper;	
	protected EffectManager linkedManager;
	
	public TickingEffect(Mobile currentPlayer, int interval) {
		this.currentPlayer = currentPlayer;
		this.interval = interval;
	}
	
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
	
	@Override
	public abstract void run();	
		
	public abstract void doOnCreation();
	
	public abstract void doOnDestruction();
	
	public abstract boolean stackedInstance(TickingEffect stackedInstance, int times);
	
	public void startTicking(int times) {
		doOnCreation();
		buildWrapper(times);		
	}
	
	private void buildWrapper(int times) {
		wrapper = new ConditionWrapper(this, times);
		ScheduledFuture<?> future = effectExecutor.scheduleWithFixedDelay(wrapper, this.interval, this.interval, TimeUnit.MILLISECONDS);
		wrapper.setOwnFuture(future);
	}
	
	public int getInterval() {
		return interval;
	}
	
	public void setLinkedManager(EffectManager manager) {
		this.linkedManager = manager;
	}
	
	public synchronized void kill() {
		wrapper.kill();
	}
	
	public class ConditionWrapper implements Runnable {
		private final TickingEffect wrappedEffect;	
		private final int timesToRun;
		private int totalTimesRan = 0;
		private Future<?> future;
		
		public ConditionWrapper(TickingEffect effect, int times) {
			this.wrappedEffect = effect;
			this.timesToRun = times;
		}
		
		public TickingEffect getWrappedEffect() {
			return wrappedEffect;
		}		
		
		public void run() {
			if (totalTimesRan < timesToRun) {
				effectExecutor.execute(wrappedEffect);
				totalTimesRan ++;
			} else {
				kill();
			}
		}
		
	//	public void modifyTimes(int times) {
	//		timesToRun = times;
	//	}		
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
