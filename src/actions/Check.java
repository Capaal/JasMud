package actions;

import processes.Type;
import skills.Arcane.Skill;
import interfaces.Action;
import interfaces.Mobile;
import interfaces.Action.Where;
import interfaces.Action.Who;

public class Check implements Action {
	
	private final String checkValue;
	private final CheckType check;
	private final Who who;
	private final Where where;
	
	public Check(String checkValue, CheckType checktype, Who who, Where where) {
		this.checkValue = checkValue;
		this.check = checktype;
		this.who = who;
		this.where = where;
	}
	
	@Override
	public boolean activate(Skill s) {
		return check.processType(checkValue, s, who, where);
	}
	
	public enum CheckType {
		
		MANA() {
			public boolean processType(String checkValue, Skill s, Who who, Where where) {
				boolean success = false;
				for (Mobile m : who.findTarget(s, where.findLoc(s))) {
	//				if (m.getMana() >= checkValue) {
	//					success = true;
	//				} else {
	//					return false;
	//				}
				}
				return success;
			}
			
		},
		
		BALANCE() {
			public boolean processType(String checkValue, Skill s, Who who, Where where) {
				boolean success = false;
				for (Mobile m : who.findTarget(s,  where.findLoc(s))) {
					if (m.hasBalance()) {
						success = true;
					} else {
						return false;
					}
				}
				return success;
			}
		},
		
		WEAPON() {
			public boolean processType(String checkValue, Skill s, Who who, Where where) {
				boolean success = false;
				for (Mobile m : who.findTarget(s, where.findLoc(s))) {
					if (m.hasWeaponType(Type.valueOf(checkValue))) {
						success = true;
					} else {
						success = false;
					}
				}
				return success;
			}
		},
		
		GOLD() {
			public boolean processType(String checkValue, Skill s, Who who, Where where) {
				System.out.println(checkValue);
				return false;
			}
		};
		
		// And probably more for compenent types.
		
		// Constructor, leave blank.
		private CheckType() {
		}
		
		public abstract boolean processType(String checkValue, Skill s, Who who, Where where);				
	}

}
