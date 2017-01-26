package TargettingStrategies;

import interfaces.Container;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.List;

import processes.Skills;

public class WhereStrategySelfInventory implements WhereStrategyInterface {

	// Returns currentPlayer, which is a container (and adding to it adds to the player's inventory).
	@Override
	public List<Container> findWhere(Skills s, String fullCommand,
			Mobile currentPlayer) {
		List<Container> loc = new ArrayList<Container>();				
		loc.add(currentPlayer);
		return loc;
	}
	@Override
	public String toString() {
		return "SELFINVENTORY";
	}
}
