package map;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import eea.engine.entity.Entity;
import entities.Ape;
import entities.ControlPanel;
import entities.Planet;
import factories.ApeFactory;
import factories.BackgroundFactory;
import factories.PlanetFactory;
import factories.PlanetFactory.PlanetType;
import interfaces.IMap;
import spaceapes.Launch;
import utils.Utils;

public class Parser {
	
	private List<Entity> playerPlanets = new ArrayList<Entity>();
	
	public IMap parseMap() {
		
		//Hier werden alle Entities, die auf der Map vorkommen erstellt
		Map map = initBackground(Map.getInstance());
		map = parsePlanet(map);
		map = parseApe(map); //parsePlanets() muss unbedingt davor ausgeführt werden!
		//map = parseControlPanel(map); -> MR: Das passt hier schlecht hin, wegen dem Render der korresponiderenden Entities... 
		
		return map;
	}
	
	protected Map initBackground(Map map) {
		map.addEntity(new BackgroundFactory().createEntity());
		return map;
	}

	protected Map parsePlanet(Map map) {
			
		float xBorder = Utils.worldWidth / 2;
		float yBorder = Utils.worldHeight / 2;
		
		//MR: die Erzeugung der Planeten muss später auch noch in eine Schleife,
		// wenn es mehr als 2 Speieler geben können soll...
		//Planet 1 für Spieler 1 in der linken Hälfte platzieren
		String namePlanetOne = "Planet1";
		float xPlanetOne = Utils.randomFloat(-xBorder * 0.6f, -xBorder * 0.3f);
		float yPlanetOne = Utils.randomFloat(-yBorder * 0.5f, yBorder * 0.5f);
		Vector2f coordinatesPlanetOne = new Vector2f(xPlanetOne, yPlanetOne);
		float radiusPlanetOne = Utils.randomFloat(0.75f, 1.5f);
		int massPlanetOne = (int) (radiusPlanetOne * Utils.randomFloat(0.91f, 1.1f) * 65);
		
		Planet planetOne = (Planet) new PlanetFactory(namePlanetOne, radiusPlanetOne, massPlanetOne,
				coordinatesPlanetOne, PlanetType.PLAYER).createEntity();
		// Spielerplaneten und für die Berechnungen notwendige Planetendaten werden in
		// der Instanz von Map abgelegt. Somit kann man von überall darauf zugreifen
		playerPlanets.add(planetOne);
		map.addPlanet(planetOne);
		map.addEntity(planetOne);
		
		//Planet 2 für Spieler 2 in der rechten Hälfte platzieren
		String namePlanetTwo = "Planet2";
		float xPlanetTwo = Utils.randomFloat(xBorder * 0.3f, xBorder * 0.6f);
		float yPlanetTwo = Utils.randomFloat(-yBorder * 0.5f, yBorder * 0.5f);
		Vector2f coordinatesPlanetTwo = new Vector2f(xPlanetTwo, yPlanetTwo);
		float radiusPlanetTwo = Utils.randomFloat(0.75f, 1.5f);
		int massPlanetTwo = (int) (radiusPlanetTwo * Utils.randomFloat(0.91f, 1.1f) * 65);
		
		Planet planetTwo = (Planet) new PlanetFactory(namePlanetTwo, radiusPlanetTwo, massPlanetTwo,
				coordinatesPlanetTwo, PlanetType.PLAYER).createEntity();
		playerPlanets.add(planetTwo);
		map.addPlanet(planetTwo);
		map.addEntity(planetTwo);
		
		
		// Versuche Schwarzes Loch zu platzieren#
		float blackHoleProbability = 0.3f;
		if (Utils.randomFloat(0, 1) < blackHoleProbability) {
			Vector2f blackHolePosition = findValidePositionForPlanetSpawning(5, 30);
			if (blackHolePosition != null) {
				String nameBlackHole = "BlackHole";
				float radiusBlackHole = Utils.randomFloat(0.4f, 0.5f);
				int massBlackHole = (int) (radiusBlackHole * 250);
				
				Planet blackHole = (Planet) new PlanetFactory(nameBlackHole, radiusBlackHole, massBlackHole,
						blackHolePosition, PlanetType.BLACKHOLE).createEntity();
				map.addPlanet(blackHole);
				map.addEntity(blackHole);
			}
		}

		// Versuche Anti Planet zu platzieren
		float antiPlanetProbability = 0.2f;
		if (Utils.randomFloat(0, 1) < antiPlanetProbability) {
			Vector2f antiPlanetPosition = findValidePositionForPlanetSpawning(4, 30);
			if (antiPlanetPosition != null) {
				String nameAntiPlanet = "AntiPlanet";
				float radiusAntiPlanet = Utils.randomFloat(0.6f, 0.8f);
				int massAntiPlanet = (int) (-0.3f * radiusAntiPlanet * 250);
				
				Planet antiPlanet = (Planet) new PlanetFactory(nameAntiPlanet, radiusAntiPlanet, massAntiPlanet,
						antiPlanetPosition, PlanetType.ANTI).createEntity();
				map.addPlanet(antiPlanet);
				map.addEntity(antiPlanet);
			}
		}

		// Restliche Planeten
		Random r = new Random();
		int morePlanetsToAdd = r.nextInt(4); // 0, 1, 2 oder 3 weitere Planeten
		for (int i = 0; i < morePlanetsToAdd; i++) {
			Vector2f validePosition = findValidePositionForPlanetSpawning(4, 10);
			// Falls keine geeignete Position gefunden wurde, fuege keinen neuen Planeten hinzu
			if (validePosition != null) {
				String namePlanet = "Planet" + (i + 3);
				float radiusPlanet = Utils.randomFloat(0.4f, 0.5f);
				int massPlanet = (int) (radiusPlanet * Utils.randomFloat(0.91f, 1.1f) * 65);
				
				Planet planet = (Planet) new PlanetFactory(namePlanet, radiusPlanet, massPlanet,
						validePosition, PlanetType.NORMAL).createEntity();
				map.addPlanet(planet);
				map.addEntity(planet);
			}
		}
		
		return map;
	}
	
