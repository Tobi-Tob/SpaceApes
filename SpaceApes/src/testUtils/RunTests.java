package testUtils;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.tutoren.suites.TestsuiteAll;

// use this when JUnit5 is used
@RunWith(Suite.class)
@SuiteClasses(TestsuiteAll.class)
public class RunTests {

}


// use this whe JUnit4 is used
//@RunWith(Suite.class)
//@Suite.SuiteClasses({
//   TestsuiteAll.class
//})
//public class RunTests {
//    // leave this class empty
//}
