package tests.tutoren.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestsuiteExtendedCE {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Tutor tests for SpaceApes - CE Extension");
		suite.addTest(new JUnit4TestAdapter(tests.tutoren.testcases.ExtendedCETest.class));
		return suite;
	}
	
}