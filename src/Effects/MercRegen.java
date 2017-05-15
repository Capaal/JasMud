package effects;

import processes.Type;
import interfaces.*;

public class MercRegen extends TickingEffect {
	
	private final Mobile currentPlayer;
	private final Type type;
	private int intensity;	

	public MercRegen(Mobile currentPlayer, int intensity) {
		super();
		if (intensity >= 0) {
			throw new IllegalArgumentException("Currently regen value must be given as a NEGATIVE.");
		}		
		interval = 5000;
		this.currentPlayer = currentPlayer;
		this.intensity = intensity;
		type = Type.MERCREGEN;
	}
	@Override
	public void run() {
		if (currentPlayer.getCurrentHp() == currentPlayer.getMaxHp()) {
			return;
		}
		currentPlayer.takeDamage(type, intensity);		
		wrapper.modifyTimesRan(1); //this might be a silly hack
	}
	
	@Override // TODO
	//useless for this skill
	public boolean stackedInstance(TickingEffect stackedInstance, int times) {
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
