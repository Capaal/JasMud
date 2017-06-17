package effects;

import processes.Type;
import interfaces.*;

public class MercRegen extends TickingEffect {
	
	private int intensity;	

	public MercRegen(Mobile currentPlayer, int intensity) {
		super(currentPlayer, 5000);
		if (intensity >= 0) {
			throw new IllegalArgumentException("Currently regen value must be given as a NEGATIVE.");
		}		
		this.intensity = intensity;
	}
	@Override
	public void run() {
		currentPlayer.takeDamage(Type.REGEN, intensity);		
		wrapper.modifyTimesRan(1); //this might be a silly hack to make infinite
	}
	
	@Override // TODO
	//useless for this skill
	public boolean stackedInstance(TickingEffect stackedInstance, int times) {
		return true;
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
