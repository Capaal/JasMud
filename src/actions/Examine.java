package actions;

import interfaces.*;
import processes.*;
import skills.Arcane.Skill;

public class Examine implements Action {
	
	private final Where where;

	public Examine(Where where) {
		this.where = where;
	}
	
	@Override
	public boolean activate(Skill s) {
		String toExamine = UsefulCommands.returnTarget(s.getFullCommand());			
		for (Container c : where.findLoc(s)) {
			for (Holdable h : c.getInventory()) {
				if (h.getName().equals(toExamine) || (h.getName() + h.getId()).equals(toExamine)) {
					s.getCurrentPlayer().tell(h.getDescription());
					return true;
				}
			}
		}
		s.getCurrentPlayer().tell("You search around for " + toExamine + " but find nothing to examine.");
		return false;	
	}
}
