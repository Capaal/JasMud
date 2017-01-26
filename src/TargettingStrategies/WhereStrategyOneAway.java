package TargettingStrategies;

import interfaces.Container;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.List;

import processes.Location;
import processes.Skills;
import processes.Skills.Syntax;

public class WhereStrategyOneAway implements WhereStrategyInterface {

	// Obtains container ONE location away from currentPlayer
	// Assumes currentPlayer is in a LOCATION
	// Assumes Skill s has defined a Syntax.DIRECTION.
	@Override
	public List<Container> findWhere(Skills s, String fullCommand, Mobile currentPlayer) {
		List<Container> loc = new ArrayList<Container>();
		String dir = s.getStringInfo(Syntax.DIRECTION, fullCommand);
		if (!dir.equals("")) {
			Container mobLocation = currentPlayer.getContainer();
			if (mobLocation instanceof Location) {
				Location futureLocation = ((Location)mobLocation).getContainer(dir);
				loc.add(futureLocation);
			}
		}
		return loc;
	}
	@Override
	public String toString() {
		return "ONEAWAY";
	}
}
