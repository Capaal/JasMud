package TargettingStrategies;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import java.util.List;
import processes.Skill;

public interface WhatStrategyInterface {
	
		// Typically a Where Strategy determined the container(s) to search for a holdable (given as containersToSearch).
		// The string inputed by the user follows SYNTAX defined by the SKILL (when created)
		// Not all strategies rely on SYNTAX, but most do (SYNTAX is found in Skill)
	public List<Holdable> findWhat(Skill s, String fullCommand, Mobile currentPlayer, List<Container> containersToSearch);
	
	public String toString(); // Should represent WHAT the strategy should find.
}
