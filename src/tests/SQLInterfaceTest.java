package tests;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import processes.SQLInterface;

public class SQLInterfaceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SQLInterface.connect("root", "".toCharArray());
	}

	@Test
	public void testViewDataEmpty() {
		Object shouldBeNull = SQLInterface.viewData(";", "ABC");
		assertTrue("A blink call should return as null.", shouldBeNull == null);
	}

	@Test
	public void testReturnBlockViewEmpty() {
		HashMap<String, Object> shouldBeEmpty = SQLInterface.returnBlockView(";");
		assertTrue("A blank call should return as empty.", shouldBeEmpty.isEmpty());
	}

}