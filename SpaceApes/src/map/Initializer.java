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
import spaceapes.SpaceApes;
import utils.Utils;

public class Initializer {

	private List<Entity> playerPlanets = new ArrayList<Entity>();

	/**
	 * This method creates all entities of a Map and takes the positions of the two player planets as input.
	 * If a a position is null or is out of the possible spawning positions
	 * then the planets will be spawned randomly in the according half of the Map
	 * 
	 * @param coordinatesPlanet1 - Positiion of the planet for player one in world coordinates
	 * @param coordinatesPlanet2 - Positiion of the planet for player two in world coordinates
	 */
	public void initMap(Vector2f coordinatesPlanet1, Vector2f coordinatesPlanet2, float radiusPlanet1, float radiusPlanet2, int massPlanet1, int massPlanet2, boolean createNonPlayerPlanets, MovementType projectileMovementType, float angleOnPlanetApe1, float angleOnPlanetApe2, boolean antiPlanetAndBlackHole) {
		// Hier werden alle Entities, die auf der Map vorkommen erstellt
		initBackground();
		initPlanets(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, createNonPlayerPlanets, antiPlanetAndBlackHole);
		initApes(projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2); // initPlanets() muss unbedingt davor ausgefuehrt werden!
		initApeInfoSigns(); // initPlanets() und initApes() muessen unbedingt davor ausgefuehrt werden!
		Map.getInstance().setGravitationConstant(0.25f);
	}

	protected void initBackground() {
		Map map = Map.getInstance();
		map.getEntityManager().addEntity(SpaceApes.GAMEPLAY_STATE, new BackgroundFactory().createEntity());
	}

	protected void initPlanets(Vector2f coordinates1, Vector2f coordinates2, float radiusPlanet1, float radiusPlanet2, int massPlanet1, int massPlanet2, boolean createNonPlayerPlanets, boolean antiPlanetAndBlackHole) {
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

		float xBorder = 8.0f;
		float yBorder = 6.0f;

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
		map.getEntityManager().addEntity(SpaceApes.GAMEPLAY_STATE, planetOne);

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
		map.getEntityManager().addEntity(SpaceApes.GAMEPLAY_STATE, planetTwo);

		if (createNonPlayerPlanets) {
			boolean otherPlanetSpawned = false; // Damit mindestens ein anderer Planet gespawnt wird
			
			// Versuche Schwarzes Loch zu platzieren
			float blackHoleProbability = 0.4f;
			if (Utils.randomFloat(0, 1) < blackHoleProbability || antiPlanetAndBlackHole) {
				Vector2f blackHolePosition = null;
				if (antiPlanetAndBlackHole) {
					blackHolePosition = map.findValidPosition(5, -1);
				} else {
					blackHolePosition = map.findValidPosition(5, 30);
				}				
				if (blackHolePosition != null) {
					otherPlanetSpawned = true;
					String nameBlackHole = "BlackHole";
					float radiusBlackHole = Utils.randomFloat(0.4f, 0.5f);
					int massBlackHole = (int) (radiusBlackHole * 275);
	
					Planet blackHole = new PlanetFactory(nameBlackHole, radiusBlackHole, massBlackHole, blackHolePosition,
							PlanetType.BLACKHOLE).createEntity();
					map.addPlanet(blackHole);
					map.getEntityManager().addEntity(SpaceApes.GAMEPLAY_STATE, blackHole);
				}
			}
	
			// Versuche Anti Planet zu platzieren
			float antiPlanetProbability = 0.3f;
			if (Utils.randomFloat(0, 1) < antiPlanetProbability || antiPlanetAndBlackHole) {
				Vector2f antiPlanetPosition = null;
				if (antiPlanetAndBlackHole) {
					antiPlanetPosition = map.findValidPosition(5, -1);
				} else {
					antiPlanetPosition = map.findValidPosition(5, 30);
				}	
				if (antiPlanetPosition != null) {
					otherPlanetSpawned = true;
					String nameAntiPlanet = "AntiPlanet";
					float radiusAntiPlanet = Utils.randomFloat(0.9f, 1.3f);
					int massAntiPlanet = (int) (-radiusAntiPlanet * 50);
	
					Planet antiPlanet = new PlanetFactory(nameAntiPlanet, radiusAntiPlanet, massAntiPlanet, antiPlanetPosition,
							PlanetType.ANTI).createEntity();
					map.addPlanet(antiPlanet);
					map.getEntityManager().addEntity(SpaceApes.GAMEPLAY_STATE, antiPlanet);
				}
			}
	
			// Restliche Planeten
			Random r = new Random();
			int morePlanetsToAdd = r.nextInt(4); // 0, 1, 2 oder 3 weitere Planeten ...
			if (!otherPlanetSpawned && morePlanetsToAdd == 0) {
				morePlanetsToAdd++; // ... aber mindestens einen Nicht-Spieler-Planet insgesamt
			}
			for (int i = 0; i < morePlanetsToAdd; i++) {
				Vector2f validePosition = null;
				if (i == 0 && !otherPlanetSpawned) {
					otherPlanetSpawned = true;
					validePosition = map.findValidPosition(4, -1); // wird bei -1 so oft durchgeführt, bis eine Position gefunden
				} else {
					validePosition = map.findValidPosition(4, 10);
				}
				
				// Falls keine geeignete Position gefunden wurde, fuege keinen neuen Planeten
				// hinzu
				if (validePosition != null) {
					otherPlanetSpawned = true;
					String namePlanet = "Planet" + (i + 3);
					float radiusPlanet = Utils.randomFloat(0.75f, 1.5f);
					int massPlanet = (int) (radiusPlanet * Utils.randomFloat(0.91f, 1.1f) * 65);
	
					Planet planet = new PlanetFactory(namePlanet, radiusPlanet, massPlanet, validePosition, PlanetType.NORMAL)
							.createEntity();
					map.addPlanet(planet);
					map.getEntityManager().addEntity(SpaceApes.GAMEPLAY_STATE, planet);
				}
			}
		}
	}

	protected void initApes(MovementType projectileMovementType, float angleOnPlanetApe1, float angleOnPlanetApe2) {
		Map map = Map.getInstance();

		for (int i = 0; i < SpaceApes.players.size(); i++) {

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
			map.getEntityManager().addEntity(SpaceApes.GAMEPLAY_STATE, ape);
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
			StateBasedEntityManager.getInstance().addEntity(SpaceApes.GAMEPLAY_STATE, apeInfoSign);
		}
	}
}
