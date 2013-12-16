package actions;

import interfaces.*;
import processes.UsefulCommands;
import skills.Arcane.Skill;

public class Get implements Action {
	
	private final Who who;
	private final Where where;
	
	public Get(Who who, Where where) {
		this.who = who;
		this.where = where;
	}	
	
	// Forcing 1 locations right now, need a better way to ensure no duplications of items
	@Override
	public boolean activate(Skill s) {
		String toGet = UsefulCommands.returnTarget(s.getFullCommand()).toLowerCase();
		for (Container c : where.findLoc(s)) {
			Holdable item = UsefulCommands.stringToHoldable(toGet, c);
			if (item != null && item instanceof Item) { // Is instanceof Item correct? Or are other things dropable here? Isn't holdable more true?
				c.removeItemFromLocation(item);
				Mobile m = who.findTarget(s, where.findLoc(s)).get(0);
				m.acceptItem(item);
				m.tell("You pick up a " + item.getShortDescription() + ".");
				return true;
			} 
		}
		return false;
	}
}
