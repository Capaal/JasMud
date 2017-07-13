package processes;

import interfaces.Mobile;
import interfaces.TickingEffect;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
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
	
	/**
	 * For registering TickingEffects.
	 * @param newEffect May not be null.
	 * @param times Number of times to tick.
	 * @return boolean True usually, false if stackedInstance of tickingEffect returns false.
	 * @throws IllegalArgumentException Throws if newEffect is null or times <= 0.
	 */
	public synchronized boolean registerActiveEffect(TickingEffect newEffect, int times) {
		if (times <= 0 || newEffect == null) {
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
	/**
	 * Registers PassiveCondion with a set duration.
	 * @param newEffect May not be null. 
	 * @param duration May not be less than or equal to 0.
	 * @return boolean True if the newEffect was attached to the Mobile.
	 * @throws IllegalArgumentException Thrown if newEffect == null or duration <= 0.
	 */
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
	
	/**
	 * Registering permanant PassiveCondition to Mobile.
	 * @param newEffect May not be null
	 * @return boolean True if successfully registered to Mobile.
	 * @throws IllegalArgumentException Thrown if newEffect == null.
	 */
	public synchronized boolean registerPermanentPassiveEffect(PassiveCondition newEffect) {
		if (newEffect == null) {
			throw new IllegalArgumentException("Invalid effect: " + newEffect);
		}		
		return registerPassiveEffect(newEffect);
	}
	
	public synchronized boolean hasCondition(TickingEffect checkedEffect) {
		return activeEffects.stream().anyMatch(x -> x.equals(checkedEffect));
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
		WorldServer.getGameState().getEffectExecutor().schedule(() -> unRegisterEffect(newEffect), milliseconds, TimeUnit.MILLISECONDS);
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
	/**
	 * Removes TickingEffect from list of current Active Effects. If successful then calls doOnDestruction().
	 * Might be intented only to be used withing kill()?
	 * @param oldEffect
	 */
	public synchronized void unRegisterActiveCondition(TickingEffect oldEffect) {
		if(activeEffects.remove(oldEffect)) {
			oldEffect.doOnDestruction();
		}
	}	
	
	/**
	 * Removes a PassiveCondition from Mobile, if successful then calls doOnDestruction().
	 * @param oldEffect Class of effect to be removed.
	 */
	public synchronized void unRegisterEffect(PassiveCondition oldEffect) {
		if (passiveEffects.remove(oldEffect)) {
			oldEffect.doOnDestruction(bondedMobile);
		}
	}
	
	/**
	 * For viewing the list of Active Effects.
	 * @return Set<TickingEffect> COPY of the list of current active effects.
	 */
	public Set<TickingEffect> getAllActiveEffects() {
		return new HashSet<TickingEffect>(activeEffects);
	}
	
	/**
	 * For viewing the list of Passive Effects.
	 * @return EnumSet<PassiveCondition> COPY of the list of current passive effects.
	 */
	public EnumSet<PassiveCondition> getAllPassiveEffects() {
		return EnumSet.copyOf(passiveEffects);
	}
}
