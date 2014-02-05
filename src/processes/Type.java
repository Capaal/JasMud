package processes;

// Can the Bleed effect, be just made into a generic type effect? Then I can have their individual effects in here, and that effect
// attachment just says, this one is a bleed thingy, so go here to do effect. It would know duration, intensity.

public enum Type {
	
	SHARP() {
		public int returnTypeId() {
			return 1;
		}
		
	},
	
	PIERCE() {
		public int returnTypeId() {
			return 2;
		}		
	},
	
	BLUNT() {
		public int returnTypeId() {
			return 3;
		}
	},
	
	POISON() {
		public int returnTypeId() {
			return 4;
		}
	},
	
	FIRE() {
		public int returnTypeId() {
			return 5;
		}
	},
	
	COLD() {
		public int returnTypeId() {
			return 6;
		}
	},
	
	BLEED() {
		public int returnTypeId() {
			return 7;
		}
	},
	
	NULL() {
		public int returnTypeId() {
			return 8;
		}
	},
	
	HEAL() {
		public int returnTypeId() {
			return 9;
		}
	};
	
	private Type() {};
	
	public abstract int returnTypeId();
	
	
}
