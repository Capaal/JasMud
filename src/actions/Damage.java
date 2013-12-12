package actions;

import interfaces.Action;
import interfaces.Container;
import interfaces.Mobile;

import java.util.ArrayList;

import processes.Location;

import skills.Arcane.Skill;

public class Damage implements Action {
	
	private final int intensity;
	private final Who who;
	private final Where where;

	public Damage(int intensity, Who who, Where where) {
		this.intensity = intensity;
		this.who = who;
		this.where = where;
	}	
	
	@Override
	public boolean activate(Skill s) {
		ArrayList<Location> loc = where.findLoc(s);
		ArrayList<Mobile> target = who.findTarget(s, loc);
		if (loc != null && target != null) {
			for (Mobile m : target) {
				m.takeDamage(s.getTypes(), intensity);
			}
			return true;
			
		}
		return false;
	}

}
