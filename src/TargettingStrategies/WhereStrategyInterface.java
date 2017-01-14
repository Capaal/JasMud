package TargettingStrategies;

import interfaces.Container;
import interfaces.Mobile;
import java.util.List;

import processes.Skill;

// Interface to represent strategies for determining a CONTAINER (or multiple containers)
// Typically based on information from fullCommand and currentPlayer's current container.
public interface WhereStrategyInterface {
	
	public List<Container> findWhere(Skill s, String fullCommand, Mobile currentPlayer);
	
	public String toString();

}
