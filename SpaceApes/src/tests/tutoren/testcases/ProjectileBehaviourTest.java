package tests.tutoren.testcases;

import static org.junit.Assert.assertTrue;

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

public class ProjectileBehaviourTest {
	
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
	public void testCollisionWithPlanet() { // belongs to task: "Kollision mit Planeten"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==Launch.GAMEPLAY_STATE);
		
		Projectile projectileCollisionPlanet1 = adapter.createProjectile(coordinatesPlanet1, velocityVector);
		assertTrue("Es wurde keine Kollision erkannt, obwohl ein Projektil sich innerhalb von Planet1 befindet!", adapter.isCollision(projectileCollisionPlanet1, projectileMovementType, timeDelta));
		Projectile projectileCollisionPlanet2 = adapter.createProjectile(coordinatesPlanet2, velocityVector);
		assertTrue("Es wurde keine Kollision erkannt, obwohl ein Projektil sich innerhalb von Planet2 befindet!", adapter.isCollision(projectileCollisionPlanet2, projectileMovementType, timeDelta));
		Projectile projectileNoCollision = adapter.createProjectile(new Vector2f(0,0), velocityVector);
		assertTrue("Es wurde eine Kollision erkannt, obwohl kein Projektil sich innerhalb eines Planeten befindet!", !adapter.isCollision(projectileNoCollision, projectileMovementType, timeDelta));
		
		adapter.stopGame();
	}
	
	@Test
	public void testCollisionWithApe() { // belongs to task: "Kollision mit Planeten"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==Launch.GAMEPLAY_STATE);
		
		Vector2f coordinatesApe1 = adapter.getApeCoordinates(0);
		Projectile projectileCollisionApe1 = adapter.createProjectile(coordinatesApe1, velocityVector);
		assertTrue("Es wurde keine Kollision erkannt, obwohl ein Projektil sich innerhalb von Ape1 befindet!", adapter.isCollision(projectileCollisionApe1, projectileMovementType, timeDelta));
		Vector2f coordinatesApe2 = adapter.getApeCoordinates(1);
		Projectile projectileCollisionApe2 = adapter.createProjectile(coordinatesApe2, velocityVector);
		assertTrue("Es wurde keine Kollision erkannt, obwohl ein Projektil sich innerhalb von Ape2 befindet!", adapter.isCollision(projectileCollisionApe2, projectileMovementType, timeDelta));
		Projectile projectileNoCollision = adapter.createProjectile(new Vector2f(0,0), velocityVector);
		assertTrue("Es wurde eine Kollision erkannt, obwohl kein Projektil sich innerhalb eines Apes befindet!", !adapter.isCollision(projectileNoCollision, projectileMovementType, timeDelta));
		
		adapter.stopGame();
	}

	
	@Test
	public void testApeDamage() { // belongs to task: "Schadensberechnung"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==Launch.GAMEPLAY_STATE);
		
		int healthApe1 = adapter.getApeHealth(0);
		assertTrue("The health of Ape1 should be 100 at the start of the game!", healthApe1==100);
		int healthApe2 = adapter.getApeHealth(1);
		assertTrue("The health of Ape2 should be 100 at the start of the game!", healthApe2==100);
		
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		Projectile projectileFlying = adapter.getProjectile();
		assertTrue("No Projectile with ID '" + Constants.PROJECTILE_ID + "' in EntityManager after hitting Space-Key!", projectileFlying!=null);
		Vector2f coordinatesApe2 = adapter.getApeCoordinates(1);
		adapter.setProjectileCoordinates(projectileFlying, coordinatesApe2);
		adapter.runGame(10);
		assertTrue("The health of Ape2 should be lower than 100 after a projectile hit it!", adapter.getApeHealth(1)<100);
		
		//TODO: evtl. abfrage des maximalen Schadens eines Projektils und dann test, ob health nach direktem Treffer geringer...
		
		adapter.stopGame();
	}
	
}
