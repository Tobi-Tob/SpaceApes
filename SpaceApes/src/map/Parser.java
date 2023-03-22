package map;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import entities.Ape;
import entities.ApeInfoSign;
import entities.Planet;
import factories.ApeFactory;
import factories.BackgroundFactory;
import factories.PlanetFactory;
import factories.PlanetFactory.PlanetType;
import factories.ProjectileFactory.MovementType;
import factories.ApeInfoSignFactory;
import spaceapes.Constants;
import spaceapes.Launch;
import utils.Utils;

public class Parser { // TL: TODO vllt name Initialiser passender (momentan wird ja noch keine Datei
						// gelesen)

	private List<Entity> playerPlanets = new ArrayList<Entity>();

	/**
	 * This method creates all entities of a Map and takes the positions of the two player planets as input.
	 * If a a position is null or is out of the possible spawning positions
	 * then the planets will be spawned randomly in the according half of the Map
	 * 
	 * @param coordinatesPlanet1 - Positiion of the planet for player one in world coordinates
	 * @param coordinatesPlanet2 - Positiion of the planet for player two in world coordinates
	 */
	public void initMap(Vector2f coordinatesPlanet1, Vector2f coordinatesPlanet2, float radiusPlanet1, float radiusPlanet2, int massPlanet1, int massPlanet2, boolean createNonPlayerPlanets, MovementType projectileMovementType, float angleOnPlanetApe1, float angleOnPlanetApe2) {
		// Hier werden alle Entities, die auf der Map vorkommen erstellt
		initBackground();
		initPlanets(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, createNonPlayerPlanets);
		initApes(projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2); // initPlanets() muss unbedingt davor ausgefuehrt werden!
		initApeInfoSigns(); // initPlanets() und initApes() muessen unbedingt davor ausgefuehrt werden!
	}

	protected void initBackground() {
		Map map = Map.getInstance();
		map.getEntityManager().addEntity(Launch.GAMEPLAY_STATE, new BackgroundFactory().createEntity());
	}

	protected void initPlanets(Vector2f coordinates1, Vector2f coordinates2, float radiusPlanet1, float radiusPlanet2, int massPlanet1, int massPlanet2, boolean createNonPlayerPlanets) {
		Map map = Map.getInstance();

		try {
		    // code that uses Constants class
			float xBorder = Constants.WORLD_WIDTH / 2;
			float yBorder = Constants.WORLD_HEIGHT / 2;
		} catch (ExceptionInInitializerError e) {
		    Throwable cause = e.getCause();
		    if (cause != null) {
		        cause.printStackTrace();
		    }
		    System.out.println("Error in initPlanets");
		}

		float xBorder = 8.0f; //MR raus nach debug
		float yBorder = 6.0f; //MR raus nach debug

		// TODO die Erzeugung der Planeten muss spaeter auch noch in eine Schleife,
		// wenn es mehr als 2 Spieler geben koennen soll...

		// Planet 1 fuer Spieler 1 in der linken Haelfte platzieren
		String namePlanetOne = "Planet1";
		if (coordinates1 == null || !map.isValidPositionForPlayerPlanet1(coordinates1)) {
			if (coordinates1 != null) {
				System.out.println("Given position of Planet1 was out of allowed area! x: " + coordinates1.x + " | y: " + coordinates1.y);
			}
			float xPlanetOne = Utils.randomFloat(-xBorder * 0.6f, -xBorder * 0.3f);
			float yPlanetOne = Utils.randomFloat(-yBorder * 0.5f, yBorder * 0.5f);
			coordinates1 = new Vector2f(xPlanetOne, yPlanetOne);
			
		} else {
			System.out.println("Create Planet1 at the given coordiantes! x: " + coordinates1.x + " | y: " + coordinates1.y);
		}
		if (radiusPlanet1 == 0) {
			radiusPlanet1 =  Utils.randomFloat(Constants.MINIMUM_RADIUS_PLAYER_PLANET, Constants.MAXIMUM_RADIUS_PLAYER_PLANET);;
		}
		if (massPlanet1 == 0) {
			massPlanet1 = (int) (radiusPlanet1 * Utils.randomFloat(0.91f, 1.1f) * 65);
		}

		Planet planetOne = new PlanetFactory(namePlanetOne, radiusPlanet1, massPlanet1, coordinates1,
				PlanetType.PLAYER).createEntity();
		// Spielerplaneten und fuer die Berechnungen notwendige Planetendaten werden in
		// der Instanz von Map abgelegt. Somit kann man von ueberall darauf zugreifen
		playerPlanets.add(planetOne);
		map.addPlanet(planetOne);
		map.getEntityManager().addEntity(Launch.GAMEPLAY_STATE, planetOne);

		// Planet 2 fuer Spieler 2 in der rechten Haelfte platzieren
		String namePlanetTwo = "Planet2";
		if (coordinates2 == null || !map.isValidPositionForPlayerPlanet2(coordinates2)) {
			if (coordinates2 != null) {
				System.out.println("Given position of Planet2 was out of allowed area! x: " + coordinates2.x + " | y: " + coordinates2.y);
			}
			float xPlanetTwo = Utils.randomFloat(xBorder * 0.3f, xBorder * 0.6f);
			float yPlanetTwo = Utils.randomFloat(-yBorder * 0.5f, yBorder * 0.5f);
			coordinates2 = new Vector2f(xPlanetTwo, yPlanetTwo);
			
		} else {
			System.out.println("Create Planet2 at the given coordiantes! x: " + coordinates2.x + " | y: " + coordinates2.y);
		}
		if (radiusPlanet2 == 0) {
			radiusPlanet2 =  Utils.randomFloat(Constants.MINIMUM_RADIUS_PLAYER_PLANET, Constants.MAXIMUM_RADIUS_PLAYER_PLANET);;
		}
		if (massPlanet2 == 0) {
			massPlanet2 = (int) (radiusPlanet2 * Utils.randomFloat(0.91f, 1.1f) * 65);
		}

		Planet planetTwo = new PlanetFactory(namePlanetTwo, radiusPlanet2, massPlanet2, coordinates2,
				PlanetType.PLAYER).createEntity();
		playerPlanets.add(planetTwo);
		map.addPlanet(planetTwo);
		map.getEntityManager().addEntity(Launch.GAMEPLAY_STATE, planetTwo);

		if (createNonPlayerPlanets) {
			// Versuche Schwarzes Loch zu platzieren
			float blackHoleProbability = 0.4f;
			if (Utils.randomFloat(0, 1) < blackHoleProbability) {
				Vector2f blackHolePosition = map.findValidPosition(5, 30);
				if (blackHolePosition != null) {
					String nameBlackHole = "BlackHole";
					float radiusBlackHole = Utils.randomFloat(0.4f, 0.5f);
					int massBlackHole = (int) (radiusBlackHole * 250);
	
					Planet blackHole = new PlanetFactory(nameBlackHole, radiusBlackHole, massBlackHole, blackHolePosition,
							PlanetType.BLACKHOLE).createEntity();
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
	
					Planet antiPlanet = new PlanetFactory(nameAntiPlanet, radiusAntiPlanet, massAntiPlanet, antiPlanetPosition,
							PlanetType.ANTI).createEntity();
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
	
					Planet planet = new PlanetFactory(namePlanet, radiusPlanet, massPlanet, validePosition, PlanetType.NORMAL)
							.createEntity();
					map.addPlanet(planet);
					map.getEntityManager().addEntity(Launch.GAMEPLAY_STATE, planet);
				}
			}
		}
	}

