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
		for (TickingEffect effect : activeEffects) {
			if (effect.equals(newEffect)) {
				return effect.stackedInstance(newEffect, times);
			}
		}
		newEffect.startTicking(times);
		activeEffects.add(newEffect); // Registers this effect with the Mobile
		newEffect.setLinkedManager(this);
		return true;
		
		
		/*if (!activeEffects.contains(newEffect)) { // If is an effect NOT present on Mobile.
			System.out.println(activeEffects.contains(newEffect)); // Always returns false TODO
			System.out.println(activeEffects);
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
		}	*/	
	}
	// For Passive effects that are on a time limit.
	public boolean registerPassiveEffectDestroyAfterXMilliseconds(PassiveCondition newEffect, int duration) {
		if (newEffect == null || duration <= 0) {
			throw new IllegalArgumentException("Invalid effect or duration: " + newEffect + " " + duration);
		}		
		if(registerPassiveEffect(newEffect)) { // If registered successfully.
			scheduleDestroyAfterXMilliseconds(newEffect, duration);
			return true;
		}
		return false;
	}
	
	
	public boolean registerPermanentPassiveEffect(PassiveCondition newEffect) {
		if (newEffect == null) {
			throw new IllegalArgumentException("Invalid effect: " + newEffect);
		}		
		return registerPassiveEffect(newEffect);
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
	
	private boolean registerPassiveEffect(PassiveCondition newEffect) {
		if (passiveEffects.add(newEffect)) {
			newEffect.doOnCreation(bondedMobile);
			return true;
		}
		return false;		
	}
	
	private void scheduleDestroyAfterXMilliseconds(PassiveCondition newEffect, int milliseconds) {
		effectExecutor.schedule(new removeTask(newEffect), milliseconds, TimeUnit.MILLISECONDS);
	}
	
	// Removing an existing effect (curing and the such)
	public void unRegisterEffect(TickingEffect oldEffect) {
		for (TickingEffect effect : activeEffects) {
			if (effect.equals(oldEffect)) {
				effect.kill();
				break;
			}
		}
	}
	
	// Specifically for use within kill();
	public void unRegisterActiveCondition(TickingEffect oldEffect) {
		activeEffects.remove(oldEffect);
		oldEffect.doOnDestruction();
	}	
	
	public void unRegisterEffect(PassiveCondition oldEffect) {
		passiveEffects.remove(oldEffect);
		oldEffect.doOnDestruction(bondedMobile);
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
}
