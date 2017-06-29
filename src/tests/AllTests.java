package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GetHoldableFromStringTest.class, LocationTest.class, StdMobBuilderTest.class, ThrowTest.class, UnwieldTest.class, WieldTest.class })
public class AllTests {

}
