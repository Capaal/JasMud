package skills;

import java.util.Arrays;

import items.Door;
import processes.Skills;
import processes.Location.Direction;

public class Close extends Skills {
	
	private Door door;
	private Direction dir;
	
	public Close() {
		super.name = "close";
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
		door.alterDoor(false);
		messageSelf("You close the door to your " + dir.toString().toLowerCase() + ".");
		messageOthers(currentPlayer.getName() + " closes the door to the " + dir.toString().toLowerCase() + ".", Arrays.asList(currentPlayer));
	}
}
