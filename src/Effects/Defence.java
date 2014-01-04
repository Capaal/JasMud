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

// Right now all defences cut any attack of that type's damage in half. It can't be different at the moment.

public class Defence extends Effect {

	private final int startTick;
	private final int endTick;
	private final Type type;
	
	public Defence(Mobile currentPlayer, int duration, Type type) {
		super(currentPlayer);
		startTick = currentPlayer.getTick();
		endTick = startTick + duration;
		this.type = type;
	}
	
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
}
