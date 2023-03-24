package tests.tutoren.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import tests.tutoren.testcases.Extended3Test;
import tests.tutoren.testcases.ExtendedCETest;

public class TestsuiteExtendedCE {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Tutor tests for SpaceApes - CE Extension");
		suite.addTest(new JUnit4TestAdapter(ExtendedCETest.class));
		return suite;
	}
	
}