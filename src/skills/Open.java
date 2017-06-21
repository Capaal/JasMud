package skills;

import java.util.Arrays;

import interfaces.Mobile;
import items.Door;
import processes.Skills;
import processes.Location.Direction;

public class Open extends Skills {
	
	private Door door;
	private Direction dir;
	private String dirString;
	
	public Open(Mobile currentPlayer, String fullCommand) {
		super("open", "Opening doors.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.DIRECTION);
	}

	@Override
	protected void performSkill() {
		dirString = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		if (preSkillChecks()) {
			door.alterDoor(true);
			messageSelf("You open the door to your " + dir.toString().toLowerCase() + ".");
			messageOthers(currentPlayer.getName() + " opens the door to the " + dir.toString().toLowerCase() + ".", Arrays.asList(currentPlayer));
		}
	}

	@Override
	protected boolean preSkillChecks() {
		dir = Direction.getDirectionName(dirString);
		if (dir == null) {
			messageSelf("There is not a direction that way, nor a door.");
			return false;
		}
		door = currentPlayer.getContainer().getDoor(dir);
		if (door == null) {
			messageSelf("There is no door in that direction.");
			return false;
		}
		if (door.isOpen()) {
			messageSelf("The door is already open.");
			return false;
		}
		return true;
	}
	
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new Open(currentPlayer, fullCommand);
	}
}
