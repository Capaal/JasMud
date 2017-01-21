package TargettingStrategies;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.List;

import processes.Skill;

public class WhatStrategyNotSelf implements WhatStrategyInterface {
	
	// Returns any MOBILES in given containersToSearch that are NOT the currentPlayer (for no-target cases)
	// Used primarily in MESSAGE for 3rd parties.
	// WhatStrategyOtherMobiles excludes SELF and TARGET
	@Override
	public List<Holdable> findWhat(Skill s, String fullCommand, Mobile currentPlayer, List<Container> containersToSearch) {
		List<Holdable> others = new ArrayList<Holdable>(); // List to add desired Mobiles to.
		// Obtains targets used in skill (determined by Syntax.TARGET within the Skill s)		
		for (Container l : containersToSearch) {
			for (Holdable h : l.getInventory()) {
				if (h instanceof Mobile && h != currentPlayer) {
					others.add(h);					
				}
			}
		}
		return others;	
	}
	
	@Override
	public String toString() {
		return "OTHERMOBILES";
	}
}
