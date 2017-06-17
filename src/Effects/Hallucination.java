package effects;

import interfaces.TickingEffect;

import java.util.Random;

import interfaces.Mobile;
import processes.Type;

public class Hallucination extends TickingEffect {
	
	public Hallucination (Mobile currentPlayer) {
		super(currentPlayer, 3000);
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
				currentPlayer.takeDamage(Type.BLEED, 5); //hallucinations are better with realism, but effect is less than real bleed
			break;
		}
		wrapper.modifyTimesRan(1); // Hack to make infinite
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