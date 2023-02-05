package map;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import eea.engine.action.basicactions.DestroyEntityAction;
import eea.engine.action.basicactions.MoveDownAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.LeavingScreenEvent;
import eea.engine.event.basicevents.LoopEvent;
import entities.Ape;
import entities.Coin;
import entities.ControlPanel;
import entities.Item;
import entities.Planet;
import entities.Projectile;
import factories.ItemFactory;
import factories.ItemFactory.ItemType;
import factories.PlanetFactory;
import factories.ProjectileFactory;
import factories.PlanetFactory.PlanetType;
import factories.ProjectileFactory.ProjectileType;
import spaceapes.Constants;
import spaceapes.Launch;
import utils.Utils;

//public class Map implements IMap {
public class Map {
	private static Map map = new Map(); // TODO soll Map static werden?
	private List<Ape> apes; // Liste aller lebenden Affen
	private List<Planet> planets; // Liste aller Planeten
	private List<Entity> items; // Liste aller Items
	private StateBasedEntityManager entityManager;
	private ControlPanel controlPanel;

	/**
	 * Erzeugt ein leeres Map Objekt. Mit den init-Methoden koennen Entitys der Map
	 * hinzugefuegt werden.
	 */
	public Map() {
		apes = new ArrayList<Ape>();
		planets = new ArrayList<Planet>();
		items = new ArrayList<Entity>();
		this.entityManager = StateBasedEntityManager.getInstance(); // TODO das ist unnötig
	}

	public static Map getInstance() {
		return map;
	}

	public void parse() {
		Parser parser = new Parser();
		parser.parseMap();
		// Control Panel hinzufuegen -> MR das muss eigentlich in Map, damit man besser
		// darauf zugreifen kann
		this.controlPanel = new ControlPanel("ControlPanel");
		controlPanel.initControlPanel();
	}

	public StateBasedEntityManager getEntityManager() {
		return entityManager;
	}

	public List<Planet> getPlanets() {
		return planets;
	}

	public void addPlanet(Planet planet) {
		planets.add(planet);
	}

	public List<Entity> getItems() {
		return items;
	}

	public void addItem(Entity item) {
		items.add(item);
	}

	public List<Ape> getApes() {
		return apes;
	}

	public void addApe(Ape ape) {
		apes.add(ape);
	}

	public ControlPanel getControlPanel() {
		return controlPanel;
	}

	public Ape getActiveApe() {
		Ape activeApe = null;
		int amountOfActiveApes = 0;
		for (Ape ape : apes) {
			if (ape.isActive()) {
				activeApe = ape;
				amountOfActiveApes++;
			}
		}
		if (amountOfActiveApes == 0) {
			//throw new RuntimeException("No ape is active");
			System.out.println("No ape is active");
		}
		if (amountOfActiveApes > 1) {
			throw new RuntimeException("More than one ape is active");
		}
		return activeApe;
	}

