package tests.tutoren.testcases;

import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import adapter.AdapterExtended1;
import entities.Projectile;
import factories.ProjectileFactory.MovementType;
import spaceapes.Constants;
import spaceapes.Launch;
import utils.Utils;

public class ProjectileSettingsTest {
	
	AdapterExtended1 adapter;
	Vector2f coordinatesPlanet1 = new Vector2f(-4.0f, 0.0f);
	Vector2f coordinatesPlanet2 = new Vector2f(4.0f, 0.0f);
	MovementType projectileMovementType = MovementType.LINEAR; //TODO: zu Aufgabenstellung hinzufügen...

	@Before
	public void setUp() {
		adapter = new AdapterExtended1();
	}
	
	@After
	public void finish() {
		adapter.stopGame();
	}
	
	@Test
	public void testShootingAngle() { // belongs to task: "Schussparameter"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, projectileMovementType);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==Launch.GAMEPLAY_STATE);
		
		// At first we test if only valid argument are accepted
		
		assertThrows(IllegalArgumentException.class, () -> {adapter.setShootingAngleOfApe(0, 91);}, "Expected an IllegalArgumentException when a shooting angle > 90° is passed for Ape1!");
		assertThrows(IllegalArgumentException.class, () -> {adapter.setShootingAngleOfApe(0, -91);}, "Expected an IllegalArgumentException when a shooting angle < -90° is passed for Ape1!");
		assertThrows(IllegalArgumentException.class, () -> {adapter.setShootingAngleOfApe(1, 91);}, "Expected an IllegalArgumentException when a shooting angle > 90° is passed for Ape2!");
		assertThrows(IllegalArgumentException.class, () -> {adapter.setShootingAngleOfApe(1, -91);}, "Expected an IllegalArgumentException when a shooting angle < -90° is passed for Ape2!");
				
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(0, 90);}, "Expected no Exception when a shooting angle of 90° is passed for Ape1!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(0, 45);}, "Expected no Exception when a shooting angle of 45° is passed for Ape1!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(0, 0);}, "Expected no Exception when a shooting angle of 0° is passed for Ape1!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(0, -45);}, "Expected no Exception when a shooting angle of -45° is passed for Ape1!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(0, -90);}, "Expected no Exception when a shooting angle of -90° is passed for Ape1!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(1, 90);}, "Expected no Exception when a shooting angle of 90° is passed for Ape2!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(1, 45);}, "Expected no Exception when a shooting angle of 45° is passed for Ape2!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(1, 0);}, "Expected no Exception when a shooting angle of 0° is passed for Ape2!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(1, -45);}, "Expected no Exception when a shooting angle of -45° is passed for Ape2!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(1, -90);}, "Expected no Exception when a shooting angle of -90° is passed for Ape2!");
		
		// Next we test if a projectile flies along the desired trajectory. The tests are run through with different angles and for both players
		
		int player = 0;
		int testedAngle = 0;
		adapter.setShootingAngleOfApe(player, testedAngle);
		int timeDelta = 50;
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
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
		testedAngle = 45;
		adapter.setShootingAngleOfApe(player, testedAngle);
		timeDelta = 50;
		scalingFactor = timeDelta * 1e-3d; // dt in Sekunden
		angleInGrad = adapter.getApeGlobalAngleOfView(player);
		angleInRad = Math.toRadians(angleInGrad);
		initVelocity = adapter.getThrowStrength(player);
		velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		assertTrue("Projectile collided during the, please run the test again.", !adapter.isCollision(projectileFlying, MovementType.LINEAR, timeDelta)); // Do one step of the linear movement
		desiredX = positionOfProjectileLaunch.x + (float) (Math.cos(angleInRad) * initVelocity * scalingFactor);
		desiredY = positionOfProjectileLaunch.y + (float) (Math.sin(angleInRad) * initVelocity * scalingFactor);
		newCoordinates = projectileFlying.getCoordinates();
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
		testedAngle = 90;
		adapter.setShootingAngleOfApe(player, testedAngle);
		timeDelta = 50;
		scalingFactor = timeDelta * 1e-3d; // dt in Sekunden
		angleInGrad = adapter.getApeGlobalAngleOfView(player);
		angleInRad = Math.toRadians(angleInGrad);
		initVelocity = adapter.getThrowStrength(player);
		velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		assertTrue("Projectile collided during the, please run the test again.", !adapter.isCollision(projectileFlying, MovementType.LINEAR, timeDelta)); // Do one step of the linear movement
		desiredX = positionOfProjectileLaunch.x + (float) (Math.cos(angleInRad) * initVelocity * scalingFactor);
		desiredY = positionOfProjectileLaunch.y + (float) (Math.sin(angleInRad) * initVelocity * scalingFactor);
		newCoordinates = projectileFlying.getCoordinates();
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
		testedAngle = -45;
		adapter.setShootingAngleOfApe(player, testedAngle);
		timeDelta = 50;
		scalingFactor = timeDelta * 1e-3d; // dt in Sekunden
		angleInGrad = adapter.getApeGlobalAngleOfView(player);
		angleInRad = Math.toRadians(angleInGrad);
		initVelocity = adapter.getThrowStrength(player);
		velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		assertTrue("Projectile collided during the, please run the test again.", !adapter.isCollision(projectileFlying, MovementType.LINEAR, timeDelta)); // Do one step of the linear movement
		desiredX = positionOfProjectileLaunch.x + (float) (Math.cos(angleInRad) * initVelocity * scalingFactor);
		desiredY = positionOfProjectileLaunch.y + (float) (Math.sin(angleInRad) * initVelocity * scalingFactor);
		newCoordinates = projectileFlying.getCoordinates();
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
		testedAngle = -90;
		adapter.setShootingAngleOfApe(player, testedAngle);
		timeDelta = 50;
		scalingFactor = timeDelta * 1e-3d; // dt in Sekunden
		angleInGrad = adapter.getApeGlobalAngleOfView(player);
		angleInRad = Math.toRadians(angleInGrad);
		initVelocity = adapter.getThrowStrength(player);
		velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		assertTrue("Projectile collided during the, please run the test again.", !adapter.isCollision(projectileFlying, MovementType.LINEAR, timeDelta)); // Do one step of the linear movement
		desiredX = positionOfProjectileLaunch.x + (float) (Math.cos(angleInRad) * initVelocity * scalingFactor);
		desiredY = positionOfProjectileLaunch.y + (float) (Math.sin(angleInRad) * initVelocity * scalingFactor);
		newCoordinates = projectileFlying.getCoordinates();
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
		player = 1;
		testedAngle = 90;
		adapter.setShootingAngleOfApe(player, testedAngle);
		timeDelta = 50;
		scalingFactor = timeDelta * 1e-3d; // dt in Sekunden
		angleInGrad = adapter.getApeGlobalAngleOfView(0);
		angleInRad = Math.toRadians(angleInGrad);
		initVelocity = adapter.getThrowStrength(player);
		velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		assertTrue("Projectile collided during the, please run the test again.", !adapter.isCollision(projectileFlying, MovementType.LINEAR, timeDelta)); // Do one step of the linear movement
		desiredX = positionOfProjectileLaunch.x + (float) (Math.cos(angleInRad) * initVelocity * scalingFactor);
		desiredY = positionOfProjectileLaunch.y + (float) (Math.sin(angleInRad) * initVelocity * scalingFactor);
		newCoordinates = projectileFlying.getCoordinates();
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
		testedAngle = 45;
		adapter.setShootingAngleOfApe(player, testedAngle);
		timeDelta = 50;
		scalingFactor = timeDelta * 1e-3d; // dt in Sekunden
		angleInGrad = adapter.getApeGlobalAngleOfView(0);
		angleInRad = Math.toRadians(angleInGrad);
		initVelocity = adapter.getThrowStrength(player);
		velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		assertTrue("Projectile collided during the, please run the test again.", !adapter.isCollision(projectileFlying, MovementType.LINEAR, timeDelta)); // Do one step of the linear movement
		desiredX = positionOfProjectileLaunch.x + (float) (Math.cos(angleInRad) * initVelocity * scalingFactor);
		desiredY = positionOfProjectileLaunch.y + (float) (Math.sin(angleInRad) * initVelocity * scalingFactor);
		newCoordinates = projectileFlying.getCoordinates();
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
		testedAngle = 0;
		adapter.setShootingAngleOfApe(player, testedAngle);
		timeDelta = 50;
		scalingFactor = timeDelta * 1e-3d; // dt in Sekunden
		angleInGrad = adapter.getApeGlobalAngleOfView(0);
		angleInRad = Math.toRadians(angleInGrad);
		initVelocity = adapter.getThrowStrength(player);
		velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		assertTrue("Projectile collided during the, please run the test again.", !adapter.isCollision(projectileFlying, MovementType.LINEAR, timeDelta)); // Do one step of the linear movement
		desiredX = positionOfProjectileLaunch.x + (float) (Math.cos(angleInRad) * initVelocity * scalingFactor);
		desiredY = positionOfProjectileLaunch.y + (float) (Math.sin(angleInRad) * initVelocity * scalingFactor);
		newCoordinates = projectileFlying.getCoordinates();
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
		testedAngle = -45;
		adapter.setShootingAngleOfApe(player, testedAngle);
		timeDelta = 50;
		scalingFactor = timeDelta * 1e-3d; // dt in Sekunden
		angleInGrad = adapter.getApeGlobalAngleOfView(0);
		angleInRad = Math.toRadians(angleInGrad);
		initVelocity = adapter.getThrowStrength(player);
		velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		assertTrue("Projectile collided during the, please run the test again.", !adapter.isCollision(projectileFlying, MovementType.LINEAR, timeDelta)); // Do one step of the linear movement
		desiredX = positionOfProjectileLaunch.x + (float) (Math.cos(angleInRad) * initVelocity * scalingFactor);
		desiredY = positionOfProjectileLaunch.y + (float) (Math.sin(angleInRad) * initVelocity * scalingFactor);
		newCoordinates = projectileFlying.getCoordinates();
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
		testedAngle = -90;
		adapter.setShootingAngleOfApe(player, testedAngle);
		timeDelta = 50;
		scalingFactor = timeDelta * 1e-3d; // dt in Sekunden
		angleInGrad = adapter.getApeGlobalAngleOfView(0);
		angleInRad = Math.toRadians(angleInGrad);
		initVelocity = adapter.getThrowStrength(player);
		velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		assertTrue("Projectile collided during the, please run the test again.", !adapter.isCollision(projectileFlying, MovementType.LINEAR, timeDelta)); // Do one step of the linear movement
		desiredX = positionOfProjectileLaunch.x + (float) (Math.cos(angleInRad) * initVelocity * scalingFactor);
		desiredY = positionOfProjectileLaunch.y + (float) (Math.sin(angleInRad) * initVelocity * scalingFactor);
		newCoordinates = projectileFlying.getCoordinates();
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
		// This could be an alternativ way to check the shooting angle... -> still not working tho
