package tests.tutoren.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;

import adapter.AdapterMinimal;
import spaceapes.Constants;
import spaceapes.Launch;

public class KeyboardInputTestMinimal {
	
	AdapterMinimal adapter;

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
		adapter.createMap();
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
		adapter.createMap();
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		float initAngleOnPlanet = adapter.getApeAngleOnPlanet(0);
		adapter.handleKeyDown(1000, Input.KEY_LEFT);
		float targetAngleOnPlanet = initAngleOnPlanet - (Constants.APE_MOVMENT_SPEED * Launch.UPDATE_INTERVAL / adapter.getApeDistanceToPlanetCenter(0));
		System.out.println("initAngleOnPlanet=" + initAngleOnPlanet + "targetAngleOnPlanet=" + targetAngleOnPlanet + "adapter.getApeDistanceToPlanetCenter(0)=" + adapter.getApeDistanceToPlanetCenter(0));
		assertEquals("The distance to the planet center should not change when pressing left arrow", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("The active ape should move left with the set speed when pressing left arrow", targetAngleOnPlanet, adapter.getApeAngleOnPlanet(0), 0.01f);
		assertEquals("Ape1 is not positioned on the surface of Planet1 after moving right", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("Ape1 is incorrect after moving left", adapter.getApeAngleOnPlanet(0) + 90f, adapter.getApeRotation(0), 0.001f);
	}
	
	@Test
	public void testMoveRight() { // belongs to task: "Affenbewegung"
		adapter.initializeGame();
		adapter.createMap();
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		float initAngleOnPlanet = adapter.getApeAngleOnPlanet(0);
		adapter.handleKeyDown(1000, Input.KEY_LEFT);
		float targetAngleOnPlanet = initAngleOnPlanet + (Constants.APE_MOVMENT_SPEED * Launch.UPDATE_INTERVAL / adapter.getApeDistanceToPlanetCenter(0));
		System.out.println("initAngleOnPlanet=" + initAngleOnPlanet + "targetAngleOnPlanet=" + targetAngleOnPlanet + "adapter.getApeDistanceToPlanetCenter(0)=" + adapter.getApeDistanceToPlanetCenter(0));
		assertEquals("The distance to the planet center should not change when pressing right arrow", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("The active ape should move left with the set speed when pressing right arrow", targetAngleOnPlanet, adapter.getApeAngleOnPlanet(0), 0.01f);
		assertEquals("Ape1 is not positioned on the surface of Planet1 after moving right", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("Ape1 is incorrect after moving right", adapter.getApeAngleOnPlanet(0) + 90f, adapter.getApeRotation(0), 0.001f);
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
