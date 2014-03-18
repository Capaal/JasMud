package tests;

import static org.junit.Assert.*;
import interfaces.Mobile;
import static org.mockito.Mockito.*;

import org.junit.Test;

import processes.AggresiveMobileDecorator;
import processes.MobileBuilder;
import processes.StdMob;

public class AggresiveMobileDecoratorTest {

	@Test
	public void testTakeDamage_DoesAttackBack() throws InterruptedException {
		Mobile testMob = mock(StdMob.class);
		when(testMob.getName()).thenReturn("");
		Mobile attackedMob = new AggresiveMobileDecorator(new StdMob(new MobileBuilder()));
		
		attackedMob.informLastAggressor(testMob);
		attackedMob.takeDamage(null, 1);
		Thread.sleep(10000);
		verify(testMob).takeDamage(null, 11);
	}

}
