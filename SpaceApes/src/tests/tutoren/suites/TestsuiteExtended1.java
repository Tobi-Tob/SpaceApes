package tests.tutoren.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestsuiteExtended1 {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Tutor tests for Tanks - Extended 1");
		//suite.addTest(new JUnit4TestAdapter(HighscoreTest.class));
		//suite.addTest(new JUnit4TestAdapter(LoadGameTest.class));
		//suite.addTest(new JUnit4TestAdapter(SaveGameTest.class));
		//suite.addTest(new JUnit4TestAdapter(ParseMapExtended1.class));
		return suite;
	}
	
}
