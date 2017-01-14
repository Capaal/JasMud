package TargettingStrategies;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.List;

import processes.Skill;

public class WhatStrategyOtherMobiles implements WhatStrategyInterface {
	
	// Returns any MOBILES in given containersToSearch that are NOT the currentPlayer NOR a TARGET
	// Used primarily in MESSAGE for 3rd parties.
	// See WhatStrategyNotSelf for all others NOT currentPlayer.
	@Override
	public List<Holdable> findWhat(Skill s, String fullCommand, Mobile currentPlayer, List<Container> containersToSearch) {
		List<Holdable> others = new ArrayList<Holdable>(); // List to add desired Mobiles to.
		// Obtains targets used in skill (determined by Syntax.TARGET within the Skill s)
		List<Holdable> targets = new WhatStrategyTarget().findWhat(s, fullCommand, currentPlayer, containersToSearch);		
		for (Container l : containersToSearch) {
			for (Holdable h : l.getInventory()) {
				if (h instanceof Mobile && h != currentPlayer) {
					for (Holdable t : targets) {
						if (t instanceof Mobile && h != t)
							others.add(h);
					}					
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
