package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import processes.Location;
import processes.Location.Direction;
import processes.LocationBuilder;

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
	
	

}
