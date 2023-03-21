package tests.tutoren.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import adapter.AdapterMinimal;
import entities.Projectile;
import factories.ProjectileFactory.MovementType;
import spaceapes.Constants;
import spaceapes.Launch;
import utils.Utils;

public class KeyboardInputTestMinimal {
	
	AdapterMinimal adapter;
	Vector2f coordinatesPlanet1 = new Vector2f(-4.0f, 0.0f);
	Vector2f coordinatesPlanet2 = new Vector2f(4.0f, 0.0f);
	float angleOnPlanetApe1 = 0;
	float angleOnPlanetApe2 = 180;
	MovementType projectileMovementType = MovementType.LINEAR; //TODO: zu Aufgabenstellung hinzufügen...

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
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, projectileMovementType);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		assertTrue("Game is not in main menu state after initialization", adapter.getStateBasedGame().getCurrentStateID()==Launch.MAINMENU_STATE);
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==Launch.GAMEPLAY_STATE);
		adapter.handleKeyPressed(0, Input.KEY_ESCAPE);
		assertTrue("Game is not in main menu or highscore state after pressing 'esc' in gameplay state", adapter.getStateBasedGame().getCurrentStateID()==Launch.MAINMENU_STATE || adapter.getStateBasedGame().getCurrentStateID()==Launch.HIGHSCORE_STATE);
		adapter.handleKeyPressed(0, Input.KEY_ESCAPE);
		adapter.stopGame();
	}
	
	@Test
	public void testMoveLeft() { // belongs to task: "Affenbewegung"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, projectileMovementType);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==Launch.GAMEPLAY_STATE);
		float initAngleOnPlanet = adapter.getApeAngleOnPlanet(0);
		adapter.handleKeyDown(1000, Input.KEY_LEFT);
		float targetAngleOnPlanet = initAngleOnPlanet - (Constants.APE_MOVMENT_SPEED * Launch.UPDATE_INTERVAL / adapter.getApeDistanceToPlanetCenter(0));
		//System.out.println("initAngleOnPlanet=" + initAngleOnPlanet + "targetAngleOnPlanet=" + targetAngleOnPlanet + "adapter.getApeDistanceToPlanetCenter(0)=" + adapter.getApeDistanceToPlanetCenter(0));
		assertEquals("The distance to the planet center should not change when pressing left arrow", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("The active ape should move left with the set speed when pressing left arrow", targetAngleOnPlanet, adapter.getApeAngleOnPlanet(0), 0.01f);
		assertEquals("Ape1 is not positioned on the surface of Planet1 after moving right", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("Rotation of Ape1 is incorrect after moving left", adapter.getApeAngleOnPlanet(0) + 90f, adapter.getApeRotation(0), 0.001f);
	}
	
	@Test
	public void testMoveRight() { // belongs to task: "Affenbewegung"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, projectileMovementType);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==Launch.GAMEPLAY_STATE);
		float originalAngleOnPlanet = adapter.getApeAngleOnPlanet(0);
		adapter.handleKeyDown(1000, Input.KEY_RIGHT);
		float targetAngleOnPlanet = originalAngleOnPlanet + (Constants.APE_MOVMENT_SPEED * Launch.UPDATE_INTERVAL / adapter.getApeDistanceToPlanetCenter(0));
		//System.out.println("initAngleOnPlanet=" + originalAngleOnPlanet + "targetAngleOnPlanet=" + targetAngleOnPlanet + "adapter.getApeDistanceToPlanetCenter(0)=" + adapter.getApeDistanceToPlanetCenter(0));
		assertEquals("The distance to the planet center should not change when pressing right arrow", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("The active ape should move right with the set speed when pressing right arrow", targetAngleOnPlanet, adapter.getApeAngleOnPlanet(0), 0.01f);
		assertEquals("Ape1 is not positioned on the surface of Planet1 after moving right", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("Rotation of Ape1 is incorrect after moving right", adapter.getApeAngleOnPlanet(0) + 90f, adapter.getApeRotation(0), 0.001f);
	}
	
	@Test
	public void testShootStraight() { // belongs to task: "Schießen entlang einer Geraden"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, projectileMovementType);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==Launch.GAMEPLAY_STATE);
		Vector2f originalCoordinatesApe = adapter.getApeCoordinates(0);
		float originalRotationApe = adapter.getApeRotation(0);
		float originalGlobalAngleOnPlanet = adapter.getApeGlobalAngleOfView(0);
		double originalAngleInRad = Math.toRadians(originalGlobalAngleOnPlanet);
		//float throwStrenght = adapter.getThrowStrength(0);
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		Vector2f projectileCoordinates = adapter.getProjectileCoordinates();
		assertTrue("No Projectile with ID '" + Constants.PROJECTILE_ID + "' in EntityManager!", projectileCoordinates!=null);
		
		// the following point is necessary to calculate m and b of the linear Trajectory //TODO: WRONG! Improve this by calculating 2D linear Function
		Vector2f potentialNewPosition = new Vector2f(originalCoordinatesApe.x + 1, (float) (originalCoordinatesApe.y + Math.tan(originalAngleInRad)));
		float m = (originalCoordinatesApe.y - potentialNewPosition.y) / (originalCoordinatesApe.x - potentialNewPosition.x);
		float b = originalCoordinatesApe.y - m * originalCoordinatesApe.x;
		float yDesired = m * projectileCoordinates.x + b;
		assertEquals("The projectile does not follow a linear trajectory", yDesired, projectileCoordinates.y, 0.001f); //TODO: siehe Tablet
		assertEquals("The ape should not move in x direction when space is pressed", originalCoordinatesApe.x, adapter.getApeCoordinates(0).x, 0.001f);
		assertEquals("The ape should not move in y direction when space is pressed", originalCoordinatesApe.y, adapter.getApeCoordinates(0).y, 0.001f);
		assertEquals("Rotation of Ape1 is incorrect after moving right", originalRotationApe, adapter.getApeRotation(0), 0.001f);
	}
	
	@Test
	public void testCollisionWithPlanet() { // belongs to task: "Kollision mit Planeten"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, projectileMovementType);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		Projectile projectileCollisionPlanet1 = adapter.createProjectile(coordinatesPlanet1);
		assertTrue("Es wurde keine Kollision erkannt, obwohl ein Projektil sich innerhalb von Planet1 befindet!", adapter.isCollision(projectileCollisionPlanet1, projectileMovementType));
		Projectile projectileCollisionPlanet2 = adapter.createProjectile(coordinatesPlanet2);
		assertTrue("Es wurde keine Kollision erkannt, obwohl ein Projektil sich innerhalb von Planet2 befindet!", adapter.isCollision(projectileCollisionPlanet2, projectileMovementType));
		Projectile projectileNoCollision = adapter.createProjectile(new Vector2f(0,0));
		assertTrue("Es wurde eine Kollision erkannt, obwohl kein Projektil sich innerhalb eines Planeten befindet!", !adapter.isCollision(projectileNoCollision, projectileMovementType));
	}
	
	@Test
	public void testCollisionWithApe() { // belongs to task: "Kollision mit Planeten"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, projectileMovementType);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		Vector2f coordinatesApe1 = adapter.getApeCoordinates(0);
		Projectile projectileCollisionApe1 = adapter.createProjectile(coordinatesApe1);
		assertTrue("Es wurde keine Kollision erkannt, obwohl ein Projektil sich innerhalb von Ape1 befindet!", adapter.isCollision(projectileCollisionApe1, projectileMovementType));
		Vector2f coordinatesApe2 = adapter.getApeCoordinates(1);
		Projectile projectileCollisionApe2 = adapter.createProjectile(coordinatesApe2);
		assertTrue("Es wurde keine Kollision erkannt, obwohl ein Projektil sich innerhalb von Ape2 befindet!", adapter.isCollision(projectileCollisionApe2, projectileMovementType));
		Projectile projectileNoCollision = adapter.createProjectile(new Vector2f(0,0));
		assertTrue("Es wurde eine Kollision erkannt, obwohl kein Projektil sich innerhalb eines Apes befindet!", !adapter.isCollision(projectileNoCollision, projectileMovementType));
	}
	
	
//	
//	@Test
//	public void testTankShot() {
//		adapter.initializeGame();
//		adapter.loadMapFromFile(new File(map));
//		assertTrue("A correct map was detected as incorrect", adapter.isCorrectMap());
//		
//		adapter.handleKeyPressN();
//		assertEquals("Your map should not change when starting a new game", stringRepresentation, adapter.getStringRepresentationOfMap());
//		assertEquals("No shot entities should be present in the map", 0, adapter.getShotCount());
//		
//		adapter.handleKeyPressK();
//		assertEquals("Shot entitiy count should be 1", 1, adapter.getShotCount());
//	}
	
}
