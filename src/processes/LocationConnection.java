package processes;

import java.util.ArrayList;

import effects.Blocking;
import interfaces.Mobile;
import items.Door;

// Represents a "tunnel" between two locations, allowing doors and blocking effects to stop progress between the two.
// Both Locations on each side of this "tunnel" should both share this same object, in their LocationMap represented in desired Direction
// that would connect to the other Location.
// TODO should isBlocking() include if door is closed?
public class LocationConnection {
	
	private final Location newLocation;
	private final Door doorSlot;
	private final Location oldLocation;
	private ArrayList<Blocking> blockingList;
	
	public LocationConnection(Location currentLoc, Door door, Location otherLoc) {
		this.doorSlot = door;
		this.oldLocation = otherLoc;
		this.newLocation = currentLoc;
		blockingList = new ArrayList<Blocking>();
	}
	
	public Location getNotOneself(Location oneSelf) {
		if (newLocation == oneSelf) {
			return oldLocation;
		}
		return newLocation;
	}
	
	public Door getDoor() {
		return doorSlot;
	}
	
	public void addBlocking(Blocking block) {
		blockingList.add(block);
	}
	
	public void removeBlocking(Blocking block) {
		blockingList.remove(block);
		for (Mobile m : newLocation.viewMobiles().values()) {
			m.tell("A " + block.getDescription() + " is no longer blocking your way.");
		}
		for (Mobile m : oldLocation.viewMobiles().values()) {
			m.tell("A " + block.getDescription() + " is no longer blocking your way.");
		}
	}

	public boolean isBlocked() {
		if (blockingList.isEmpty()) {
			return false; 
		}
		return true;
	}

	public ArrayList<Blocking> getBlocking() {
		return new ArrayList<Blocking>(blockingList);
	}
}
