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

// Contains methods to handle passive and ticking effects on a Mobile
public class EffectManager {
	
	private ScheduledExecutorService effectExecutor = Executors.newScheduledThreadPool(4);
	private ExecutorService wrapperExecutor = Executors.newCachedThreadPool();
	private Set<TickingEffect> activeEffects;
	private EnumSet<PassiveCondition> passiveEffects;
	private Mobile bondedMobile;
	
	public EffectManager(Mobile currentPlayer) {
		bondedMobile = currentPlayer;
		activeEffects = new HashSet<TickingEffect>();
		passiveEffects = EnumSet.noneOf(PassiveCondition.class);
	}
	
	// For all active effects.
	public synchronized boolean registerActiveEffect(TickingEffect newEffect, int times) {
		if (times == 0 || newEffect == null) {
			throw new IllegalArgumentException("Invalid times or Effect cannot be null: " + times);
		}
		if (!activeEffects.contains(newEffect)) { // If is an effect NOT present on Mobile.
			newEffect.startTicking(times);
			activeEffects.add(newEffect); // Registers this effect with the Mobile
			newEffect.setLinkedManager(this);
			return true;
		} else {  // If the effect is already present on mobile
			for (TickingEffect effect : activeEffects) {
				if (effect.equals(newEffect)) {
					return effect.stackedInstance(newEffect, times);
				}
			}
			return false;
		}		
	}
	// For Passive effects that are on a time limit.
	public boolean registerPassiveEffectDestroyAfterXMilliseconds(PassiveCondition newEffect, int duration) {
		if (newEffect == null || duration <= 0) {
			throw new IllegalArgumentException("Invalid effect or duration: " + newEffect + " " + duration);
		}		
		if(registerEffect(newEffect)) { // If registered successfully.
			scheduleDestroyAfterXMilliseconds(newEffect, duration);
			return true;
		}
		return false;
	}
	
	
	public boolean registerPermanentPassiveEffect(PassiveCondition newEffect) {
		if (newEffect == null) {
			throw new IllegalArgumentException("Invalid effect: " + newEffect);
		}		
		return registerEffect(newEffect);
	}
	
	public boolean hasCondition(TickingEffect checkedEffect) {
		for (TickingEffect effect : activeEffects) {
			if (effect.equals(checkedEffect)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasCondition(PassiveCondition checkedEffect) {
		return passiveEffects.contains(checkedEffect);
	}
	
	public void shutDown() {
		WorldServer.shutdownAndAwaitTermination(effectExecutor);
		WorldServer.shutdownAndAwaitTermination(wrapperExecutor);
	}
	
	private boolean registerEffect(PassiveCondition newEffect) {
		if (passiveEffects.add(newEffect)) {
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
		System.out.println(activeEffects);
		for (TickingEffect effect : activeEffects) {
			if (effect.equals(oldEffect)) {
				effect.kill();
				break;
			}
			return;
		}
	//	activeConditions.remove(oldEffect); Does within kill()?
		System.out.println(activeEffects);
	//	if (!hasCondition(oldEffect)) {
	//		throw new IllegalArgumentException("Not Present: " + oldEffect);
	//	}
	//	activeConditions.get(oldEffect.getClass()).kill();
	}
	
	public void unRegisterActiveCondition(TickingEffect oldEffect) {
		activeEffects.remove(oldEffect);
		oldEffect.doOnDestruction();
//		bondedMobile.displayPrompt();
	}	
	
	public void unRegisterEffect(PassiveCondition oldEffect) {
		passiveEffects.remove(oldEffect);
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
