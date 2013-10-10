package skills.Arcane;

import java.util.ArrayList;

import processes.UsefulCommands;
import Interfaces.Action;
import Interfaces.Mobile;

public class Damage implements Action {
	
	private int damage;
	
	public Damage(int damage) {
		this.damage = damage;
	}

	@Override
	public boolean activate(Skill s) {
		ArrayList<Mobile> targets = s.getTargets(0);
		for (Mobile m : targets) {
			m.takeDamage(damage);
		}
		return true;
	}

}
