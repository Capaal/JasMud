package TargettingStrategies;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import java.util.Arrays;
import java.util.List;
import processes.Skill;

public class WhatStrategySelf implements WhatStrategyInterface {

	// Returns a SINGLE holdable, which is the currentPlayer.
	@Override
	public List<Holdable> findWhat(Skill s, String fullCommand, Mobile currentPlayer, List<Container> containersToSearch) {
		return Arrays.asList((Holdable)currentPlayer);
	}
	
	@Override
	public String toString() {
		return "SELF";
	}

}
