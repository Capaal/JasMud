package actions;

import interfaces.*;
import skills.Arcane.Skill;

public class Look implements Action {
	
	private Where where;
	
	public Look(Where where) {
		this.where = where;
	}

	@Override
	public boolean activate(Skill s) {
		Mobile currentPlayer = s.getCurrentPlayer();
		for (Container c : where.findLoc(s)) {
			c.look(currentPlayer);
		}
		return true;
	}
}
