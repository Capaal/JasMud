package skills.Arcanist.Targetting;

import java.util.ArrayList;
import java.util.List;

import processes.Location;
import processes.Location.Direction;
import processes.Skills.Syntax;
import skills.Arcanist.ArcanistSkill;

public class WhereTargettingBlockProjectile implements WhereTargettingBlock {

	@Override
	public List<Location> findWhere(ArcanistSkill skill) {
		List<Location> allLocations = new ArrayList<Location>();
		Location currentPlayerLocation = (skill.getCurrentPlayer().getContainer());			
		String dir = Syntax.DIRECTION.getStringInfo(skill.getFullCommand(), skill);
		if (!dir.equals("")) {
			Location nextLocation = currentPlayerLocation.getLocation(dir);
			if (skill.isDirectionBlocked(currentPlayerLocation, Direction.getDirectionName(dir))) {
				skill.messageCurrentPlayer("There is a door blocking your way.");
				return null;
			}
			if (nextLocation == null) {
				skill.messageCurrentPlayer("There isn't a location that way.");
				return null;
			} else {
				allLocations.add(nextLocation);
				getNextLocation(dir, nextLocation, allLocations, skill);
			}
		} else {
			skill.messageCurrentPlayer("Which direction do you wish to cast this spell?");
			return null;
		}
		return allLocations;
	}
	
	private void getNextLocation(String dir, Location currentLocation, List<Location> allLocations, ArcanistSkill skill) {
		Location anotherLocation = currentLocation.getLocation(dir);
		if (!skill.isDirectionBlocked(currentLocation, Direction.getDirectionName(dir))) {
			if (anotherLocation != null && !allLocations.contains(anotherLocation)) {
				allLocations.add(anotherLocation);
				getNextLocation(dir, anotherLocation, allLocations, skill);
			}
		}		
	}
	

	@Override
	public int determineCost() {
		return -20;
	}

	@Override
	public StringBuilder describeOneself(StringBuilder sb) {
		sb.append(System.lineSeparator());
		sb.append("Targetting: In a straight line. Cost: ");
		sb.append(determineCost());
		return sb;
	}

	@Override
	public Syntax requestSyntax() {
		return Syntax.DIRECTION;
	}

}
