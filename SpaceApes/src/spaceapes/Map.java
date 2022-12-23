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
		Planet planet1 = new Planet("Planet1", Utils.randomFloat(-5.5f, -2.5f), Utils.randomFloat(-3.5f, 3.5f)); // rechte
																													// Haelfte
		Planet planet2 = new Planet("Planet2", Utils.randomFloat(2.5f, 5.5f), Utils.randomFloat(-3.5f, 3.5f)); // linke
																												// Haelfte
		listOfPlanets.add(planet1); // Speichern in der Planeten Liste des Map Objekts
		listOfPlanets.add(planet2);
		try {
			addRandomImageToPlanet(planet1, false);
			addRandomImageToPlanet(planet2, false);
		} catch (SlickException e) {
			System.err.println("Cannot find image for planet");
		}
		Random r = new Random();
		int morePlanetsToAdd = r.nextInt(4); // 0, 1, 2 oder 3 weitere Planeten
		// Schleife zum Initialisieren aller restlichen Planeten (ohne Affen)
		for (int i = 0; i < morePlanetsToAdd; i++) {
			Vector2f validePosition = findValidePositionForPlanetSpawning(4, 10);
			// Falls keine geeignete Position gefunden wurde, fuege keinen neuen Planeten
			// hinzu
			if (validePosition != null) {
				Planet planet_i = new Planet("Planet" + (i + 3), validePosition.x, validePosition.y);
				listOfPlanets.add(planet_i);
				try {
					addRandomImageToPlanet(planet_i, true);
				} catch (SlickException e) {
					System.err.println("Cannot find image for planet");
				}
			}
		}
		if (Utils.randomFloat(0, 1) < 0.3f) {
			Vector2f blackHolePosition = findValidePositionForPlanetSpawning(5, 10);
			if (blackHolePosition != null) {
				BlackHole blackHole = new BlackHole("BlackHole", blackHolePosition.x, blackHolePosition.y);
				listOfPlanets.add(blackHole);
			}
		}
	}

	/**
	 * Fuegt Planeten ein zufaelliges Bild hinzu und skaliert dieses individuell
	 * 
	 * @param planet
	 * @param ringsAllowed true, wenn Planetenbilder mit Ringen verwedet werden
	 *                     duerfen
	 * @throws SlickException
	 */
	private void addRandomImageToPlanet(Planet planet, boolean ringsAllowed) throws SlickException {
		Random r = new Random();
		int imageNumber = r.nextInt(6) + 1; // Integer im Intervall [1, 6]
		if (ringsAllowed) {
			imageNumber = r.nextInt(9) + 1; // Integer im Intervall [1, 9]
		}

		switch (imageNumber) {
		default: // Eqivalent zu case 1
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet1.png")));
			float ScalingFactorPlanet1 = 0.315f;
			planet.setScale(planet.getRadius() * ScalingFactorPlanet1);
			break;
		case 2:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet2.png")));
			float ScalingFactorPlanet2 = 0.33f;
			planet.setScale(planet.getRadius() * ScalingFactorPlanet2);
			break;
		case 3:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet3.png")));
			float ScalingFactorPlanet3 = 0.315f;
			planet.setScale(planet.getRadius() * ScalingFactorPlanet3);
			break;
		case 4:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet4.png")));
			float ScalingFactorPlanet4 = 0.31f;
			planet.setScale(planet.getRadius() * ScalingFactorPlanet4);
			break;
		case 5:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet5.png")));
			float ScalingFactorPlanet5 = 0.34f;
			planet.setScale(planet.getRadius() * ScalingFactorPlanet5);
			break;
		case 6:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet6.png")));
			float ScalingFactorPlanet6 = 0.335f;
			planet.setScale(planet.getRadius() * ScalingFactorPlanet6);
			break;
		case 7:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/ring_planet1.png")));
			float ScalingFactorRingPlanet1 = 0.36f;
			planet.setScale(planet.getRadius() * ScalingFactorRingPlanet1);
			break;
		case 8:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/ring_planet2.png")));
			float ScalingFactorRingPlanet2 = 0.335f;
			planet.setScale(planet.getRadius() * ScalingFactorRingPlanet2);
			break;
		case 9:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/ring_planet3.png")));
			float ScalingFactorRingPlanet3 = 0.31f;
			planet.setScale(planet.getRadius() * ScalingFactorRingPlanet3);
			break;
		}
	}

	/**
	 * Findet mithilfe von Random-Search einen Koordinaten-Vektor, der weit genug
	 * von allen anderen Planeten entfernt ist
	 * 
	 * @param marginToNextPlanetCenter Gibt Abstand an, wie weit der naechste Planet
	 *                                 mindestens entfernt sein muss
	 * @param iterations               Wie oft soll maximal nach einer gueltigen
	 *                                 Position gesucht werden
	 * @return Vector2f oder null, falls die vorgegebene Anzahl an Iterationen
	 *         ueberschritten wurde
	 */
	private Vector2f findValidePositionForPlanetSpawning(float marginToNextPlanetCenter, int iterations) {
		for (int n = 0; n < iterations; n++) { // Suche so lange wie durch iterations vorgegeben
			Vector2f randomPosition = new Vector2f(Utils.randomFloat(-6.5f, 6.5f), Utils.randomFloat(-4.5f, 4.5f));
			boolean positionIsValide = true;
			// Iteriere ueber alle Planeten
			for (int i = 0; i < listOfPlanets.size(); i++) {
				Planet p_i = listOfPlanets.get(i);
				Vector2f vectorToPlanetCenter = new Vector2f(p_i.getCoordinates().x - randomPosition.x,
						p_i.getCoordinates().y - randomPosition.y);
				// Test ob randomPosition zu nahe am Planeten i liegt (durch Kreisgleichung)
				if (Math.pow(vectorToPlanetCenter.x, 2) + Math.pow(vectorToPlanetCenter.y, 2) < Math
						.pow(marginToNextPlanetCenter, 2)) {
					positionIsValide = false; // Ist dies der Fall, ist die Position ungueltig
					break;
				}
			}
			if (positionIsValide) {
				java.lang.System.out.println("Planet spawning after: n=" + n);
				return randomPosition; // Wenn gueltige Position gefunden, gib diese zurueck
			}
		}
		// Falls Such-Schleife bis zum Ende durch laeuft:
		java.lang.System.out.println("Planet spawning after: null");
		return null;
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

	/**
	 * WICHTIG: Muss ausgefuehrt werden, nachdem Planeten initalisiert wurden
	 */
	public void initApes(List<Player> listOfAllPlayers) {
		if (listOfPlanets.isEmpty() || listOfAllPlayers.isEmpty()) {
			throw new RuntimeException("initApes failed, one of the required lists is empty");
		}
		// Jeder Spieler in der Liste bekommt seinen eigenen Affen
		for (int i = 0; i < listOfAllPlayers.size(); i++) {
			Ape ape;
			if (i >= listOfPlanets.size()) { // Tritt ein falls weniger Planeten existieren als Spieler
				int randomPlanet = (int) Utils.randomFloat(0, listOfPlanets.size());
				ape = new Ape("ape" + (i + 1), listOfPlanets.get(randomPlanet), listOfAllPlayers.get(i));
			} else {
				ape = new Ape("ape" + (i + 1), listOfPlanets.get(i), listOfAllPlayers.get(i));
			}
			listOfApes.add(ape); // Speichern in der Affen Liste des Map Objekts
			try {
				ape.addComponent(new ImageRenderComponent(new Image("/assets/ape" + (i + 1) + ".png")));
			} catch (SlickException | RuntimeException e) {
				try {
					ape.addComponent(new ImageRenderComponent(new Image("/assets/ape1.png")));
				} catch (SlickException e1) {
					System.err.println("Cannot find image for ape");
				}
			}
		}

	}
}
