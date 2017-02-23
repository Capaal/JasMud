package effects;

import processes.Type;
import interfaces.*;

public class Regen extends TickingEffect {
	
	private final Mobile currentPlayer;
	private final Type type;
	private int intensity;	

	public Regen(Mobile currentPlayer, int intensity) {
		super();
		if (intensity >= 0) {
			throw new IllegalArgumentException("Currently regen value must be given as a NEGATIVE.");
		}		
		interval = 7000;
		this.currentPlayer = currentPlayer;
		this.intensity = intensity;
		type = Type.REGEN;
	}
	@Override
	public void run() {
		if (currentPlayer.getCurrentHp() == currentPlayer.getMaxHp()) {
			return;
		}
		currentPlayer.takeDamage(type, intensity);		
	}
	
	@Override // TODO
	public boolean stackedInstance(TickingEffect stackedInstance, int times) {
		// set to maxIntensity in case the new regen is better? Or just replace?
		// then changes countdown wrapper's remaining times to run and resets counter.
		// OR should we just re-create the wrapper??? we've practically re-made it.
		int incomingIntensity = ((Regen)stackedInstance).intensity;
		if (intensity < incomingIntensity) {
			this.intensity = incomingIntensity;
		}
		wrapper.modifyTimes(times);
		wrapper.modifyTimesRan(0);
		return true;
	}
	
	@Override
	public int getInterval() {
		return interval;
	}
	
	@Override
	public void doOnCreation() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doOnDestruction() {
		// TODO Auto-generated method stub
		
	}
}
