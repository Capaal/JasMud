package effects;

import interfaces.TickingEffect;

import java.util.Random;

import interfaces.Mobile;
import processes.Type;

public class Unbalanced extends TickingEffect {
	private final Mobile currentPlayer;
	private final Type type;
	
	public Unbalanced (Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
		this.interval = 3000;
		type = Type.UNBALANCED;
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