package effectors;

import processes.Skill;
import processes.Type;
import effects.Defence;
import interfaces.*;

public class DefenceEffect implements Action {
	
	private final int duration;
	private final Who who;
	private final Where where;
	
	private final Type type;
	
	public DefenceEffect(int duration, Type type, Who who, Where where) {
		this.duration = duration;
		this.who = who;
		this.where = where;
		this.type = type;
	}

	@Override
	public boolean activate(Skill s) {
		for (Mobile m : who.findTarget(s,  where.findLoc(s))) {
			m.addEffect(new Defence(m, duration, type));
		}
		return true;
	}
}
