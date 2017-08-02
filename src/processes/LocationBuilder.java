package processes;

import items.Door;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Quests.Quest;
import processes.Location.Direction;

/*
 * Builder class for safely creating a Location
 * Typically follows the following steps:
 *		LocationBuilder newLoc30 = new LocationBuilder();
		newLoc30.setName("Apartments north of Merchant Row");
		newLoc30.setDescription("Some exciting description.");
		newLoc30.addLocationConnection(Direction.SOUTH, 28);  Direction to another Location, that other Location's ID.
		newLoc30.complete();
   Declares the new Location complete, a Location will be generated, and connections between locations created.
 * Will over-write an Existing Location's connected locations if existing location's direction would connect to multiple locations.
 */
public class LocationBuilder {
	
	private int id;
	private String name;
	private String description;
	public Map<Direction, LocationConnection> locationMap;
	public Map<Location, LocationConnectionDataBox> locationConnections;	
	private Location finishedLocation;	
	private boolean buildComplete = false;
	private Quest bondedQuest = null;
	
	private ArrayList<LocationConnectionDataBox> locationBuildingBlocks;
	
	public LocationBuilder() {
		id = 0;
		this.name = "Default name";
		this.description = "default description.";
		this.locationMap = new HashMap<Direction, LocationConnection>();
		locationConnections = new HashMap<Location, LocationConnectionDataBox>();
		locationBuildingBlocks = new ArrayList<LocationConnectionDataBox>();
	}
	
	// Declares the Location Builder done building. Generates a new Location and finds a valid ID to assign.
	public boolean complete() {	
		handleId();		
		buildComplete = true;
		finishedLocation = new Location(this);
		return true;
	}
	
	private synchronized void handleId() {
		WorldServer.getGameState().maxLocationId ++;
		this.id = WorldServer.getGameState().maxLocationId;		
	}
	
	/** If an instance of LocationBuilder has been completed via "complete()" then returns the generated Location.
	* @Throws IllegalStateException if complete() has not been called
	*/
	public Location getFinishedLocation() {
		if (buildComplete) {
			return finishedLocation;
		} else {
			throw new IllegalStateException("Location is not complete.");
		}
	}
	
	public int getId() {return id;}	
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}	

	/**
	 * Creates a LocationConnection between this new location and the designated other Location in the given direction.
	 * Allows non-mirrored connections and creation of an unlocked Door.
	 * @param directionToOtherLocation Direction from this new Location pointing to the given Location.
	 * @param otherLocationId ID of the Location we want to connect to.
	 * @param otherLocationToHereDirection Direction that other Location should get modified to make a connection to this new Location.
	 * @param door Door if one is desired, or give Null.
	 */
	public void addLocationConnection(Direction directionToOtherLocation, int otherLocationId, Direction otherLocationToHereDirection, Door door) {			
		if (WorldServer.getGameState().checkForLocation(otherLocationId)) { // If otherLocation already exists, continue.	
			Location otherLocation = WorldServer.getGameState().viewLocations().get(otherLocationId);		
			locationBuildingBlocks.add(new LocationConnectionDataBox(directionToOtherLocation, otherLocation, otherLocationToHereDirection, door));
		}
	}
	
	/**
	 * Creates a Location Connection between two Locations (this new one and an existing one). Simplified version of addLocation Connection
	 * This version assumes connection is opposite directions (North from here connects to South in other Location). Assumes no Door.
	 * @param directionToOtherLocation Direction to connect this Location to the Given Location.
	 * @param otherLocationId ID of the Desired Location to connect to.
	 */
	public void addLocationConnection(Direction directionToOtherLocation, int otherLocationId) {
		addLocationConnection(directionToOtherLocation, otherLocationId, Direction.getDirectionName(directionToOtherLocation.getOpp()), null);
	}
	
	// Class representing the Data required to build a LocationConnection between two locations.
	// LocationConnection cannot be built until THIS location is completed and exists, so information is stored here until ready.
	public class LocationConnectionDataBox {		
		final Location other;
		final Direction toOther;
		final Door door;
		final Direction otherLocationToCurrentDirection;
		
		public LocationConnectionDataBox(Direction toOther, Location other, Direction otherToHere, Door door) {
			this.door = door;
			this.otherLocationToCurrentDirection = otherToHere;
			this.other = other;
			this.toOther = toOther;
		}
	}	
	
	public boolean isCompleted() {
		return buildComplete;
	}	
	
	public void setQuest(Quest quest) {bondedQuest = quest;}
	public Quest getQuest() {return bondedQuest;}

	public Map<Direction, LocationConnection> getlocationMap() {
		return new HashMap<Direction, LocationConnection>(locationMap);
	}	
	
	public ArrayList<LocationConnectionDataBox> getLocationBuildingBlocks() {
		return new ArrayList<LocationConnectionDataBox>(locationBuildingBlocks);
	}
}