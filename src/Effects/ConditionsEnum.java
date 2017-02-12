package effects;


public enum ConditionsEnum {
		
		BROKENRIGHTARM() {
			
		},
		
		BROKENLEFTARM() {
			
		},
		
		BROKENLEGS() {
			
		},
		
		DIZZY() {
			
		};
		
		
		
		private ConditionsEnum() {}
		
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
