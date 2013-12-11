package actions;

import effects.Bleed;
import skills.Arcane.Skill;
import interfaces.Action;
import interfaces.Mobile;

public class Effecter implements Action {
	
	private final int duration;
	private final Who who;
	private final Where where;
	private final EffectType type;
	
	public Effecter(int duration, EffectType type, Who who, Where where) {
		this.duration = duration;
		this.who = who;
		this.where = where;
		this.type = type;
	}

	@Override
	public boolean activate(Skill s) {
		return type.applyEffect(duration, s, who, where);
	}
	
	public enum EffectType {
		
		BLEED() {
			public boolean applyEffect(int duration, Skill s, Who who, Where where) {
				for (Mobile m : who.findTarget(s,  where.findLoc(s))) {
					m.addEffect("bleed", new Bleed(m, duration));
				}
				return true;
			}
			
		},
		
		Defence() {
			public boolean applyEffect(int duration, Skill s, Who who, Where where) {
				return false;
			}
			
		};
		
		private EffectType() {
		}
		
		public abstract boolean applyEffect(int duration, Skill s, Who who, Where where);
	}

}
