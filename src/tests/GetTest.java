package tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import interfaces.*;

import org.junit.Before;
import org.junit.Test;

import actions.Get;
import TargettingStrategies.WhatTargettingStrategy;
import TargettingStrategies.WhereTargettingStrategy;
import processes.Skill;
import processes.WorldServer;
import processes.Skill.Syntax;
import processes.SkillBuilder;

public class GetTest {	
	
	public Container mockStartContainer;
	public Container mockFinalContainer;
//	public Holdable stubItem = new stubItem();
	public Holdable mockItem;
	
	
	@Before
	public void initialize() {
		mockStartContainer = mock(Container.class);
		mockFinalContainer = mock(Container.class);
		mockItem = mock(Holdable.class);
	}
	@Test
	public void test() {
		WorldServer.setInterface(new StubDatabaseInterface());
		Action testGet = new Get(new stubWhat(), new stubWhere());
		when(mockStartContainer.getHoldableFromString("testItem")).thenReturn(mockItem);
		when(mockItem.getContainer()).thenReturn(mockStartContainer);
		SkillBuilder skillBuild = new SkillBuilder();
		skillBuild.setId(1);
		skillBuild.setSyntax(Arrays.asList(Syntax.SKILL, Syntax.ITEM));
		Skill testSkill = new Skill(skillBuild);
		Mobile mockMob = mock(Mobile.class);
		testGet.activate(testSkill, "get testItem", mockMob);
		verify(mockStartContainer).removeItemFromLocation(mockItem);
		verify(mockMob).acceptItem(mockItem);
		verify(mockItem).setContainer(mockMob);
	}
	
	public class stubWhere implements WhereTargettingStrategy {
		@Override
		public List<Container> findWhere(Skill s, String fullCommand,
				Mobile currentPlayer) {
			return Arrays.asList(mockStartContainer);
		}		
	}
	
	
	public class stubWhat implements WhatTargettingStrategy {
		@Override
		public List<Holdable> findWhat(Skill s, String fullCommand,
				Mobile currentPlayer, List<Container> containersToSearch) {
			return Arrays.asList(mockItem);
		}		
	}	
}
