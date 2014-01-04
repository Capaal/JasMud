package actions;

import java.util.Random;

import processes.Skill;
import interfaces.Action;

public class Chance implements Action {
	
	private final int chance;
	private final Action action;
	
	public Chance(int chance, Action action) {
		this.chance = chance;
		this.action = action;
	}

	@Override
	public boolean activate(Skill s) {
		Random ran = new Random();
		int roll = ran.nextInt(101);
		if (roll <= chance) {
			return action.activate(s);
		}
		return true;
	}

}
