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
	}
	
	@Override protected boolean findDirection() {
		startContainer = currentPlayer.getContainer();
		endContainer = null;
		Map<Direction, LocationConnection> availDir = currentPlayer.getContainer().getLocationMap();
		ArrayList<LocationConnection> locList = new ArrayList<LocationConnection>();
		locList.addAll(availDir.values());
		Random r = new Random();
		LocationConnection randomDir = locList.get(r.nextInt(locList.size()));		
		if (randomDir != null) {
			endContainer = randomDir.getNotOneself(startContainer);		
		}
		if (endContainer == null) {
			currentPlayer.tell("You run around in circles, panicking but unable to find an exit.");
			return false;
		}
		dir = startContainer.getDirectionToLocation(endContainer).toString().toLowerCase();
		return true;
	}
	
	@Override protected void displayLeaveMsg() {
		messageSelf("You flee away to the " + dir + ".");
		messageOthers(currentPlayer.getNameColored() + " flees away to the " + dir + ".", Arrays.asList(currentPlayer)); //TODO need to specify direction
	}
	
	@Override protected void displayEnterMsg() {
		messageOthers(currentPlayer.getNameColored() + " flees in from a random direction.", Arrays.asList(currentPlayer));
	}
	
	@Override protected void moveFollowers() {
		currentPlayer.moveFollowers("move " + dir);
	}
	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new MoveFear(currentPlayer, fullCommand);
	}
}

