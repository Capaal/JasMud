package TargettingStrategies;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.List;

import processes.Skill;

public class TargetOthersWhatStrategy implements WhatTargettingStrategy {

	@Override
	public List<Holdable> findWhat(Skill s, String fullCommand, Mobile currentPlayer, List<Container> containersToSearch) {
		List<Holdable> targs = new ArrayList<Holdable>();
		List<Holdable> targets = new TargetTargetWhatStrategy().findWhat(s, fullCommand, currentPlayer, containersToSearch);
		Holdable self = null;
		List<Holdable> selfs = new TargetSelfWhatStrategy().findWhat(s, fullCommand, currentPlayer, containersToSearch);
		Holdable target = null;
		if (!targets.isEmpty() && !selfs.isEmpty()) {
			target = targets.get(0);
			self = selfs.get(0);
		} else {
			return targs;
		}			
		for (Container l : containersToSearch) {
			for (Holdable h : l.getInventory()) {
				if (h != target && h != self) {
					targs.add((Mobile)h);
				}
			}
			return targs;
		}
		return targs;	
	}
	@Override
	public String toString() {
		return "OTHERS";
	}
}
