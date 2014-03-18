package TargettingStrategies;

import interfaces.Container;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.List;

import processes.Skill;

public class TargetInventoryWhereStrategy implements WhereTargettingStrategy {

	@Override
	public List<Container> findWhere(Skill s, String fullCommand,
			Mobile currentPlayer) {
		List<Container> loc = new ArrayList<Container>();				
		loc.add(currentPlayer);
		return loc;
	}
	@Override
	public String toString() {
		return "INVENTORY";
	}
}
