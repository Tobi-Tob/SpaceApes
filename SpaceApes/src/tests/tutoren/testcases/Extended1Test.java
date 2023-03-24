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
	float gravitation = 0.25f;

	@Before
	public void setUp() {
		adapter = new AdapterExtended1();
	}
	
	@After
	public void finish() {
		adapter.stopGame();
	}
	
	@Test
	public void testExplicitEuler() { // belongs to task: "Bahnberechnung mittels explizitem Euler Verfahren"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, projectileMovementType, -25f, 180, gravitation);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
		
		// We test if a projectile flies along the desired explicit euler trajectory
		int updateTimeDelta = 20;
		int numberOfSteps = 72;
		float desiredX = 6.58f;
		float desiredY = -0.03f;
		float shootingPowerApe = 4f;
				
		adapter.setShootingPowerOfApe(0, shootingPowerApe);
		// Do 'numberOfSteps' steps of the explicit euler movement
		adapter.handleKeyPressed(20, Input.KEY_SPACE);
		for (int i = 0; i < numberOfSteps; i++) {
			adapter.runGame(updateTimeDelta);
			//System.out.println("projectile Coordinates = " + adapter.getProjectileCoordinates() + " at iteration i = " + i);
		}
		assertTrue("No Projectile in EntityManager but should be!", adapter.getProjectileCoordinates()!=null);
		assertEquals("The projectile does not has the expected x-coordinate after applying multiple steps of the linear movement!", desiredX, adapter.getProjectileCoordinates().x, 1f);
		assertEquals("The projectile does not has the expected y-coordinate after applying multiple steps of the linear movement!", desiredY, adapter.getProjectileCoordinates().y, 1f);
		
		while (adapter.getProjectileCoordinates()!=null) { // to initiate a changeTurn and remove the projectile from the entity manager
			adapter.runGame(updateTimeDelta);
		}
		// change turn back to player1
		adapter.setShootingPowerOfApe(0, 7);
		adapter.handleKeyPressed(20, Input.KEY_SPACE);
		while (adapter.getProjectileCoordinates()!=null) {
			adapter.runGame(updateTimeDelta);
		}
		
		// Test 2
		numberOfSteps = 29;
		desiredX = -3.86f;
		desiredY = 3.35f;
		shootingPowerApe = 2.5f;
		
		adapter.setApeAngleOnPlanet(0, 90);
		adapter.setShootingPowerOfApe(0, shootingPowerApe);
		// Do 'numberOfSteps' steps of the linear movement
		adapter.handleKeyPressed(20, Input.KEY_SPACE);
		for (int i = 0; i < numberOfSteps; i++) {
			adapter.runGame(updateTimeDelta);
			//System.out.println("projectile Coordinates = " + adapter.getProjectileCoordinates() + " at iteration i = " + i);
		}
		assertTrue("No Projectile in EntityManager but should be!", adapter.getProjectileCoordinates()!=null);
		assertEquals("The projectile does not has the expected x-coordinate after applying multiple steps of the linear movement!", desiredX, adapter.getProjectileCoordinates().x, 0.5f);
		assertEquals("The projectile does not has the expected y-coordinate after applying multiple steps of the linear movement!", desiredY, adapter.getProjectileCoordinates().y, 0.5f);
		
		adapter.stopGame();
	}
	
	@Test
	public void testShootingAngle() { // belongs to task: "Schussparameter"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, projectileMovementType, 0, -90, gravitation);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
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
		
		// Next we test if a projectile flies along the desired trajectory. The tests are run with different angles and for both players
		
		// Test 1
		int updateTimeDelta = 20;
		int numberOfSteps = 16;
		float desiredX = 0.05f;
		float desiredY = 2.23f;
		int testedAngle = 45;
		
		adapter.setShootingAngleOfApe(0, testedAngle);
		adapter.setShootingPowerOfApe(0, 5f);
		adapter.handleKeyPressed(20, Input.KEY_SPACE);
		for (int i = 0; i < numberOfSteps; i++) {
			adapter.runGame(updateTimeDelta);
			//System.out.println("projectile Coordinates = " + adapter.getProjectileCoordinates() + " at iteration i = " + i);
		}
		assertTrue("No Projectile in EntityManager but should be!", adapter.getProjectileCoordinates()!=null);
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other x-coordinates! Explicit Euler Movement of the porjectile is used.", desiredX, adapter.getProjectileCoordinates().x, 0.5f);
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other y-coordinates! Explicit Euler Movement of the porjectile is used.", desiredY, adapter.getProjectileCoordinates().y, 0.5f);
		
		while (adapter.getProjectileCoordinates()!=null) { // to initiate a changeTurn and remove the projectile from the entity manager
			adapter.runGame(updateTimeDelta);
		}
		
		// Test 2
		numberOfSteps = 16;
		desiredX = 0.88f;
		desiredY = -1.45f;
		testedAngle = -90;
		
		adapter.setShootingAngleOfApe(1, testedAngle);
		adapter.setShootingPowerOfApe(1, 5f);
		adapter.handleKeyPressed(20, Input.KEY_SPACE);
		for (int i = 0; i < numberOfSteps; i++) {
			adapter.runGame(updateTimeDelta);
			//System.out.println("projectile Coordinates = " + adapter.getProjectileCoordinates() + " at iteration i = " + i);
		}
		assertTrue("No Projectile in EntityManager but should be!", adapter.getProjectileCoordinates()!=null);
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other x-coordinates! Explicit Euler Movement of the porjectile is used.", desiredX, adapter.getProjectileCoordinates().x, 0.5f);
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other y-coordinates! Explicit Euler Movement of the porjectile is used.", desiredY, adapter.getProjectileCoordinates().y, 0.5f);
		
		while (adapter.getProjectileCoordinates()!=null) { // to initiate a changeTurn and remove the projectile from the entity manager
			adapter.runGame(updateTimeDelta);
		}
		
		// Test 3
		numberOfSteps = 10;
		desiredX = 0f;
		desiredY = 0f;
		testedAngle = 0;
		
		adapter.setShootingAngleOfApe(0, testedAngle);
		adapter.setShootingPowerOfApe(0, 5f);
		adapter.handleKeyPressed(20, Input.KEY_SPACE);
		for (int i = 0; i < numberOfSteps; i++) {
			adapter.runGame(updateTimeDelta);
			//System.out.println("projectile Coordinates = " + adapter.getProjectileCoordinates() + " at iteration i = " + i);
		}
		assertTrue("No Projectile in EntityManager but should be!", adapter.getProjectileCoordinates()!=null);
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other x-coordinates! Explicit Euler Movement of the porjectile is used.", desiredX, adapter.getProjectileCoordinates().x, 0.5f);
		assertEquals("With the shooting angle of " + testedAngle + "° the projectile should have other y-coordinates! Explicit Euler Movement of the porjectile is used.", desiredY, adapter.getProjectileCoordinates().y, 0.5f);
		
		adapter.stopGame();
	}
	
	@Test
	public void testShootingPower() { // belongs to task: "Schussparameter"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2, gravitation);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
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
		
		// Next we test if a projectile flies as fast as expected. The tests are run with different shooting powers and for both players
		
		// Test 1
		int updateTimeDelta = 20;
		int numberOfSteps = 21;
		float desiredX = 1.96f;
		float desiredY = 4.04f;
		int testedPower = 7;
		
		adapter.setShootingAngleOfApe(0, 45f);
		adapter.setShootingPowerOfApe(0, testedPower);
		adapter.handleKeyPressed(20, Input.KEY_SPACE);
		for (int i = 0; i < numberOfSteps; i++) {
			adapter.runGame(updateTimeDelta);
			//System.out.println("projectile Coordinates = " + adapter.getProjectileCoordinates() + " at iteration i = " + i);
		}
		assertTrue("No Projectile in EntityManager but should be!", adapter.getProjectileCoordinates()!=null);
		assertEquals("With the shooting power of " + testedPower + " the projectile should have other x-coordinates! Explicit Euler Movement of the porjectile is used.", desiredX, adapter.getProjectileCoordinates().x, 0.5f);
		assertEquals("With the shooting power of " + testedPower + " the projectile should have other y-coordinates! Explicit Euler Movement of the porjectile is used.", desiredY, adapter.getProjectileCoordinates().y, 0.5f);
		
		while (adapter.getProjectileCoordinates()!=null) { // to initiate a changeTurn and remove the projectile from the entity manager
			adapter.runGame(updateTimeDelta);
		}
		
		// Test 2
		numberOfSteps = 21;
		desiredX = 2.97f;
		desiredY = -3.24f;
		testedPower = 3;
		
		adapter.setShootingAngleOfApe(1, 45f);
		adapter.setShootingPowerOfApe(1, testedPower);
		adapter.setApeAngleOnPlanet(1, -135);
		adapter.handleKeyPressed(20, Input.KEY_SPACE);
		for (int i = 0; i < numberOfSteps; i++) {
			adapter.runGame(updateTimeDelta);
			System.out.println("projectile Coordinates = " + adapter.getProjectileCoordinates() + " at iteration i = " + i);
		}
		assertTrue("No Projectile in EntityManager but should be!", adapter.getProjectileCoordinates()!=null);
		assertEquals("With the shooting power of " + testedPower + " the projectile should have other x-coordinates! Explicit Euler Movement of the porjectile is used.", desiredX, adapter.getProjectileCoordinates().x, 0.5f);
		assertEquals("With the shooting power of " + testedPower + " the projectile should have other y-coordinates! Explicit Euler Movement of the porjectile is used.", desiredY, adapter.getProjectileCoordinates().y, 0.5f);
		
		adapter.stopGame();
	}
	
}
