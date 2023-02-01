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
import entities.ControlPanel;
import entities.Planet;
import entities.Projectile;
import factories.ProjectileFactory;
import factories.ProjectileFactory.ProjectileType;
import spaceapes.Launch;
import utils.Utils;

//public class Map implements IMap {
public class Map {
	private static Map map = new Map(); // TODO soll Map static werden?
	private List<Ape> apes; // Liste aller lebenden Affen
	private List<Planet> planets; // Liste aller Planeten
	private StateBasedEntityManager entityManager;
	private ControlPanel controlPanel;

	/**
	 * Erzeugt ein leeres Map Objekt. Mit den init-Methoden koennen Entitys der Map
	 * hinzugefuegt werden.
	 */
	public Map() {
		apes = new ArrayList<Ape>();
		planets = new ArrayList<Planet>();
		this.entityManager = StateBasedEntityManager.getInstance();
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
			throw new RuntimeException("No ape is active");
		}
		if (amountOfActiveApes > 1) {
			throw new RuntimeException("More than one ape is active");
		}
		return activeApe;
	}

	public void changeTurn() {
		int indexActiveApe = apes.indexOf(getActiveApe());
		int indexNextApe = indexActiveApe + 1;
		if (indexNextApe >= apes.size()) {
			indexNextApe = 0; // Nach dem letzten Spieler in der Liste, ist wieder der erste dran
		}
		Ape activeApe = apes.get(indexActiveApe);
		activeApe.setActive(false);
		activeApe.setInteractionAllowed(false);
		Ape nextApe = apes.get(indexNextApe);
		nextApe.setActive(true);
		nextApe.setInteractionAllowed(true);
		updateAimline();
		java.lang.System.out.println("Am Zug: " + nextApe.getID());
		controlPanel.setPanelAndComponentsVisible(true); // TODO
		//TODO random spawner
		spawnItem(0.5f);
	}
	
	public void spawnItem(float probability) {
		
	}

	public void apeDied(Ape ape) {
		ape.setActive(false);
		ape.setInteractionAllowed(false);
		apes.remove(ape);
		java.lang.System.out.println(ape.getID() + " is dead");

		// Ape faellt nach unten
		LoopEvent deathLoop = new LoopEvent();
		deathLoop.addAction(new MoveDownAction(0.8f));
		ape.addComponent(deathLoop);

		// Wenn der Bildschirm verlassen wird, dann ...
		LeavingScreenEvent lse = new LeavingScreenEvent(); // TODO lse sorgt fuer rote Kommandozeilenausgabe (Eigenes
															// LeavingWorlsEvent muss erstellt werden)

		// ... zerstoere den Ape
		lse.addAction(new DestroyEntityAction());

		ape.addComponent(lse);
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
			Projectile dummyProjectile = (Projectile) new ProjectileFactory("DummyProjectile", positionOfProjectileLaunch,
					velocity, false, true, ProjectileType.COCONUT).createEntity();
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
					entityManager.addEntity(Launch.GAMEPLAY_STATE, dot); // TODO
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
			//List<Planet> plantes = Map.getInstance().getPlanets();
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
