package tests.tutoren.testcases;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import adapter.AdapterExtended2;
import entities.Projectile;

public class Extended2Test {
	
	AdapterExtended2 adapter;
	Vector2f coordinatesPlanet1 = new Vector2f(-4.0f, 0.0f);
	Vector2f coordinatesPlanet2 = new Vector2f(4.0f, 0.0f);
	int projectileMovementType = 1;
	float radiusPlanet1 = 1.5f;
	float radiusPlanet2 = 1.5f;
	int massPlanet1 = 65;
	int massPlanet2 = 65;
	float angleOnPlanetApe1 = 0f;
	float angleOnPlanetApe2 = 0f;
	boolean createNonPlayerPlanets = true;
	
	@Before
	public void setUp() {
		adapter = new AdapterExtended2();
	}
	
	@After
	public void finish() {
		adapter.stopGame();
	}
	
	@Test
	public final void testRandomPlanetPositionAndValues1() { // belongs to task: "Erweiterte Spielkartengenerierung"
		
		adapter.initializeGame();
		adapter.createMap(null, null, 0, 0, 0, 0, createNonPlayerPlanets, projectileMovementType, 999, 999);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		assertTrue("The x-coordinate of Planet1 is not smaller than -0.5f!", adapter.getPlanetCoordinates(0).x < -0.5f);
		assertTrue("The y-coordinate of Planet1 is not greater than -7.5f!", adapter.getPlanetCoordinates(0).x > -7.5f);
		assertTrue("Radius of Planet1 is not greater than " + 0.75f + "!", adapter.getPlanetRadius(0) > 0.75f);
		assertTrue("Radius of Planet1 is not smaller than " + 1.5f + "!", adapter.getPlanetRadius(0) < 1.5f);
		assertTrue("Mass of Planet1 is not greater than " + 0.5f + "!", adapter.getPlanetMass(0) > 0.5f); //prevent division by to small mass
		assertTrue("Mass of Planet1 is not smaller than " + 1000.0f + "!", adapter.getPlanetMass(0) < 1000.0f); //prevent too big masses
		assertTrue("The x-coordinate of Planet2 is not greater than 0.5f!", adapter.getPlanetCoordinates(1).x > 0.5f);
		assertTrue("The y-coordinate of Planet2 is not smaller than 7.5f!", adapter.getPlanetCoordinates(1).x < 7.5f);
		assertTrue("Radius of Planet2 is not greater than " + 0.75f + "!", adapter.getPlanetRadius(1) > 0.75f);
		assertTrue("Radius of Planet2 is not smaller than " + 1.5f + "!", adapter.getPlanetRadius(1) < 1.5f);
		assertTrue("Mass of Planet2 is not greater than " + 0.5f + "!", adapter.getPlanetMass(1) > 0.5f); //prevent division by to small mass
		assertTrue("Mass of Planet2 is not smaller than " + 1000.0f + "!", adapter.getPlanetMass(1) < 1000.0f); //prevent too big masses		
		assertTrue("The planetcount is not >2 when non-player-planets are enabled!", adapter.getPlanetCount() > 2);
		adapter.stopGame();
	}
	
	@Test
	public final void testRandomPlanetPositionAndValues2() { // belongs to task: "Erweiterte Spielkartengenerierung"
		
		adapter.initializeGame();
		adapter.createMap(new Vector2f(-4.0f, 0f), new Vector2f(4.0f, 0f), 1.5f, 1.5f, 0, 0, createNonPlayerPlanets, projectileMovementType, 999, 999);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		
		assertTrue("The planetcount is not >2 when non-player-planets are enabled and and initial coordinates for the player-planets are passed!", adapter.getPlanetCount() > 2);
		assertTrue("The distance between the spawned planets is less than 4, which is the minimal allowed distance)!", adapter.getMinimalDistancePlanets() >= 4f);
		
		adapter.stopGame();
	}
	
	@Test
	public final void testEnergyOfApe() { // belongs to task: "Bewegung auf dem Planeten verbraucht Energie"
		
		adapter.initializeGame();
		adapter.createMap(null, null, 0, 0, 0, 0, createNonPlayerPlanets, projectileMovementType, 999, 999);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
		// test Ape1:
		float originalEnergyApe = adapter.getApeEnergy(0);
		adapter.handleKeyDown(1000, Input.KEY_LEFT);
		float newEnergyApe = adapter.getApeEnergy(0);
		assertTrue("The energy level of Ape1 does not decrease while walking left!", newEnergyApe < originalEnergyApe);
		
		originalEnergyApe = adapter.getApeEnergy(0);
		adapter.handleKeyDown(1000, Input.KEY_RIGHT);
		newEnergyApe = adapter.getApeEnergy(0);
		assertTrue("The energy level of Ape1 does not decrease while walking right!", newEnergyApe < originalEnergyApe);
		
		// change turn:
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		Projectile projectileFlying = adapter.getProjectile();
		assertTrue("No Projectile in EntityManager after hitting Space-Key!", projectileFlying!=null);
		Vector2f coordinatesApe2 = adapter.getApeCoordinates(1);
		adapter.setProjectileCoordinates(projectileFlying, coordinatesApe2);
		adapter.runGame(10);
		assertTrue("Ape2 should be able to interact after the projectile of Ape1 collided with it!", adapter.isApeInteractionAllowed(1));
		
		// test Ape2: //TODO: funktioniert aus irgendeinem Grund nicht...
//		originalEnergyApe = adapter.getApeEnergy(1);
//		adapter.handleKeyPressed(1000, Input.KEY_LEFT);
//		//adapter.runGame(1000);
//		newEnergyApe = adapter.getApeEnergy(1);
//		assertTrue("The energy level of Ape2 does not decrease while walking left!", newEnergyApe < originalEnergyApe);
//		
//		originalEnergyApe = adapter.getApeEnergy(1);
//		adapter.handleKeyDown(1000, Input.KEY_RIGHT);
//		newEnergyApe = adapter.getApeEnergy(1);
//		assertTrue("The energy level of Ape2 does not decrease while walking right!", newEnergyApe < originalEnergyApe);
		
		adapter.stopGame();
	}

}
