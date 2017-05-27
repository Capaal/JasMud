package processes;

import items.Door;

public class LocationConnection {
	
	private Location otherLocation;
	private Door doorSlot;
	
	public LocationConnection(Door door, Location loc) {
		this.doorSlot = door;
		this.otherLocation = loc;
	}
	
	public Location getLocation() {
		return otherLocation;
	}
	
	public Door getDoor() {
		return doorSlot;
	}
	
	public void setDoor(Door newDoor) {
		this.doorSlot = newDoor;
	}
	
}
