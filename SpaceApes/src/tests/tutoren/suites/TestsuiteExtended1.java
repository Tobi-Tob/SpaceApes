package tests.tutoren.suites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import tests.tutoren.testcases.ProjectileBehaviourExt1Test;
import tests.tutoren.testcases.ProjectileSettingsTest;

public class TestsuiteExtended1 {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Tutor tests for Tanks - Extended 1");
		suite.addTest(new JUnit4TestAdapter(ProjectileSettingsTest.class));
		suite.addTest(new JUnit4TestAdapter(ProjectileBehaviourExt1Test.class));
		return suite;
	}
	
}
