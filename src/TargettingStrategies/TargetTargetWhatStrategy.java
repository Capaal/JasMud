package TargettingStrategies;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.List;

import processes.Skill;
import processes.Skill.Syntax;

public class TargetTargetWhatStrategy implements WhatTargettingStrategy {


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
