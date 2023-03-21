package tests.tutoren.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import tests.tutoren.testcases.*;

public class TestsuiteMinimal {
	
	public static Test suite() {
		
		TestSuite suite = new TestSuite("Tutor tests for Space Apes - Minimal");
		suite.addTest(new JUnit4TestAdapter(CreateMapTest.class));
		suite.addTest(new JUnit4TestAdapter(ChangeStateTest.class));
		suite.addTest(new JUnit4TestAdapter(KeyboardInputTestMinimal.class));
		suite.addTest(new JUnit4TestAdapter(ProjectileBehaviourTest.class));
		return suite;
	}
}
