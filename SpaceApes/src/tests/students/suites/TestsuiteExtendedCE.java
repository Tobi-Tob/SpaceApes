package tests.students.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestsuiteExtendedCE {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Student tests for SpaceApes - CE Extension");
		suite.addTest(new JUnit4TestAdapter(tests.students.testcases.ExtendedCETest.class));
		return suite;
	}
	
}