package TargettingStrategies;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.List;

import processes.Skills;

public class WhatStrategyAll implements WhatStrategyInterface {

	// Returns all Holdables in given container(s).
	@Override
	public List<Holdable> findWhat(Skills s, String fullCommand,	Mobile currentPlayer, List<Container> containersToSearch) {
		List<Holdable> targ = new ArrayList<Holdable>();
		for (Container l : containersToSearch) {
			for (Holdable m : l.getInventory()) {
				targ.add(m);			
			}
		}
		return targ;
	}
	@Override
	public String toString() {
		return "ALL";
	}
}
