package tests.students.suites;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestsuiteAll {
	
	public static Test suite() {
		
		TestSuite suite = new TestSuite("All student tests for Space Apes");
		
		suite.addTest(tests.students.suites.TestsuiteMinimal.suite());
		suite.addTest(tests.students.suites.TestsuiteExtended1.suite());
		suite.addTest(tests.students.suites.TestsuiteExtended2.suite());
		suite.addTest(tests.students.suites.TestsuiteExtended3.suite());
		suite.addTest(tests.students.suites.TestsuiteExtendedCE.suite());
		
		return suite;
	}
}
