package processes;

import interfaces.Mobile;
import interfaces.TickingEffect;
import java.util.EnumSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import effects.PassiveCondition;


// Can TickingEffect just contain it's own wrapper? So that it can kill and we can not be in a map?
public class EffectManager {
	
	private ScheduledExecutorService effectExecutor = Executors.newScheduledThreadPool(4);
	private ExecutorService wrapperExecutor = Executors.newCachedThreadPool();
	private Map<TickingEffect, ConditionWrapper> activeConditions;
	private EnumSet<PassiveCondition> passiveConditions;
	private Mobile bondedMobile;
	
	public EffectManager(Mobile currentPlayer) {
		bondedMobile = currentPlayer;
		activeConditions = new TreeMap<TickingEffect, ConditionWrapper>();
		passiveConditions = EnumSet.noneOf(PassiveCondition.class);
	}
	
	public boolean registerEffectRepeatNTimesOverXMilliseconds(TickingEffect newEffect, int times, int interval) {
		if (times == 0 || interval <= 50 || newEffect == null) {
			throw new IllegalArgumentException("Invalid duration or times: " + interval + " " + times);
		}
//		registerEffect(newEffect);
//		scheduleDestroyAfterXMilliseconds(newEffect, interval * times);
		return scheduleEffectRepeatNTimesOverXMilliseconds(newEffect, times, interval);	// This should also handle destroy			
	}
	
	public boolean registerEffectDestroyAfterXMilliseconds(PassiveCondition newEffect, int duration) {
		if (newEffect == null || duration <= 0) {
			throw new IllegalArgumentException("Invalid effect or duration: " + newEffect + " " + duration);
		}		
		if(registerEffect(newEffect)) {
			scheduleDestroyAfterXMilliseconds(newEffect, duration);
			return true;
		}
		return false;
	}
	
	public boolean registerPermanentEffect(PassiveCondition newEffect) {
		if (newEffect == null) {
			throw new IllegalArgumentException("Invalid effect: " + newEffect);
		}		
		return registerEffect(newEffect);
	}
	
	public boolean hasCondition(TickingEffect checkedEffect) {
		return activeConditions.containsKey(checkedEffect);
	}
	
	public boolean hasCondition(PassiveCondition checkedEffect) {
		return passiveConditions.contains(checkedEffect);
	}
	
	public void shutDown() {
		WorldServer.shutdownAndAwaitTermination(effectExecutor);
		WorldServer.shutdownAndAwaitTermination(wrapperExecutor);
	}
	
	private boolean registerEffect(PassiveCondition newEffect) {
		if (passiveConditions.add(newEffect)) {
			newEffect.doOnCreation(bondedMobile);
			return true;
		}
		return false;		
	}
	
	private boolean scheduleEffectRepeatNTimesOverXMilliseconds(TickingEffect newEffect, int times, int interval) {		
		if (!activeConditions.containsKey(newEffect)) {
			ConditionWrapper wrapper = new ConditionWrapper(newEffect, times);
			activeConditions.put(newEffect, wrapper);
			newEffect.doOnCreation();
			ScheduledFuture<?> future = effectExecutor.scheduleWithFixedDelay(wrapper, interval, interval, TimeUnit.MILLISECONDS);
			wrapper.setOwnFuture(future);
			return true;
		}
		return false;		
	}
	
	private void scheduleDestroyAfterXMilliseconds(PassiveCondition newEffect, int milliseconds) {
		effectExecutor.schedule(new removeTask(newEffect), milliseconds, TimeUnit.MILLISECONDS);
	}
	
	public void unRegisterEffect(TickingEffect oldEffect) {
		if (!activeConditions.containsKey(oldEffect)) {
			throw new IllegalArgumentException("Not Present: " + oldEffect);
		}
		activeConditions.get(oldEffect).kill();
	}
	
	private void unRegisterActiveCondition(TickingEffect oldEffect) {
		activeConditions.remove(oldEffect);
		oldEffect.doOnDestruction();
		bondedMobile.displayPrompt();
	}	
	
	public void unRegisterEffect(PassiveCondition oldEffect) {
		passiveConditions.remove(oldEffect);
		oldEffect.doOnDestruction(bondedMobile);
		bondedMobile.displayPrompt();
	}	
	
	private class removeTask implements Runnable {		
		private PassiveCondition effectToBeRemoved;		
		public removeTask(PassiveCondition effect) {
			this.effectToBeRemoved = effect;
		}		
		public void run() {
			unRegisterEffect(effectToBeRemoved);			
		}
	}
	
	private class ConditionWrapper implements Runnable {
		private final TickingEffect wrappedEffect;	
		private int timesToRun;
		private int totalTimesRan = 0;
		private Future<?> future;
		
		public ConditionWrapper(TickingEffect effect, int times) {
			this.wrappedEffect = effect;
			this.timesToRun = times;
		}
		
		public void run() {
			if (totalTimesRan < timesToRun) {
				wrapperExecutor.execute(wrappedEffect);
				totalTimesRan ++;
			} else {
				future.cancel(true);
				unRegisterActiveCondition(wrappedEffect);
			}
		}
		
		public void setOwnFuture(Future<?> future) {
			this.future = future;
		}
		
		public void kill() {
			future.cancel(true);
			unRegisterActiveCondition(wrappedEffect);
		}
	}
}
