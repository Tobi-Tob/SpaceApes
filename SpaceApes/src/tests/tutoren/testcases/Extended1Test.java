package tests.tutoren.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import adapter.AdapterExtended1;
import entities.Projectile;
import spaceapes.SpaceApes;
import utils.Utils;

public class Extended1Test {
	
	AdapterExtended1 adapter;
	Vector2f coordinatesPlanet1 = new Vector2f(-4.0f, 0.0f);
	Vector2f coordinatesPlanet2 = new Vector2f(4.0f, 0.0f);
	int projectileMovementType = 1;
	float radiusPlanet1 = 1.5f;
	float radiusPlanet2 = 1.5f;
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
	public void testShootingAngle1() { // belongs to task: "Bahnberechnung mittels explizitem Euler Verfahren"
		adapter.initializeGame();
		float otherAngleOnPlanetApe1 = -25f;
		float otherAngleOnPlanetApe2 = 180f;
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, projectileMovementType, otherAngleOnPlanetApe1, otherAngleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.GAMEPLAY_STATE);
		
		// We test if a projectile flies approx. along the desired explicit euler trajectory for different test cases.
		int player = 0;
		int timeDelta = 20;
		int numberOfSteps = 147;
		float desiredX = 6.9f;
		float desiredY = 0f;
		
		adapter.setShootingPowerOfApe(player, 4f);
		double angleInGrad = adapter.getApeGlobalAngleOfView(player);
		float initVelocity = adapter.getThrowStrength(player);
		Vector2f velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		Vector2f positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		Projectile projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		
		// Do 'numberOfSteps' steps of the explicit euler movement
		boolean collision = false;
		for (int i = 0; i < numberOfSteps; i++) {
			collision = adapter.doExplicitEulerStep(projectileFlying, timeDelta);
			//System.out.println("projectile Coordinates = " + projectileFlying.getCoordinates() + " at iteration i = " + i);
			if (collision) {
				break;
			}
		}
		assertTrue("Projectile collided during the test, this should not happen!", !collision);
		assertEquals("The projectile does not has the expected x-coordinate after applying multiple steps of the explicit euler movement!", desiredX, projectileFlying.getCoordinates().x, 1f);
		assertEquals("The projectile does not has the expected y-coordinate after applying multiple steps of the explicit euler movement!", desiredY, projectileFlying.getCoordinates().y, 1f);

		adapter.stopGame();
	}
	
	@Test
	public void testShootingAngle2() { // belongs to task: "Bahnberechnung mittels explizitem Euler Verfahren"
		adapter.initializeGame();
		float otherAngleOnPlanetApe1 = 90f;
		float otherAngleOnPlanetApe2 = 0f;
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, projectileMovementType, otherAngleOnPlanetApe1, otherAngleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.GAMEPLAY_STATE);
		
		// We test if a projectile flies approx. along the desired explicit euler trajectory for different test cases.
		int player = 0;
		int timeDelta = 20;
		int numberOfSteps = 60;
		float desiredX = -3.856f;
		float desiredY = 3.355f;
		
		adapter.setShootingPowerOfApe(player, 2.5f);
		double angleInGrad = adapter.getApeGlobalAngleOfView(player);
		float initVelocity = adapter.getThrowStrength(player);
		Vector2f velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		Vector2f positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		Projectile projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		
		// Do 'numberOfSteps' steps of the explicit euler movement
		boolean collision = false;
		for (int i = 0; i < numberOfSteps; i++) {
			collision = adapter.doExplicitEulerStep(projectileFlying, timeDelta);
			//System.out.println("projectile Coordinates = " + projectileFlying.getCoordinates() + " at iteration i = " + i);
			if (collision) {
				break;
			}
		}
		assertTrue("Projectile collided during the test, this should not happen!", !collision);
		assertEquals("The projectile does not has the expected x-coordinate after applying multiple steps of the explicit euler movement!", desiredX, projectileFlying.getCoordinates().x, 0.5f);
		assertEquals("The projectile does not has the expected y-coordinate after applying multiple steps of the explicit euler movement!", desiredY, projectileFlying.getCoordinates().y, 0.5f);

		adapter.stopGame();
	}
	
}
