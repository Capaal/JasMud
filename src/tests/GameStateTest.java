package tests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import interfaces.Holdable;
import items.StackableItem;
import items.StdItem;
import items.Weapon.MercEffect;
import items.Weapon.WeaponItemBuilder;
import processes.CreateWorld;
import processes.Equipment;
import processes.GameState;
import processes.Location;
import processes.LocationConverter;
import processes.StdMob;
import processes.WorldServer;

public class GameStateTest {

	private static GameState testGameState;
	public static XStream xstream;
	private static final String TESTGAMESTATENAME = "testgamestate";
	
	private static void createXStream() {
		xstream = new XStream();	
        xstream.alias("StdMob", StdMob.class);
        xstream.registerConverter(new LocationConverter());
		xstream.alias("Location", Location.class);
		xstream.alias("StdItem", StdItem.class);
		xstream.alias("Stackable", StackableItem.class);
		xstream.alias("GameState", GameState.class);
		xstream.processAnnotations(Location.class);
		xstream.processAnnotations(GameState.class);
		xstream.processAnnotations(Equipment.class);
		xstream.processAnnotations(StdItem.class);
		xstream.processAnnotations(StdMob.class);	
		WorldServer.xstream = xstream;
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		createXStream();		
		testGameState = new GameState(TESTGAMESTATENAME);
		WorldServer.setGameState(testGameState);
		CreateWorld.loadMap();
		System.out.println("Test World Created.");
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Test
	public void testBasicLocationsExist() {
		assertTrue("Basic locations should exist in game state.",!testGameState.viewLocations().isEmpty());
	}

	

	@Test
	public void testGetSaveName() {
		String saveName = testGameState.getSaveName();
		assertTrue("Save name should be: testgamestate. Is: " + saveName, saveName.contentEquals("testgamestate"));
	}
	
	@Test
	public void testSimpleItemCreation() {
		Location location1 = testGameState.viewLocations().get(1);
		assertTrue("Location 1 should exist", location1 != null);
		
		WeaponItemBuilder newItem = new WeaponItemBuilder();	
		newItem.setName("stick");
		newItem.setDescription("It's an evil stick.");
		newItem.setDamageMult(0.5);
		newItem.setWeight(.5);
		newItem.setMercEffect(MercEffect.FEAR);
		newItem.setItemContainer(location1);
		newItem.complete();
		Holdable item = newItem.getFinishedItem();
		WorldServer.getGameState().itemTemplates.put("stick", newItem);
		
		int itemCount = location1.viewInventory().size();
		assertTrue("A single item should exist, count: " + itemCount, itemCount == 1);
		
		location1.removeItemFromLocation(item);
		itemCount = location1.viewInventory().size();
		assertTrue("Zero items should exist, count: " + itemCount, itemCount == 0);
	}
	
	@Test
	public void testXStream() {
		Location location1 = testGameState.viewLocations().get(1);		
		WeaponItemBuilder newItem = new WeaponItemBuilder();	
		newItem.setName("stick");
		newItem.setDescription("It's an evil stick.");
		newItem.setItemContainer(location1);
		newItem.complete();
		Holdable item = newItem.getFinishedItem();
		
		String xml = xstream.toXML(item);
		assertTrue("File should not exist.", !(new File(System.getProperty("user.dir") + "/" + TESTGAMESTATENAME + "/Items/" 
				+ item.getName() + item.getId() + ".xml")).exists());
		Path path = Paths.get(System.getProperty("user.dir") + "/" + TESTGAMESTATENAME + "/Items/" 
				+ item.getName() + item.getId() + ".xml");
		try {
			Files.write(path, xml.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			System.out.println("Fail");
		}
		File file = new File(System.getProperty("user.dir") + "/" + TESTGAMESTATENAME + "/Items/" 
				+ item.getName() + item.getId() + ".xml");
		
		//Cleanup
		assertTrue("File should exist.", file.exists());
		file.delete();
		assertTrue("File should not exist.", !file.exists());
		location1.removeItemFromLocation(item);
	}
	
	// Need to test writing ANYTHING to file
	@Test
	public void testSavingSimpleItem() {
		Location location1 = testGameState.viewLocations().get(1);		
		WeaponItemBuilder newItem = new WeaponItemBuilder();	
		newItem.setName("stick");
		newItem.setDescription("It's an evil stick.");
		newItem.setItemContainer(location1);
		newItem.complete();
		Holdable item = newItem.getFinishedItem();
		int itemCount = location1.viewInventory().size();
		assertTrue("A single item should exist, count: " + itemCount, itemCount == 1);
		
		File file = new File(System.getProperty("user.dir") + "/" + TESTGAMESTATENAME + "/Items/" + item.getName() + item.getId() + ".xml");
		assertTrue("The file should not yet exist.", !file.exists());		
		
		testGameState.saveItem(item);
		file = new File(System.getProperty("user.dir") + "/" + TESTGAMESTATENAME + "/Items/" + item.getName() + item.getId() + ".xml");
		assertTrue("The file should exist.", file.exists());
	//	WorldServer.saveGameState(TESTGAMESTATENAME);
		
		//Cleanup
		file.delete();
		location1.removeItemFromLocation(item);
	}

	/*
	@Test
	public void testGameStateString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEffectExecutor() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testAddClass() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBooksFromClass() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddClient() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveClient() {
		fail("Not yet implemented");
	}

	@Test
	public void testViewActiveClients() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewPlayer() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeletePlayer() {
		fail("Not yet implemented");
	}

	@Test
	public void testViewAllPlayers() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPlayer() {
		fail("Not yet implemented");
	}

	@Test
	public void testViewAllBooks() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckForBookId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBook() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddBook() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddMob() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveMob() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckForMob() {
		fail("Not yet implemented");
	}

	@Test
	public void testViewMobs() {
		fail("Not yet implemented");
	}

	@Test
	public void testViewLocations() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddLocation() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckForLocation() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddToQueue() {
		fail("Not yet implemented");
	}

	@Test
	public void testTakeFromQueue() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveItem() {
		fail("Not yet implemented");
	}

	@Test
	public void testLoadSavedItems() {
		fail("Not yet implemented");
	}
*/
}
