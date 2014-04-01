package TargettingStrategies;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.List;

import processes.Skill;

public class TargetAllWhatStrategy implements WhatTargettingStrategy {

	@Override
	public List<Holdable> findWhat(Skill s, String fullCommand,	Mobile currentPlayer, List<Container> containersToSearch) {
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