package tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import interfaces.Holdable;
import interfaces.Mobile;
import items.ItemBuilder;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;



//import effects.Balance;
import processes.GameState;
import processes.Location;
import processes.Location.Direction;
import processes.LocationBuilder;
import processes.MobileBuilder;
import processes.Skills;
import processes.StdMob;
import processes.WorldServer;
import skills.Throw;

public class ThrowTest {
	
	Skills testSkill;
	Mobile currentPlayer;
	Mobile target;
	Location location;
	Holdable item;
	final int expectedDamage = 8;

	@Test
	public void testCannotThrowTwiceInstantly() {
		new Throw(currentPlayer, "throw dagger target").run();
		new Throw(currentPlayer, "throw dagger target").run();
		verify(target, Mockito.times(1)).takeDamage(expectedDamage);
	}	
	
	@Test
	public void testBasic() {
		new Throw(currentPlayer, "throw dagger target").run();
		verify(target, Mockito.times(1)).takeDamage(expectedDamage);
	}	
	
	@Test
	public void testCannotThrowBlockingTarget() {
//		fail("Not Implemented");
	}
	
	
	@Test
	public void testCannotThrowWhenOffBalance() {
//		currentPlayer.addPassiveCondition(new Balance(), 3000);
//		testSkill.perform("throw dagger target", currentPlayer);
//		verify(target, Mockito.times(0)).takeDamage(Type.SHARP,  expectedDamage);
	}
	
	
	@Test
	public void testBalanceLength() {
//		fail("Not Implemented.");
	}
	
	@Test
	public void testCannotThrowDeadMobiles() {
		when(target.isDead()).thenReturn(true);
		new Throw(currentPlayer, "throw dagger target").run();
		verify(target, Mockito.times(0)).takeDamage(expectedDamage);		
	}
	
	@Test
	public void testCannotThrowSelf() {
		new Throw(currentPlayer, "throw dagger currentPlayer").run();
		assertTrue("CurrentPlayer's hp should be at Max HP but is: " + currentPlayer.getCurrentHp(), currentPlayer.getCurrentHp() == currentPlayer.getMaxHp());
	}
	
	@Test
	public void testEnemyNOTPresent() {		
		LocationBuilder lb = new LocationBuilder();
	//	lb.setId(2);
		lb.complete();
		Location newLocation = lb.getFinishedLocation();
		newLocation.acceptItem(target);
		location.removeItemFromLocation(target);
		when(target.getContainer()).thenReturn(newLocation);
		new Throw(currentPlayer, "throw dagger target").run();
		verify(target, Mockito.times(0)).takeDamage(expectedDamage);		
	}
	
	@Test
	public void testEnemyPresent() {
		new Throw(currentPlayer, "throw dagger target").run();
		verify(target).takeDamage(expectedDamage);
	}
	
	@Test
	public void testThrowOneAway() {
		LocationBuilder lb = new LocationBuilder();
	//	lb.setId(2);
		lb.addLocationConnection(Direction.SOUTH,  1, Direction.NORTH, null);
	//	lb.south(1, "north");
		lb.complete();
		Location newLocation = lb.getFinishedLocation();
		newLocation.acceptItem(target);
		location.removeItemFromLocation(target);
		when(target.getContainer()).thenReturn(newLocation);
		new Throw(currentPlayer, "throw dagger target north").run();
		verify(target).takeDamage(expectedDamage);
	}
	
	@Test
	public void testThrowTargetIsHereButIncludeNorth() {
		LocationBuilder lb = new LocationBuilder();
//		lb.setId(2);
		lb.addLocationConnection(Direction.SOUTH,  1, Direction.NORTH, null);
		lb.complete();
		Location newLocation = lb.getFinishedLocation();
		new Throw(currentPlayer, "throw dagger target north").run();
		verify(target, Mockito.times(0)).takeDamage(expectedDamage);	
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		WorldServer.setGameState(new GameState());
		LocationBuilder lb = new LocationBuilder();
//		lb.setId(1);
		lb.complete();
		location = lb.getFinishedLocation();
		MobileBuilder mb = new MobileBuilder();
		mb.setLocation(location);
		mb.setName("currentplayer");
		mb.complete();
		currentPlayer = mb.getFinishedMob();
		ItemBuilder ib = new ItemBuilder();
		ib.setName("dagger");
		ib.setId(1);
		ib.setItemContainer(currentPlayer);
		ib.complete();
		item = ib.getFinishedItem();
		target = mock(StdMob.class);
		when(target.getName()).thenReturn("target");
		when(target.getId()).thenReturn(1);

		when(target.getContainer()).thenReturn(location);
		location.acceptItem(target);
		
	}

	@After
	public void tearDown() throws Exception {
	}	
}