//		adapter.handleKeyPressed(0, Input.KEY_SPACE);
//		Projectile projectileFlying = adapter.getProjectile();
//		assertTrue("No Projectile with ID '" + Constants.PROJECTILE_ID + "' in EntityManager after hitting Space-Key!", projectileFlying!=null);
//		Vector2f projectileCoordinates = projectileFlying.getCoordinates();
//		float desiredX = projectileCoordinates.x + (float) (Math.cos(angleInRad) * initVelocity * scalingFactor);
//		float desiredY = projectileCoordinates.y + (float) (Math.sin(angleInRad) * initVelocity * scalingFactor);
//		adapter.runGame(timeDelta);
//		Vector2f newCoordinates = adapter.getProjectileCoordinates();
//		assertTrue("No Projectile with ID '" + Constants.PROJECTILE_ID + "' in EntityManager after hitting Space-Key!", newCoordinates!=null);
//		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other x-coordinates!", newCoordinates.x, desiredX, 0.01f);
//		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other y-coordinates!", newCoordinates.y, desiredY, 0.01f);
	}
	
	@Test
	public void testShootingPower() { // belongs to task: "Schussparameter"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, projectileMovementType);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==Launch.GAMEPLAY_STATE);
		
		// At first we test if only valid argument are accepted
		
		assertThrows(IllegalArgumentException.class, () -> {adapter.setShootingPowerOfApe(0, 0);}, "Expected an IllegalArgumentException when a shooting power < 1 is passed for Ape1!");
		assertThrows(IllegalArgumentException.class, () -> {adapter.setShootingPowerOfApe(0, 8);}, "Expected an IllegalArgumentException when a shooting power > 7 is passed for Ape1!");
		assertThrows(IllegalArgumentException.class, () -> {adapter.setShootingPowerOfApe(1, 0);}, "Expected an IllegalArgumentException when a shooting power < 1 is passed for Ape2!");
		assertThrows(IllegalArgumentException.class, () -> {adapter.setShootingPowerOfApe(1, 8);}, "Expected an IllegalArgumentException when a shooting power > 7 is passed for Ape2!");
				
		assertDoesNotThrow(() -> {adapter.setShootingPowerOfApe(0, 1);}, "Expected no Exception when a shooting angle of 90° is passed for Ape1!");
		assertDoesNotThrow(() -> {adapter.setShootingPowerOfApe(0, 5);}, "Expected no Exception when a shooting angle of 45° is passed for Ape1!");
		assertDoesNotThrow(() -> {adapter.setShootingPowerOfApe(0, 7);}, "Expected no Exception when a shooting angle of 0° is passed for Ape1!");
		assertDoesNotThrow(() -> {adapter.setShootingPowerOfApe(1, 1);}, "Expected no Exception when a shooting angle of 90° is passed for Ape2!");
		assertDoesNotThrow(() -> {adapter.setShootingPowerOfApe(1, 5);}, "Expected no Exception when a shooting angle of 45° is passed for Ape2!");
		assertDoesNotThrow(() -> {adapter.setShootingPowerOfApe(1, 7);}, "Expected no Exception when a shooting angle of 0° is passed for Ape2!");
		
		// Next we test if a projectile flies as fast as expected. The tests are run through with different shooting powers and for both players
		
		int player = 0;
		int testedPower = 1;
		adapter.setShootingPowerOfApe(player, testedPower);
		int timeDelta = 50;
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
		assertEquals("With the shooting angle of " + testedPower + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		assertEquals("With the shooting angle of " + testedPower + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
		testedPower = 5;
		adapter.setShootingPowerOfApe(player, testedPower);
		timeDelta = 50;
		scalingFactor = timeDelta * 1e-3d; // dt in Sekunden
		angleInGrad = adapter.getApeGlobalAngleOfView(player);
		angleInRad = Math.toRadians(angleInGrad);
		initVelocity = adapter.getThrowStrength(player);
		velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		assertTrue("Projectile collided during the, please run the test again.", !adapter.isCollision(projectileFlying, MovementType.LINEAR, timeDelta)); // Do one step of the linear movement
		desiredX = positionOfProjectileLaunch.x + (float) (Math.cos(angleInRad) * initVelocity * scalingFactor);
		desiredY = positionOfProjectileLaunch.y + (float) (Math.sin(angleInRad) * initVelocity * scalingFactor);
		newCoordinates = projectileFlying.getCoordinates();
		assertEquals("With the shooting angle of " + testedPower + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		assertEquals("With the shooting angle of " + testedPower + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
		testedPower = 7;
		adapter.setShootingPowerOfApe(player, testedPower);
		timeDelta = 50;
		scalingFactor = timeDelta * 1e-3d; // dt in Sekunden
		angleInGrad = adapter.getApeGlobalAngleOfView(player);
		angleInRad = Math.toRadians(angleInGrad);
		initVelocity = adapter.getThrowStrength(player);
		velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		assertTrue("Projectile collided during the, please run the test again.", !adapter.isCollision(projectileFlying, MovementType.LINEAR, timeDelta)); // Do one step of the linear movement
		desiredX = positionOfProjectileLaunch.x + (float) (Math.cos(angleInRad) * initVelocity * scalingFactor);
		desiredY = positionOfProjectileLaunch.y + (float) (Math.sin(angleInRad) * initVelocity * scalingFactor);
		newCoordinates = projectileFlying.getCoordinates();
		assertEquals("With the shooting angle of " + testedPower + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		assertEquals("With the shooting angle of " + testedPower + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
		player = 1;
		testedPower = 1;
		adapter.setShootingPowerOfApe(player, testedPower);
		timeDelta = 50;
		scalingFactor = timeDelta * 1e-3d; // dt in Sekunden
		angleInGrad = adapter.getApeGlobalAngleOfView(player);
		angleInRad = Math.toRadians(angleInGrad);
		initVelocity = adapter.getThrowStrength(player);
		velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		assertTrue("Projectile collided during the, please run the test again.", !adapter.isCollision(projectileFlying, MovementType.LINEAR, timeDelta)); // Do one step of the linear movement
		desiredX = positionOfProjectileLaunch.x + (float) (Math.cos(angleInRad) * initVelocity * scalingFactor);
		desiredY = positionOfProjectileLaunch.y + (float) (Math.sin(angleInRad) * initVelocity * scalingFactor);
		newCoordinates = projectileFlying.getCoordinates();
		assertEquals("With the shooting angle of " + testedPower + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		assertEquals("With the shooting angle of " + testedPower + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
		testedPower = 5;
		adapter.setShootingPowerOfApe(player, testedPower);
		timeDelta = 50;
		scalingFactor = timeDelta * 1e-3d; // dt in Sekunden
		angleInGrad = adapter.getApeGlobalAngleOfView(player);
		angleInRad = Math.toRadians(angleInGrad);
		initVelocity = adapter.getThrowStrength(player);
		velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		assertTrue("Projectile collided during the, please run the test again.", !adapter.isCollision(projectileFlying, MovementType.LINEAR, timeDelta)); // Do one step of the linear movement
		desiredX = positionOfProjectileLaunch.x + (float) (Math.cos(angleInRad) * initVelocity * scalingFactor);
		desiredY = positionOfProjectileLaunch.y + (float) (Math.sin(angleInRad) * initVelocity * scalingFactor);
		newCoordinates = projectileFlying.getCoordinates();
		assertEquals("With the shooting angle of " + testedPower + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		assertEquals("With the shooting angle of " + testedPower + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
		testedPower = 7;
		adapter.setShootingPowerOfApe(player, testedPower);
		timeDelta = 50;
		scalingFactor = timeDelta * 1e-3d; // dt in Sekunden
		angleInGrad = adapter.getApeGlobalAngleOfView(player);
		angleInRad = Math.toRadians(angleInGrad);
		initVelocity = adapter.getThrowStrength(player);
		velocityVec = Utils.toCartesianCoordinates(initVelocity, (float) angleInGrad);
		positionOfProjectileLaunch = new Vector2f(adapter.getApeCoordinates(player)).add(
				Utils.toCartesianCoordinates(adapter.getApeRadiusInWorldUnits(player), adapter.getApeAngleOnPlanet(player)));
		projectileFlying = adapter.createProjectile(positionOfProjectileLaunch, velocityVec);
		assertTrue("Projectile collided during the, please run the test again.", !adapter.isCollision(projectileFlying, MovementType.LINEAR, timeDelta)); // Do one step of the linear movement
		desiredX = positionOfProjectileLaunch.x + (float) (Math.cos(angleInRad) * initVelocity * scalingFactor);
		desiredY = positionOfProjectileLaunch.y + (float) (Math.sin(angleInRad) * initVelocity * scalingFactor);
		newCoordinates = projectileFlying.getCoordinates();
		assertEquals("With the shooting angle of " + testedPower + "° the projectile should have other x-coordinates! Tested Ape" + (player+1), desiredX, newCoordinates.x, 0.01f);
		assertEquals("With the shooting angle of " + testedPower + "° the projectile should have other y-coordinates! Tested Ape" + (player+1), desiredY, newCoordinates.y, 0.01f);
		
	}

}
