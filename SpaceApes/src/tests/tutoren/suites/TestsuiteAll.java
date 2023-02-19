package tests.tutoren.suites;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestsuiteAll {
	
	public static Test suite() {
		
		TestSuite suite = new TestSuite("All tutor tests for Space Apes");
		
		suite.addTest(TestsuiteMinimal.suite());
		suite.addTest(TestsuiteExtended1.suite());
		suite.addTest(TestsuiteExtended2.suite());
		suite.addTest(TestsuiteExtended3.suite());
		
		return suite;
	}
}
