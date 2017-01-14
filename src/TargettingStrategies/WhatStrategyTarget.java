package TargettingStrategies;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.List;

import processes.Skill;
import processes.Skill.Syntax;

public class WhatStrategyTarget implements WhatStrategyInterface {

	// Typically a Where Strategy determined the container(s) to search for a holdable (given as containersToSearch).
	// This Strategy returns the FIRST matching holdable.
	// The string inputed by the user follows SYNTAX defined by the SKILL (when created)
	// Said skill MUST have defined a Syntax.TARGET within the expected command.
	// This strategy grabs the string located at said syntax's location, searches
	// given containers, and returns the FIRST holdable matching said string.
	@Override
	public List<Holdable> findWhat(Skill s, String fullCommand, Mobile currentPlayer, List<Container> containersToSearch) {
		List<Holdable> targ = new ArrayList<Holdable>();
		for (Container l : containersToSearch) {
			Holdable h = l.getHoldableFromString(s.getStringInfo(Syntax.TARGET, fullCommand));
			if (h != null) {
				targ.add(h);
				return targ;
			}
		}
		return targ;	
	}
	
	@Override
	public String toString() {
		return "TARGET";
	}

}
