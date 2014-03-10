package tests;

import static org.junit.Assert.*;
import interfaces.Effect;
import interfaces.EffectTakeDamage;
import interfaces.Mobile;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import effects.Balance;
import processes.EffectManager;
import processes.StdMob;
import processes.Type;

public class EffectManagerTest {
	
	public class EffectStub extends Effect {

		public ArrayList<Long> timings = new ArrayList<Long>();
		long startTime;
		
		public EffectStub(Mobile currentPlayer) {
			super(currentPlayer);
			this.startTime = System.currentTimeMillis();
		}

		@Override
		public int doRunEffect(Set<Type> incomingTypes, int damage) {
			timings.add(System.currentTimeMillis() - startTime);
			return 0;
		}		
	}

	@Test (expected = IllegalArgumentException.class) 
	public void testRegisterEffectDestroyAfterXSecondsNullEffect() {
		EffectManager manager = new EffectManager();
		manager.registerEffectDestroyAfterXMilliseconds(null, 1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRegisterEffectDestroyAfterXSecondsZeroSeconds() {
		EffectManager manager = new EffectManager();
		manager.registerEffectDestroyAfterXMilliseconds(mock(Effect.class), 0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRegisterEffectDestroyAfterXSecondsNegativeSeconds() {
		EffectManager manager = new EffectManager();
		manager.registerEffectDestroyAfterXMilliseconds(mock(Effect.class), -100);
	}
	
	@Test
	public void testRegisterEffectDestroyAfterXSecondsDestroysTooEarly() throws InterruptedException {
		EffectManager manager = new EffectManager();
		Effect testEffect = new EffectStub(mock(StdMob.class));
		manager.registerEffectDestroyAfterXMilliseconds(testEffect, 250);
		Thread.sleep(240);
		assertFalse("Should not be removed more than 10ms early.", testEffect.wasRemoved());
	}
	
	@Test
	public void testRegisterEffectDestroyAfterXSecondsDestroysTooLate() throws InterruptedException {
		EffectManager manager = new EffectManager();
		Effect testEffect = new EffectStub(mock(StdMob.class));
		manager.registerEffectDestroyAfterXMilliseconds(testEffect, 250);
		Thread.sleep(260);
		assertTrue("Should not be removed more than 10ms late.", testEffect.wasRemoved());
	}
		
	@Test (expected = IllegalArgumentException.class) 
	public void testRegisterEffectRepeatNTimesOverXMillisecondsNullEffect() {
		EffectManager manager = new EffectManager();
		manager.registerEffectRepeatNTimesOverXMilliseconds(null, 1, 250);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRegisterEffectRepeatNTimesOverXMillisecondsZeroMilliecondsTime() {
		EffectManager manager = new EffectManager();
		manager.registerEffectRepeatNTimesOverXMilliseconds(mock(Effect.class), 0, 250);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRegisterEffectRepeatNTimesOverXMillisecondsNegativeMillisecondsTime() {
		EffectManager manager = new EffectManager();
		manager.registerEffectRepeatNTimesOverXMilliseconds(mock(Effect.class), -1, 250);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRegisterEffectRepeatNTimesOverXMillisecondsZeroMilliesecondsDuration() {
		EffectManager manager = new EffectManager();
		manager.registerEffectRepeatNTimesOverXMilliseconds(mock(Effect.class), 2, 0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRegisterEffectRepeatNTimesOverXMillisecondsNegativeMillisecondsDuration() {
		EffectManager manager = new EffectManager();
		manager.registerEffectRepeatNTimesOverXMilliseconds(mock(Effect.class), 2, -1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRegisterEffectRepeatNTimeOverXMillisecondsRunsTooOften() {
		EffectManager manager = new EffectManager();
		manager.registerEffectRepeatNTimesOverXMilliseconds(mock(Effect.class), 4, 200);
	}
	
	@Test
	public void testRegisterEffectRepeatNTimesOverXMillisecondsDestroysTooEarly() throws InterruptedException {
		EffectManager manager = new EffectManager();
		Effect testEffect = new EffectStub(mock(StdMob.class));
		manager.registerEffectRepeatNTimesOverXMilliseconds(testEffect, 1, 250);
		Thread.sleep(240);
		assertFalse("Should not be removed more than 10ms early.", testEffect.wasRemoved());
	}
	
	@Test
	public void testRegisterEffectRepeatNTimesOverXMillisecondsDestroysTooLate() throws InterruptedException {
		EffectManager manager = new EffectManager();
		Effect testEffect = new EffectStub(mock(StdMob.class));
		manager.registerEffectRepeatNTimesOverXMilliseconds(testEffect, 1, 250);
		Thread.sleep(260);
		assertTrue("Should not be removed more than 10ms late.", testEffect.wasRemoved());
	}
	
	@Test
	public void testRegisterEffectRepeatNTimesOverXMillisecondsRepeatsAtCorrectTime() throws InterruptedException {
		EffectManager manager = new EffectManager();
		EffectStub testEffect = new EffectStub(mock(StdMob.class));
		manager.registerEffectRepeatNTimesOverXMilliseconds(testEffect, 2, 400);
		Thread.sleep(450);
		if (testEffect.timings.get(0) < 190 || testEffect.timings.get(0) > 210) {
			fail("Triggered timing was not correct.");
		}
		if (testEffect.timings.get(1) < 390 || testEffect.timings.get(1) > 410) {
			fail("Triggered timing was not correct.");
		}
	}
	
	@Test
	public void testRegisterRepeatingEffectTriggersCorrectNumberOfTimesAtBeginning() throws InterruptedException {
		EffectManager manager = new EffectManager();
		EffectStub testEffect = new EffectStub(mock(StdMob.class));
		manager.registerEffectRepeatNTimesOverXMilliseconds(testEffect, 5, 300);
		Thread.sleep(20);
		assertTrue("Should only have triggered 0 times: " + testEffect.timings.size(), testEffect.timings.size() == 0);
	}
	
	@Test
	public void testRegisterRepeatingEffectTriggersCorrectNumberOfTimesAtMiddle() throws InterruptedException {
		EffectManager manager = new EffectManager();
		EffectStub testEffect = new EffectStub(mock(StdMob.class));
		manager.registerEffectRepeatNTimesOverXMilliseconds(testEffect, 5, 300);
		Thread.sleep(200);
		assertTrue("Should only have triggered 3 times: " + testEffect.timings.size(), testEffect.timings.size() == 3);
	}
	
	@Test
	public void testRegisterRepeatingEffectTriggersCorrectNumberOfTimesAtEnd() throws InterruptedException {
		EffectManager manager = new EffectManager();
		EffectStub testEffect = new EffectStub(mock(StdMob.class));
		manager.registerEffectRepeatNTimesOverXMilliseconds(testEffect, 5, 300);
		Thread.sleep(340);
		assertTrue("Should only have triggered 5 times: " + testEffect.timings.size(), testEffect.timings.size() == 5);
	}
	
	@Test
	public void testRegisterRepeatingEffectTriggersCorrectNumberOfTimesFarFuture() throws InterruptedException {
		EffectManager manager = new EffectManager();
		EffectStub testEffect = new EffectStub(mock(StdMob.class));
		manager.registerEffectRepeatNTimesOverXMilliseconds(testEffect, 5, 300);
		Thread.sleep(500);
		assertTrue("Should only have triggered 5 times: " + testEffect.timings.size(), testEffect.timings.size() == 5);
	}
}
