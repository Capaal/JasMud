package actions;


// **** NOTE: This probably needs to be changed so that all costs are ALSO checks, I think that will always be true.

import actions.Check.CheckType;
import skills.Arcane.Skill;
import interfaces.Action;
import interfaces.Mobile;

public class Cost implements Action {
	
	private final int amount;
	private final Who who;
	private final Where where;
	private final CostType costType;
	
	public Cost(int amount, CostType costType, Who who, Where where) {
		this.amount = amount;
		this.who = who;
		this.where = where;
		this.costType = costType;
	}

	@Override
	public boolean activate(Skill s) {
		return costType.processCost(amount, s, who, where);
	}
	
	public enum CostType {
		
		BALANCE() {
			public boolean processCost(int amount, Skill s, Who who, Where where) {
				Check thisCheck;
				if (amount == -1) {
					thisCheck = new Check("false", CheckType.BALANCE, who, where);
				} else {
					thisCheck = new Check("true", CheckType.BALANCE, who, where);
				}
				if (!thisCheck.activate(s)) {
					return false;
				}
				for (Mobile m : who.findTarget(s, where.findLoc(s))) {
					if (amount == -1) {
						m.setBalance(true);
					} else {
						m.setBalance(false);
					}
				}
				return true;
			}
		},	
		
		MANA() {
			public boolean processCost(int amount, Skill s, Who who, Where where) {
				return false;
			}			
		},
		
		GOLD() {
			public boolean processCost(int amount, Skill s, Who who, Where where) {
				return false;
			}			
		};
		
		private CostType() {
		}
		
		public abstract boolean processCost(int amount, Skill s, Who who, Where where);
	}

}
