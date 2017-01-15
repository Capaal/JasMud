package TargettingStrategies;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.List;

import processes.Skill;

public class WhatStrategyAllMobiles implements WhatStrategyInterface {

	// Returns all Mobiles in given container(s).
	@Override
	public List<Holdable> findWhat(Skill s, String fullCommand,	Mobile currentPlayer, List<Container> containersToSearch) {
		List<Holdable> targ = new ArrayList<Holdable>();
		for (Container l : containersToSearch) {
			for (Holdable m : l.getInventory()) {
				if (m instanceof Mobile) {
					targ.add(m);	
				}						
			}
		}
		return targ;
	}
	@Override
	public String toString() {
		return "ALLMOBILES";
	}
}