	protected void initApes(MovementType projectileMovementType, float angleOnPlanetApe1, float angleOnPlanetApe2) {
		Map map = Map.getInstance();

		for (int i = 0; i < Launch.players.size(); i++) {

			String nameApe = "Ape" + (i + 1);
			Planet homePlanet;
			if (i > playerPlanets.size() - 1) { // Fall es gibt nicht genug PlayerPlanets
				int randomIndex = new Random().nextInt(map.getPlanets().size());
				homePlanet = map.getPlanets().get(randomIndex);
			} else {
				homePlanet = (Planet) playerPlanets.get(i);
			}
			int health = Constants.APE_MAX_HEALTH;
			int energy = Constants.APE_MAX_ENERGY;
			int apeImage = i + 1;
			boolean apeActive = (i == 0);
			boolean apeInteraction = (i == 0);
			float movementSpeed = Constants.APE_MOVMENT_SPEED;
			float angleOnPlanet = Utils.randomFloat(0, 360);
			if (i == 0 && angleOnPlanetApe1 != 999) {
				angleOnPlanet = angleOnPlanetApe1;
			} else if (i == 1 && angleOnPlanetApe2 != 999) {
				angleOnPlanet = angleOnPlanetApe2;
			}
			float angleOfView = 0;
			float throwStrength = 5f;

			Ape ape = new ApeFactory(nameApe, homePlanet, health, energy, apeImage, apeActive, apeInteraction,
					movementSpeed, angleOnPlanet, angleOfView, throwStrength, projectileMovementType).createEntity();
			// TODO eine Apefactory fuer alle Apes?
			map.addApe(ape);
			map.getEntityManager().addEntity(Launch.GAMEPLAY_STATE, ape);
		}
	}

	protected void initApeInfoSigns() {
		Map map = Map.getInstance();

		for (int i = 0; i < map.getApes().size(); i++) {
			Ape ape = map.getApes().get(i);
			Planet planet = ape.getPlanet();
			Vector2f panelCoordinates = planet.getCoordinates(); // Uebergibt man das der Factory oder machtr die das
																	// intern selbst?
//			float ApeInfoSignScale = planet.getScale() * 0.4f; // TODO es waere schlauer den Scale ueber den Radius zu
//																// ermitteln
//																// (wegen den Ringplaneten)

			ApeInfoSign apeInfoSign = new ApeInfoSignFactory(panelCoordinates, ape).createEntity();
			StateBasedEntityManager.getInstance().addEntity(Launch.GAMEPLAY_STATE, apeInfoSign);
		}
	}
}
