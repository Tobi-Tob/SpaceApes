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
import factories.PlanetFactory;
import factories.PlanetFactory.PlanetType;
import factories.ApeInfoSignFactory;
import spaceapes.Constants;
import spaceapes.SpaceApes;
import utils.Utils;

public class Initializer {

	private List<Entity> playerPlanets = new ArrayList<Entity>();

	/**
	 * This method creates all entities of a Map
	 */
	public void initMap(Integer level) {
		initPlanets(level);
		initApes(); // initPlanets() muss unbedingt davor ausgefuehrt werden!
		initApeInfoSigns(); // initPlanets() und initApes() muessen unbedingt davor ausgefuehrt werden!
		Map.getInstance().setGravitationConstant(Constants.GRAVITATION_CONSTANT);
		Map.getInstance().useAirFriction(false);
	}

	protected void initPlanets(Integer level) {
		Map map = Map.getInstance();
		StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();

		float xBorder = Constants.WORLD_WIDTH / 2;
		float yBorder = Constants.WORLD_HEIGHT / 2;

		if (level != null) {
			// TODO Hier auslesen der Planeten Eigenschaften vom Parser
			throw new RuntimeException("Levels not implemented yet!");
		} else {

			/* Erstelle Random Level */

			// TODO Kompatibilitaet fuer mehr als 2 Spieler benoetigt (entwender mehr
			// Affenplaneten oder Teams von Spielern auf einem Planeten)

			// Planet 1 fuer Spieler 1 in der linken Haelfte platzieren
			float xPlanet1 = Utils.randomFloat(-xBorder * 0.6f, -xBorder * 0.3f);
			float yPlanet1 = Utils.randomFloat(-yBorder * 0.5f, yBorder * 0.5f);
			Vector2f coordinatesPlanet1 = new Vector2f(xPlanet1, yPlanet1);

			float radiusPlanet1 = Utils.randomFloat(Constants.MINIMUM_RADIUS_PLAYER_PLANET, Constants.MAXIMUM_RADIUS_PLAYER_PLANET);

			int massPlanet1 = (int) (radiusPlanet1 * Utils.randomFloat(0.91f, 1.1f) * 65);

			Planet planet1 = new PlanetFactory("Planet1", radiusPlanet1, massPlanet1, coordinatesPlanet1, PlanetType.PLAYER, true)
					.createEntity();
			// Spielerplaneten und fuer die Berechnungen notwendige Planetendaten werden in
			// der Instanz von Map abgelegt. Somit kann man von ueberall darauf zugreifen
			playerPlanets.add(planet1);
			map.addPlanet(planet1);
			entityManager.addEntity(SpaceApes.GAMEPLAY_STATE, planet1);

			// Planet 2 fuer Spieler 2 in der rechten Haelfte platzieren
			float xPlanet2 = Utils.randomFloat(xBorder * 0.3f, xBorder * 0.6f);
			float yPlanet2 = Utils.randomFloat(-yBorder * 0.5f, yBorder * 0.5f);
			Vector2f coordinatesPlanet2 = new Vector2f(xPlanet2, yPlanet2);

			float radiusPlanet2 = Utils.randomFloat(Constants.MINIMUM_RADIUS_PLAYER_PLANET, Constants.MAXIMUM_RADIUS_PLAYER_PLANET);

			int massPlanet2 = (int) (radiusPlanet2 * Utils.randomFloat(0.91f, 1.1f) * 65);

			Planet planet2 = new PlanetFactory("Planet2", radiusPlanet2, massPlanet2, coordinatesPlanet2, PlanetType.PLAYER, false)
					.createEntity();
			playerPlanets.add(planet2);
			map.addPlanet(planet2);
			entityManager.addEntity(SpaceApes.GAMEPLAY_STATE, planet2);

			// Versuche Schwarzes Loch zu platzieren
			if (Utils.randomFloat(0, 1) < Constants.BLACKHOLE_PROBABILITY) {
				Vector2f blackHolePosition = null;
				blackHolePosition = map.findValidPosition(5, 100);
				if (blackHolePosition != null) {
					String nameBlackHole = "BlackHole";
					float radiusBlackHole = Utils.randomFloat(0.4f, 0.5f);
					int massBlackHole = (int) (radiusBlackHole * 275);

					Planet blackHole = new PlanetFactory(nameBlackHole, radiusBlackHole, massBlackHole, blackHolePosition,
							PlanetType.BLACKHOLE, false).createEntity();
					map.addPlanet(blackHole);
					entityManager.addEntity(SpaceApes.GAMEPLAY_STATE, blackHole);
				}
			}

			// Versuche Anti Planet zu platzieren
			if (Utils.randomFloat(0, 1) < Constants.ANTI_PLANET_PROBABILITY) {
				Vector2f antiPlanetPosition = null;
				antiPlanetPosition = map.findValidPosition(5, 100);
				if (antiPlanetPosition != null) {
					String nameAntiPlanet = "AntiPlanet";
					float radiusAntiPlanet = Utils.randomFloat(0.9f, 1.3f);
					int massAntiPlanet = (int) (-radiusAntiPlanet * 50);

					Planet antiPlanet = new PlanetFactory(nameAntiPlanet, radiusAntiPlanet, massAntiPlanet,
							antiPlanetPosition, PlanetType.ANTI, false).createEntity();
					map.addPlanet(antiPlanet);
					entityManager.addEntity(SpaceApes.GAMEPLAY_STATE, antiPlanet);
				}
			}

			// Restliche Planeten
			Random r = new Random();
			int morePlanetsToAdd = r.nextInt(4); // 0, 1, 2 oder 3 weitere Planeten ...
			for (int i = 0; i < morePlanetsToAdd; i++) {
				Vector2f validePosition = null;
				validePosition = map.findValidPosition(4, 10);

				// Falls keine geeignete Position gefunden wurde, fuege keinen neuen Planeten
				// hinzu
				if (validePosition != null) {
					String namePlanet = "Planet" + (i + 3);
					float radiusPlanet = Utils.randomFloat(0.75f, 1.5f);
					int massPlanet = (int) (radiusPlanet * Utils.randomFloat(0.91f, 1.1f) * 65);

					Planet planet = new PlanetFactory(namePlanet, radiusPlanet, massPlanet, validePosition,
							PlanetType.NORMAL, true).createEntity();
					map.addPlanet(planet);
					entityManager.addEntity(SpaceApes.GAMEPLAY_STATE, planet);
				}
			}
		}
	}

