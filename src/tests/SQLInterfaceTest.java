package tests;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import processes.SQLInterface;
import processes.WorldServer;

public class SQLInterfaceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		WorldServer.databaseInterface.connect("root", "".toCharArray());
	}

	@Test
	public void testViewDataEmpty() {
		Object shouldBeNull = WorldServer.databaseInterface.viewData("", "ABC");
		assertTrue("A blink call should return as null.", shouldBeNull == null);
	}

	@Test
	public void testReturnBlockViewEmpty() {
		HashMap<String, Object> shouldBeEmpty = WorldServer.databaseInterface.returnBlockView("");
		assertTrue("A blank call should return as empty.", shouldBeEmpty.isEmpty());
	}
	
	@Test
	public void testReturnBlockViewSkillDoesNotExist() {
		String skillSelect = "SELECT * FROM SKILL WHERE SKILLNAME='fakenamethatdoesnotexist';";
		HashMap<String, Object> skillView = WorldServer.databaseInterface.returnBlockView(skillSelect);
		assertTrue("A skill that doesn't exist should return an empty map.", skillView.isEmpty());
	}
	
	
}
