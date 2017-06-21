package processes;

import items.Door;
import java.util.HashMap;
import java.util.Map;
import Quests.Quest;
import processes.Location.Direction;

/*
 * Builder class for safely creating a Location
 * Typically follows the following steps:
 * LocationBuilder newLocation = new LocationBuilder();   Readies a new builder and sets variables to generics
 * newLocation.setID(int id);  Assigns ID for this new location.
 * newLocation.setName(String name);  Sets a brief name.
 * newLocation.setDescription(String description);  Longer description of location
 * newLocation.setGroundType(Groundtype groundtype);  See Location for Groundtype ENUMS
 * newLocation.north(int futureId, String connectionDirection)  EXAMPLE: newLocation.north(12, "south");
 * The Above should call the direction FROM THIS LOCATION to another EXISTING LOCATION (so NORTH of here is otherID and connects to here via it's south)	
 * Additional calls to south, east, in, up etc would follow for other EXISTING locations.
 * newLocation.complete();   Declares the new Location complete, a Location will be generated, and connections between locations created.
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
	private static int maxId;
	
	public LocationBuilder() {
		id = -1;
		this.name = "Default name";
		this.description = "default description.";
		this.locationMap = new HashMap<Direction, LocationConnection>();
		locationConnections = new HashMap<Location, LocationConnectionDataBox>();
	}
	
	// Declares the Location Builder done building. Generates a new Location and finds a valid ID to assign.
	public boolean complete() {	
		handleId();		
		buildComplete = true;
		finishedLocation = new Location(this);
		return true;
	}
	
	private synchronized void handleId() {
		maxId ++;
		this.id = maxId;		
	}
	
	// If an instance of LocationBuilder has been completed via "complete()" then returns the generated Location.
	// @Throws IllegalStateException
	public Location getFinishedLocation() {
		if (buildComplete) {
			return finishedLocation;
		} else {
			throw new IllegalStateException("Location is not complete.");
		}
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}	

	public void addLocationConnection(Direction directionToOtherLocation, int otherLocationId, Direction otherLocationToHereDirection, Door door) {		
		if (WorldServer.gameState.checkForLocation(otherLocationId)) { // If otherLocation already exists, continue.
			Location otherLocation = WorldServer.gameState.viewLocations().get(otherLocationId);	
			if (directionToOtherLocation != null) { // if two-ways					
				locationConnections.put(otherLocation, new LocationConnectionDataBox(door,otherLocationToHereDirection)); // Set up for the Other existing location to connect here.
			}
			locationMap.put(directionToOtherLocation, new LocationConnection(door, otherLocation));	// Set-up for this location to connect to other location.
		} else {
			throw new IllegalStateException("Other Location does not exist.");
		}
	}
	
	//overload of above method
	public void addLocationConnection(Direction directionToOtherLocation, int otherLocationId) {
		addLocationConnection(directionToOtherLocation, otherLocationId, Direction.getDirectionName(directionToOtherLocation.getOpp()), null);
	}
	
	public class LocationConnectionDataBox {
		
		final Door door;
		final Direction otherLocationToCurrentDirection;
		
		public LocationConnectionDataBox(Door door, Direction dir) {
			this.door = door;
			this.otherLocationToCurrentDirection = dir;
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
}