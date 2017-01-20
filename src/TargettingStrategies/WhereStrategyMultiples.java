package TargettingStrategies;

import interfaces.Container;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.List;

import processes.Skill;

public class WhereStrategyMultiples implements WhereStrategyInterface {
	
	private final WhereStrategyInterface whereOne;
	private final WhereStrategyInterface whereTwo;

	public WhereStrategyMultiples(WhereStrategyInterface whereOne, WhereStrategyInterface whereTwo) {
		this.whereOne = whereOne;
		this.whereTwo = whereTwo;
	}
	
	@Override
	public List<Container> findWhere(Skill s, String fullCommand,
			Mobile currentPlayer) {
		List<Container> multipleStrategies = new ArrayList<Container>();
		multipleStrategies.addAll(whereOne.findWhere(s, fullCommand, currentPlayer));
		multipleStrategies.addAll(whereTwo.findWhere(s, fullCommand, currentPlayer));
		return multipleStrategies;
	}

}
