package actions;

import processes.Skill;
import interfaces.*;

public class Move implements Action {
		
	private final Who who;
	private final Where where;
	private final Where finalLoc;
	
	public Move(Who who, Where where, Where finalLoc) {
		this.who = who;
		this.where = where;
		this.finalLoc = finalLoc;
	}
	
	// Currently gets 1 location poorly, need a better Where ENUM to handle 1 location away. Or something else.
	public boolean activate(Skill s) {	
		for (Mobile m : who.findTarget(s, where.findLoc(s))) {
			Container c = finalLoc.findLoc(s).get(0);		
			m.getContainer().removeItemFromLocation(m);
			m.setContainer(c);
			c.acceptItem(m);
			c.look(m);
		}
		return true;
	}
}
	