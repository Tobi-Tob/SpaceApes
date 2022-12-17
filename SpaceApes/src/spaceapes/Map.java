package spaceapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

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
		background.setScale(0.5f); // Skalieren des Hintergrunds
		try {
			background.addComponent(new ImageRenderComponent(new Image("/assets/space1.jpg")));
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
			addRandomImageToPlanet(planetA, false);
			addRandomImageToPlanet(planetB, false);
		} catch (SlickException e) {
			System.err.println("Cannot find image for planet");
		}
		// Ausgabe zum testen
		java.lang.System.out
				.println(planetA.getID() + " -> Radius: " + planetA.getRadius() + " Mass: " + planetA.getMass());
		java.lang.System.out
				.println(planetB.getID() + " -> Radius: " + planetB.getRadius() + " Mass: " + planetB.getMass());
		Random r = new Random();
		int morePlanetsToAdd = r.nextInt(3); // 0, 1 oder 2
		// Schleife zum Initialisieren aller restlichen Planeten (ohne Affen)
		for (int i = 0; i < morePlanetsToAdd; i++) {
			Vector2f validePosition = findValidePositionForPlanetSpawning();
			Planet planet_i = new Planet("Planet" + i, validePosition.x, validePosition.y);
			listOfPlanets.add(planet_i);
			try {
				addRandomImageToPlanet(planet_i, true);
			} catch (SlickException e) {
				System.err.println("Cannot find image for planet");
			}
		}
	}

	/**
	 * Fuegt Planeten ein zufaelliges Bild hinzu und skaliert dieses individuell
	 * 
	 * @param planet
	 * @param allowRings true, wenn Planetenbilder mit Ringen verwedet werden
	 *                   duerfen
	 * @throws SlickException
	 */
	private void addRandomImageToPlanet(Planet planet, boolean allowRings) throws SlickException {
		Random r = new Random();
		int imageNumber = r.nextInt(3) + 1; // Integer im Intervall [1, 3]
		if (allowRings) {
			imageNumber = r.nextInt(4) + 1; // Integer im Intervall [1, 4]
		}

		switch (imageNumber) {
		default: // Eqivalent zu case 1
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet1.png")));
			float ScalingFactorPlanet1 = 0.31f;
			planet.setScale(planet.getRadius() * ScalingFactorPlanet1);
			break;
		case 2:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet2.png")));
			float ScalingFactorPlanet2 = 0.32f;
			planet.setScale(planet.getRadius() * ScalingFactorPlanet2);
			break;
		case 3:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet3.png")));
			float ScalingFactorPlanet3 = 0.31f;
			planet.setScale(planet.getRadius() * ScalingFactorPlanet3);
			break;
		case 4:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/ring_planet1.png")));
			float ScalingFactorRingPlanet1 = 0.35f;
			planet.setScale(planet.getRadius() * ScalingFactorRingPlanet1);
			break;
		}
	}

	private Vector2f findValidePositionForPlanetSpawning() {
		for (int n = 0; n < 100; n++) { // Suche bis zu 100 Zufallspositionen ab
			Vector2f randomPosition = new Vector2f(Utils.randomFloat(-6.5f, 6.5f), Utils.randomFloat(-4.5f, 4.5f));
			boolean positionIsValide = true;
			float safetyMargin = 3f;
			// Iteriere ueber alle Planeten
			for (int i = 0; i < listOfPlanets.size(); i++) {
				Planet p_i = listOfPlanets.get(i);
				Vector2f vectorToPlanetCenter = new Vector2f(p_i.getCoordinates().x - randomPosition.x,
						p_i.getCoordinates().y - randomPosition.y);
				// Test ob randomPosition zu nahe am Planeten i liegt (durch Kreisgleichung)
				if (Math.pow(vectorToPlanetCenter.x, 2) + Math.pow(vectorToPlanetCenter.y, 2) < Math
						.pow(p_i.getRadius() + safetyMargin, 2)) {
					positionIsValide = false; // Ist dies der Fall, ist die Position ungueltig
				}
			}
			if (positionIsValide) {
				java.lang.System.out.println("n= " + n);
				return randomPosition; // Wenn gueltige Position gefunden, gib diese zurueck
			}
		}
		// Falls Such Schleife bis zum Ende durch laeuft:
		throw new RuntimeException("Could not find a valide Position for Planet spawning");
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

	/**
	 * Sammelt alle fuer die spaetere Berechnung benoetigten Daten in einer Liste
	 * von Arrays
	 * 
	 * @return Liste mit Planetendaten. Jeder Listeneintrag enthaelt ein Array mit
	 *         Struktur [x, y, mass, radius]
	 */
	public List<float[]> generatePlanetData() {
		List<float[]> planetData = new ArrayList<>();
		for (int i = 0; i < listOfPlanets.size(); i++) {

			Vector2f planetPosition = listOfPlanets.get(i).getCoordinates();
			float planetMass = listOfPlanets.get(i).getMass();
			float planetRadius = listOfPlanets.get(i).getRadius();

			planetData.add(new float[] { planetPosition.x, planetPosition.y, planetMass, planetRadius });
		}
		return planetData;
	}
}
