package TargettingStrategies;

import interfaces.Container;
import interfaces.Holdable;
import interfaces.Mobile;
import java.util.List;
import processes.Skill;

public interface TargettingStrategyWhat {
	
	public List<Holdable> findWhat(Skill s, String fullCommand, Mobile currentPlayer, List<Container> containersToSearch);

}
