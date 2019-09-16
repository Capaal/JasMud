package tests;

import static org.junit.Assert.*;
import interfaces.Holdable;
import items.ItemBuilder;
import items.Weapon;
import items.Weapon.MercEffect;
import items.Weapon.WeaponItemBuilder;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import processes.GameState;
import processes.Location;
import processes.LocationBuilder;
import processes.WorldServer;

public class GetHoldableFromStringTest {
	
	static Location LocationOne;
	static Weapon StickOne;
	static Weapon StickTwo;
	static Weapon StickThree;
	
	static Holdable stItem;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		WorldServer.setGameState(new GameState());
		
		LocationBuilder firstLoc = new LocationBuilder();
		firstLoc.setName("Start.");
		firstLoc.setDescription("You have to start somewhere");
		firstLoc.complete();
		LocationOne = firstLoc.getFinishedLocation();
		
		WeaponItemBuilder newItem = new WeaponItemBuilder();	
		newItem.setName("stick");
		newItem.setItemContainer(LocationOne);
		newItem.complete();
		StickOne = (Weapon) newItem.getFinishedItem();
		
		WeaponItemBuilder newItem2 = new WeaponItemBuilder();	
		newItem2.setName("stick");
		newItem2.setItemContainer(LocationOne);
		newItem2.complete();
		StickTwo = (Weapon) newItem2.getFinishedItem();
		
		WeaponItemBuilder newItem3 = new WeaponItemBuilder();	
		newItem3.setName("stick");
		newItem3.setItemContainer(LocationOne);
		newItem3.complete();
		StickThree = (Weapon) newItem3.getFinishedItem();
		
		ItemBuilder newStItem = new ItemBuilder();
		newStItem.setName("sta");
		newStItem.setItemContainer(LocationOne);
		newStItem.complete();
		stItem = newStItem.getFinishedItem();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testBasics() {
		assertTrue("Location One's ID should be 1.", LocationOne.getId() == 1);
		assertTrue("Stick One's ID should be 1.", StickOne.getId() == 1);
		assertTrue("Stick One's name should be stick", StickOne.getName().equals("stick"));
		
		assertTrue("Stick Two's ID should be 2: " + StickTwo.getId(), StickTwo.getId() == 2);
		assertTrue("Stick Three's ID should be 3.", StickThree.getId() == 3);
	}
	
	@Test
	public void testExactName() {
		Holdable ourStick = LocationOne.getHoldableFromString("stick2");
		assertTrue("Stick2 should be false, not identical to stick1", ourStick != StickOne);
		
		ourStick = LocationOne.getHoldableFromString("stick1");
		assertTrue("Stick1 should be true.", ourStick == StickOne);
		
		ourStick = LocationOne.getHoldableFromString("stick2");
		assertTrue("Stick2 should be true.", ourStick == StickTwo);
		
		ourStick = LocationOne.getHoldableFromString("sti1");
		assertTrue("sti1 should be far from true.", ourStick == null);
		
		ourStick = LocationOne.getHoldableFromString("Stick2");
		assertTrue("Stick2 should be true with Caps.", ourStick == StickTwo);
		
		ourStick = LocationOne.getHoldableFromString("StICk2"); 
		assertTrue("StICk2 should be true with unusual caps.", ourStick == StickTwo);
		
		ourStick = LocationOne.getHoldableFromString("stick 2");
		assertTrue("stick 2 should be false from space.", ourStick == null);
		
		ourStick = LocationOne.getHoldableFromString("stiCk2");
		assertTrue("stiCk2 should be true with unusual caps.", ourStick == StickTwo);
	}

	@Test
	public void testNameWithoutNumber() {
		Holdable ourStick = LocationOne.getHoldableFromString("stick");
		assertTrue("stick should be true: " + ourStick, ourStick == StickOne);
		
		ourStick = LocationOne.getHoldableFromString("Stick");
		assertTrue("Stick should be true", ourStick == StickOne);
		
		ourStick = LocationOne.getHoldableFromString("STick");
		assertTrue("STick should be true", ourStick == StickOne);
		
		ourStick = LocationOne.getHoldableFromString("sti ck");
		assertTrue("Sti ck should be false, spaces are not ok", ourStick == null);
	}
	
	@Test
	public void testPartialNames() {
		Holdable ourStick = LocationOne.getHoldableFromString("sti");
		assertTrue("Sti should be true." , ourStick == StickOne);
		
		ourStick = LocationOne.getHoldableFromString("stic");
		assertTrue("Stic should be true.", ourStick == StickOne);
		
		ourStick = LocationOne.getHoldableFromString("st");
		assertTrue("St should be false, expecting a sta item.: " + ourStick.toString(), ourStick != StickOne);
		assertTrue("St should be false, expecting a sta item.: " + ourStick.toString(), ourStick == stItem);
		
		ourStick = LocationOne.getHoldableFromString("sta");
		assertTrue("Sta should be false, expecting a sta item.: " + ourStick.toString(), ourStick != StickOne);
		assertTrue("Sta should be false, expecting a sta item.: " + ourStick.toString(), ourStick == stItem);
	}
	
	@Test
	public void testDoubleDigitNames() {
		Holdable ourStick = LocationOne.getHoldableFromString("stick22");
		assertTrue("stick22 should be null." , ourStick == null);
		
		WeaponItemBuilder newItem2 = new WeaponItemBuilder();	
		for (int i = 1; i < 20; i++) {
			newItem2.setName("stick");
			newItem2.setItemContainer(LocationOne);
			newItem2.complete();
		}		
		Weapon stick22 = (Weapon) newItem2.getFinishedItem();
		assertTrue("stick22 should be id 22: " + stick22.getId(), stick22.getId() == 22);
		
		ourStick = LocationOne.getHoldableFromString("stick22");
		assertTrue("stick22 should be true." , ourStick == stick22);
		
		ourStick = LocationOne.getHoldableFromString("stick2");
		assertTrue("stick2 should be true." , ourStick == StickTwo);
	}
}
