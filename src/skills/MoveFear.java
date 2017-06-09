package skills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import processes.Location;
import processes.LocationConnection;
import processes.Location.Direction;

public class MoveFear extends Move {
	
	@Override protected boolean findDirection() {
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
	
	@Override protected void displayMessages() {
		messageSelf("You flee away to the " + dir + ".");
		messageOthers(currentPlayer.getName() + " flees away to the " + dir + ".", Arrays.asList(currentPlayer)); //TODO need to specify direction
		currentPlayer.moveHoldable(endContainer);
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
	
	@Override
	public void removeFollower(Follow follow) {
		// Blank because we don't want to remove our followers.
	}
}

