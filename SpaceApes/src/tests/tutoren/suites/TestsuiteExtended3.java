package tests.tutoren.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import tests.tutoren.testcases.Extended3Test;

public class TestsuiteExtended3 {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Tutor tests for SpaceApes - Extended 3");
		suite.addTest(new JUnit4TestAdapter(Extended3Test.class));
		return suite;
	}
	
}
