package actions;

import interfaces.*;
import processes.UsefulCommands;
import skills.Arcane.Skill;

public class Drop implements Action {
	
	private final Who who;
	private final Where where;
	private final Where finalLoc;

	public Drop(Who who, Where where, Where finalLoc) {
		this.who = who;
		this.where = where;
		this.finalLoc = finalLoc;
	}	
	
	@Override
	public boolean activate(Skill s) {
		String toDrop = UsefulCommands.returnTarget(s.getFullCommand()).toLowerCase();
		Boolean success = false;
		for (Mobile m : who.findTarget(s, where.findLoc(s))) {
			Holdable item = UsefulCommands.stringToHoldable(toDrop, m);
			if (item != null && item instanceof Item) { // Is instanceof Item correct? Or are other things dropable here? Isn't holdable more true?
				m.removeItem(item);
				/// currently drops in first location for finalLoc, to ensure one location gets item, but better way is better.
				((finalLoc.findLoc(s)).get(0)).acceptItem(item); 
				success = true;
			} else {
				return false;
			}
		}
		return success;
	}
}
