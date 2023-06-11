package map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import eea.engine.action.basicactions.DestroyEntityAction;
import eea.engine.action.basicactions.MoveDownAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.LoopEvent;
import entities.Ape;
import entities.ApeInfoSign;
import entities.ControlPanel;
import entities.Item;
import entities.Planet;
import entities.Projectile;
import entities.ControlPanel.Location;
import events.LeavingWorldEvent;
import factories.ItemFactory;
import factories.ItemFactory.ItemType;
import factories.ProjectileFactory;
import factories.ProjectileFactory.MovementType;
import factories.ProjectileFactory.ProjectileType;
import spaceapes.Constants;
import spaceapes.SpaceApes;
import utils.Utils;

public class Map {
	private static Map INSTANCE;
	private List<Ape> apes; // Liste aller lebenden Affen
	private List<Planet> planets; // Liste aller Planeten
	private List<Item> items; // Liste aller Items
	private List<Entity> moons; // Liste aller Monde
	private ControlPanel controlPanel;
	private boolean useAirFriction = false; // TODO hier benötigt?
	private HashMap<String, String[]> statisticsTable; // Tabelle zum speichern aller Statistiken

	/**
	 * Erzeugt ein leeres Map Objekt. Mit den init-Methoden koennen Entitys der Map
	 * hinzugefuegt werden.
	 */
	private Map() {
		apes = new ArrayList<Ape>();
		planets = new ArrayList<Planet>();
		items = new ArrayList<Item>();
		moons = new ArrayList<Entity>();
		int numberOfPlayers = SpaceApes.players.size();
		this.statisticsTable = new HashMap<String, String[]>() {
			{
				put("Ape", new String[numberOfPlayers]);
				put("Damage dealt:", new String[numberOfPlayers]);
				put("Damage received:", new String[numberOfPlayers]);
				put("Energy used:", new String[numberOfPlayers]);
				put("Items collected:", new String[numberOfPlayers]);
				put("Money spend:", new String[numberOfPlayers]);
			}
		};
	}

