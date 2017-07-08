package skills.Arcanist.Targetting;

import java.util.Arrays;
import java.util.List;

import processes.Location;
import processes.Location.Direction;
import processes.Skills.Syntax;
import skills.Arcanist.ArcanistSkill;

public class WhereTargettingBlockOneAway implements WhereTargettingBlock {

	@Override
	public List<Location> findWhere(ArcanistSkill skill) {
		Location futureLocation = null;
		String dir = Syntax.DIRECTION.getStringInfo(skill.getFullCommand(), skill);
		Location mobLocation = skill.getCurrentPlayer().getContainer();
		if (!dir.equals("")) {			
			futureLocation = mobLocation.getLocation(dir);
		} else {
			skill.messageCurrentPlayer("Which direction do you wish to cast this spell?");
			return null;
		}
		if (skill.isDirectionBlocked(mobLocation, Direction.getDirectionName(dir))) {
			skill.messageCurrentPlayer("There is a door blocking your way.");
			return null;
		}
		if (futureLocation == null) {
			skill.messageCurrentPlayer("There is no location that way to cast!");
			return null;
		}
		return Arrays.asList(futureLocation);
	}

	@Override
	public int determineCost() {
		return -10;
	}

	@Override
	public StringBuilder describeOneself(StringBuilder sb) {
		sb.append(System.lineSeparator());
		sb.append("Targetting: One Away.");
		sb.append(" Cost: ");
		sb.append(determineCost());
		return sb;	
	}

	@Override
	public Syntax requestSyntax() {
		return Syntax.DIRECTION;
	}

}
