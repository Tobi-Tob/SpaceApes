package tests.tutoren.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestsuiteMinimal {
	
	public static Test suite() {
		
		TestSuite suite = new TestSuite("Tutor tests for Space Apes - Minimal");
		suite.addTest(new JUnit4TestAdapter(tests.tutoren.testcases.MinimalTest.class));
		return suite;
	}
}
