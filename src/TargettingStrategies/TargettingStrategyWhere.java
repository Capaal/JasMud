package TargettingStrategies;

import interfaces.Container;
import interfaces.Mobile;
import java.util.List;

import processes.Skill;

public interface TargettingStrategyWhere {
	
	public List<Container> findWhere(Skill s, String fullCommand, Mobile currentPlayer);

}
