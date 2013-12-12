package actions;

import processes.Type;
import effects.Bleed;
import effects.Defence;
import skills.Arcane.Skill;
import interfaces.Action;
import interfaces.Mobile;

public class Effecter implements Action {
	
	private final int duration;
	private final Who who;
	private final Where where;
	private final EffectType effect;
	private final Type type;
	
	public Effecter(int duration, EffectType effect, Type type, Who who, Where where) {
		this.duration = duration;
		this.type = type;
		this.effect = effect;
		this.who = who;
		this.where = where;
	}

	@Override
	public boolean activate(Skill s) {
		return effect.applyEffect(duration, type, s, who, where);
	}
	
	public enum EffectType {
		
		BLEED() {
			public boolean applyEffect(int duration, Type type, Skill s, Who who, Where where) {
				for (Mobile m : who.findTarget(s,  where.findLoc(s))) {
					m.addEffect(new Bleed(m, duration));
				}
				return true;
			}
			
		},
		
		DEFENCE() {
			public boolean applyEffect(int duration, Type type, Skill s, Who who, Where where) {
				for (Mobile m : who.findTarget(s,  where.findLoc(s))) {
					m.addEffect(new Defence(m, duration, type));
				}
				return true;
			}
			
		};
		
		private EffectType() {
		}
		
		public abstract boolean applyEffect(int duration, Type type, Skill s, Who who, Where where);
	}

}