	protected Map parseApe(Map map) {
		
		for (int i = 0; i < Launch.players.size(); i++) {
			
			String nameApe = "ape" + (i + 1);
			Planet homePlanet = (Planet) playerPlanets.get(i);
			int health = 100;
			int energy = 50;
			int apeImage = i+1;
			boolean apeActive = (i==0);
			boolean apeInteraction = (i==0);
			float movementSpeed = 0.08f;
			float angleOnPlanet = Utils.randomFloat(0, 360);
			float angleOfView = 0;
			float throwStrength = 5f;
			
			//MR folgende Zeilen überarbeiten!!
			float apePixelHeight = 300;
			float pixelfromFeetToCenter = 130;
			float desiredApeSizeInWorldUnits = 0.6f;
			float scalingFactor = desiredApeSizeInWorldUnits / Utils.pixelLengthToWorldLength(apePixelHeight);
			float distancePlanetCenter = homePlanet.getRadiusWorld() + Utils.pixelLengthToWorldLength(pixelfromFeetToCenter * scalingFactor);
			if (distancePlanetCenter > 0.1f) {
				//nichts
			} else
				throw new RuntimeException("Radius ist zu nah an null");
			
			Entity ape = new ApeFactory(nameApe, homePlanet, health, energy,
					apeImage, apeActive, apeInteraction, movementSpeed, angleOnPlanet,
					angleOfView, throwStrength, distancePlanetCenter).createEntity();
			map.addApe((Ape) ape);
			map.addEntity(ape);
		}
		
		return map;
	}
	
	protected Map parseControlPanel(Map map) {
		
		ControlPanel controlPanel = new ControlPanel("ControlPanel");
		controlPanel.initControlPanel();
		map.addEntity(controlPanel);
		
		//map.addEntity(new ControlPanelFactory().createEntity()); -> MR: Factory für Panel benutzen?
		
		return map;
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
		float xBorder = Utils.worldWidth / 2;
		float yBorder = Utils.worldHeight / 2;
		for (int n = 0; n < iterations; n++) { // Suche so lange wie durch iterations vorgegeben
			Vector2f randomPosition = new Vector2f(Utils.randomFloat(-xBorder * 0.8f, xBorder * 0.8f),
					Utils.randomFloat(-yBorder * 0.7f, yBorder * 0.7f));
			boolean positionIsValide = true;
			List<Planet> plantes = Map.getInstance().getPlanets();
			// Iteriere ueber alle Planeten
			for (int i = 0; i < plantes.size(); i++) {
				Planet p_i = plantes.get(i);
				Vector2f vectorToPlanetCenter = new Vector2f(p_i.getXCoordinateWorld() - randomPosition.x,
						p_i.getYCoordinateWorld() - randomPosition.y);
				// Test ob randomPosition zu nahe am Planeten i liegt (durch Kreisgleichung)
				if (Math.pow(vectorToPlanetCenter.x, 2) + Math.pow(vectorToPlanetCenter.y, 2) < Math
						.pow(marginToNextPlanetCenter, 2)) {
					positionIsValide = false; // Ist dies der Fall, ist die Position ungueltig
					break;
				}
			}
			if (positionIsValide) {
				// java.lang.System.out.println("Planet spawning after: n=" + n);
				return randomPosition; // Wenn gueltige Position gefunden, gib diese zurueck
			}
		}
		// Falls Such-Schleife bis zum Ende durch laeuft:
		// java.lang.System.out.println("Planet spawning after: null");
		return null;
	}
	
}
