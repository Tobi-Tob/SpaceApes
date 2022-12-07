package spaceapes;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;

public class Map {
	public List<Planet> listOfPlanets;
	public List<Ape> listOfApes;
	public Entity background;

	/**
	 * Erzeugt ein leeres Map Objekt. Mit den init-Methoden koennen Entitys der Map
	 * hinzugefuegt werden.
	 */
	public Map() {
		listOfPlanets = new ArrayList<>();
		listOfApes = new ArrayList<>();
		background = new Entity("background"); // Entitaet fuer Hintergrund erzeugen
	}

	public void initBackground() {
		background.setPosition(Utils.toPixelCoordinates(0, 0)); // Startposition des Hintergrunds (Mitte des Fensters)
		background.setScale(0.9f); // Skalieren des Hintergrunds
		try {
			background.addComponent(new ImageRenderComponent(new Image("/assets/stars2.jpeg")));
		} catch (SlickException e) {
			System.err.println("Cannot find image for background");
		}
	}

	/**
	 * WICHTIG: Muss ausgefuehrt werden, bevor Affen initalisiert werden
	 */
	public void initPlanets() {
		// Home planets zufaellig auf den Spielfeld Haelften platzieren
		Planet planetA = new Planet("PlanetA", Utils.randomFloat(-6, -2), Utils.randomFloat(-4, 4)); // rechte Haelfte
		Planet planetB = new Planet("PlanetB", Utils.randomFloat(2, 6), Utils.randomFloat(-4, 4)); // linke Haelfte
		listOfPlanets.add(planetA); // Speichern in der Planeten Liste des Map Objekts
		listOfPlanets.add(planetB);
		try {
			planetA.addComponent(new ImageRenderComponent(new Image("/assets/planet1.png")));
			planetB.addComponent(new ImageRenderComponent(new Image("/assets/planet1.png")));
		} catch (SlickException e) {
			System.err.println("Cannot find image for planet");
			;
		}
		// Ausgabe zum testen
		/*
		 * java.lang.System.out .println(planetA.getID() + " -> Radius: " +
		 * planetA.getRadius() + " Mass: " + planetA.getMass()); java.lang.System.out
		 * .println(planetB.getID() + " -> Radius: " + planetB.getRadius() + " Mass: " +
		 * planetB.getMass());
		 */
	}

	/**
	 * @param playerA Spieler A (linke Spielhaelfte)
	 * @param playerB Spieler B (rechte Spielhaelfte)
	 */
	public void initApes(Player playerA, Player playerB) {
		if (listOfPlanets.isEmpty()) {
			throw new RuntimeException("List of Planets is empty");
		}
		Ape apeA = new Ape("apeA", listOfPlanets.get(0), playerA);
		Ape apeB = new Ape("apeB", listOfPlanets.get(1), playerB);
		listOfApes.add(apeA); // Speichern in der Affen Liste des Map Objekts
		listOfApes.add(apeB);
		try {
			apeA.addComponent(new ImageRenderComponent(new Image("/assets/ape_blue.png")));
			apeB.addComponent(new ImageRenderComponent(new Image("/assets/ape_yellow.png")));
		} catch (SlickException e) {
			System.err.println("Cannot find image for apes");
		}
	}
}
