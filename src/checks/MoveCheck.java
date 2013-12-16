package checks;

import java.util.ArrayList;

import processes.*;
import skills.Arcane.Skill;
import interfaces.*;

public class MoveCheck implements Action {
	
	final String LEAVEMSG = " leaves to the ";
	final String ENTERMSG = " enters from the ";
	final String FAILMSG = "You can't go that way.";
	final String WRONGTYPE = "water"; // Bad, what about flying?
	final String WRONGTYPEMSG = "You'll need to swim to go that way."; // Bad like above.	
	
	private final Who who;
	private final Where where;
	private final Where finalLoc;
	
	public MoveCheck(Who who, Where where, Where finalLoc) {
		this.who = who;
		this.where = where;
		this.finalLoc = finalLoc;
	}

	@Override
	public boolean activate(Skill s) {
		for (Mobile m : who.findTarget(s,  where.findLoc(s))) {
			for (Container c : finalLoc.findLoc(s)) {
				if (c == null) {
					return false;
				}
			}
		}		
		return true;
	}
}