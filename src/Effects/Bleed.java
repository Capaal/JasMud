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
public class Bleed implements TickingEffect {
	
	private final Mobile currentPlayer;
	private final Type type;

	public Bleed(Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
		type = Type.BLEED;
	}
	@Override
	public boolean isInstanceOf(Effect otherEffect) {
		if (otherEffect.getClass() == Bleed.class) {
			return true;
		}
		return false;
	}
	@Override
	public void run() {
		currentPlayer.takeDamage(type, currentPlayer.getMaxHp() / 20);		
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
