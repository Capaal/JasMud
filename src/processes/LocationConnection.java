package processes;

import java.util.ArrayList;

import effects.Blocking;
import interfaces.Mobile;
import items.Door;

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
		for (Mobile m : newLocation.getMobiles().values()) {
			m.tell("A " + block.getDescription() + " is no longer blocking your way.");
		}
		for (Mobile m : oldLocation.getMobiles().values()) {
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
