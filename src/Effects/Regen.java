package effects;

import processes.Type;
import interfaces.*;

public class Regen extends TickingEffect {
	
	protected int intensity;	

	public Regen(Mobile currentPlayer, int intensity) {
		super(currentPlayer, 7000);
		if (intensity >= 0) {
			throw new IllegalArgumentException("Currently regen value must be given as a NEGATIVE.");
		}	
		this.intensity = intensity;
	}
	@Override
	public void run() {
		currentPlayer.takeDamage(Type.REGEN, intensity);		
	}
	
	@Override // TODO
	public boolean stackedInstance(TickingEffect stackedInstance, int times) {
		// set to maxIntensity in case the new regen is better? Or just replace?
		// then changes countdown wrapper's remaining times to run and resets counter.
		// OR should we just re-create the wrapper??? we've practically re-made it.
		wrapper.modifyTimesRan(0);
		return true;
	}
	
	@Override public String getInfo() {
		return intensity + "/tick for " + wrapper.remainingTime() + " milliseconds.";
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
