package effects;

import java.util.ArrayList;
import java.util.List;

import processes.Type;
import interfaces.Effect;
import interfaces.Mobile;

//**** Possibly set up for a lot of choices, like how much damage is blocked? Percentage, set?
// How often can it block damage?
// What type, obviously. But can it be a defence against multiple types? Or multiples in that it is very specific?
// What about removal? Is being so generic going to make removal difficult?

public class Defence extends Effect {

//	private final Mobile currentPlayer;
	private final int startTick;
	private final int endTick;
	private final Type type;
	
	public Defence(Mobile currentPlayer, int duration, Type type) {
		super(currentPlayer);
	//	intensity = currentPlayer.getMaxHp()/100;
		startTick = currentPlayer.getTick();
		endTick = startTick + duration;
		this.type = type;
	}
	
//	@Override
//	public void destroyEffect() {
//		currentPlayer.removeEffect(this);
//		this.currentPlayer = null;
//	}

	@Override
	public void doTickEffect() {		
	}
	
	@Override
	public int doRunEffect(List<Type> incomingTypes, int damage) {
		for (Type t : incomingTypes) {
			if (t == this.type){
				return (int)(damage*.5);
			}
		}
		return damage;
	}

//	@Override
//	public Mobile getCurrentPlayer() {
//		return currentPlayer;
//	}

//	@Override
//	public void setCurrentPlayer(Mobile currentPlayer) {
//		this.currentPlayer = currentPlayer;
//		
//	}

}