	public void changeTurn() {
		Ape activeApe = getActiveApe();
		Ape nextApe = findNextLivingApe();
		if (nextApe == null) {
			System.out.println("findNextLivingApe returned null");
		}
		List<Ape> livingApes = new ArrayList<Ape>();
		for (Ape ape : apes) {
			if (ape.isAlive()) { 
				livingApes.add(ape);
			} else { // tote Affen aus Liste entfernen
				ape.setActive(false); // Wird zwar eh aus der Liste entfernt aber safty first
				ape.setInteractionAllowed(false);
				System.out.println(ape.getID() + " is dead");

				// Ape faellt nach unten
				LoopEvent deathLoop = new LoopEvent();
				deathLoop.addAction(new MoveDownAction(0.8f));
				ape.addComponent(deathLoop);

				// Wenn der Bildschirm verlassen wird, dann ...
				LeavingScreenEvent lse = new LeavingScreenEvent(); // TODO lse sorgt fuer rote Kommandozeilenausgabe
																	// (Eigenes
																	// LeavingWorlsEvent muss erstellt werden)

				// ... zerstoere den Ape
				lse.addAction(new DestroyEntityAction());

				ape.addComponent(lse);
			}
		}
		apes = livingApes;
		// TODO Unfertig -> beschreiben was genau!
		if (apes.isEmpty()) {
			System.out.println(activeApe.getID() + " has killed all apes");
			// TODO Change State
		} else if (apes.size() == 1) {
			System.out.println(apes.get(0).getID() + " has won!!!!!!!!!!!!!!!!! SUIII");
			// TODO Change State
		} else {
			activeApe.setActive(false);
			activeApe.setInteractionAllowed(false);
			nextApe.setActive(true);
			nextApe.setInteractionAllowed(true);
			updateAimline();
			java.lang.System.out.println("Am Zug: " + nextApe.getID() + " | energy = " + nextApe.getEnergy() +  " | health = " + nextApe.getHealth() +  " | coins = " + nextApe.getCoins());
			controlPanel.setPanelAndComponentsVisible(true); // TODO
			// TODO random spawner
			spawnItem(Constants.COIN_SPAWN_POSSIBILITY, Constants.HEALTH_PACK_SPAWN_POSSIBILITY, Constants.ENERGY_PACK_SPAWN_POSSIBILITY);
		}
	}

	private Ape findNextLivingApe() {
		int indexActiveApe = apes.indexOf(getActiveApe());
		int i = indexActiveApe + 1;
		while (i != indexActiveApe) {
			if (i >= apes.size()) { // Ende der Liste
				i = 0;
			}
			if (apes.get(i).isAlive()) {
				return apes.get(i);
			}
			i++;
		}
		return null;

	}

	public void spawnItem(float probCoin, float probHealth, float probEnergy) {
		
		String itemName;
		ItemType itemType;
		
		// Coin Spawnen
		if (Utils.randomFloat(0, 1) < probCoin) {
			Vector2f itemPosition = map.findValidPosition(2, 10);
			if (itemPosition != null) {
				itemName = "Coin";
				
				float probForCoinType = Utils.randomFloat(0, 1);
				if (probForCoinType < Constants.COPPER_COIN_SPAWN_POSSIBILITY) {
					itemType = ItemType.COPPER_COIN;
				} else if (probForCoinType < Constants.COPPER_COIN_SPAWN_POSSIBILITY + Constants.GOLD_COIN_SPAWN_POSSIBILITY) {
					itemType = ItemType.GOLD_COIN;
				} else {
					itemType = ItemType.DIAMOND_COIN;
				}

				Item coin = (Item) new ItemFactory(itemName, itemType, itemPosition).createEntity();
				map.addItem(coin);
			}
		}
		
		// Healthpack Spawnen
		if (Utils.randomFloat(0, 1) < probHealth) {
			Vector2f itemPosition = map.findValidPosition(2, 10);
			if (itemPosition != null) {
				itemName = "Healthpack";
				itemType = ItemType.HEALTH_PACK;

				//TODO Healthpack erzeugen!!
				Item healthpack = (Item) new ItemFactory(itemName, itemType, itemPosition).createEntity();
				map.addItem(healthpack);
			}
		}
		
		// Energypack Spawnen
		if (Utils.randomFloat(0, 1) < probEnergy) {
			Vector2f itemPosition = map.findValidPosition(2, 10);
			if (itemPosition != null) {
				itemName = "Energypack";
				itemType = ItemType.ENERGY_PACK;

				// TODO Energypack erzeugen!!
				Item energypack = (Item) new ItemFactory(itemName, itemType, itemPosition).createEntity();
				map.addItem(energypack);
			}
		}
	}

