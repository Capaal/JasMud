package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import processes.MobileBuilder;
import processes.StdMob;

public class StdMobBuilderTest {
	
	MobileBuilder mob;
	
	@Before
	public void initialize() {
		mob = new MobileBuilder();
		mob.setId(1);
	}

	@Test
	public void testGetName() {
	
		mob.setName("m");
		mob.complete();
		StdMob newMob = mob.getFinishedMob();
		assertTrue("get name should return m", "m".equals(newMob.getName()));
	}
	
	@Test
	public void testGetNameUpper() {
	
		mob.setName("M");
		mob.complete();
		StdMob newMob = mob.getFinishedMob();
		assertTrue("get name should return M", "M".equals(newMob.getName()));
	}
	@Test
	public void testGetNameMix() {
	
		mob.setName("meMeE");
		mob.complete();
		StdMob newMob = mob.getFinishedMob();
		assertTrue("get name should return meMeE", "meMeE".equals(newMob.getName()));
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNameNull() {
	
		mob.setName(null);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNameNums() {
	
		mob.setName("jason1234");
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNamesSymbolsUnderscore() {
	
		mob.setName("j_ason");
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNamesSymbolsSemiColon() {
	
		mob.setName("ja;son");
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNamesSymbolsSingleQuote() {
	
		mob.setName("jaso'n");
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNamesSymbolsQuote() {
	
		mob.setName("jaso\"n");
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNameBlank() {
	
		mob.setName("");
	}


}
