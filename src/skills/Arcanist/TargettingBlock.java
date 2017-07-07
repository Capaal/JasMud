package skills.Arcanist;

import java.util.List;

import interfaces.Mobile;
import processes.Location;

public class TargettingBlock implements ArcanistBlock {
	
	private final WhoTargettingBlock who;
	private final WhereTargettingBlock where;

	
	public TargettingBlock(WhoTargettingBlock who, WhereTargettingBlock where) {
		this.who = who;
		this.where = where;
	}
	
	@Override
	public void perform(ArcanistSkill skill) {
		List<Location> locations = where.findWhere(skill);
		List<Mobile> targets = who.findWho(skill, locations);
		skill.setTargets(targets);	
	}
	
	@Override
	public int determineCost() {
		int cost = 0;
		if (who != null) {
			cost += who.determineCost();
		}
		if (where != null) {
			cost += where.determineCost();
		}
		return cost;
	}

	@Override
	public StringBuilder describeOneself(StringBuilder sb) {
		if (who != null) {
			sb = who.describeOneself(sb);
		}
		if (where != null) {
			sb = where.describeOneself(sb);
		}
		return sb;
	}

	// May exist as incomplete while building, but should not be able to end up in a skill without both WHO and WHERE.
	@Override
	public boolean isValid() {
		if (who == null || where == null) {
			return false;
		}
		return true;
	}
	
	public WhoTargettingBlock getWho() {
		return who;
	}

	public WhereTargettingBlock getWhere() {
		return where;
	}
}
