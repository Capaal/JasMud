package TargettingStrategies;

import interfaces.Container;
import interfaces.Mobile;

import java.util.ArrayList;
import java.util.List;

import processes.Location;
import processes.Skill;
import processes.Skill.Syntax;

public class TargetOneAwayProjectileWhereStrategy implements
		WhereTargettingStrategy {

	@Override
	public List<Container> findWhere(Skill s, String fullCommand,
			Mobile currentPlayer) {
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
		return "ONEAWAYPROJECTILE";
	}
}
