package tests.tutoren.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import adapter.AdapterExtendedCE;

public class ExtendedCETest {
	
	AdapterExtendedCE adapter;
	Vector2f coordinatesPlanet1 = new Vector2f(-4.0f, 0.0f);
	Vector2f coordinatesPlanet2 = new Vector2f(4.0f, 0.0f);
	int projectileMovementType = 1; // explicit euler used
	float radiusPlanet1 = 1.5f;
	float radiusPlanet2 = 1.5f;
	int massPlanet1 = 65;
	int massPlanet2 = 65;
	float angleOnPlanetApe1 = -25f;
	float angleOnPlanetApe2 = 155f;
	float gravitation = 0.25f;
	float atmosphereRadiusFactor = 2f;
	boolean useAirFriction = true;
	
	@Before
	public void setUp() {
		adapter = new AdapterExtendedCE();
	}
	
	@After
	public void finish() {
		adapter.stopGame();
	}
	
	@Test
	public final void testAirFriction() { // belongs to task: "Luftreibung" (CE)
		
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2, gravitation);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
		// We test if a projectile flies along the desired explicit euler trajectory
		adapter.setAtmosphereRadiusFactor(atmosphereRadiusFactor);
		adapter.useAirFriction(useAirFriction);
		
		// Test 1
		int updateTimeDelta = 20;
		int numberOfSteps = 32;
		float desiredX = 0f;
		float desiredY = -1.55f;
		float shootingPowerApe = 4f;
				
		adapter.setShootingPowerOfApe(0, shootingPowerApe);
		// Do 'numberOfSteps' steps of the explicit euler movement with air friction
		adapter.handleKeyPressed(20, Input.KEY_SPACE);
		for (int i = 0; i < numberOfSteps; i++) {
			adapter.runGame(updateTimeDelta);
			//System.out.println("projectile Coordinates = " + adapter.getProjectileCoordinates() + " at iteration i = " + i);
		}
		assertTrue("No Projectile in EntityManager but should be!", adapter.getProjectileCoordinates()!=null);
		assertEquals("The projectile does not has the expected x-coordinate after applying multiple steps of the linear movement!", desiredX, adapter.getProjectileCoordinates().x, 0.5f);
		assertEquals("The projectile does not has the expected y-coordinate after applying multiple steps of the linear movement!", desiredY, adapter.getProjectileCoordinates().y, 0.5f);
		
		while (adapter.getProjectileCoordinates()!=null) { // to initiate a changeTurn and remove the projectile from the entity manager
			adapter.runGame(updateTimeDelta);
		}
		
		// Test 2
		int numberOfStepsFirstLoop = 32;
		int numberOfStepsSecondLoop = 16;
		desiredX = -2.51f;
		desiredY = 2.51f;
		shootingPowerApe = 7f;
		
		adapter.setShootingPowerOfApe(1, shootingPowerApe);
		// Do 'numberOfSteps' steps of the explicit euler movement with air friction
		adapter.handleKeyPressed(20, Input.KEY_SPACE);
		for (int i = 0; i < numberOfStepsFirstLoop; i++) {
			adapter.runGame(updateTimeDelta);
			//System.out.println("projectile Coordinates first loop = " + adapter.getProjectileCoordinates() + " at iteration i = " + i);
		}
		assertTrue("No Projectile in EntityManager but should be!", adapter.getProjectileCoordinates()!=null);
		assertEquals("The projectile does not has the expected x-coordinate after applying multiple steps of the linear movement!", desiredX, adapter.getProjectileCoordinates().x, 0.5f);
		assertEquals("The projectile does not has the expected y-coordinate after applying multiple steps of the linear movement!", desiredY, adapter.getProjectileCoordinates().y, 0.5f);
		// continue the game
		for (int i = 0; i < numberOfStepsSecondLoop; i++) {
			adapter.runGame(updateTimeDelta);
			//System.out.println("projectile Coordinates second loop = " + adapter.getProjectileCoordinates() + " at iteration i = " + i);
		}
		assertTrue("No Projectile should be in the EntityManager by now!", adapter.getProjectileCoordinates()==null);
		
		adapter.stopGame();
	}
	
}
