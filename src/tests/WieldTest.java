package tests;

import static org.junit.Assert.*;

import java.util.EnumSet;

import interfaces.Mobile;
import items.Weapon;
import items.Weapon.WeaponItemBuilder;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import effects.PassiveCondition;
import processes.Equipment.EquipmentSlot;
import processes.GameState;
import processes.Location;
import processes.LocationBuilder;
import processes.MobileBuilder;
import processes.WorldServer;
import skills.Wield;

public class WieldTest {
	
	static Location LocationOne;
	Weapon StickOne;
	Mobile player;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		WorldServer.setGameState(new GameState());
		
		LocationBuilder firstLoc = new LocationBuilder();
		firstLoc.setName("Start.");
		firstLoc.setDescription("You have to start somewhere");
		firstLoc.complete();
		LocationOne = firstLoc.getFinishedLocation();
		
	
		
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MobileBuilder mb = new MobileBuilder();
		mb.setLocation(LocationOne);
		mb.complete();
		player = mb.getFinishedMob();
		
		WeaponItemBuilder newItem = new WeaponItemBuilder();	
		newItem.setName("stick");
		newItem.setItemContainer(player);
		newItem.complete();
		StickOne = (Weapon) newItem.getFinishedItem();
	}

	@Test
	public void testNoBalance() {
		player.addPassiveCondition(PassiveCondition.BALANCE, 5000);
		new Wield(player, "wield stick").run();
		assertTrue("Player should fail to wield stick when offbalance.", player.getEquipmentInSlot(EquipmentSlot.RIGHTHAND) == null);
	}
	
	@Test
	public void testBasic() {
		assertTrue("Player should have stick already.", player.getHoldableFromString("stick") == StickOne);
		new Wield(player, "wield stick").run();
		assertTrue("Player should wield stick in righthand on default.", player.getEquipmentInSlot(EquipmentSlot.RIGHTHAND) == StickOne);
	
	}
	
	@Test
	public void testLeftHandWieldLowercase() {
		new Wield(player, "wield stick lefthand").run();
		assertTrue("Player should wield stick in lefthand.", player.getEquipmentInSlot(EquipmentSlot.LEFTHAND) == StickOne);
	}
	@Test
	public void testLeftHandWieldOddcase() {
		new Wield(player, "wield stIck leftHAnd").run();
		assertTrue("Player should wield stick in lefthand.", player.getEquipmentInSlot(EquipmentSlot.LEFTHAND) == StickOne);
	}
	@Test
	public void testLeftHandWieldPartial() {
		new Wield(player, "wield sti left").run();
		assertTrue("Player should wield stick in lefthand.", player.getEquipmentInSlot(EquipmentSlot.LEFTHAND) == StickOne);
	}
	
	@Test
	public void testLeftHandWieldPartialAll() {
		new Wield(player, "wie sti lef").run();
		assertTrue("Player should wield stick in lefthand.", player.getEquipmentInSlot(EquipmentSlot.LEFTHAND) == StickOne);
	}
	
	@Test
	public void testAllowedSlots() {
		WeaponItemBuilder newItem = new WeaponItemBuilder();	
		newItem.setName("staff");
		newItem.setAllowedSlots(EnumSet.noneOf(EquipmentSlot.class));
		newItem.setAllowedSlots(EquipmentSlot.LEFTHAND);
		newItem.setItemContainer(player);
		newItem.complete();
		Weapon staff = (Weapon) newItem.getFinishedItem();
		new Wield(player, "wield staff right").run();
		assertFalse("Righthand not valid slot.", player.getEquipmentInSlot(EquipmentSlot.RIGHTHAND) == staff);
		new Wield(player, "wield staff left").run();
		assertTrue("Lefthand is a valid slot.", player.getEquipmentInSlot(EquipmentSlot.LEFTHAND) == staff);
	}
	@Test
	public void testBrokenArmsRight() {
		player.addPassiveCondition(PassiveCondition.BROKENRIGHTARM, 5000);
		new Wield(player, "wield stick right").run();
		assertFalse("Righthand should be broken.", player.getEquipmentInSlot(EquipmentSlot.RIGHTHAND) == StickOne);
		}
	@Test
	public void testBrokenArmsLeft() {
		player.addPassiveCondition(PassiveCondition.BROKENLEFTARM, 5000);
		new Wield(player, "wield stick left").run();
		assertFalse("Lefthand should be broken.", player.getEquipmentInSlot(EquipmentSlot.LEFTHAND) == StickOne);
	}
}
