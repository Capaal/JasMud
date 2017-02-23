package processes;

import interfaces.Mobile;
import interfaces.TickingEffect;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import effects.PassiveCondition;

// Can TickingEffect just contain it's own wrapper? So that it can kill and we can not be in a map?
public class EffectManager {
	
	private ScheduledExecutorService effectExecutor = Executors.newScheduledThreadPool(4);
	private ExecutorService wrapperExecutor = Executors.newCachedThreadPool();
	private Set<TickingEffect> activeConditions;
	private EnumSet<PassiveCondition> passiveConditions;
	private Mobile bondedMobile;
	
	public EffectManager(Mobile currentPlayer) {
		bondedMobile = currentPlayer;
		activeConditions = new HashSet<TickingEffect>();
		passiveConditions = EnumSet.noneOf(PassiveCondition.class);
	}
	// For all active conditions (ticking effects).
	public boolean registerEffectRepeatNTimesOverXMilliseconds(TickingEffect newEffect, int times) {
		if (times == 0 || newEffect == null) {
			throw new IllegalArgumentException("Invalid duration or times: " + times);
		}
		
//		registerEffect(newEffect);
//		scheduleDestroyAfterXMilliseconds(newEffect, interval * times);
		
		int interval = newEffect.getInterval();
		if (!activeConditions.contains(newEffect)) {
			newEffect.startTicking(times); // Also calls doOnCreation() and set's up wrapper stuff like future
		//	ConditionWrapper wrapper = new ConditionWrapper(newEffect, times);
			activeConditions.add(newEffect);
			newEffect.setLinkedManager(this);
		//	newEffect.doOnCreation();
	//		ScheduledFuture<?> future = effectExecutor.scheduleWithFixedDelay(wrapper, interval, interval, TimeUnit.MILLISECONDS);
	//		wrapper.setOwnFuture(future);
			return true;
		} else { // Tell existing ticking condition that another instance wants to be applied (and possibly modify the current effect)
			for (TickingEffect effect : activeConditions) {
				if (effect.equals(newEffect)) {
					return effect.stackedInstance(newEffect, times);
				}
			}
			return false;
		}
//		return false;
		
	//	return scheduleEffectRepeatNTimesOverXMilliseconds(newEffect, times);	// This should also handle destroy			
	}
	// For Passive Conditions that are on a time limit.
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
		for (TickingEffect effect : activeConditions) {
			if (effect.equals(checkedEffect)) {
				return true;
			}
		}
		return false;
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
	
/*	private boolean scheduleEffectRepeatNTimesOverXMilliseconds(TickingEffect newEffect, int times) {	
		int interval = newEffect.getInterval();
		if (!activeConditions.containsKey(newEffect)) {
			ConditionWrapper wrapper = new ConditionWrapper(newEffect, times);
			activeConditions.put(newEffect, wrapper);
			newEffect.doOnCreation();
			ScheduledFuture<?> future = effectExecutor.scheduleWithFixedDelay(wrapper, interval, interval, TimeUnit.MILLISECONDS);
			wrapper.setOwnFuture(future);
			return true;
		} else { // Tell existing ticking condition that another instance wants to be applied (and possibly modify the current effect)
			activeConditions.get(newEffect).getWrappedEffect().stackedInstance(newEffect, times);
		}
		return false;		
	}*/
	
	private void scheduleDestroyAfterXMilliseconds(PassiveCondition newEffect, int milliseconds) {
		effectExecutor.schedule(new removeTask(newEffect), milliseconds, TimeUnit.MILLISECONDS);
	}
	
	public void unRegisterEffect(TickingEffect oldEffect) {
		System.out.println(activeConditions);
		for (TickingEffect effect : activeConditions) {
			if (effect.equals(oldEffect)) {
				effect.kill();
				break;
			}
			return;
		}
	//	activeConditions.remove(oldEffect); Does within kill()?
		System.out.println(activeConditions);
	//	if (!hasCondition(oldEffect)) {
	//		throw new IllegalArgumentException("Not Present: " + oldEffect);
	//	}
	//	activeConditions.get(oldEffect.getClass()).kill();
	}
	
	public void unRegisterActiveCondition(TickingEffect oldEffect) {
		activeConditions.remove(oldEffect);
		oldEffect.doOnDestruction();
//		bondedMobile.displayPrompt();
	}	
	
	public void unRegisterEffect(PassiveCondition oldEffect) {
		passiveConditions.remove(oldEffect);
		oldEffect.doOnDestruction(bondedMobile);
//		bondedMobile.displayPrompt();
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
	
	/*public class ConditionWrapper implements Runnable {
		private final TickingEffect wrappedEffect;	
		private int timesToRun;
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
	}*/
}