	protected void initApes() {
		Map map = Map.getInstance();

		for (int i = 0; i < SpaceApes.players.size(); i++) {

			String nameApe = "Ape" + (i + 1);
			Planet homePlanet;
			if (i > playerPlanets.size() - 1) { // Fall es nicht genug PlayerPlanets gibt
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
			float angleOfView = 0;
			float throwStrength = 5f;

			Ape ape = new ApeFactory(nameApe, homePlanet, health, energy, apeImage, apeActive, apeInteraction,
					movementSpeed, angleOnPlanet, angleOfView, throwStrength).createEntity();
			// eine Apefactory fuer alle Apes?
			map.addApe(ape);
			StateBasedEntityManager.getInstance().addEntity(SpaceApes.GAMEPLAY_STATE, ape);
		}
	}

	protected void initApeInfoSigns() {
		Map map = Map.getInstance();

		for (int i = 0; i < map.getApes().size(); i++) {
			Ape ape = map.getApes().get(i);
			Planet planet = ape.getPlanet();
			Vector2f panelCoordinates = planet.getCoordinates(); // Uebergibt man das der Factory oder machtr die das
																	// intern selbst?
//			float ApeInfoSignScale = planet.getScale() * 0.4f; // es waere schlauer den Scale ueber den Radius zu
//																// ermitteln
//																// (wegen den Ringplaneten)

			ApeInfoSign apeInfoSign = new ApeInfoSignFactory(panelCoordinates, ape).createEntity();
			StateBasedEntityManager.getInstance().addEntity(SpaceApes.GAMEPLAY_STATE, apeInfoSign);
		}
	}
}
