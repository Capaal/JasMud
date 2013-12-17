package actions;

import processes.Skill;
import processes.UsefulCommands;
import interfaces.*;

public class Look implements Action {
	
	private Where where;
	
	public Look(Where where) {
		this.where = where;
	}

	@Override
	public boolean activate(Skill s) {
		String dir = UsefulCommands.getSecondWord(s.getFullCommand());
		if (dir != null) {
			for (Container c : where.findLoc(s)) {
				c.look(s.getCurrentPlayer());
			}
		} else {
			for (Container c : Where.HERE.findLoc(s)) {
				c.look(s.getCurrentPlayer());
			}
		}
		return true;
	}
}
