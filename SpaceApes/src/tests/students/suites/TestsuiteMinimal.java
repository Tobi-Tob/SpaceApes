package tests.students.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestsuiteMinimal {
	
	public static Test suite() {
		
		TestSuite suite = new TestSuite("Student tests for Space Apes - Minimal");
		suite.addTest(new JUnit4TestAdapter(tests.students.testcases.MinimalTest.class));
		return suite;
	}
}
