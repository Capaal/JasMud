package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import processes.*;
import processes.Location.Direction;

public class LocationTest {
	
	@Test
	public void testGetDirectionNameNorth() {
		Direction testDirection = Direction.getDirectionName("north");
		assertTrue("north should give Direction NORTH", testDirection.equals(Direction.NORTH));
		testDirection = Direction.getDirectionName("NORTH");
		assertTrue("NORTH should give Direction NORTH", testDirection.equals(Direction.NORTH));
		testDirection = Direction.getDirectionName("n");
		assertTrue("n should give Direction NORTH", testDirection.equals(Direction.NORTH));
		testDirection = Direction.getDirectionName("N");
		assertTrue("N should give Direction NORTH", testDirection.equals(Direction.NORTH));
	}
	@Test
	public void testGetDirectionNameNorthEast() {
		Direction testDirection = Direction.getDirectionName("northeast");
		assertTrue("northeast should give Direction NORTHEAST", testDirection.equals(Direction.NORTHEAST));
		testDirection = Direction.getDirectionName("NORTHEAST");
		assertTrue("NORTHEAST should give Direction NORTHEAST", testDirection.equals(Direction.NORTHEAST));
		testDirection = Direction.getDirectionName("ne");
		assertTrue("ne should give Direction NORTHEAST", testDirection.equals(Direction.NORTHEAST));
		testDirection = Direction.getDirectionName("NE");
		assertTrue("NE should give Direction NORTHEAST", testDirection.equals(Direction.NORTHEAST));
		testDirection = Direction.getDirectionName("Ne");
		assertTrue("Ne should give Direction NORTHEAST", testDirection.equals(Direction.NORTHEAST));
	}
	
	@Test
	public void testDirectionsFail() {
		Direction testDirection = Direction.getDirectionName("nort");
		assertTrue("nort should ALWAYS return NORTH", testDirection.equals(Direction.NORTH));
		testDirection = Direction.getDirectionName("northe");
		assertTrue("northe should ALWAYS return NORTHEAST", testDirection.equals(Direction.NORTHEAST));
		testDirection = Direction.getDirectionName("sou");
		assertTrue("sou should ALWAYS return SOUTH", testDirection.equals(Direction.SOUTH));
	}
	
	@Test
	public void testTwoLocationsMutualDirections() {
		WorldServer.setGameState(new GameState());
	//	WorldServer.setInterface(new StubDatabaseInterface());
		LocationBuilder buildSouth = new LocationBuilder();
		buildSouth.setId(1);
		buildSouth.complete();
		Location south = new Location(buildSouth);
		LocationBuilder buildNorth = new LocationBuilder();
		buildNorth.setId(2);
		buildNorth.south(1, "north");		
		buildNorth.complete();
		Location north = new Location(buildNorth);
		assertTrue("South location's north should point at location north" , south.getLocation(Direction.NORTH) == north);
		assertTrue("North location's south should point at location south", north.getLocation(Direction.SOUTH) == south);
	}
	
	@Test
	public void testTwoLocationsConnectInDifferentWays() {
		WorldServer.setGameState(new GameState());
//		WorldServer.setInterface(new StubDatabaseInterface());
		LocationBuilder buildSouth = new LocationBuilder();
		buildSouth.setId(1);
		buildSouth.complete();
		Location south = new Location(buildSouth);
		LocationBuilder buildNorth = new LocationBuilder();
		buildNorth.setId(2);
		buildNorth.south(1, "northwest");
		buildNorth.complete();
		Location north = new Location(buildNorth);
		assertTrue("South location's northwest should point at location north" , south.getLocation(Direction.NORTHWEST) == north);
		assertTrue("North location's south should point at location south", north.getLocation(Direction.SOUTH) == south);
	}
	
	@Test
	public void testTwoLocationsOnlyOneConnects() {
		WorldServer.setGameState(new GameState());
//		WorldServer.setInterface(new StubDatabaseInterface());
		LocationBuilder buildSouth = new LocationBuilder();
		buildSouth.setId(1);
		buildSouth.complete();
		Location south = new Location(buildSouth);
		LocationBuilder buildNorth = new LocationBuilder();
		buildNorth.setId(2);
		buildNorth.south(1, null);
		buildNorth.complete();
		Location north = new Location(buildNorth);
		assertFalse("South should not have any location pointing at north" , south.getLocation(Direction.NORTH) == north);
		assertTrue("North location's south should point at location south", north.getLocation(Direction.SOUTH) == south);
	}
}
