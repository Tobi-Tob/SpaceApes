package tests.tutoren.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import adapter.AdapterExtended1;
import entities.Projectile;
import factories.ProjectileFactory.MovementType;
import spaceapes.Launch;
import utils.Utils;

public class ProjectileBehaviourExt1Test {
	
	AdapterExtended1 adapter;
	Vector2f coordinatesPlanet1 = new Vector2f(-4.0f, 0.0f);
	Vector2f coordinatesPlanet2 = new Vector2f(4.0f, 0.0f);
	MovementType projectileMovementType = MovementType.EXPLICIT_EULER; //TODO: zu Aufgabenstellung hinzufügen...
	float radiusPlanet1 = 1f;
	float radiusPlanet2 = 1f;
	int massPlanet1 = 65;
	int massPlanet2 = 65;
	float angleOnPlanetApe1 = 0f;
	float angleOnPlanetApe2 = 0f;

	@Before
	public void setUp() {
		adapter = new AdapterExtended1();
	}
	
	@After
	public void finish() {
		adapter.stopGame();
	}
	
	@Test
	public void testShootingAngle() { // belongs to task: "Bahnberechnung mittels explizitem Euler Verfahren"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==Launch.GAMEPLAY_STATE);
		
		// We test if a projectile flies approx. along the desired explicit euler trajectory for different test cases.
		
		
		
		int player = 0;
		int timeDelta = 10;
		double scalingFactor = timeDelta * 1e-3d; // dt in Sekunden
		double angleInGrad = adapter.getApeGlobalAngleOfView(player);
		double angleInRad = Math.toRadians(angleInGrad);
		float initVelocity = adapter.getThrowStrength(player);
		Vector2f velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		Vector2f positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		Projectile projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		//System.out.println("projectileFlyingCoordinates = " + projectileFlying.getCoordinates());
		// The follwing assert does not indicate an error of the student. Because of the random placement of the apes on their planets, one cant predict for sure if no collision will occur. This should happen very rarely due to the small timeDelta
		assertTrue("Projectile collided during the, please run the test again.", !adapter.isCollision(projectileFlying, MovementType.LINEAR, timeDelta)); // Do one step of the linear movement
		float desiredX = positionOfProjectileLaunch.x + (float) (Math.cos(angleInRad) * initVelocity * scalingFactor);
		float desiredY = positionOfProjectileLaunch.y + (float) (Math.sin(angleInRad) * initVelocity * scalingFactor);
		Vector2f newCoordinates = projectileFlying.getCoordinates();
		//System.out.println("projectileFlyingCoordinates = " + projectileFlying.getCoordinates());
		//assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		//assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
		adapter.stopGame();
	}
}
