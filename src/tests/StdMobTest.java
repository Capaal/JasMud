package tests;

import static org.junit.Assert.*;
import interfaces.Mobile;

import org.junit.BeforeClass;
import org.junit.Test;

import processes.StdMob;

public class StdMobTest {
	
	static Mobile mob;


	@Test
	public void testGetName() {
		mob = new StdMob.Builder(1, "m").build();
		assertTrue("get name should return m", "m".equals(mob.getName()));
	}
	
	@Test
	public void testGetNameUpper() {
		mob = new StdMob.Builder(1, "M").build();
		assertTrue("get name should return M", "M".equals(mob.getName()));
	}
	@Test
	public void testGetNameMix() {
		mob = new StdMob.Builder(1, "MeEe").build();
		assertTrue("get name should return MeEe", "MeEe".equals(mob.getName()));
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNameNull() {
		mob = new StdMob.Builder(1, null).build();
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNameNums() {
		mob = new StdMob.Builder(1, "jason123").build();
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNamesSymbols() {
		mob = new StdMob.Builder(1, "j_a;s'o\"n").build();
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetNameBlank() {
		mob = new StdMob.Builder(1, "").build();
	}


}
