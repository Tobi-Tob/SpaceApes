package tests.tutoren.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import tests.tutoren.testcases.Extended2Test;

public class TestsuiteExtended2 {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Tutor tests for SpaceApes - Extended 2");
		suite.addTest(new JUnit4TestAdapter(Extended2Test.class));
		return suite;
	}
	
}
