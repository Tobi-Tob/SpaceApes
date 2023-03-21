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

public class ChangeStateTest {
	
	AdapterMinimal adapter;
	Vector2f coordinatesPlanet1 = new Vector2f(-4.0f, 0.0f);
	Vector2f coordinatesPlanet2 = new Vector2f(4.0f, 0.0f);
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
	public void testApe1Dead() { // belongs to task: "Spielende"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, projectileMovementType);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==Launch.GAMEPLAY_STATE);
		
		Vector2f coordinatesApe1 = adapter.getApeCoordinates(0);
		while(adapter.getNumberOfLivingApes() > 1) {
			adapter.handleKeyPressed(10, Input.KEY_SPACE);
			Projectile projectileFlying = adapter.getProjectile();
			assertTrue("No Projectile with ID '" + Constants.PROJECTILE_ID + "' in EntityManager after hitting Space-Key!", projectileFlying!=null);
			adapter.setProjectileCoordinates(projectileFlying, coordinatesApe1);
			adapter.runGame(10);
			adapter.runGame(0); // Game has to be run two times otherwise one update call is missing due to the change of the State
		}
		assertTrue("Game is not in main menu or highscore state after Ape1 is dead!", adapter.getStateBasedGame().getCurrentStateID()==Launch.MAINMENU_STATE || adapter.getStateBasedGame().getCurrentStateID()==Launch.HIGHSCORE_STATE);
	}
	
	@Test
	public void testApe2Dead() { // belongs to task: "Spielende"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, projectileMovementType);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==Launch.GAMEPLAY_STATE);
		
		Vector2f coordinatesApe2 = adapter.getApeCoordinates(1);
		while(adapter.getNumberOfLivingApes() > 1) {
			adapter.handleKeyPressed(10, Input.KEY_SPACE);
			Projectile projectileFlying = adapter.getProjectile();
			assertTrue("No Projectile with ID '" + Constants.PROJECTILE_ID + "' in EntityManager after hitting Space-Key!", projectileFlying!=null);
			adapter.setProjectileCoordinates(projectileFlying, coordinatesApe2);
			adapter.runGame(10);
			adapter.runGame(0); // Game has to be run two times otherwise one update call is missing due to the change of the State
		}
		assertTrue("Game is not in main menu or highscore state after Ape2 is dead!", adapter.getStateBasedGame().getCurrentStateID()==Launch.MAINMENU_STATE || adapter.getStateBasedGame().getCurrentStateID()==Launch.HIGHSCORE_STATE);
	}

}
