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
import skills.Punch;

public class PunchTest {
	
	Skills testSkill;
	Mobile currentPlayer;
	Mobile target;
	Location location;
	final int expectedDamage = 10;

	@Test
	public void testCannotPunchTwiceInstantly() {
		testSkill.perform("punch testtarget", currentPlayer);
		testSkill.perform("punch testtarget", currentPlayer);
		verify(target, Mockito.times(1)).takeDamage(Type.BLUNT,  expectedDamage);
	}	
	
	@Test
	public void testCannotPunchBlockingTarget() {
//		fail("Not Implemented");
	}
	
	
	@Test
	public void testCannotPunchWhenOffBalance() {
		currentPlayer.addPassiveCondition(new Balance(), 3000);
		testSkill.perform("punch testtarget", currentPlayer);
		verify(target, Mockito.times(0)).takeDamage(Type.BLUNT,  expectedDamage);
	}
	
	
	@Test
	public void testBalanceLength() {
//		fail("Not Implemented.");
	}
	
	@Test
	public void testCannotPunchDeadMobiles() {
		when(target.isDead()).thenReturn(true);
		testSkill.perform("punch testtarget", currentPlayer);
		verify(target, Mockito.times(0)).takeDamage(Type.BLUNT,  expectedDamage);		
	}
	
	@Test
	public void testCannotPunchItems() {
		ItemBuilder ib = new ItemBuilder();
		ib.setItemContainer(location);
		ib.setName("item");
		ib.complete();
		Holdable item = ib.getFinishedItem();
		testSkill.perform("punch item", currentPlayer);
	}
	
	@Test
	public void testCannotPunchSelf() {
		testSkill.perform("punch currentplayer", currentPlayer);
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
		testSkill.perform("punch testtarget", currentPlayer);
		verify(target, Mockito.times(0)).takeDamage(Type.BLUNT,  expectedDamage);		
	}
	
	@Test
	public void testEnemyPresent() {
		testSkill.perform("punch testtarget", currentPlayer);
		verify(target).takeDamage(Type.BLUNT,  expectedDamage);
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
		testSkill = new Punch();
		LocationBuilder lb = new LocationBuilder();
//		lb.setId(1);
		lb.complete();
		location = lb.getFinishedLocation();
		MobileBuilder mb = new MobileBuilder();
		mb.setLocation(location);
		mb.setName("currentplayer");
		mb.complete();
		currentPlayer = mb.getFinishedMob();
		target = mock(StdMob.class);
		location.acceptItem(target);
		when(target.getName()).thenReturn("testtarget");
		when(target.getContainer()).thenReturn(location);
		testSkill = new Punch();
	}

	@After
	public void tearDown() throws Exception {
	}	
}
