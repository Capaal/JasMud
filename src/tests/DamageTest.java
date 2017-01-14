package tests;


import interfaces.Container;
import interfaces.Mobile;
import static org.mockito.Mockito.*;
import processes.Skill;
import processes.SkillBuilder;
import processes.StdMob;
import processes.WorldServer;

import org.junit.Test;
import org.junit.Before;

import TargettingStrategies.*;
import actions.Damage;

public class DamageTest {
	
	public class SkillStub extends Skill {
		
		public SkillStub(SkillBuilder build) {
			super(build);
		}		
	}
	
	Skill testSkill;
	
	@Before
	public void initialize() {
		WorldServer.setInterface(new StubDatabaseInterface());
	}
	

	@Test
	public void testActivateWeaponDoesNotMatter() {
		SkillBuilder testBuilder = new SkillBuilder();
		testBuilder.addAction(new Damage(10, new WhatStrategySelf(), new WhereStrategyHere(), false, null));
		testBuilder.setId(10);;
		testBuilder.complete();
		testSkill = new SkillStub(testBuilder);
		Mobile testMob = mock(StdMob.class);
		when(testMob.getContainer()).thenReturn(mock(Container.class));
		testSkill.perform("test asdf", testMob);
		verify(testMob).takeDamage(null,  10);
	}
	
	@Test
	public void testActivateWeaponMatters() {
		SkillBuilder testBuilder = new SkillBuilder();
		testBuilder.addAction(new Damage(10, new WhatStrategySelf(), new WhereStrategyHere(), true, null));
		testBuilder.setId(1);
		testBuilder.complete();
		testSkill = new SkillStub(testBuilder);
		Mobile testMob = mock(StdMob.class);
		when(testMob.getContainer()).thenReturn(mock(Container.class));
		when(testMob.getWeaponMultiplier()).thenReturn(1.5);
		testSkill.perform("test asdf", testMob);
		verify(testMob).takeDamage(null,  15);
	}

}
