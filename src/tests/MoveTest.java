package tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import interfaces.Mobile;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import processes.GameState;
import processes.Location;
import processes.LocationBuilder;
import processes.MobileBuilder;
import processes.Skills;
import processes.WorldServer;
import skills.Move;

public class MoveTest {
	
	Skills testSkill;
	Location locationOne;
	Location locationTwo;
	Mobile currentPlayer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		WorldServer.setGameState(new GameState());
		testSkill = new Move();
		LocationBuilder lb = new LocationBuilder();
	//	lb.setId(1);
		lb.complete();
		locationOne = lb.getFinishedLocation();
		lb = new LocationBuilder();
	//	lb.setId(2);
		lb.south(1, "north");
		lb.complete();
		locationTwo = lb.getFinishedLocation();
		MobileBuilder mb = new MobileBuilder();
		mb.setLocation(locationOne);
		mb.setName("currentplayer");
		mb.complete();
		currentPlayer = mb.getFinishedMob();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCanMoveNorthWhenValid() {
		testSkill.perform("move north", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationTwo));
	}
	
	@Test
	public void testInfiniteLoop() {
		LocationBuilder lb = new LocationBuilder();
	//	lb.setId(3);
		lb.south(2, "north");
		lb.north(1, "south");
		lb.complete();
		Location locationThree = lb.getFinishedLocation();
		testSkill.perform("move north", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationTwo));
		testSkill.perform("move north", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationThree));
		testSkill.perform("move north", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationOne));
		testSkill.perform("move north", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationTwo));
		testSkill.perform("move north", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationThree));
		testSkill.perform("move north", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationOne));	
		
		testSkill.perform("move south", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationThree));
		testSkill.perform("move south", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationTwo));
		testSkill.perform("move south", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationOne));
		testSkill.perform("move south", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationThree));	
	}
	
	@Test
	public void testMaze() {
		LocationBuilder lb = new LocationBuilder();
	//	lb.setId(3);
		lb.south(2, "east");
		lb.north(1, "southwest");
		lb.complete();
		Location locationThree = lb.getFinishedLocation();
		testSkill.perform("move north", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationTwo));
		testSkill.perform("move east", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationThree));
		testSkill.perform("move south", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationTwo));
		testSkill.perform("move east", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationThree));
		testSkill.perform("move north", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationOne));
		testSkill.perform("move southwest", currentPlayer);
		assertTrue(currentPlayer.getContainer().equals(locationThree));
	}	
}
