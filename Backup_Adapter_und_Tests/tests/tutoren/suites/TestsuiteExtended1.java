package tests.tutoren.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestsuiteExtended1 {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Tutor tests for SpaceApes - Extended 1");
		suite.addTest(new JUnit4TestAdapter(tests.tutoren.testcases.Extended1Test.class));
		return suite;
	}
	
}
