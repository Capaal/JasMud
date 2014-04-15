package tests;

import static org.junit.Assert.*;
import interfaces.Action;
import interfaces.Mobile;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import processes.Skill;
import processes.SkillBuilder;
import processes.StdMob;
import processes.WorldServer;
import actions.Charge;
import actions.Damage;

public class ChargeTest {
		
	@BeforeClass
	public static void testTheSQLInterfaceTest() {
		WorldServer.setInterface(new TestSQLInterface());
		WorldServer.databaseInterface.connect("root", "".toCharArray());	
	}

	
	
	@Test
	public void testActivate() {
		Mobile mockMobile = mock(StdMob.class);
		Action mockAction = mock(Action.class);
		SkillBuilder sb = new SkillBuilder();
		Queue testQueue = new LinkedList();
		testQueue.add(mockAction);
		sb.addAction(new Charge(100, testQueue));
		sb.complete();
		Skill testSkill = sb.getFinishedSkill();
		verify(mockAction).activate(testSkill, "", mockMobile);
	}
	
	@Test
	public void testActivateInterrupt() {
		Mobile mockMobile = mock(StdMob.class);
		Action mockAction = mock(Action.class);
		SkillBuilder sb = new SkillBuilder();
		Queue testQueue = new LinkedList();
		testQueue.add(mockAction);
		sb.addAction(new Charge(100, testQueue));
		sb.complete();
		Skill testSkill = sb.getFinishedSkill();
		fail("Not yet implemented");
		verify(mockAction).activate(testSkill, "", mockMobile);
	}

	@Test
	public void testSelectOneself() {
		Queue<Action> testQueue = new LinkedList<Action>();
		testQueue.add(new Damage());
		Action testCharge = new Charge(200, testQueue);
		testCharge.insertOneself(0);
		HashMap<String, Object> testSelect = testCharge.selectOneself(0);
		assertTrue("Should have gotten a non empty map back.", !testSelect.isEmpty());
		assertTrue("Blocktype is correct", testSelect.get("BLOCKTYPE").equals("CHARGE"));
		assertTrue("Int value is correct", (int)testSelect.get("INTVALUE") == 200);
		assertTrue("Position is correct", (int)testSelect.get("BLOCKPOS") == 0);
	}

	@Test
	public void testSaveZeroCharge() {
		Action testCharge = new Charge(0, null);
		assertTrue("Save should work.", testCharge.save(0));
	}
	
	@Test
	public void testNegativeCharge() {
		Action testCharge = new Charge(-200, null);
		HashMap<String, Object> testSelect = testCharge.selectOneself(0);
		assertTrue("Should default to 0.", (int)testSelect.get("INTVALUE") == 0);
	}
	
	@Test
	public void testSaveNormalCharge() {
		Action testCharge = new Charge(200, null);
		assertTrue("Save should work.", testCharge.save(0));
	}

}
