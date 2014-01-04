package costs;

import processes.Skill;
import checks.BalanceCheck;
import interfaces.*;

public class BalanceCost implements Action {
	
	private final Who who;
	private final Where where;
	private final boolean goesTo;
	
	public BalanceCost(Boolean goesTo, Who who, Where where) {
		this.who = who;
		this.where = where;
		this.goesTo = goesTo;
	}

	@Override
	public boolean activate(Skill s) {
		BalanceCheck thisCheck = new BalanceCheck(!goesTo, who, where);
		if (!thisCheck.activate(s)) {
			return false;
		}
		for (Mobile m : who.findTarget(s, where.findLoc(s))) {
			m.setBalance(goesTo);
		}
		return true;
	}
}
