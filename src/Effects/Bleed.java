package effects;

import processes.Type;
import interfaces.*;


// HOW SHOULD BLEED WORK
// Intensity = 200 then every tick you take 200 damage
// More effects of bleed increase the ONE instance of bleed.
// Possibly up to intensity 205, then 210.
// Each tick DECREASES the intensity after damage.
// So lowers to 205, 200, then 195 and so on.
// So it counts down as it deals damage until it goes away.
public class Bleed extends TickingEffect {
	
	private int intensity;

	public Bleed(Mobile currentPlayer, int initialIntensity) {
		super(currentPlayer, 2000);
		intensity = initialIntensity;
	}
	
	
	@Override
	public void run() {
		currentPlayer.takeDamage(Type.BLEED, intensity);	
		changeIntensity(-5);
	}
	
	@Override
	public boolean stackedInstance(TickingEffect stackedInstance, int times) {
		changeIntensity(((Bleed)stackedInstance).intensity);
		return true;
	}
	
	public synchronized void changeIntensity(int change) {
		this.intensity += change;
		if (intensity <= 0) {
			currentPlayer.removeCondition(this);
		}
	}
	@Override
	public void doOnCreation() {
		currentPlayer.displayPrompt();
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doOnDestruction() {
		// TODO Auto-generated method stub
		
	}
}
