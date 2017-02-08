package processes;

// Can the Bleed effect, be just made into a generic type effect? Then I can have their individual effects in here, and that effect
// attachment just says, this one is a bleed thingy, so go here to do effect. It would know duration, intensity.

public enum Type {
	
	SHARP() {				
	},
	
	PIERCE() {		
	},
	
	BLUNT() {
	},
	
	POISON() {
	},
	
	FIRE() {
	},
	
	COLD() {
	},
	
	BLEED() {
	},
	
	NULL() {
	},
	
	HEAL() {
	}, 
	
	REGEN() {
		
	},
	;
	
	private Type() {};
	
//	public abstract int returnTypeId();
	
	
}
