package checks;

import processes.Type;
import skills.Arcane.Skill;
import interfaces.*;

public class WeaponEquippedCheck implements Action {
	
	private final Type isItThis;
	private final Who who;
	private final Where where;
	
	public WeaponEquippedCheck(Type isItThis, Who who, Where where) {
		this.isItThis = isItThis;
		this.who = who;
		this.where = where;
	}
	
	@Override
	public boolean activate(Skill s) {
		boolean success = false;
		for (Mobile m : who.findTarget(s, where.findLoc(s))) {
			if (m.hasWeaponType(isItThis)) {
				success = true;
			} else {
				return false;
			}
		}
		return success;
	}
}
