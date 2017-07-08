package skills.Arcanist.Targetting;

import java.util.ArrayList;
import java.util.List;

import interfaces.Mobile;
import processes.Location;
import processes.Skills.Syntax;
import skills.Arcanist.ArcanistBlock;
import skills.Arcanist.ArcanistSkill;

public class TargettingBlock implements ArcanistBlock {
	
	private final WhoTargettingBlock who;
	private final WhereTargettingBlock where;
	private final List<Syntax> syntax;

	
	public TargettingBlock(WhoTargettingBlock who, WhereTargettingBlock where) {
		this.who = who;
		this.where = where;
		List<Syntax> preSyntax = new ArrayList<Syntax>();
		Syntax whoSyntax = null;
		if (who != null) {
			whoSyntax = who.requestSyntax();
		}
		Syntax whereSyntax = null;
		if (where != null) {
			whereSyntax = where.requestSyntax();
		}
		preSyntax.add(Syntax.SKILL);
		if (whoSyntax != null) {
			preSyntax.add(whoSyntax);
		}
		if (whereSyntax != null) {
			preSyntax.add(whereSyntax);
		}
		this.syntax = preSyntax;
		// Based on BOTH who AND where, define the syntax? So who= all and where = here should be SKILL
		// who=all where=oneaway should be SKILL DIRECTION
		// who = target where = projectile should be SKILL TARGET DIRECTION TODO
	}
	
	@Override
	public void perform(ArcanistSkill skill) {
		List<Location> locations = where.findWhere(skill);
		if (locations == null) {
			return;
		}
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

	public List<Syntax> getSyntax() {
		return syntax;
	}
}
