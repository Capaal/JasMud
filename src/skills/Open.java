package skills;

import java.util.Arrays;

import items.Door;
import processes.Skills;
import processes.Location.Direction;

public class Open extends Skills {
	
	private Door door;
	private Direction dir;
	
	public Open() {
		super.name = "open";
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.DIRECTION);
	}

	@Override
	protected void performSkill() {
		String dirString = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		dir = Direction.getDirectionName(dirString);
		if (dir == null) {
			messageSelf("There is not a direction that way, nor a door.");
			return;
		}
		door = currentPlayer.getContainer().getDoor(dir);
		if (door == null) {
			messageSelf("There is no door in that direction.");
			return;
		}
		if (door.isOpen()) {
			messageSelf("The door is already open.");
			return;
		}
		door.alterDoor(true);
		messageSelf("You open the door to your " + dir.toString().toLowerCase() + ".");
		messageOthers(currentPlayer.getName() + " opens the door to the " + dir.toString().toLowerCase() + ".", Arrays.asList(currentPlayer));
	}
}
