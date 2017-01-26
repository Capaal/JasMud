package TargettingStrategies;

import interfaces.Container;
import interfaces.Mobile;

import java.util.Arrays;
import java.util.List;

import processes.Skills;

public class WhereStrategyHere implements WhereStrategyInterface {
	
	// Utilizes the Mobile currentPlayer to obtain the CONTAINER the current player occupies.
	@Override
	public List<Container> findWhere(Skills s, String fullCommand, Mobile currentPlayer) {
		return Arrays.asList(currentPlayer.getContainer());	
	}
	
	@Override
	public String toString() {
		return "HERE";
	}

}
