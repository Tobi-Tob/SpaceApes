package map;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import entities.Ape;
import entities.Planet;
import factories.ApeFactory;
import factories.BackgroundFactory;
import factories.PlanetFactory;
import factories.PlanetFactory.PlanetType;
import factories.ApeInfoSignFactory;
import spaceapes.Constants;
import spaceapes.Launch;
import utils.Utils;

public class Parser {

	private List<Entity> playerPlanets = new ArrayList<Entity>();

	public void initMap() {

		Map map = Map.getInstance();
		// Hier werden alle Entities, die auf der Map vorkommen erstellt
		initBackground(map);
		initPlanets(map);
		initApes(map); // parsePlanets() muss unbedingt davor ausgefuehrt werden!
		initApeInfoSigns(map); // parsePlanets() und parseApes() müssen unbedingt davor ausgefuehrt werden!
	}

	protected void initBackground(Map map) {
		map.getEntityManager().addEntity(Launch.GAMEPLAY_STATE, new BackgroundFactory().createEntity());
	}

	protected void initPlanets(Map map) {

		float xBorder = Constants.WORLD_WIDTH / 2;
		float yBorder = Constants.WORLD_HEIGHT / 2;

		// TODO die Erzeugung der Planeten muss spaeter auch noch in eine Schleife,
		// wenn es mehr als 2 Spieler geben koennen soll...
		
		// Planet 1 fuer Spieler 1 in der linken Haelfte platzieren
		String namePlanetOne = "Planet1";
		float xPlanetOne = Utils.randomFloat(-xBorder * 0.6f, -xBorder * 0.3f);
		float yPlanetOne = Utils.randomFloat(-yBorder * 0.5f, yBorder * 0.5f);
		Vector2f coordinatesPlanetOne = new Vector2f(xPlanetOne, yPlanetOne);
		float radiusPlanetOne = Utils.randomFloat(0.75f, 1.5f);
		int massPlanetOne = (int) (radiusPlanetOne * Utils.randomFloat(0.91f, 1.1f) * 65);

		Planet planetOne = (Planet) new PlanetFactory(namePlanetOne, radiusPlanetOne, massPlanetOne, coordinatesPlanetOne,
				PlanetType.PLAYER).createEntity();
		// Spielerplaneten und fuer die Berechnungen notwendige Planetendaten werden in
		// der Instanz von Map abgelegt. Somit kann man von ueberall darauf zugreifen
		playerPlanets.add(planetOne);
		map.addPlanet(planetOne);
		map.getEntityManager().addEntity(Launch.GAMEPLAY_STATE, planetOne);

		// Planet 2 fuer Spieler 2 in der rechten Haelfte platzieren
		String namePlanetTwo = "Planet2";
		float xPlanetTwo = Utils.randomFloat(xBorder * 0.3f, xBorder * 0.6f);
		float yPlanetTwo = Utils.randomFloat(-yBorder * 0.5f, yBorder * 0.5f);
		Vector2f coordinatesPlanetTwo = new Vector2f(xPlanetTwo, yPlanetTwo);
		float radiusPlanetTwo = Utils.randomFloat(0.75f, 1.5f);
		int massPlanetTwo = (int) (radiusPlanetTwo * Utils.randomFloat(0.91f, 1.1f) * 65);

		Planet planetTwo = (Planet) new PlanetFactory(namePlanetTwo, radiusPlanetTwo, massPlanetTwo, coordinatesPlanetTwo,
				PlanetType.PLAYER).createEntity();
		playerPlanets.add(planetTwo);
		map.addPlanet(planetTwo);
		map.getEntityManager().addEntity(Launch.GAMEPLAY_STATE, planetTwo);

		// Versuche Schwarzes Loch zu platzieren
		float blackHoleProbability = 0.4f;
		if (Utils.randomFloat(0, 1) < blackHoleProbability) {
			Vector2f blackHolePosition = map.findValidPosition(5, 30);
			if (blackHolePosition != null) {
				String nameBlackHole = "BlackHole";
				float radiusBlackHole = Utils.randomFloat(0.4f, 0.5f);
				int massBlackHole = (int) (radiusBlackHole * 250);

				Planet blackHole = (Planet) new PlanetFactory(nameBlackHole, radiusBlackHole, massBlackHole,
						blackHolePosition, PlanetType.BLACKHOLE).createEntity();
				map.addPlanet(blackHole);
				map.getEntityManager().addEntity(Launch.GAMEPLAY_STATE, blackHole);
			}
		}

		// Versuche Anti Planet zu platzieren
		float antiPlanetProbability = 0.3f;
		if (Utils.randomFloat(0, 1) < antiPlanetProbability) {
			Vector2f antiPlanetPosition = map.findValidPosition(4, 30);
			if (antiPlanetPosition != null) {
				String nameAntiPlanet = "AntiPlanet";
				float radiusAntiPlanet = Utils.randomFloat(0.9f, 1.3f);
				int massAntiPlanet = (int) (-radiusAntiPlanet * 50);

				Planet antiPlanet = (Planet) new PlanetFactory(nameAntiPlanet, radiusAntiPlanet, massAntiPlanet,
						antiPlanetPosition, PlanetType.ANTI).createEntity();
				map.addPlanet(antiPlanet);
				map.getEntityManager().addEntity(Launch.GAMEPLAY_STATE, antiPlanet);
			}
		}

		// Restliche Planeten
		Random r = new Random();
		int morePlanetsToAdd = r.nextInt(4); // 0, 1, 2 oder 3 weitere Planeten
		for (int i = 0; i < morePlanetsToAdd; i++) {
			Vector2f validePosition = map.findValidPosition(4, 10);
			// Falls keine geeignete Position gefunden wurde, fuege keinen neuen Planeten
			// hinzu
			if (validePosition != null) {
				String namePlanet = "Planet" + (i + 3);
				float radiusPlanet = Utils.randomFloat(0.75f, 1.5f);
				int massPlanet = (int) (radiusPlanet * Utils.randomFloat(0.91f, 1.1f) * 65);

				Planet planet = (Planet) new PlanetFactory(namePlanet, radiusPlanet, massPlanet, validePosition,
						PlanetType.NORMAL).createEntity();
				map.addPlanet(planet);
				map.getEntityManager().addEntity(Launch.GAMEPLAY_STATE, planet);
			}
		}
	}

