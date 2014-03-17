package TargettingStrategies;

import interfaces.Container;
import interfaces.Mobile;
import java.util.Arrays;
import java.util.List;

import processes.Skill;

public class TargetHereWhereStrategy implements TargettingStrategyWhere {
	
	@Override
	public List<Container> findWhere(Skill s, String fullCommand, Mobile currentPlayer) {
		return Arrays.asList(currentPlayer.getContainer());	
	}

}