	/**
	 * Entfernt alle Hilfslinien Punkte
	 */
	public void removeAimeLine() {
		for (int i = 0; i < 100; i++) { // MR: 100 ist gehardcoded!
			Entity dot = entityManager.getEntity(Launch.GAMEPLAY_STATE, "dot");
			if (dot == null) {
				break;
			}
			entityManager.removeEntity(Launch.GAMEPLAY_STATE, dot);
		}
	}

	/**
	 * Entfernt zuerst alle Hilfslinien Punkte, um anschliessend eine neue
	 * Hilfslinien zu malen
	 */
	public void updateAimline() {

		Ape ape = Map.getInstance().getActiveApe();

		if (ape.isInteractionAllowed()) { // TODO brauchen wir die abfrage wirklcih?

			removeAimeLine();

			float startDirection = ape.getGlobalAngleOfView();
			float startVelocity = ape.getThrowStrength();
			Vector2f velocity = Utils.toCartesianCoordinates(startVelocity, startDirection);
			Vector2f positionOfApe = ape.getWorldCoordinates();
			// Das Projektil wird leicht ausserhalb des Apes gestartet, damit nicht sofort
			// eine Kollision eintritt...
			Vector2f positionOfProjectileLaunch = new Vector2f(positionOfApe)
					.add(Utils.toCartesianCoordinates(ape.getRadiusInWorldUnits(), ape.getAngleOnPlanet()));

			// TODO Variablen!!
			int flightTime = 1000; // in ms
			int updateFrequency = 3; // in ms
			// sollte moeglichst nahe an der tatsaechlichen Updatefrequenz liegen
			boolean draw = true;
			int numberOfDots = 5;
			int iterations = (int) flightTime / updateFrequency;

			// Hilfsprojektil wird erzeugt
			Projectile dummyProjectile = (Projectile) new ProjectileFactory("DummyProjectile",
					positionOfProjectileLaunch, velocity, false, true, ProjectileType.COCONUT).createEntity();
			for (int i = 1; i < iterations; i++) {
				if (dummyProjectile.explizitEulerStep(updateFrequency)) {
					// Wenn Kollision mit einem Objekt
					break;
				}
				if (draw && i % (60 / updateFrequency) == 0) { // In bestimmten Abstaenden werden Punkte der Hilfslinie
																// gesetzt
					Entity dot = new Entity("dot"); // Entitaet fuer einen Punkt der Linie
					dot.setPosition(Utils.toPixelCoordinates(dummyProjectile.getCoordinates()));
					dot.setScale(1 - (i * 0.8f / iterations));
					try {
						dot.addComponent(new ImageRenderComponent(new Image("img/assets/dot.png")));
						// System.out.println("add dot");
					} catch (SlickException e) {
						System.err.println("Problem with dot image");
					}
					entityManager.addEntity(Launch.GAMEPLAY_STATE, dot); // TODO Warum steht hier TODO?? mehr Beschreibung bitte
				}
			}
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
	public Vector2f findValidPosition(float marginToNextPlanetCenter, int iterations) {
		float xBorder = Utils.worldWidth / 2;
		float yBorder = Utils.worldHeight / 2;
		for (int n = 0; n < iterations; n++) { // Suche so lange wie durch iterations vorgegeben
			Vector2f randomPosition = new Vector2f(Utils.randomFloat(-xBorder * 0.8f, xBorder * 0.8f),
					Utils.randomFloat(-yBorder * 0.7f, yBorder * 0.7f));
			boolean positionIsValide = true;
			// List<Planet> plantes = Map.getInstance().getPlanets();
			// Iteriere ueber alle Planeten
			for (int i = 0; i < planets.size(); i++) {
				Planet p_i = planets.get(i);
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
				// java.lang.System.out.println("Planet spawning after: n=" + n);
				return randomPosition; // Wenn gueltige Position gefunden, gib diese zurueck
			}
		}
		// Falls Such-Schleife bis zum Ende durch laeuft:
		// java.lang.System.out.println("Planet spawning after: null");
		return null;
	}

}
