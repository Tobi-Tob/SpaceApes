package tests.tutoren.suites;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestsuiteAll {
	
	public static Test suite() {
		
		TestSuite suite = new TestSuite("All tutor tests for Space Apes");
		
		suite.addTest(tests.tutoren.suites.TestsuiteMinimal.suite());
		suite.addTest(tests.tutoren.suites.TestsuiteExtended1.suite());
		suite.addTest(tests.tutoren.suites.TestsuiteExtended2.suite());
		suite.addTest(tests.tutoren.suites.TestsuiteExtended3.suite());
		suite.addTest(tests.tutoren.suites.TestsuiteExtendedCE.suite());
		
		return suite;
	}
}
