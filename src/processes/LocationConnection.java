package processes;

import items.Door;

public class LocationConnection {
	
	private final Location newLocation;
	private final Door doorSlot;
	private final Location oldLocation;
//	private ArrayList<Blocking> blockingList;
	
	public LocationConnection(Location currentLoc, Door door, Location otherLoc) {
		this.doorSlot = door;
		this.oldLocation = otherLoc;
		this.newLocation = currentLoc;
	}
	
	public Location getOldLocation() {
		return oldLocation;
	}
	
	public Location getNewLocation() {
		return newLocation;
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
}
