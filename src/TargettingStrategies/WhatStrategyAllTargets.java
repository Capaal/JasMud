package TargettingStrategies;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.List;

import processes.Skills;
import processes.Skills.Syntax;

public class WhatStrategyAllTargets implements WhatStrategyInterface {

	// Typically a Where Strategy determined the container(s) to search for holdables (given as containersToSearch).
	// This Strategy returns all matching Holdables.
	// The string inputed by the user follows SYNTAX defined by the SKILL (when created)
	// Said skill MUST have defined a Syntax.TARGET within the expected command. (what about Syntax.LIST?)
	// This strategy grabs the string located at said syntax's location, searches
	// given containers, and returns all holdables matching said string.
	@Override
	public List<Holdable> findWhat(Skills s, String fullCommand, Mobile currentPlayer, List<Container> containersToSearch) {
		List<Holdable> targ = new ArrayList<Holdable>();
		for (Container l : containersToSearch) {
			Holdable h = l.getHoldableFromString(s.getStringInfo(Syntax.TARGET, fullCommand));
			if (h != null) {
				targ.add(h);
			}
		}
		return targ;	
	}
	
	@Override
	public String toString() {
		return "ALLTARGETS";
	}

}
