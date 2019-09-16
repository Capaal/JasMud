package skills;

import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import effects.Blocking;
import effects.PassiveCondition;
import interfaces.Mobile;
import processes.Location;
import processes.WorldServer;
import processes.Location.Direction;
import processes.Skills;

public class Icewall extends Skills {
	
	private Direction direction;
	private Location otherLocation;
	
	public Icewall(Mobile currentPlayer, String fullCommand) {
		super("icewall", "Create a wall of ice, blocking movement into and out of your location.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.DIRECTION);
	}

	@Override
	protected void performSkill() {
		currentPlayer.addPassiveCondition(PassiveCondition.BALANCE, 4000);
		messageSelf("You cast a wall of ice to the " + direction.name().toLowerCase() + ".");
		messageOthers(currentPlayer.getNameColored() + " casts a wall of ice to the " + direction.name().toLowerCase() + ".",
				Arrays.asList(currentPlayer));
		messageOthersAway(currentPlayer.getNameColored() + " casts a wall of ice to the " + (otherLocation.getDirectionToLocation(currentPlayer.getContainer()).name().toLowerCase()) + ".",
				Arrays.asList(currentPlayer), otherLocation);
		Blocking block = new Blocking("Icewall");
		Location currentLocation = currentPlayer.getContainer();
		currentLocation.addBlocking(direction, block);
		ScheduledExecutorService effectExecutor = WorldServer.getGameState().getEffectExecutor();
		effectExecutor.schedule(() -> currentLocation.removeBlocking(direction, block), 10000, TimeUnit.MILLISECONDS);	
	}

	@Override
	protected boolean preSkillChecks() {
		if (!hasBalance()) {return false;}
		if (!hasDirection()) {return false;}
		return true;
	}
	
	private boolean hasDirection() {
		String dir = Syntax.DIRECTION.getStringInfo(fullCommand, this);
		direction = Direction.getDirectionName(dir);
		otherLocation = currentPlayer.getContainer().getLocation(direction);
		if (direction == null) {
			messageSelf("Specify direction to create the icewall.");
			return false;
		}
		otherLocation = currentPlayer.getContainer().getLocation(direction);
		if (otherLocation == null) {
			messageSelf("There is not a direction there to block.");
			return false;
		}
		if (isDirectionBlocked(currentPlayer.getContainer(), Direction.getDirectionName(dir))) {
			messageSelf("A door is already blocking that direction.");
			return false;
		}
		
		return true;
	}
	
	@Override
	public String displaySyntax() {
		return "ICEWALL [DIRECTION]";
	}

}
