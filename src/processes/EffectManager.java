package processes;

import interfaces.Effect;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EffectManager {
	
	private ScheduledExecutorService effectExecutor = Executors.newScheduledThreadPool(4);
	private ExecutorService wrapperExecutor = Executors.newCachedThreadPool();
	private List<Effect> effectList;
	
	public EffectManager() {
		effectList = new ArrayList<Effect>();
	}
	
	public void registerEffectRepeatNTimesOverXMilliseconds(Effect newEffect, int times, int duration) {
		if (times == 0 || duration / times <= 50 || newEffect == null || duration <= 0) {
			throw new IllegalArgumentException("Invalid duration or times: " + duration + " " + times);
		}
		registerEffect(newEffect);
		scheduleDestroyAfterXMilliseconds(newEffect, duration);
		scheduleEffectRepeatNTimesOverXMilliseconds(newEffect, times, duration);	
			
	}
	
	public void registerEffectDestroyAfterXMilliseconds(Effect newEffect, int duration) {
		if (newEffect == null || duration <= 0) {
			throw new IllegalArgumentException("Invalid effect or duration: " + newEffect + " " + duration);
		}		
		registerEffect(newEffect);
		scheduleDestroyAfterXMilliseconds(newEffect, duration);
	}
	
	private void registerEffect(Effect newEffect) {
		effectList.add(newEffect);
	}
	
	private void scheduleEffectRepeatNTimesOverXMilliseconds(Effect newEffect, int times, int duration) {
		int timeGaps = duration / times;
		EffectWrapper wrapper = new EffectWrapper(newEffect, times);
		ScheduledFuture<?> future = effectExecutor.scheduleWithFixedDelay(wrapper, timeGaps, timeGaps, TimeUnit.MILLISECONDS);
		wrapper.setOwnFuture(future);
	}
	
	private void scheduleDestroyAfterXMilliseconds(Effect newEffect, int milliseconds) {
		effectExecutor.schedule(new destroyTask(newEffect), milliseconds, TimeUnit.MILLISECONDS);
	}
	
	private class destroyTask implements Runnable {
		
		private Effect effectToBeDestroyed;
		
		public destroyTask(Effect effect) {
			this.effectToBeDestroyed = effect;
		}		

		public void run() {
			effectToBeDestroyed.destroyEffect();
		}
	}
	
	private class EffectWrapper implements Runnable {
		private final Effect wrappedEffect;	
		private int timesToRun;
		private int totalTimesRan = 0;
		private Future future;
		
		public EffectWrapper(Effect effect, int times) {
			this.wrappedEffect = effect;
			this.timesToRun = times;
		}
		
		public void run() {
			if (totalTimesRan < timesToRun) {
				wrapperExecutor.execute(wrappedEffect);
				totalTimesRan ++;
			} else {
				wrappedEffect.destroyEffect();
				future.cancel(true);
			}
		}
		
		public void setOwnFuture(Future future) {
			this.future = future;
		}
	}
}
