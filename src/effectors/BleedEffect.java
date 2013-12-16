package effectors;

import processes.Type;
import effects.Bleed;
import skills.Arcane.Skill;
import interfaces.*;

public class BleedEffect implements Action {
	
	private final int duration;
	private final Who who;
	private final Where where;
	
	public BleedEffect(int duration, Who who, Where where) {
		this.duration = duration;
		this.who = who;
		this.where = where;
	}

	@Override
	public boolean activate(Skill s) {
		for (Mobile m : who.findTarget(s,  where.findLoc(s))) {
			m.addEffect(new Bleed(m, duration));
		}
		return true;
	}
}
