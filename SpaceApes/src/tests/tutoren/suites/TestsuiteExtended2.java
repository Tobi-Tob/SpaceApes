package tests.tutoren.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestsuiteExtended2 {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Tutor tests for Tanks - Extended 2");
		//suite.addTest(new JUnit4TestAdapter(ParseMapExtended2.class));
		//suite.addTest(new JUnit4TestAdapter(TimeLimitTest.class));
		//suite.addTest(new JUnit4TestAdapter(LimitedAmmoTest.class));
		return suite;
	}
	
}
