package processes;

import interfaces.Mobile;
import interfaces.TickingEffect;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import effects.PassiveCondition;

// Contains methods to handle passive and ticking effects on a Mobile
public class EffectManager {

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
	}
	// For Passive effects that are on a time limit.
	public synchronized boolean registerPassiveEffectDestroyAfterXMilliseconds(PassiveCondition newEffect, int duration) {
		if (newEffect == null || duration <= 0) {
			throw new IllegalArgumentException("Invalid effect or duration: " + newEffect + " " + duration);
		}		
		if(registerPassiveEffect(newEffect)) { // If registered successfully.
			scheduleDestroyAfterXMilliseconds(newEffect, duration);
			return true;
		}
		return false;
	}
	
	
	public synchronized boolean registerPermanentPassiveEffect(PassiveCondition newEffect) {
		if (newEffect == null) {
			throw new IllegalArgumentException("Invalid effect: " + newEffect);
		}		
		return registerPassiveEffect(newEffect);
	}
	
	public synchronized boolean hasCondition(TickingEffect checkedEffect) {
		for (TickingEffect effect : activeEffects) {
			if (effect.equals(checkedEffect)) {
				return true;
			}
		}
		return false;
	}
	
	public synchronized boolean hasCondition(PassiveCondition checkedEffect) {
		return passiveEffects.contains(checkedEffect);
	}
	
	private synchronized boolean registerPassiveEffect(PassiveCondition newEffect) {
		if (passiveEffects.add(newEffect)) {
			newEffect.doOnCreation(bondedMobile);
			return true;
		}
		return false;		
	}
	
	private void scheduleDestroyAfterXMilliseconds(PassiveCondition newEffect, int milliseconds) {
		WorldServer.getGameState().getEffectExecutor().schedule(new removeTask(newEffect), milliseconds, TimeUnit.MILLISECONDS);
	}
	
	// Removing an existing effect (curing and the such)
	public synchronized void unRegisterEffect(TickingEffect oldEffect) {
		for (TickingEffect effect : activeEffects) {
			if (effect.equals(oldEffect)) {
				effect.kill();
				break;
			}
		}
	}
	
	// Specifically for use within kill();
	public synchronized void unRegisterActiveCondition(TickingEffect oldEffect) {
		activeEffects.remove(oldEffect);
		oldEffect.doOnDestruction();
	}	
	
	public synchronized void unRegisterEffect(PassiveCondition oldEffect) {
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
	
	public Set<TickingEffect> getAllActiveEffects() {
		return new HashSet<TickingEffect>(activeEffects);
	}
	
	public EnumSet<PassiveCondition> getAllPassiveEffects() {
		return EnumSet.copyOf(passiveEffects);
	}
}
