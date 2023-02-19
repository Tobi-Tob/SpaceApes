package testUtils;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.tutoren.suites.TestsuiteAll;

@RunWith(Suite.class)
@SuiteClasses(TestsuiteAll.class)
public class RunTests {

}
