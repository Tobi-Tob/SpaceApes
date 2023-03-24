package tests.tutoren.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import adapter.AdapterMinimal;

/**
 * Tests if a given map is correctly created.
 */
public class MinimalTest {
	
	AdapterMinimal adapter;
	Vector2f coordinatesPlanet1 = new Vector2f(-4.0f, 0.0f);
	Vector2f coordinatesPlanet2 = new Vector2f(4.0f, 0.0f);
	float radiusPlanet1 = 1f;
	float radiusPlanet2 = 1f;
	int massPlanet1 = 65;
	int massPlanet2 = 65;
	float angleOnPlanetApe1 = 0f;
	float angleOnPlanetApe2 = 0f;
	Vector2f velocityVector = new Vector2f(0,0);
	int timeDelta = 0;
	
	@Before
	public void setUp() {
		adapter = new AdapterMinimal();
	}
	
	@After
	public void finish() {
		adapter.stopGame();
	}
	
	@Test
	public void testNewGame() { // belongs to task: "Wechsel zwischen Gamestates"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		assertTrue("Game is not in main menu state after initialization", adapter.getStateBasedGame().getCurrentStateID()==adapter.getMainMenuStateID());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		adapter.handleKeyPressed(0, Input.KEY_ESCAPE);
		assertTrue("Game is not in main menu state after pressing 'esc' in gameplay state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getMainMenuStateID());
		adapter.stopGame();
	}
	
	@Test
	public final void testPlanetPositionAndValues() { // belongs to task: "Initialisieren einer simplen Spielwelt"
		
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		assertEquals("Incorrect planet count!", 2, adapter.getPlanetCount());		
		assertEquals("Incorrect x-coordinate of Planet1!", -4.0f, adapter.getPlanetCoordinates(0).x, 0.001f);
		assertEquals("Incorrect y-coordinate of Planet1!", 0.0f, adapter.getPlanetCoordinates(0).y, 0.001f);
		assertEquals("Incorrect x-coordinate of Planet2!", 4.0f, adapter.getPlanetCoordinates(1).x, 0.001f);
		assertEquals("Incorrect y-coordinate of Planet2!", 0.0f, adapter.getPlanetCoordinates(1).y, 0.001f);
		assertTrue("Radius of planet1 is not " + radiusPlanet1 + " when initialized with a fixed value!", adapter.getPlanetRadius(0) == radiusPlanet1);
		assertTrue("Radius of planet2 is not " + radiusPlanet2 + " when initialized with a fixed value!", adapter.getPlanetRadius(1) == radiusPlanet2);
		adapter.stopGame();
	}
	
	@Test
	public final void testRandomPlanetRadiusAndMass() { // belongs to task: "Initialisieren einer simplen Spielwelt"
		
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, 0, 0, 0, 0, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		assertTrue("Radius of Planet1 is not greater than " + 0.75f + "!", adapter.getPlanetRadius(0) > 0.75f);
		assertTrue("Radius of Planet1 is not smaller than " + 1.5f + "!", adapter.getPlanetRadius(0) < 1.5f);
		assertTrue("Mass of Planet1 is not greater than " + 0.5f + "!", adapter.getPlanetMass(0) > 0.5f); //prevent division by to small mass
		assertTrue("Mass of Planet1 is not smaller than " + 1000.0f + "!", adapter.getPlanetMass(0) < 1000.0f); //prevent too big masses
		assertTrue("Radius of Planet2 is not greater than " + 0.75f + "!", adapter.getPlanetRadius(1) > 0.75f);
		assertTrue("Radius of Planet2 is not smaller than " + 1.5f + "!", adapter.getPlanetRadius(1) < 1.5f);
		assertTrue("Mass of Planet2 is not greater than " + 0.5f + "!", adapter.getPlanetMass(1) > 0.5f); //prevent division by to small mass
		assertTrue("Mass of Planet2 is not smaller than " + 1000.0f + "!", adapter.getPlanetMass(1) < 1000.0f); //prevent too big masses
		adapter.stopGame();
	}
	
	@Test
	public final void testApePositionAndValues() { // belongs to task: "Platzieren der Affen"
		
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		assertEquals("Incorrect ape count!", 2, adapter.getApeCount());
		assertTrue("There is no ape on Planet1 and no on Planet2!", adapter.hasApe(0));
		assertTrue("There is no ape on Planet2 and no on Planet1!", adapter.hasApe(1));
		assertEquals("Ape1 is not positioned on the surface of Planet1 correctly!", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("Incorrect rotation of Ape1!", adapter.getApeAngleOnPlanet(0) + 90f, adapter.getApeRotation(0), 0.001f);
		assertEquals("Incorrect angle on planet of Ape1 when initialized with a fixed value!", adapter.getApeAngleOnPlanet(0), angleOnPlanetApe1, 0.01f);
		assertEquals("Ape2 is not positioned on the surface of Planet2 correctly!", adapter.getPlanetRadius(1), adapter.getApeDistanceFeetToPlanetCenter(1), 0.001f);
		assertEquals("Incorrect rotation of Ape2!", adapter.getApeAngleOnPlanet(1) + 90f, adapter.getApeRotation(1), 0.001f);
		assertEquals("Incorrect angle on planet of Ape2 when initialized with a fixed value!", adapter.getApeAngleOnPlanet(1), angleOnPlanetApe2, 0.01f);
		adapter.stopGame();
	}
	
	@Test
	public void testApe2Dead() { // belongs to task: "Spielende"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, 0, 180);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
		// we placed the apes opposite to each other on the planets and let them shoot all the time. One ape should be killed after a view rounds
		int maxIterations = 1000; // to prevent an infinite loop if some kind of setup got wrong -> if your test fail try to increase this value!!
		int i = 0;
		while (i < maxIterations && adapter.getNumberOfLivingApes() > 1) {
			i++;
			if (adapter.getProjectile() == null) {
				adapter.handleKeyPressed(20, Input.KEY_SPACE); // since Ape1 shoots first, Ape2 will die first
			} else {
				adapter.runGame(20);
				adapter.runGame(20);
			}
		}
		adapter.runGame(20); // another update needs to be called to change state
		assertTrue("During this test one ape should be killed! The apes stand directly opposite to each other and constantly shoot when its their turn.", !(adapter.getNumberOfLivingApes() > 1));
		assertTrue("Game is not in main menu state after an ape is dead!", adapter.getStateBasedGame().getCurrentStateID()==adapter.getMainMenuStateID());
		adapter.stopGame();
	}
	
	@Test
	public void testApe1Dead() { // belongs to task: "Spielende"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, 0, 180);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
		// we placed the apes opposite to each other on the planets and let them shoot all the time. One ape should be killed after a view rounds
		int maxIterations = 1000; // to prevent an infinite loop if some kind of setup got wrong -> if your test fail try to increase this value!!
		int i = 0;
		while (i < maxIterations && adapter.getNumberOfLivingApes() > 1) {
			i++;
			if (adapter.getProjectile() == null) {
				if (adapter.isApeInteractionAllowed(0)) {
					adapter.changeTurn(); // only shoot with Ape2 to kill Ape1
				} else {
					adapter.handleKeyPressed(20, Input.KEY_SPACE);
				}
			} else {
				adapter.runGame(20);
				adapter.runGame(20);
			}
		}
		adapter.runGame(20); // another update needs to be called to change state
		assertTrue("During this test one ape should be killed! The apes stand directly opposite to each other and constantly shoot when its their turn.", !(adapter.getNumberOfLivingApes() > 1));
		assertTrue("Game is not in main menu state after an ape is dead!", adapter.getStateBasedGame().getCurrentStateID()==adapter.getMainMenuStateID());
		adapter.stopGame();
	}
	
	@Test
	public void testMoveLeft() { // belongs to task: "Affenbewegung"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
		float initAngleOnPlanet = adapter.getApeAngleOnPlanet(0);
		adapter.handleKeyDown(1000, Input.KEY_LEFT);
		float targetAngleOnPlanet = initAngleOnPlanet - (adapter.getApeMovementSpeed() * adapter.getUpdateIntervall() / adapter.getApeDistanceToPlanetCenter(0));
		//System.out.println("initAngleOnPlanet=" + initAngleOnPlanet + "targetAngleOnPlanet=" + targetAngleOnPlanet + "adapter.getApeDistanceToPlanetCenter(0)=" + adapter.getApeDistanceToPlanetCenter(0));
		assertEquals("The distance to the planet center should not change when pressing left arrow", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("The active ape should move left with the set speed when pressing left arrow", targetAngleOnPlanet, adapter.getApeAngleOnPlanet(0), 0.01f);
		assertEquals("Ape1 is not positioned on the surface of Planet1 after moving right", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("Rotation of Ape1 is incorrect after moving left", adapter.getApeAngleOnPlanet(0) + 90f, adapter.getApeRotation(0), 0.001f);
		adapter.stopGame();
	}
	
	@Test
	public void testMoveRight() { // belongs to task: "Affenbewegung"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
		float originalAngleOnPlanet = adapter.getApeAngleOnPlanet(0);
		adapter.handleKeyDown(1000, Input.KEY_RIGHT);
		float targetAngleOnPlanet = originalAngleOnPlanet + (adapter.getApeMovementSpeed() * adapter.getUpdateIntervall() / adapter.getApeDistanceToPlanetCenter(0));
		//System.out.println("initAngleOnPlanet=" + originalAngleOnPlanet + "targetAngleOnPlanet=" + targetAngleOnPlanet + "adapter.getApeDistanceToPlanetCenter(0)=" + adapter.getApeDistanceToPlanetCenter(0));
		assertEquals("The distance to the planet center should not change when pressing right arrow", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("The active ape should move right with the set speed when pressing right arrow", targetAngleOnPlanet, adapter.getApeAngleOnPlanet(0), 0.01f);
		assertEquals("Ape1 is not positioned on the surface of Planet1 after moving right", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("Rotation of Ape1 is incorrect after moving right", adapter.getApeAngleOnPlanet(0) + 90f, adapter.getApeRotation(0), 0.001f);
		adapter.stopGame();
	}
	
	@Test
	public void testShootStraight() { // belongs to task: "Schießen entlang einer Geraden"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, 45, 135);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
		// We test if a projectile flies along the desired linear trajectory for different test cases.
		// Test 1
		int updateTimeDelta = 20;
		int numberOfSteps = 21;
		float desiredX = 0.14f;
		float desiredY = 4.14f;
		
		// Do 'numberOfSteps' steps of the linear movement
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
		desiredX = -0.14f;
		desiredY = 4.14f;
		
		// Do 'numberOfSteps' steps of the linear movement
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
		
		// Test 3
		numberOfSteps = 10;
		desiredX = -4f;
		desiredY = 3.67f;
		adapter.setApeAngleOnPlanet(0, 90);
		
		// Do 'numberOfSteps' steps of the linear movement
		adapter.handleKeyPressed(20, Input.KEY_SPACE);
		for (int i = 0; i < numberOfSteps; i++) {
			adapter.runGame(updateTimeDelta);
			//System.out.println("projectile Coordinates = " + adapter.getProjectileCoordinates() + " at iteration i = " + i);
		}
		assertTrue("No Projectile in EntityManager but should be!", adapter.getProjectileCoordinates()!=null);
		assertEquals("The projectile does not has the expected x-coordinate after applying multiple steps of the linear movement!", desiredX, adapter.getProjectileCoordinates().x, 0.01f);
		assertEquals("The projectile does not has the expected y-coordinate after applying multiple steps of the linear movement!", desiredY, adapter.getProjectileCoordinates().y, 0.2f);

		adapter.stopGame();
	}
	
	@Test
	public void testActivePlayer() { // belongs to task: "Spielzug Logik"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, 0, 180);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
		assertTrue("Ape1 should be able to interact before shooting its projectile at the start of the game!", adapter.isApeInteractionAllowed(0));
		assertTrue("Ape2 should not be able to interact at the start of the game!", !adapter.isApeInteractionAllowed(1));
		
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		assertTrue("No Projectile in EntityManager after hitting Space-Key with Ape1!", adapter.getProjectileCoordinates()!=null);
		assertTrue("Ape1 should not be able to interact after shooting its projectile!", !adapter.isApeInteractionAllowed(0));
		assertTrue("Ape2 should not be able to interact after Ape1 shot its projectile which is still flying!", !adapter.isApeInteractionAllowed(1));
		
		while (adapter.getProjectileCoordinates()!=null) { // to initiate a changeTurn and remove the projectile from the entity manager
			adapter.runGame(20);
		}
		
		assertTrue("There should not be a Projectile in the EntityManager after it collided with Ape2!", adapter.getProjectileCoordinates()==null);
		assertTrue("Ape1 should not be able to interact after its projectile collided!", !adapter.isApeInteractionAllowed(0));
		assertTrue("Ape2 should be able to interact after the projectile of Ape1 collided with it!", adapter.isApeInteractionAllowed(1));
		
		adapter.setApeAngleOnPlanet(1, 90);
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		assertTrue("No Projectile in EntityManager after hitting Space-Key with Ape2!", adapter.getProjectileCoordinates()!=null);
		assertTrue("Ape2 should not be able to interact after shooting its projectile!", !adapter.isApeInteractionAllowed(1));
		assertTrue("Ape1 should not be able to interact after Ape2 shot its projectile which is still flying!", !adapter.isApeInteractionAllowed(0));
		
		while (adapter.getProjectileCoordinates()!=null) { // to initiate a changeTurn and remove the projectile from the entity manager
			adapter.runGame(20);
		}
		
		assertTrue("There should not be a Projectile in the EntityManager after it went out to far in space!", adapter.getProjectileCoordinates()==null);
		assertTrue("Ape2 should not be able to interact after its projectile went out to far in space!", !adapter.isApeInteractionAllowed(1));
		assertTrue("Ape1 should be able to interact after the projectile of Ape2 went out to far in space!", adapter.isApeInteractionAllowed(0));
		
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		assertTrue("No Projectile in EntityManager after hitting Space-Key with Ape1!", adapter.getProjectileCoordinates()!=null);
		assertTrue("Ape1 should not be able to interact after shooting its projectile!", !adapter.isApeInteractionAllowed(0));
		assertTrue("Ape2 should not be able to interact after Ape1 shot its projectile which is still flying!", !adapter.isApeInteractionAllowed(1));
		
		while (adapter.getProjectileCoordinates()!=null) { // to initiate a changeTurn and remove the projectile from the entity manager
			adapter.runGame(20);
		}
		
		assertTrue("There should not be a Projectile in the EntityManager after it collided with Planet2!", adapter.getProjectileCoordinates()==null);
		assertTrue("Ape1 should not be able to interact after its projectile collided with Planet2!", !adapter.isApeInteractionAllowed(0));
		assertTrue("Ape2 should be able to interact after the projectile of Ape1 collided with Planet2!", adapter.isApeInteractionAllowed(1));
		
		adapter.stopGame();
	}
	
	@Test
	public void testCollisionWithPlanet() { // belongs to task: "Kollision mit Planeten"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, 0, 90);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
		// Test 1
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		assertTrue("No Projectile in EntityManager after hitting Space-Key with Ape1!", adapter.getProjectileCoordinates()!=null);
		
		// run shot until it collided or exceeded position of Planet2 (should have hit planet by then)
		while (adapter.getProjectileCoordinates()!=null && adapter.getProjectileCoordinates().x < adapter.getPlanetCoordinates(1).x) {
			adapter.runGame(20);
		}
	
		assertTrue("Projectile has not collided with Planet2 as expected!", adapter.getProjectileCoordinates()==null);
		
		while (adapter.getProjectileCoordinates()!=null) { // to initiate a changeTurn and remove the projectile from the entity manager
			adapter.runGame(20);
		}
		
		// Test 2
		adapter.setApeAngleOnPlanet(0, 90);
		adapter.setApeAngleOnPlanet(1, 180);
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		assertTrue("No Projectile in EntityManager after hitting Space-Key with Ape2!", adapter.getProjectileCoordinates()!=null);
		
		// run shot until it collided or exceeded position of Planet1 (should have hit planet by then)
		while (adapter.getProjectileCoordinates()!=null && adapter.getProjectileCoordinates().x > adapter.getPlanetCoordinates(0).x) {
			adapter.runGame(20);
		}
	
		assertTrue("Projectile has not collided with Planet1 as expected!", adapter.getProjectileCoordinates()==null);
		
		adapter.stopGame();
	}
	
	@Test
	public void testCollisionWithApe() { // belongs to task: "Kollision mit Planeten"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, 0, 180);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
		// Test 1
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		assertTrue("No Projectile in EntityManager after hitting Space-Key with Ape1!", adapter.getProjectileCoordinates()!=null);
		
		// run shot until it collided or exceeded x position of Ape2 (should have hit planet by then)
		while (adapter.getProjectileCoordinates()!=null && adapter.getProjectileCoordinates().x < adapter.getApeCoordinates(1).x) {
			adapter.runGame(20);
		}
	
		assertTrue("Projectile has not collided with Ape2 as expected!", adapter.getProjectileCoordinates()==null);
		
		while (adapter.getProjectileCoordinates()!=null) { // to initiate a changeTurn and remove the projectile from the entity manager
			adapter.runGame(20);
		}
		
		// Test 2
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		assertTrue("No Projectile in EntityManager after hitting Space-Key with Ape2!", adapter.getProjectileCoordinates()!=null);
		
		// run shot until it collided or exceeded x position of Ape1 (should have hit planet by then)
		while (adapter.getProjectileCoordinates()!=null && adapter.getProjectileCoordinates().x > adapter.getApeCoordinates(0).x) {
			adapter.runGame(20);
		}
	
		assertTrue("Projectile has not collided with Ape1 as expected!", adapter.getProjectileCoordinates()==null);
		
		adapter.stopGame();
	}

	
	@Test
	public void testApeDamage() { // belongs to task: "Schadensberechnung"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, 0, 180);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
		int healthApe1 = adapter.getApeHealth(0);
		assertTrue("The health of Ape1 should be 100 at the start of the game!", healthApe1==100);
		int healthApe2 = adapter.getApeHealth(1);
		assertTrue("The health of Ape2 should be 100 at the start of the game!", healthApe2==100);
		
		// Test1
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		
		// run shot until it collided with Ape2
		while (adapter.getProjectileCoordinates()!=null) {
			adapter.runGame(20);
		}
		
		assertTrue("The health of Ape2 should be lower than 100 after a projectile hit it!", adapter.getApeHealth(1)<100);

		// Test 2
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		
		// run shot until it collided with Ape1
		while (adapter.getProjectileCoordinates()!=null) {
			adapter.runGame(20);
		}
		
		assertTrue("The health of Ap1 should be lower than 100 after a projectile hit it!", adapter.getApeHealth(0)<100);
		
		adapter.stopGame();
	}
	
}
