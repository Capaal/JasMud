package effects;

import interfaces.TickingEffect;

import java.util.Random;

import interfaces.Mobile;
import processes.Type;

public class Hallucination extends TickingEffect {
	private final Mobile currentPlayer;
	private final Type type;
	
	public Hallucination (Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
		this.interval = 3000;
		type = Type.HALLUCINATION;
	}
	
	@Override
	public void run() {
		Random rand = new Random();
		int i = rand.nextInt(3);
		switch (i) {
			case 1 :
			currentPlayer.tell("Hallucination message 1."); //TODO make this the same as a different effect
			break;
			case 2 :
			currentPlayer.tell("Hallucination message 2.");
			break;
			case 3 :
			currentPlayer.tell("You are bleeding. (HM3)");
			currentPlayer.takeDamage(type.BLEED, 5); //hallucinations are better with realism, but effect is less than real bleed
			break;
		}
		wrapper.modifyTimesRan(1);
	}

	@Override
	public void doOnCreation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doOnDestruction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean stackedInstance(TickingEffect stackedInstance, int times) {
		// TODO Auto-generated method stub
		return false;
	}

}