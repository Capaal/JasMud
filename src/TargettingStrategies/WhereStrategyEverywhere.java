package TargettingStrategies;

import interfaces.Container;
import interfaces.Mobile;
import java.util.ArrayList;
import java.util.List;

import processes.Skills;
import processes.WorldServer;

public class WhereStrategyEverywhere implements WhereStrategyInterface {
	
	@Override
	public List<Container> findWhere(Skills s, String fullCommand, Mobile currentPlayer) {
		ArrayList<Container> everywhere = new ArrayList<Container>();
		everywhere.addAll(WorldServer.gameState.viewLocations().values());
		return everywhere;	
	}
	
	@Override
	public String toString() {
		return "EVERYWHERE";
	}

}
