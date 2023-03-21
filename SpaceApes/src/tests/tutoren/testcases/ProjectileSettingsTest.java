package tests.tutoren.testcases;

//import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import adapter.AdapterExtended1;
import entities.Projectile;
import factories.ProjectileFactory.MovementType;
import spaceapes.Constants;
import spaceapes.Launch;

public class ProjectileSettingsTest {
	
	AdapterExtended1 adapter;
	Vector2f coordinatesPlanet1 = new Vector2f(-4.0f, 0.0f);
	Vector2f coordinatesPlanet2 = new Vector2f(4.0f, 0.0f);
	MovementType projectileMovementType = MovementType.LINEAR; //TODO: zu Aufgabenstellung hinzufügen...

	@Before
	public void setUp() {
		adapter = new AdapterExtended1();
	}
	
	@After
	public void finish() {
		adapter.stopGame();
	}
	
	@Test
	public void testShootingAngle() { // belongs to task: "Schussparameter"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, projectileMovementType);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==Launch.GAMEPLAY_STATE);
		
		//TODO: mehr als 90° oder weniger als -90° Abschusswinkel darf kein Projektil erzeugen...
		//TODO: teste Winkel -90°, -45°, 0°, 45°, 90° und prüfe, ob das Projektil korrekt abgeschossen wird...
		
		assertThrows(IllegalArgumentException.class, () -> {adapter.setShootingAngleOfApe(0, 91);}, "Expected an IllegalArgumentException when a shooting angle > 90° is passed for Ape1!");
		assertThrows(IllegalArgumentException.class, () -> {adapter.setShootingAngleOfApe(0, -91);}, "Expected an IllegalArgumentException when a shooting angle < -90° is passed for Ape1!");
		assertThrows(IllegalArgumentException.class, () -> {adapter.setShootingAngleOfApe(1, 91);}, "Expected an IllegalArgumentException when a shooting angle > 90° is passed for Ape2!");
		assertThrows(IllegalArgumentException.class, () -> {adapter.setShootingAngleOfApe(1, -91);}, "Expected an IllegalArgumentException when a shooting angle < -90° is passed for Ape2!");
				
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(0, 90);}, "Expected no Exception when a shooting angle of 90° is passed for Ape1!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(0, 45);}, "Expected no Exception when a shooting angle of 45° is passed for Ape1!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(0, 0);}, "Expected no Exception when a shooting angle of 0° is passed for Ape1!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(0, -45);}, "Expected no Exception when a shooting angle of -45° is passed for Ape1!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(0, -90);}, "Expected no Exception when a shooting angle of -90° is passed for Ape1!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(1, 90);}, "Expected no Exception when a shooting angle of 90° is passed for Ape2!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(1, 45);}, "Expected no Exception when a shooting angle of 45° is passed for Ape2!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(1, 0);}, "Expected no Exception when a shooting angle of 0° is passed for Ape2!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(1, -45);}, "Expected no Exception when a shooting angle of -45° is passed for Ape2!");
		assertDoesNotThrow(() -> {adapter.setShootingAngleOfApe(1, -90);}, "Expected no Exception when a shooting angle of -90° is passed for Ape2!");
		
		adapter.setShootingAngleOfApe(0, 90);
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		Projectile projectileFlying = adapter.getProjectile();
		assertTrue("No Projectile with ID '" + Constants.PROJECTILE_ID + "' in EntityManager after hitting Space-Key!", projectileFlying!=null);
		Vector2f projectileCoordinatesT0 = projectileFlying.getCoordinates();
		adapter.runGame(10);
		Vector2f projectileCoordinatesT1 = projectileFlying.getCoordinates();
		
	}
	
	@Test
	public void testShootingPower() { // belongs to task: "Schussparameter"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, projectileMovementType);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==Launch.GAMEPLAY_STATE);
		
	}

}