	public static Map getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Map();
		}
		return INSTANCE;
	}

	public void init() {
		Integer level = null; // TODO Hierrueber kann spaeter die gewuenschte Level Datei ausgewaehlt
		// werden. NULL = Random Level. Wichtig: Level Datei solle modulare Struktur
		// haben, damit neue Features einfach den Leveln hinzugefuegt werden koennen.
		new Initializer().initMap(level);
		this.controlPanel = new ControlPanel(Location.FREE); // TODO Location kann durchs Level bestimmt werden, sonst FREE
		controlPanel.initControlPanel();
	}

	public List<Planet> getPlanets() {
		return planets;
	}

	public void addPlanet(Planet planet) {
		planets.add(planet);
	}

	public List<Item> getItems() {
		return items;
	}

	public void addItem(Item item) {
		items.add(item);
	}

	public List<Ape> getApes() {
		return apes;
	}

	public void addApe(Ape ape) {
		apes.add(ape);
	}

	public List<Entity> getMoons() {
		return moons;
	}

	public void addMoon(Entity moon) {
		moons.add(moon);
	}

	public ControlPanel getControlPanel() {
		return controlPanel;
	}

	public void useAirFriction(boolean useAirFriction) {
		this.useAirFriction = useAirFriction;
	}

	public boolean isAirFrictionUsed() {
		return useAirFriction;
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
			System.out.println("No ape is active");
		}
		if (amountOfActiveApes > 1) {
			throw new RuntimeException("More than one ape is active");
		}
		return activeApe;
	}

	// This method works for more than two players covers spacial cases when 2
	// players are on the same planet and so on...
	public void changeTurn() {
		StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();
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

				// Entferne zugehoeriges ApeInfoSign
				for (Entity e : entityManager.getEntitiesByState(SpaceApes.GAMEPLAY_STATE)) {
					if (e.getID() == Constants.APE_INFO_SIGN_ID) {
						if (((ApeInfoSign) e).getApe().equals(ape)) {
							entityManager.removeEntity(SpaceApes.GAMEPLAY_STATE, e);
							System.out.println("ApeInfoSign removed");
							break;
						}
					}
				}
				this.fillStatisticsTable(ape); // Speichere Statistik des toten Affen
				System.out.println(ape.getID() + " is dead");

				// Ape faellt nach unten
				LoopEvent deathLoop = new LoopEvent();
				deathLoop.addAction(new MoveDownAction(0.8f));
				ape.addComponent(deathLoop);

				// Wenn der Bildschirm verlassen wird, dann zerstoere den Ape
				LeavingWorldEvent leavingWorldEvent = new LeavingWorldEvent(ape);
				leavingWorldEvent.addAction(new DestroyEntityAction());
				ape.addComponent(leavingWorldEvent);
			}
		}
		apes = livingApes;
		if (apes.isEmpty()) {
			System.out.println(activeApe.getID() + " has killed all apes");
			// Change State 
		} else if (apes.size() == 1) {
			System.out.println(apes.get(0).getID() + " has won!!!!!!!!!!!!!!!!! SUIII");
			this.fillStatisticsTable(apes.get(0)); // Speichere Statistik des Siegers
		} else {
			activeApe.setActive(false);
			activeApe.setInteractionAllowed(false);
			nextApe.setActive(true);
			nextApe.setInteractionAllowed(true);
			updateAimline();
			java.lang.System.out.println("Am Zug: " + nextApe.getID() + " | energy = " + nextApe.getEnergy()
					+ " | health = " + nextApe.getHealth() + " | coins = " + nextApe.getCoins());
			controlPanel.setPanelAndComponentsVisible(true);
			spawnItem(Constants.COIN_SPAWN_POSSIBILITY, Constants.HEALTH_PACK_SPAWN_POSSIBILITY,
					Constants.ENERGY_PACK_SPAWN_POSSIBILITY); // TODO muss eigl nicht übergeben werden, wenn Konstante
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

		ItemType itemType;

		// Coin Spawnen
		if (Utils.randomFloat(0, 1) < probCoin) {
			Vector2f itemPosition = this.findValidPosition(2, 10);
			if (itemPosition != null) {

				float probForCoinType = Utils.randomFloat(0, 1);
				if (probForCoinType < Constants.COPPER_COIN_SPAWN_POSSIBILITY) {
					itemType = ItemType.COPPER_COIN;
				} else if (probForCoinType < Constants.COPPER_COIN_SPAWN_POSSIBILITY
						+ Constants.GOLD_COIN_SPAWN_POSSIBILITY) {
					itemType = ItemType.GOLD_COIN;
				} else {
					itemType = ItemType.DIAMOND_COIN;
				}

				ItemFactory.createItem(itemType, itemPosition);
			}
		}

		// Healthpack Spawnen
		if (Utils.randomFloat(0, 1) < probHealth) {
			Vector2f itemPosition = this.findValidPosition(2, 10);
			if (itemPosition != null) {
				itemType = ItemType.HEALTH_PACK;

				ItemFactory.createItem(itemType, itemPosition);
			}
		}

		// Energypack Spawnen
		if (Utils.randomFloat(0, 1) < probEnergy) {
			Vector2f itemPosition = this.findValidPosition(2, 10);
			if (itemPosition != null) {
				itemType = ItemType.ENERGY_PACK;

				ItemFactory.createItem(itemType, itemPosition);
			}
		}
	}

	/**
	 * Entfernt alle Hilfslinien Punkte
	 */
	public void removeAimeLine() {
		StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();
		for (int i = 0; i < 100; i++) {
			Entity dot = entityManager.getEntity(SpaceApes.GAMEPLAY_STATE, Constants.AIMLINE_DOT_ID);
			if (dot == null) {
				break;
			}
			entityManager.removeEntity(SpaceApes.GAMEPLAY_STATE, dot);
		}
	}

	/**
	 * Entfernt zuerst alle Hilfslinien Punkte, um anschliessend eine neue
	 * Hilfslinien zu malen
	 */
	public void updateAimline() {

		Ape ape = Map.getInstance().getActiveApe();

		if (ape.isInteractionAllowed()) {

			removeAimeLine();

			float startDirection = ape.getGlobalAngleOfView();
			float startVelocity = ape.getThrowStrength();
			Vector2f velocity = Utils.toCartesianCoordinates(startVelocity, startDirection);
			Vector2f positionOfApe = ape.getWorldCoordinates();
			// Das Projektil wird leicht ausserhalb des Apes gestartet, damit nicht sofort
			// eine Kollision eintritt...
			Vector2f positionOfProjectileLaunch = new Vector2f(positionOfApe)
					.add(Utils.toCartesianCoordinates(ape.getRadiusInWorldUnits(), ape.getAngleOnPlanet()));

			int flightTime = Constants.AIMLINE_LENGTH; // in ms
			float updateFrequency = SpaceApes.UPDATE_INTERVAL;
			boolean draw = true;
			// int numberOfDots = 16; // TL: Wird nicht benoetigt (numberOfDots ergibt sich
			// aus der Laenge der Linie und ist nicht fix)
			int iterations = Math.round(flightTime / updateFrequency);

			// Hilfsprojektil wird erzeugt
			Projectile dummyProjectile = ProjectileFactory.createProjectile(Constants.DUMMY_PROJECTILE_ID, ProjectileType.COCONUT,
					positionOfProjectileLaunch, velocity, false, true, MovementType.EXPLICIT_EULER);

			for (int i = 0; i <= iterations; i++) {
				if (dummyProjectile.explizitEulerStep((int) updateFrequency, useAirFriction)) {
					// Wenn Kollision mit einem Objekt
					break; // -> laufe trotzdem durch die schleife, damit es nicht so wirkt als wuerde es
							// laggen TL: laggt jz nicht mehr
				}
				if (draw && i % Math.round(60 / updateFrequency) == 0) { // In bestimmten Abstaenden
					// werden Punkte der Hilfslinie gesetzt
					// if (draw && i % (iterations / numberOfDots) == 0) { // In bestimmten
					// Abstaenden werden Punkte der Hilfslinie gesetzt

					Entity dot = new Entity(Constants.AIMLINE_DOT_ID); // Entitaet fuer einen Punkt der Linie
					dot.setPosition(Utils.toPixelCoordinates(dummyProjectile.getCoordinates()));
					dot.setScale(1 - (i * 0.8f / iterations)); // Scalingfactor abhaengig von Bildschirmgroesse machen!
					try {
						if (SpaceApes.renderImages) {
							dot.addComponent(new ImageRenderComponent(new Image("img/assets/dot.png")));
							// System.out.println("add dot");
						} else {
							// System.out.println("noRenderImages: assign dot image.");
						}
					} catch (SlickException e) {
						System.err.println("Problem with dot image");
					}
					StateBasedEntityManager.getInstance().addEntity(SpaceApes.GAMEPLAY_STATE, dot);
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
	 *                                 Position gesucht werden. -1 bedeutet so lange
	 *                                 bis eine Position gefunden wurde
	 * @return Vector2f oder null, falls die vorgegebene Anzahl an Iterationen
	 *         ueberschritten wurde
	 */
	public Vector2f findValidPosition(float marginToNextPlanetCenter, int iterations) {
		float xBorder = Constants.WORLD_WIDTH / 2;
		float yBorder = Constants.WORLD_HEIGHT / 2;
		boolean infiniteSearch = false;
		if (iterations == -1) {
			infiniteSearch = true;
			iterations = 10; // any positive number
		}
		boolean continueSearch = true;
		while (continueSearch) { // Falls iterations == -1 wird so lange gesucht bis eine Position gefunden
									// wurde!! TODO kann zu Endlosschleife fuehren
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
			if (!infiniteSearch) {
				continueSearch = false;
			}
		}

		// Falls Such-Schleife bis zum Ende durch laeuft:
		// java.lang.System.out.println("Planet spawning after: null");
		return null;
	}

	/**
	 * Returns true if given position is in the allowed area, else false
	 * 
	 * @param position - position of the planet
	 * @return true if given position is in the allowed area, else false
	 */
	public boolean isValidPositionForPlayerPlanet1(Vector2f position) {

		float xBorder = Constants.WORLD_WIDTH / 2;
		float yBorder = Constants.WORLD_HEIGHT / 2;

		if (Math.abs(position.y) <= yBorder / 2) {
			if (position.x >= -xBorder * 0.6f && position.x <= -xBorder * 0.3f) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if given position is in the allowed area, else false
	 * 
	 * @param position - position of the planet
	 * @return true if given position is in the allowed area, else false
	 */
	public boolean isValidPositionForPlayerPlanet2(Vector2f position) {

		float xBorder = Constants.WORLD_WIDTH / 2;
		float yBorder = Constants.WORLD_HEIGHT / 2;

		if (Math.abs(position.y) <= yBorder / 2) {
			if (position.x <= xBorder * 0.6f && position.x >= xBorder * 0.3f) {
				return true;
			}
		}

		return false;
	}

	public HashMap<String, String[]> getStatistics() {
		return this.statisticsTable;
	}
	
	private void fillStatisticsTable(Ape ape) {
		boolean collumFilled = false;
		for (int i = SpaceApes.players.size() - 1; i >= 0; i--) { // fill last collum first
			if (statisticsTable.get("Ape")[i] == null) {
				statisticsTable.get("Ape")[i] = ape.getID();
				statisticsTable.get("Damage dealt:")[i] = ape.getDamageDealtStatistics();
				statisticsTable.get("Damage received:")[i] = ape.getDamageReceivedStatistics();
				statisticsTable.get("Energy used:")[i] = ape.getEnergyUsedStatistics();
				statisticsTable.get("Items collected:")[i] = ape.getItemsCollectedStatistics();
				statisticsTable.get("Money spend:")[i] = ape.getMoneySpendStatistics();
				collumFilled = true;
				break;
			}
		}
		if (! collumFilled) {
			System.err.println("No collum was filled after call of fillStatisticsTable()");
		}
	}

	public void resetToDefault() {
		apes = new ArrayList<Ape>();
		planets = new ArrayList<Planet>();
		items = new ArrayList<Item>();
		moons = new ArrayList<Entity>();
	}

}
