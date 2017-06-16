package effects;

import interfaces.Mobile;
import interfaces.TickingEffect;
import processes.Type;

public class Belladonna extends TickingEffect {
	private final Mobile currentPlayer;
	private final Type type;
	private int stack;
	
	public Belladonna(Mobile currentPlayer) {
		this.currentPlayer = currentPlayer;
		this.interval = 3000;
		type = Type.BELLADONNA;
	}

	@Override
	public void run() {
		//could switch/case this without break
		if (stack == 1) { //apply hallucin
			applyHallucin();
		} else if (stack == 2) { //apply hallucin + balance loss
			applyHallucin();
			if (!(currentPlayer.hasCondition(new Unbalanced(currentPlayer)))) {
				currentPlayer.addActiveCondition(new Unbalanced(currentPlayer), 100);
			}
		} else if (stack == 3) { 
			currentPlayer.takeDamage(Type.NULL, currentPlayer.getMaxHp());
			currentPlayer.tell("You finally succumb to the belladonna poison.");
		}
	}

	@Override
	public void doOnCreation() {
		stack = 1;
		applyHallucin();
	}

	@Override
	public void doOnDestruction() {
		stack = 0;
	}

	@Override //handles + and -
	public boolean stackedInstance(TickingEffect stackedInstance, int times) {
		stack = stack + times;
		return true;
	}
	
	private void applyHallucin() {
		if (!(currentPlayer.hasCondition(new Hallucination(currentPlayer)))) {
			currentPlayer.addActiveCondition(new Hallucination(currentPlayer), 5);
		}
	}
}
