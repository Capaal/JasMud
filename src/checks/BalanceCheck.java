package checks;

import skills.Arcane.Skill;
import interfaces.*;

public class BalanceCheck implements Action {
	
	private final Boolean isItThis;
	private final Who who;
	private final Where where;
	
	public BalanceCheck(Boolean isItThis, Who who, Where where) {
		this.isItThis = isItThis;
		this.who = who;
		this.where = where;
	}
	
	@Override
	public boolean activate(Skill s) {
		boolean success = false;
		for (Mobile m : who.findTarget(s,  where.findLoc(s))) {
			if (m.hasBalance() == isItThis) {
				success = true;
			} else {
				return false;
			}
		}
		return success;
	}
}
