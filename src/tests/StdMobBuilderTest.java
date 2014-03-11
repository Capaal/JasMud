package tests;

import static org.junit.Assert.*;
import org.junit.Test;
import processes.MobileBuilder;
import processes.StdMob;

public class StdMobBuilderTest {
	
	static MobileBuilder mob;

	@Test
	public void testGetName() {
		mob = new MobileBuilder();
		mob.name("m");
		StdMob newMob = mob.complete();
		assertTrue("get name should return m", "m".equals(newMob.getName()));
	}
	
	@Test
	public void testGetNameUpper() {
		mob = new MobileBuilder();
		mob.name("M");
		StdMob newMob = mob.complete();
		assertTrue("get name should return M", "M".equals(newMob.getName()));
	}
	@Test
	public void testGetNameMix() {
		mob = new MobileBuilder();
		mob.name("meMeE");
		StdMob newMob = mob.complete();
		assertTrue("get name should return meMeE", "meMeE".equals(newMob.getName()));
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNameNull() {
		mob = new MobileBuilder();
		mob.name(null);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNameNums() {
		mob = new MobileBuilder();
		mob.name("jason1234");
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNamesSymbolsUnderscore() {
		mob = new MobileBuilder();
		mob.name("j_ason");
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNamesSymbolsSemiColon() {
		mob = new MobileBuilder();
		mob.name("ja;son");
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNamesSymbolsSingleQuote() {
		mob = new MobileBuilder();
		mob.name("jaso'n");
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNamesSymbolsQuote() {
		mob = new MobileBuilder();
		mob.name("jaso\"n");
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNameBlank() {
		mob = new MobileBuilder();
		mob.name("");
	}


}
