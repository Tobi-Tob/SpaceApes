package tests.tutoren.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Scanner;

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
	MovementType projectileMovementType = MovementType.EXPLICIT_EULER; //TODO: zu Aufgabenstellung hinzufügen...
	float radiusPlanet1 = 1f;
	float radiusPlanet2 = 1f;
	int massPlanet1 = 65;
	int massPlanet2 = 65;
	float angleOnPlanetApe1 = 0f;
	float angleOnPlanetApe2 = 0f;

	@Before
	public void setUp() {
		adapter = new AdapterMinimal();
	}
	
	@After
	public void finish() {
		adapter.stopGame();
	}
	
	@Test
	public void testMoveLeft() { // belongs to task: "Affenbewegung"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2);
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
		adapter.stopGame();
	}
	
	@Test
	public void testMoveRight() { // belongs to task: "Affenbewegung"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2);
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
		adapter.stopGame();
	}
	
	@Test
	public void testShootStraight() { // belongs to task: "Schießen entlang einer Geraden"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2);
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
		adapter.stopGame();
	}
	
	@Test
	public void testActivePlayer() { // belongs to task: "Spielzug Logik"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==Launch.GAMEPLAY_STATE);
		
		assertTrue("Ape1 should be able to interact before shooting its projectile at the start of the game!", adapter.isApeInteractionAllowed(0));
		assertTrue("Ape2 should not be able to interact at the start of the game!", !adapter.isApeInteractionAllowed(1));
		
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		Projectile projectileFlying = adapter.getProjectile();
		assertTrue("No Projectile with ID '" + Constants.PROJECTILE_ID + "' in EntityManager after hitting Space-Key!", projectileFlying!=null);
		assertTrue("Ape1 should not be able to interact after shooting its projectile!", !adapter.isApeInteractionAllowed(0));
		assertTrue("Ape2 should not be able to interact after Ape1 shot its projectile which is still flying!", !adapter.isApeInteractionAllowed(1));
		
		Vector2f coordinatesApe2 = adapter.getApeCoordinates(1);
		//System.out.println("coordinatesApe2 = " + coordinatesApe2);
		//System.out.println("coordinates Projectile before move" + adapter.getProjectileCoordinates());
		adapter.setProjectileCoordinates(projectileFlying, coordinatesApe2);
		//System.out.println("coordinates Projectile after move" + adapter.getProjectileCoordinates());
		adapter.runGame(10);
		projectileFlying = adapter.getProjectile();
		assertTrue("There should not be an Entity with ID '" + Constants.PROJECTILE_ID + "' in the EntityManager!", projectileFlying==null);
		assertTrue("Ape1 should not be able to interact after its projectile collided!", !adapter.isApeInteractionAllowed(0));
		assertTrue("Ape2 should be able to interact after the projectile of Ape1 collided with it!", adapter.isApeInteractionAllowed(1));
		
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		assertTrue("Ape2 should not be able to interact after shooting its projectile!", !adapter.isApeInteractionAllowed(0));
		assertTrue("Ape1 should not be able to interact after Ape2 shot its projectile which is still flying!", !adapter.isApeInteractionAllowed(1));
		
		projectileFlying = adapter.getProjectile();
		assertTrue("No Projectile with ID '" + Constants.PROJECTILE_ID + "' in EntityManager!", projectileFlying!=null);
		adapter.setProjectileCoordinates(projectileFlying, coordinatesPlanet1);
		adapter.runGame(10);
		projectileFlying = adapter.getProjectile();
		assertTrue("There should not be an Entity with ID '" + Constants.PROJECTILE_ID + "' in the EntityManager!", projectileFlying==null);
		assertTrue("Ape2 should not be able to interact after its projectile collided!", !adapter.isApeInteractionAllowed(1));
		assertTrue("Ape1 should be able to interact after the projectile of Ape2 collided with Planet2!", adapter.isApeInteractionAllowed(0));
		
		//TODO: teste alle Fälle durch, wie es sich verhält bei Treffer von eigenem Ape, Treffer von eigenem Planet, Treffer von ..., Schuss außerhalb von der Welt, 2x Space
		
		adapter.stopGame();
	}

}
