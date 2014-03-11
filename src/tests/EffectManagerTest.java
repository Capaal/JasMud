package tests;

import static org.junit.Assert.*;
import interfaces.Effect;
import interfaces.Mobile;
import interfaces.TickingEffect;
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
	
	public class EffectStub implements TickingEffect {

		public ArrayList<Long> timings = new ArrayList<Long>();
		long startTime;
		
		public EffectStub() {
			super();
			this.startTime = System.currentTimeMillis();
		}

		@Override
		public void run() {
			timings.add(System.currentTimeMillis() - startTime);
		}

		@Override
		public boolean isInstanceOf(Effect otherEffect) {
			if (otherEffect.getClass() == EffectStub.class) {
				return true;
			}
			return false;
		}

		@Override
		public double checkAgainstIncomingDamage(Set<Type> incomingTypes, double damage) {
			// TODO Auto-generated method stub
			return damage;
		}		
	}
	
	@Test
	public void testHasEffect() {
		EffectManager manager = new EffectManager();
		manager.registerEffectDestroyAfterXMilliseconds(new EffectStub(), 200);
		assertFalse("Effects should compare as false if not the exact same class", manager.hasEffect(new EffectStub()));
	}
	
	@Test
	public void testHasInstanceOfEffect() {
		EffectManager manager = new EffectManager();
		manager.registerEffectDestroyAfterXMilliseconds(new EffectStub(), 200);
		assertTrue("Effects should compare as true if of the same class.", manager.hasInstanceOf(new EffectStub()));
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
		Effect testEffect = new EffectStub();
		manager.registerEffectDestroyAfterXMilliseconds(testEffect, 250);
		Thread.sleep(240);
		assertTrue("Should not be removed more than 10ms early.", manager.hasEffect(testEffect));
	}
	
	@Test
	public void testRegisterEffectDestroyAfterXSecondsDestroysTooLate() throws InterruptedException {
		EffectManager manager = new EffectManager();
		Effect testEffect = new EffectStub();
		manager.registerEffectDestroyAfterXMilliseconds(testEffect, 250);
		Thread.sleep(260);
		assertFalse("Should not be removed more than 10ms late.", manager.hasEffect(testEffect));
	}
		
	@Test (expected = IllegalArgumentException.class) 
	public void testRegisterEffectRepeatNTimesOverXMillisecondsNullEffect() {
		EffectManager manager = new EffectManager();
		manager.registerEffectRepeatNTimesOverXMilliseconds(null, 1, 250);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRegisterEffectRepeatNTimesOverXMillisecondsZeroMilliecondsTime() {
		EffectManager manager = new EffectManager();
		manager.registerEffectRepeatNTimesOverXMilliseconds(mock(TickingEffect.class), 0, 250);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRegisterEffectRepeatNTimesOverXMillisecondsNegativeMillisecondsTime() {
		EffectManager manager = new EffectManager();
		manager.registerEffectRepeatNTimesOverXMilliseconds(mock(TickingEffect.class), -1, 250);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRegisterEffectRepeatNTimesOverXMillisecondsZeroMilliesecondsDuration() {
		EffectManager manager = new EffectManager();
		manager.registerEffectRepeatNTimesOverXMilliseconds(mock(TickingEffect.class), 2, 0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRegisterEffectRepeatNTimesOverXMillisecondsNegativeMillisecondsDuration() {
		EffectManager manager = new EffectManager();
		manager.registerEffectRepeatNTimesOverXMilliseconds(mock(TickingEffect.class), 2, -1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRegisterEffectRepeatNTimeOverXMillisecondsRunsTooOften() {
		EffectManager manager = new EffectManager();
		manager.registerEffectRepeatNTimesOverXMilliseconds(mock(TickingEffect.class), 4, 200);
	}
	
	@Test
	public void testRegisterEffectRepeatNTimesOverXMillisecondsDestroysTooEarly() throws InterruptedException {
		EffectManager manager = new EffectManager();
		TickingEffect testEffect = new EffectStub();
		manager.registerEffectRepeatNTimesOverXMilliseconds(testEffect, 1, 250);
		Thread.sleep(240);
		assertTrue("Should not be removed more than 10ms early.", manager.hasEffect(testEffect));
	}
	
	@Test
	public void testRegisterEffectRepeatNTimesOverXMillisecondsDestroysTooLate() throws InterruptedException {
		EffectManager manager = new EffectManager();
		TickingEffect testEffect = new EffectStub();
		manager.registerEffectRepeatNTimesOverXMilliseconds(testEffect, 1, 250);
		Thread.sleep(260);
		assertFalse("Should not be removed more than 10ms late.", manager.hasEffect(testEffect));
	}
	
	@Test
	public void testRegisterEffectRepeatNTimesOverXMillisecondsRepeatsAtCorrectTime() throws InterruptedException {
		EffectManager manager = new EffectManager();
		EffectStub testEffect = new EffectStub();
		manager.registerEffectRepeatNTimesOverXMilliseconds(testEffect, 10, 1000);
		Thread.sleep(1100);
		for (int i = 1; i <= 10; i++) {			
			Long currentTiming = testEffect.timings.get(i - 1);
			assertTrue("Triggered timing should remain in expected range: " + currentTiming + " vs: " + (i * 100 - 50), currentTiming >= (i * 100 - 50));
			assertTrue("Triggered timing should remain in expected range: " + currentTiming + " vs: " + (i * 100 + 50), currentTiming <= (i * 100 + 50));
		}
	}
	
	@Test
	public void testRegisterRepeatingEffectTriggersCorrectNumberOfTimesAtBeginning() throws InterruptedException {
		EffectManager manager = new EffectManager();
		EffectStub testEffect = new EffectStub();
		manager.registerEffectRepeatNTimesOverXMilliseconds(testEffect, 5, 300);
		Thread.sleep(20);
		assertTrue("Should only have triggered 0 times: " + testEffect.timings.size(), testEffect.timings.size() == 0);
	}
	
	@Test
	public void testRegisterRepeatingEffectTriggersCorrectNumberOfTimesAtMiddle() throws InterruptedException {
		EffectManager manager = new EffectManager();
		EffectStub testEffect = new EffectStub();
		manager.registerEffectRepeatNTimesOverXMilliseconds(testEffect, 5, 300);
		Thread.sleep(200);
		assertTrue("Should only have triggered 3 times: " + testEffect.timings.size(), testEffect.timings.size() == 3);
	}
	
	@Test
	public void testRegisterRepeatingEffectTriggersCorrectNumberOfTimesAtEnd() throws InterruptedException {
		EffectManager manager = new EffectManager();
		EffectStub testEffect = new EffectStub();
		manager.registerEffectRepeatNTimesOverXMilliseconds(testEffect, 5, 300);
		Thread.sleep(340);
		assertTrue("Should only have triggered 5 times: " + testEffect.timings.size(), testEffect.timings.size() == 5);
	}
	
	@Test
	public void testRegisterRepeatingEffectTriggersCorrectNumberOfTimesFarFuture() throws InterruptedException {
		EffectManager manager = new EffectManager();
		EffectStub testEffect = new EffectStub();
		manager.registerEffectRepeatNTimesOverXMilliseconds(testEffect, 5, 300);
		Thread.sleep(500);
		assertTrue("Should only have triggered 5 times: " + testEffect.timings.size(), testEffect.timings.size() == 5);
	}
}
