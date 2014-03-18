package TargettingStrategies;

import interfaces.Container;
import interfaces.Mobile;
import java.util.List;

import processes.Skill;

public interface WhereTargettingStrategy {
	
	public List<Container> findWhere(Skill s, String fullCommand, Mobile currentPlayer);
	
	public String toString();

}
