package tests.tutoren.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

import adapter.AdapterMinimal;
import eea.engine.entity.StateBasedEntityManager;
import spaceapes.Constants;
import spaceapes.SpaceApes;

/**
 * Tests if a given map is correctly created.
 */
public class CreateMapTest {
	
	AdapterMinimal adapter;
	Vector2f coordinatesPlanet1 = new Vector2f(-4.0f, 0.0f);
	Vector2f coordinatesPlanet2 = new Vector2f(4.0f, 0.0f);
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
	public void testCreateMap() { // belongs to task: "Initialisieren einer simplen Spielwelt"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.stopGame();
	}

	@Test
	public final void testMapEntities() { // belongs to task: "Initialisieren einer simplen Spielwelt"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		
		assertEquals("Incorrect planet count", 2, adapter.getPlanetCount());
		assertEquals("Incorrect ape count", 2, adapter.getApeCount());
		
		assertTrue("StateBasedEntityManager doesnt contain Planet1", StateBasedEntityManager.getInstance().hasEntity(SpaceApes.GAMEPLAY_STATE, "Planet1"));
		assertTrue("StateBasedEntityManager doesnt contain Planet2", StateBasedEntityManager.getInstance().hasEntity(SpaceApes.GAMEPLAY_STATE, "Planet2"));
		assertTrue("StateBasedEntityManager doesnt contain Ape1", StateBasedEntityManager.getInstance().hasEntity(SpaceApes.GAMEPLAY_STATE, "Ape1"));
		assertTrue("StateBasedEntityManager doesnt contain Ape2", StateBasedEntityManager.getInstance().hasEntity(SpaceApes.GAMEPLAY_STATE, "Ape2"));
		
		adapter.stopGame();
	}
	
	
	
	@Test
	public final void testPlanetPositionAndValues() { // belongs to task: "Initialisieren einer simplen Spielwelt"
		
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		
		assertEquals("Incorrect x-coordinate of Planet1", -4.0f, adapter.getPlanetCoordinates(0).x, 0.001f);
		assertEquals("Incorrect y-coordinate of Planet1", 0.0f, adapter.getPlanetCoordinates(0).y, 0.001f);
		assertTrue("Radius of Planet1 is not greater than " + Constants.MINIMUM_RADIUS_PLAYER_PLANET, adapter.getPlanetRadius(0) > Constants.MINIMUM_RADIUS_PLAYER_PLANET);
		assertTrue("Radius of Planet1 is not smaller than " + Constants.MAXIMUM_RADIUS_PLAYER_PLANET, adapter.getPlanetRadius(0) < Constants.MAXIMUM_RADIUS_PLAYER_PLANET);
		assertTrue("Mass of Planet1 is not greater than " + 0.5f, adapter.getPlanetMass(0) > 0.5f); //prevent division by to small mass
		assertTrue("Mass of Planet1 is not smaller than " + 1000.0f, adapter.getPlanetMass(0) < 1000.0f); //prevent too big masses
		
		assertEquals("Incorrect x-coordinate of Planet2", 4.0f, adapter.getPlanetCoordinates(1).x, 0.001f);
		assertEquals("Incorrect y-coordinate of Planet2", 0.0f, adapter.getPlanetCoordinates(1).y, 0.001f);
		assertTrue("Radius of Planet2 is not greater than " + Constants.MINIMUM_RADIUS_PLAYER_PLANET, adapter.getPlanetRadius(1) > Constants.MINIMUM_RADIUS_PLAYER_PLANET);
		assertTrue("Radius of Planet2 is not smaller than " + Constants.MAXIMUM_RADIUS_PLAYER_PLANET, adapter.getPlanetRadius(1) < Constants.MAXIMUM_RADIUS_PLAYER_PLANET);
		assertTrue("Mass of Planet2 is not greater than " + 0.5f, adapter.getPlanetMass(1) > 0.5f); //prevent division by to small mass
		assertTrue("Mass of Planet2 is not smaller than " + 1000.0f, adapter.getPlanetMass(1) < 1000.0f); //prevent too big masses
		
		adapter.stopGame();
	}
	
	@Test
	public final void testApePositionAndValues() { // belongs to task: "Platzieren der Affen"
		
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		
		assertEquals("Ape1 is not positioned on the surface of Planet1 correctly", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("Incorrect rotation of Ape1", adapter.getApeAngleOnPlanet(0) + 90f, adapter.getApeRotation(0), 0.001f);
		
		adapter.stopGame();
	}
	
}