	protected void initApes(Map map) {

		for (int i = 0; i < Launch.players.size(); i++) {

			String nameApe = "Ape" + (i + 1);
			Planet homePlanet;
			if (i > playerPlanets.size() - 1) { // Fall es gibt nicht genug PlayerPlanets
				int randomIndex = new Random().nextInt(map.getPlanets().size());
				homePlanet = map.getPlanets().get(randomIndex);
			} else {
				homePlanet = (Planet) playerPlanets.get(i);
			}
			int health = 100;
			int energy = 100;
			int apeImage = i + 1;
			boolean apeActive = (i == 0);
			boolean apeInteraction = (i == 0);
			float movementSpeed = 0.05f;
			float angleOnPlanet = Utils.randomFloat(0, 360);
			float angleOfView = 0;
			float throwStrength = 5f;

			Entity ape = new ApeFactory(nameApe, homePlanet, health, energy, apeImage, apeActive, apeInteraction,
					movementSpeed, angleOnPlanet, angleOfView, throwStrength).createEntity();
			map.addApe((Ape) ape);
			map.getEntityManager().addEntity(Launch.GAMEPLAY_STATE, ape);
		}
	}
	
	protected void initApeInfoSigns(Map map) {
		
		
		for (int i = 0; i < map.getApes().size(); i++) {
			Planet planet = map.getApes().get(i).getPlanet();
			String entityID = "PlanetPanel" + i;
			Vector2f panelCoordinates = planet.getCoordinates();
			float panelScale = planet.getScale() * 0.4f; //TODO es wäre schlauer den Scale über den Radius zu ermitteln (wegen den Ringplaneten)
			
			Entity planetPanel = new ApeInfoSignFactory(entityID, panelCoordinates, panelScale, map.getApes().get(i)).createEntity();
			StateBasedEntityManager.getInstance().addEntity(Launch.GAMEPLAY_STATE, planetPanel);
		}
	}
}
