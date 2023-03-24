package tests.tutoren.testcases;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import adapter.AdapterExtended3;

public class Extended3Test {
	
	AdapterExtended3 adapter;
	Vector2f coordinatesPlanet1 = new Vector2f(-4.0f, 0.0f);
	Vector2f coordinatesPlanet2 = new Vector2f(4.0f, 0.0f);
	int projectileMovementType = 1; // explicit euler used
	float radiusPlanet1 = 0.75f;
	float radiusPlanet2 = 0.75f;
	int massPlanet1 = 65;
	int massPlanet2 = 65;
	float angleOnPlanetApe1 = 0f;
	float angleOnPlanetApe2 = 0f;
	boolean createNonPlayerPlanets = true;
	boolean antiPlanetAndBlackHole = true;
	float gravitation = 0.25f;
	
	@Before
	public void setUp() {
		adapter = new AdapterExtended3();
	}
	
	@After
	public void finish() {
		adapter.stopGame();
	}
	
	@Test
	public final void testMainMenuMusic() { // belongs to task: "Musik"
		
		adapter.initializeGame();
		assertTrue("No music file is active in main menu state!", adapter.getMainMenuMusic() != null);
		adapter.stopGame();
	}
	
	@Test
	public final void testItemSpawning() { // belongs to task: "Item Spawning"
		
		adapter.initializeGame();
		adapter.createMap(null, null, 0, 0, 0, 0, createNonPlayerPlanets, projectileMovementType, 999, 999, gravitation);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
		// change turn 6 times:
		for (int i = 0; i < 6; i++) {
			adapter.changeTurn();
		}
		
		assertTrue("No Item has spawned after 6 rounds!", adapter.getItemCount() > 0);
		//System.out.println("Items: " +  adapter.getItemCount());
		assertTrue("Items are spawned inside a planet!", adapter.getMinimalDistanceItemToPlanet() > 0.75f);
		
		adapter.stopGame();
	}
	
	@Test
	public final void testBuyingProjectiles() { // belongs to task: "Kauf von Projektilen"
		
		adapter.initializeGame();
		adapter.createMap(null, null, 0, 0, 0, 0, createNonPlayerPlanets, projectileMovementType, 999, 999, gravitation);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
		adapter.selectExpensiveProjectile();
		int price = adapter.getSelectedProjectilePrice();
		assertTrue("The selected projectile is expected to cost more than zero!", price > 0);
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		assertTrue("No projectile should be fired after hitting Space-Key, when the selected projectile is too expensive!", adapter.getProjectile()==null);
		
		adapter.setApeCoins(0, price);
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		assertTrue("A projectile should be fired after hitting Space-Key, when the selected projectile costs less than the amount of coins of the ape!", adapter.getProjectile()!=null);
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		assertTrue("No projectile should be fired after hitting Space-Key, when the selected projectile is too expensive!", adapter.getProjectile()!=null);
		
		adapter.stopGame();
	}
	
	@Test
	public final void testBlackHole() { // belongs to task: "Schwarzes Loch und Anti Planet"
		
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, createNonPlayerPlanets, projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2, gravitation, antiPlanetAndBlackHole);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
		assertTrue("The planetcount is not >2 when non-player-planets are enabled!", adapter.getPlanetCount() > 2);
		assertTrue("The distance between the spawned planets is less than 4, which is the minimal allowed distance)!", adapter.getMinimalDistancePlanets() >= 4f);
		assertTrue("No black hole is created, but it is expected to!", adapter.getBlackHoleCount() > 0);
		//System.out.println("blackHole mass: " + adapter.getBlackHoleMass() + " planet1 mass: " + adapter.getPlanetMass(0));
		assertTrue("The mass of the created black hole is not big enough in relation to the other planets! At least double mass is required!", adapter.getBlackHoleMass() > adapter.getPlanetMass(0));
		
		adapter.stopGame();
	}
	
	@Test
	public final void testAntiPlanet() { // belongs to task: "Schwarzes Loch und Anti Planet"
		
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, createNonPlayerPlanets, projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2, gravitation, antiPlanetAndBlackHole);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==adapter.getGameplayStateID());
		
		assertTrue("The planetcount is not >2 when non-player-planets are enabled!", adapter.getPlanetCount() > 2);
		assertTrue("The distance between the spawned planets is less than 4, which is the minimal allowed distance)!", adapter.getMinimalDistancePlanets() >= 4f);
		assertTrue("No anti planet is created, but it is expected to!", adapter.getAntiPlanetCount() > 0);
		System.out.println("anti mass: " + adapter.getAntiPlanetMass());
		assertTrue("The mass of the created anti planet is not less than zero!", adapter.getAntiPlanetMass() < 0);
		
		adapter.stopGame();
	}

}
