package checks;

import processes.*;
import processes.Location.GroundType;
import interfaces.*;

public class MoveCheck implements Action {
	
	private final Who who;
	private final Where where;
	private final Where finalLoc;
	private final GroundType moveType;
		
	public MoveCheck(GroundType moveType, Who who, Where where, Where finalLoc) {
		this.who = who;
		this.where = where;
		this.finalLoc = finalLoc;
		this.moveType = moveType;
	}

	@Override
	public boolean activate(Skill s) {
		for (Mobile m : who.findTarget(s,  where.findLoc(s))) {
			for (Container c : finalLoc.findLoc(s)) {
				if (c == null) { 
					return false;
				}
				if ((m.getContainer().getGroundType() != moveType && c.getGroundType() != moveType)) {
					return false;
				}
				if (moveType == GroundType.GROUND && m.getContainer().getGroundType() != c.getGroundType()) {
					return false;
				}
			}
		}		
		return true;
	}
}