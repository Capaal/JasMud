package effects;

import interfaces.TickingEffect;

import java.util.Random;
import interfaces.Mobile;

public class Unbalanced extends TickingEffect {
	
	public Unbalanced (Mobile currentPlayer) {
		super(currentPlayer, 3000);
	}
	
	@Override
	public void run() {
		Random rand = new Random();
		int i = rand.nextInt(100);
		if (i <= 50) {
			currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 1200);
			currentPlayer.tell("You stumble clumsily.");
		}
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