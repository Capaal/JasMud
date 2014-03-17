package processes;

import interfaces.DamageEffect;
import interfaces.Effect;
import interfaces.TickingEffect;
import java.util.ArrayList;
import java.util.List;
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
	
	public void registerEffectRepeatNTimesOverXMilliseconds(TickingEffect newEffect, int times, int duration) {
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
	
	public boolean hasEffect(Effect checkedEffect) {
		return effectList.contains(checkedEffect);
	}
	
	public boolean hasInstanceOf(Effect effectType) {
		for (Effect possibleEffect : effectList) {
			if (possibleEffect.isInstanceOf(effectType)) {
				return true;
			}
		}
		return false;
	}
	
	public void removeInstanceOf(Effect effectType) {
		Effect effectToBeRemoved = null;
		for (Effect possibleEffect : effectList) {
			
			if (possibleEffect.isInstanceOf(effectType)) {
				effectToBeRemoved = possibleEffect;
				break;
			}
		}
		effectList.remove(effectToBeRemoved);
	}
	
	public int checkEffectsAgainstIncomingDamage(Type incomingType, int damage) {
		double finalDamage = damage;
		for (Effect effect : effectList) {
			if (effect instanceof DamageEffect) {
				finalDamage = ((DamageEffect) effect).checkAgainstIncomingDamage(incomingType, finalDamage);
			}
		}
		return (int)finalDamage;
	}
	
	public void shutDown() {
		WorldServer.shutdownAndAwaitTermination(effectExecutor);
		WorldServer.shutdownAndAwaitTermination(wrapperExecutor);
	}
	
	private void registerEffect(Effect newEffect) {
		effectList.add(newEffect);
	}
	
	private void scheduleEffectRepeatNTimesOverXMilliseconds(TickingEffect newEffect, int times, int duration) {
		int timeGaps = duration / times;
		EffectWrapper wrapper = new EffectWrapper(newEffect, times);
		ScheduledFuture<?> future = effectExecutor.scheduleWithFixedDelay(wrapper, timeGaps, timeGaps, TimeUnit.MILLISECONDS);
		wrapper.setOwnFuture(future);
	}
	
	private void scheduleDestroyAfterXMilliseconds(Effect newEffect, int milliseconds) {
		effectExecutor.schedule(new removeTask(newEffect), milliseconds, TimeUnit.MILLISECONDS);
	}
	
	private void unRegisterEffect(Effect oldEffect) {
		effectList.remove(oldEffect);
	}	
	
	private class removeTask implements Runnable {
		
		private Effect effectToBeRemoved;
		
		public removeTask(Effect effect) {
			this.effectToBeRemoved = effect;
		}		

		public void run() {
			unRegisterEffect(effectToBeRemoved);
		}
	}
	
	private class EffectWrapper implements Runnable {
		private final TickingEffect wrappedEffect;	
		private int timesToRun;
		private int totalTimesRan = 0;
		private Future<?> future;
		
		public EffectWrapper(TickingEffect effect, int times) {
			this.wrappedEffect = effect;
			this.timesToRun = times;
		}
		
		public void run() {
			if (totalTimesRan < timesToRun) {
				wrapperExecutor.execute(wrappedEffect);
				totalTimesRan ++;
			} else {
				future.cancel(true);
			}
		}
		
		public void setOwnFuture(Future<?> future) {
			this.future = future;
		}
	}
}
