package processes;

import interfaces.Mobile;

import java.util.HashMap;
import java.util.Map;

import actions.Godcreate;
import processes.Location.Direction;
import processes.Location.GroundType;

public class LocationBuilder {
	private int id;
	private String name;
	private String description;
	private GroundType groundType;
	public Map<Direction, Location> locationMap;
	public Map<Integer, Direction> locationConnections;
	
	public LocationBuilder() {
		id = -1;
		this.name = "Default name";
		this.description = "default description.";
		this.groundType = GroundType.GROUND;
		this.locationMap = new HashMap<Direction, Location>();
		locationConnections = new HashMap<Integer, Direction>();
	}
	
	public int getId() {
		return id;
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

	public void setGroundType(GroundType groundType) {
		this.groundType = groundType;
	}	
	
	public void north(int futureId, String connectionDirection) {
		buildDirections(id, Direction.NORTH, futureId, connectionDirection);
	}			
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
		// SQL will call for a direction even if there is no location here, so just ends.
		if (newLocationId != 0) {	
			Direction futureDirection = Direction.NORTH;			
			if (WorldServer.gameState.locationCollection.containsKey(idOfConnectingLocation)) {
				Location futureLoc = WorldServer.gameState.locationCollection.get(idOfConnectingLocation);	
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
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	public void complete() {
		new Location(this);
	}

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
				builderLocation.complete();
				return true;
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
			Location connectedLocation = WorldServer.gameState.locationCollection.get(newConnectionId);
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
	}
}