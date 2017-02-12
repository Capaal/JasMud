package tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import interfaces.Container;
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

import effects.Balance;
import processes.GameState;
import processes.Location;
import processes.LocationBuilder;
import processes.MobileBuilder;
import processes.Skills;
import processes.StdMob;
import processes.Type;
import processes.WorldServer;
import skills.Shoot;

public class ThrowTest {
	
	Skills testSkill;
	Mobile currentPlayer;
	Mobile target;
	Location location;
	Holdable item;
	final int expectedDamage = 8;

	@Test
	public void testCannotThrowTwiceInstantly() {
		testSkill.perform("throw dagger target", currentPlayer);
		testSkill.perform("throw dagger target", currentPlayer);
		verify(target, Mockito.times(1)).takeDamage(Type.SHARP,  expectedDamage);
	}	
	
	@Test
	public void testCannotThrowBlockingTarget() {
//		fail("Not Implemented");
	}
	
	
	@Test
	public void testCannotThrowWhenOffBalance() {
		currentPlayer.addEffect(new Balance(), 3000);
		testSkill.perform("throw dagger target", currentPlayer);
		verify(target, Mockito.times(0)).takeDamage(Type.SHARP,  expectedDamage);
	}
	
	
	@Test
	public void testBalanceLength() {
//		fail("Not Implemented.");
	}
	
	@Test
	public void testCannotThrowDeadMobiles() {
		when(target.isDead()).thenReturn(true);
		testSkill.perform("throw dagger target", currentPlayer);
		verify(target, Mockito.times(0)).takeDamage(Type.SHARP,  expectedDamage);		
	}
	
	@Test
	public void testCannotThrowSelf() {
		testSkill.perform("throw dagger currentplayer", currentPlayer);
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
		testSkill.perform("throw dagger target", currentPlayer);
		verify(target, Mockito.times(0)).takeDamage(Type.SHARP,  expectedDamage);		
	}
	
	@Test
	public void testEnemyPresent() {
		testSkill.perform("throw dagger target", currentPlayer);
		verify(target).takeDamage(Type.SHARP,  expectedDamage);
	}
	
	@Test
	public void testThrowOneAway() {
		LocationBuilder lb = new LocationBuilder();
	//	lb.setId(2);
		lb.south(1, "north");
		lb.complete();
		Location newLocation = lb.getFinishedLocation();
		newLocation.acceptItem(target);
		location.removeItemFromLocation(target);
		when(target.getContainer()).thenReturn(newLocation);
		testSkill.perform("throw dagger target north", currentPlayer);
		verify(target).takeDamage(Type.SHARP, expectedDamage);
	}
	
	@Test
	public void testThrowTargetIsHereButIncludeNorth() {
		LocationBuilder lb = new LocationBuilder();
//		lb.setId(2);
		lb.south(1, "north");
		lb.complete();
		Location newLocation = lb.getFinishedLocation();
		testSkill.perform("throw dagger target north", currentPlayer);
		verify(target, Mockito.times(0)).takeDamage(Type.SHARP,  expectedDamage);	
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
		testSkill = new Shoot();
		LocationBuilder lb = new LocationBuilder();
	//	lb.setId(1);
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
		location.acceptItem(target);
		when(target.getName()).thenReturn("target");
		when(target.getContainer()).thenReturn(location);
		testSkill = new Shoot();
	}

	@After
	public void tearDown() throws Exception {
	}	
}
