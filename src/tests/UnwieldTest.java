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
import skills.Unwield;
import skills.Wield;

public class UnwieldTest {
	
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
		
		new Wield(player, "wield stick").run();

	}

	@Test
	public void testNoBalance() {
		player.addPassiveCondition(PassiveCondition.BALANCE, 5000);
		new Unwield(player, "unWield stick righthand").run();
		assertTrue("Player can unWield when off balance.", player.getEquipmentInSlot(EquipmentSlot.RIGHTHAND) == null);
	}
	
	@Test
	public void testBasic() {
		assertTrue("Player starts with stick in righthand.", player.getEquipmentInSlot(EquipmentSlot.RIGHTHAND) == StickOne);
		new Unwield(player, "unWield stick").run();
		assertTrue("Player should unWield stick from righthand on default.", player.getEquipmentInSlot(EquipmentSlot.RIGHTHAND) == null);
	
	}
	
	@Test
	public void testRightHandUnWieldLowercase() {
		new Unwield(player, "UnWield stick righthand").run();
		assertTrue("Player should UnWield stick in righthand.", player.getEquipmentInSlot(EquipmentSlot.RIGHTHAND) == null);
	}
	@Test
	public void testRightHandUnWieldOddcase() {
		new Unwield(player, "UnWield stIck rightHand").run();
		assertTrue("Player should UnWield stick in righthand.", player.getEquipmentInSlot(EquipmentSlot.RIGHTHAND) == null);
	}
	@Test
	public void testRightHandUnWieldPartial() {
		new Unwield(player, "UnWie sti right").run();
		assertTrue("Player should UnWield stick in righthand.", player.getEquipmentInSlot(EquipmentSlot.RIGHTHAND) == null);
	}
	
	@Test
	public void testRightHandUnWieldPartialAll() {
		new Unwield(player, "unwie sti rig").run();
		assertTrue("Player should UnWield stick in righthand.", player.getEquipmentInSlot(EquipmentSlot.RIGHTHAND) == null);
	}
	@Test
	public void testLeftHandUnWieldPartialAll() {
		new Unwield(player, "unwie sti righ").run();
		new Wield(player, "wield stick left").run();
		assertTrue("Player should have stick in left hand.", player.getEquipmentInSlot(EquipmentSlot.LEFTHAND) == StickOne);
		assertTrue("Player should UnWield stick in righthand.", player.getEquipmentInSlot(EquipmentSlot.RIGHTHAND) == null);
		new Unwield(player, "unwie sti lef").run();
		assertTrue("Player should UnWield stick in lefthand.", player.getEquipmentInSlot(EquipmentSlot.LEFTHAND) == null);

	}
	@Test
	public void testBrokenArmsRight() {
		player.addPassiveCondition(PassiveCondition.BROKENRIGHTARM, 5000);
		assertTrue("Righthand should be broken, so auto-unwield.", player.getEquipmentInSlot(EquipmentSlot.RIGHTHAND) == null);
	}
}
