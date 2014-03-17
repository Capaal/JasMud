package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EffectManagerTest.class, LocationTest.class, DamageTest.class,
		SQLInterfaceTest.class, StdMobBuilderTest.class })
public class AllTests {

}
