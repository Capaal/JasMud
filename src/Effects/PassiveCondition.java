package effects;

import interfaces.Mobile;


public enum PassiveCondition {
		
		BROKENRIGHTARM() {
			
		},
		
		BROKENLEFTARM() {
			
		},
		
		BROKENLEGS() {
			
		},
		
		BALANCE() {
			
		},
		
		DEFENCE() {
			@Override
			public void doOnCreation(Mobile currentPlayer) {
				currentPlayer.addDefense(10);
			}
			@Override
			public void doOnDestruction(Mobile currentPlayer) {
				currentPlayer.addDefense(-10);
				
			}
		},
		
		DIZZY() {
			
		};	
		
		private PassiveCondition() {}
		
		public void doOnCreation(Mobile currentPlayer) {}
		public void doOnDestruction(Mobile currentPlayer) {}
		
		//this doesn't work here
/*		public ConditionsEnum getBroken(String slot) {
			if (slot.equals("righthand")) {
				return ConditionsEnum.BROKENRIGHTARM;
			} else if (slot.equals("lefthand")) {
				return ConditionsEnum.BROKENLEFTARM;
			} else if (slot.equals("legs")) {
				return ConditionsEnum.BROKENLEGS;
			}
			return null;
		} */
	
}
