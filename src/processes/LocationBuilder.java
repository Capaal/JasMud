package processes;

import interfaces.Mobile;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import Quests.Quest;
import processes.Location.Direction;
import processes.Location.GroundType;

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
	private GroundType groundType;
	public Map<Direction, Location> locationMap;
	public Map<Integer, Direction> locationConnections;	
	private Location finishedLocation;	
	private boolean buildComplete = false;
	private Quest bondedQuest = null;
	private static int maxId;
	
	public LocationBuilder() {
		id = -1;
		this.name = "Default name";
		this.description = "default description.";
		this.groundType = GroundType.GROUND;
		this.locationMap = new HashMap<Direction, Location>();
		locationConnections = new HashMap<Integer, Direction>();
	}
	
	// Declares the Location Builder done building. Generates a new Location and finds a valid ID to assign.
	public boolean complete() {
		// Should check that ID is ok, if assigned manually.
		
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
	
	// Dangerous, remove? Another Location might be assigned that ID, or add a check during Complete()
//	public void setId(int id) {
//		this.id = id;
//	}
	
	// UNUSED currently, but needs to search for valid ID. TODO
	private void setId() throws IllegalStateException {
	/*	String sqlQuery = "SELECT sequencetable.sequenceid FROM sequencetable"
				+ " LEFT JOIN locationstats ON sequencetable.sequenceid = locationstats.locid"
				+ " WHERE locationstats.locid IS NULL";		
		Object availableId = WorldServer.databaseInterface.viewData(sqlQuery, "sequenceid");
		if (availableId == null || !(availableId instanceof Integer)) {
			WorldServer.databaseInterface.increaseSequencer();
			availableId = WorldServer.databaseInterface.viewData(sqlQuery, "sequenceid");
			if (availableId == null || !(availableId instanceof Integer)) {
				throw new IllegalStateException("The location could not determine a valid id, it is invalid.");				
			} else {
				id = (int)availableId;
			}
		} else {
			id = (int)availableId;
		}		*/
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public GroundType getGroundType() {
		return groundType;
	}

	// See Location for GroundType Enums.
	public void setGroundType(GroundType groundType) {
		this.groundType = groundType;
	}	
	
	public void north(int futureId, String connectionDirection) {buildDirections(id, Direction.NORTH, futureId, connectionDirection);}			
	public void northEast(int futureId, String connectionDirection) {buildDirections(id, Direction.NORTHEAST, futureId, connectionDirection);}			
	public void east(int futureId, String connectionDirection) {buildDirections(id, Direction.EAST, futureId, connectionDirection);}				
	public void southEast(int futureId, String connectionDirection) {buildDirections(id, Direction.SOUTHEAST, futureId, connectionDirection);}		
	public void south(int futureId, String connectionDirection) {buildDirections(id, Direction.SOUTH, futureId, connectionDirection);}			
	public void southWest(int futureId, String connectionDirection) {buildDirections(id, Direction.SOUTHWEST, futureId, connectionDirection);}			
	public void west(int futureId, String connectionDirection) {buildDirections(id, Direction.WEST, futureId, connectionDirection);}			
	public void northWest(int futureId, String connectionDirection) {buildDirections(id, Direction.NORTHWEST, futureId, connectionDirection);}			
	public void up(int futureId, String connectionDirection) {buildDirections(id, Direction.UP, futureId, connectionDirection);}			
	public void down(int futureId, String connectionDirection) {buildDirections(id, Direction.DOWN, futureId, connectionDirection);}		
	public void in(int futureId, String connectionDirection) {buildDirections(id, Direction.IN, futureId, connectionDirection);}			
	public void out(int futureId, String connectionDirection) {buildDirections(id, Direction.OUT, futureId, connectionDirection);}			
	
	
	private void buildDirections(int newLocationId, Direction directionConnectingLocationWillBe, int idOfConnectingLocation, String directionConnectingLocationWillConnectToNewLocation) {
		if (newLocationId != 0) {	
			Direction futureDirection = Direction.NORTH;			
			if (WorldServer.gameState.checkForLocation(idOfConnectingLocation)) {
				Location futureLoc = WorldServer.gameState.viewLocations().get(idOfConnectingLocation);	
				if (directionConnectingLocationWillConnectToNewLocation != null) {
					try {
						futureDirection = Direction.valueOf(directionConnectingLocationWillConnectToNewLocation.toUpperCase());
					} catch (IllegalArgumentException e) {
						System.out.println(directionConnectingLocationWillConnectToNewLocation + " is not a valid direction loaded from database. CRITICAL ERROR, Defaulted to north.");				
					}
					locationConnections.put(idOfConnectingLocation, futureDirection);		
				}
				locationMap.put(directionConnectingLocationWillBe, futureLoc);		
			} else {
				System.out.println("I think a location was made that is pointing to an unmade location: " + newLocationId);
			}
		}
	}	
	
	public boolean isCompleted() {
		return buildComplete;
	}
	
/*
	public static boolean newLocation(Mobile player, LocationBuilder builderLocation) {
		String nextTask = Godcreate.askQuestion("What next? 1:name 2:description 3:groundtype 4:connections 5:preview 6:complete 7:exit", player).toLowerCase();
		switch(nextTask) {
			case "1":
			case "name":
				builderLocation.setName(Godcreate.askQuestionMaintainCase("What is this location's name?", player));
				return newLocation(player, builderLocation);
			case "2":
			case "description":
				builderLocation.setDescription(Godcreate.askQuestionMaintainCase("What is the location's description?", player));
				return newLocation(player, builderLocation);
			case "3":
			case "groundtype":
				builderLocation.setGroundType(GroundType.valueOf(Godcreate.askQuestion("Location's groundtype?", player).toUpperCase()));
				return newLocation(player, builderLocation);
			case "4":
			case "connections":
				return newLocation(player, newConnection(player, builderLocation));
			case "5":
			case "preview":
				player.tell("Name: "+ builderLocation.name);
				player.tell("Id: "+ builderLocation.id);
				player.tell("Description: "+ builderLocation.description);
				player.tell("Groundtype: "+ builderLocation.groundType.toString());
				StringBuilder sb;
				player.tell("Connections:");
				for (Direction dir : builderLocation.locationMap.keySet()) {
					sb = new StringBuilder();
					sb.append("To the ");
					sb.append(dir.toString());
					sb.append(" is ");
					sb.append(builderLocation.locationMap.get(dir).getId());
					player.tell(sb.toString());
				}
				return newLocation(player, builderLocation);
			case "6":
			case "complete":
				if (builderLocation.complete()) {
					builderLocation.finishedLocation.save();		
					return true;
				} else {
					player.tell("Location failed to create, most likely a critical error determining unique ID.");
					return false;
				}
			case "7":
			case "exit":
			case "quit":
				return false;
			default:
				player.tell("That was not a valid option");
				return newLocation(player, builderLocation);
		}
	}
	
	private static LocationBuilder newConnection(Mobile player, LocationBuilder builderLocation) {
		String nextTask = Godcreate.askQuestion("What next? 1:connection 2:done", player);
		switch(nextTask) {
		case "1":
		case "connection":
			Direction newConnectionDirection = null;
			Direction newConnectionReverseDirection = null;
			try {
				newConnectionDirection = Direction.valueOf(Godcreate.askQuestion("Which direction is the connected location?", player).toUpperCase());
				String newStringConnectionReverseDirection = Godcreate.askQuestion("Which direction is the connected location connecting to this location?", player).toUpperCase();
				if (!newStringConnectionReverseDirection.equalsIgnoreCase("null")) {
					newConnectionReverseDirection = Direction.valueOf(newStringConnectionReverseDirection);
				}
			} catch(IllegalArgumentException e) {
				player.tell("That direction wasn't valid.");
				return newConnection(player, builderLocation);
			}
			int newConnectionId = 0;
			try {
				newConnectionId = Integer.parseInt(Godcreate.askQuestion("The connected locations id?", player));
			} catch(NumberFormatException e) {
				player.tell("That isn't a valid number.");
				return newConnection(player, builderLocation);
			}
			Location connectedLocation = WorldServer.gameState.viewLocations().get(newConnectionId);
			if (newConnectionDirection == null || newConnectionId == 0 || connectedLocation == null) {
				player.tell("Invalid state");
				return newConnection(player, builderLocation);
			}
			builderLocation.locationConnections.put(newConnectionId, newConnectionReverseDirection);				
			builderLocation.locationMap.put(newConnectionDirection, connectedLocation);	
			return newConnection(player, builderLocation);
		case "2":
		case "quit":
		case "exit":
		case "done":
			return builderLocation;
		default:
			player.tell("That is not a valid option.");
			return newConnection(player, builderLocation);
		}
	}*/
	
	public void setQuest(Quest quest) {
		bondedQuest = quest;
	}

	public Quest getQuest() {
		return bondedQuest;
	}

	public Map<Direction, Location> getlocationMap() {
		return new HashMap<Direction, Location>(locationMap);
	}

	
}