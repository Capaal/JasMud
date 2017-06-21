package skills;

import interfaces.Mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import processes.LocationConnection;
import processes.Skills;
import processes.Location.Direction;

public class MoveFear extends Move {
	
	public MoveFear(Mobile currentPlayer, String fullCommand) {
		super(currentPlayer, fullCommand);
		followers = null;
	}
	
	@Override protected boolean findDirection() {
		if (followers == null) {
			followers = ((Move)currentPlayer.getCommand("move")).followers;
		}
		startContainer = currentPlayer.getContainer();
		endContainer = null;
		Map<Direction, LocationConnection> availDir = currentPlayer.getContainer().getLocationMap();
		ArrayList<LocationConnection> locList = new ArrayList<LocationConnection>();
		locList.addAll(availDir.values());
		Random r = new Random();
		LocationConnection randomDir = locList.get(r.nextInt(locList.size()));		
		if (randomDir != null) {
			endContainer = randomDir.getLocation();		
		}
		if (endContainer == null) {
			currentPlayer.tell("You run around in circles, panicking but unable to find an exit.");
			return false;
		}
		return true;
	}
	
	@Override protected void displayLeaveMsg() {
		messageSelf("You flee away to the " + dir + ".");
		messageOthers(currentPlayer.getName() + " flees away to the " + dir + ".", Arrays.asList(currentPlayer)); //TODO need to specify direction
	}
	
	@Override protected void displayEnterMsg() {
		messageOthers(currentPlayer.getName() + " flees in from a random direction.", Arrays.asList(currentPlayer));
	}
	
	@Override protected boolean checkDoor() {
		directionEnum = startContainer.getDirectionToLocation(endContainer);
		dir = directionEnum.toString().toLowerCase();
		if (isDoorBlocking(startContainer, directionEnum)) {
			messageSelf("The door is closed before you.");
			return false;
		}
		return true;
	}
	
	@Override protected void moveFollowers() {
		for (Follow f : followers) {
			f.move("move " + dir);
		}
	}
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new MoveFear(currentPlayer, fullCommand);
	}
}

