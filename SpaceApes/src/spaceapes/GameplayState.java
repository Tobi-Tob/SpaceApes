package spaceapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.KeyDownEvent;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;

/**
 * @author Timo Baehr
 *
 *         Diese Klasse repraesentiert das Spielfenster
 */
public class GameplayState extends BasicGameState {

	public int stateID; // Identifier dieses BasicGameState
	public List<Player> listOfAllPlayers = new ArrayList<>(); // Liste die alle Spieler enthaelt
	public Player activePlayer; // Spieler, der am Zug ist
	public boolean userInteractionAllowed = true;
	public ControlPanel controlPanel; // Menu fuer Benutzerinteraktion
	public StateBasedEntityManager entityManager; // zugehoeriger entityManager

	GameplayState(int sid) {
		stateID = sid; // GAMEPLAY_STATE = 1
		entityManager = StateBasedEntityManager.getInstance();
		listOfAllPlayers.add(new Player("Player1"));
		listOfAllPlayers.add(new Player("Player2"));
		// listOfAllPlayers.add(new Player("Player3"));
		// listOfAllPlayers.add(new Player("Player4"));
	}

	/**
	 * Wird vor dem (erstmaligen) Starten dieses States ausgefuehrt
	 */
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {

		/* Spieler */

		Random r = new Random();
		this.activePlayer = listOfAllPlayers.get(r.nextInt(listOfAllPlayers.size())); // Zuaelligen Spieler zum Starten
																						// auswaehlen
		// playerInteractionAllowed = true;
		java.lang.System.out.println("Am Zug: " + activePlayer.iD);

		/* Erzeugen des Objekts Map */

		Map map = new Map(); // Objekt zum Speichern von Hintergrund, Planeten, etc

		/* Hintergrund */

		map.initBackground();
		entityManager.addEntity(stateID, map.background); // Hintergrund-Entitaet an StateBasedEntityManager uebergeben

		/* Planeten */

		map.spawnPlanets(0.3f, 0.2f);
		List<float[]> planetData = map.generatePlanetData();

		// Alle Planeten erhalten ein Click_Event als Componente, welches Informationen
		// ueber sie ausgibt
		for (Planet planet : map.listOfPlanets) {
			ANDEvent clicked_On_Planet_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
			clicked_On_Planet_Event.addAction(new DisplayPlanetInfoAction());
			planet.addComponent(clicked_On_Planet_Event);
			entityManager.addEntity(stateID, planet);
		}
		/* Affen */

		map.initApes(listOfAllPlayers);
		for (Ape ape : map.listOfApes) {
			ANDEvent clicked_On_Ape_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
			clicked_On_Ape_Event.addAction(new DisplayApeInfoAction());
			ape.addComponent(clicked_On_Ape_Event);
			entityManager.addEntity(stateID, ape);
		}

		/* Affenbewegung */
		// Bei Druecken der Pfeiltasten Taste soll Affe nach rechts/links laufen
		Entity right_Listener = new Entity("Right_Listener");
		Entity left_Listener = new Entity("Left_Listener");
		KeyDownEvent right_key_pressed = new KeyDownEvent(Input.KEY_RIGHT);
		KeyDownEvent left_key_pressed = new KeyDownEvent(Input.KEY_LEFT);
		left_key_pressed.addAction(new MoveOnPlanetAction(-1f));
		right_key_pressed.addAction(new MoveOnPlanetAction(1f));
		//MR: Mein Plan war die removeAimlineMethode in eine separate Klasse zu verlagern, aber das hat nur teilweise funktioniert...
		//left_key_pressed.addAction(new RemoveAimlineAction(entityManager));
		//right_key_pressed.addAction(new RemoveAimlineAction(entityManager));
		AimlineAction leftAimlineAction = new AimlineAction(entityManager, planetData);
		left_key_pressed.addAction(leftAimlineAction);
		AimlineAction rightAimlineAction = new AimlineAction(entityManager, planetData);
		right_key_pressed.addAction(rightAimlineAction);
		right_Listener.addComponent(right_key_pressed);
		left_Listener.addComponent(left_key_pressed);
		entityManager.addEntity(stateID, right_Listener);
		entityManager.addEntity(stateID, left_Listener);

		//clacTrajectory(activePlayer.getApe(), planetData, 1000, 3, true);

		/* Control Panel */

		controlPanel = new ControlPanel("ControlPanel");
		controlPanel.initControlPanel(map, activePlayer, stateID, entityManager);
		
		/* Coin */

		Entity coin = new Entity("Coin");
		coin.setPosition(new Vector2f(Launch.WIDTH / 2, Launch.HEIGHT / 2));
		coin.addComponent(new ImageRenderComponent(new Image("assets/items/coin_gold.png")));
		float coinWidthInPixel = 100;
		float desiredCoinWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
		float panelScaleFactor = desiredCoinWidth * Launch.WIDTH / coinWidthInPixel;
		coin.setScale(panelScaleFactor);
		entityManager.addEntity(stateID, coin);

		/* Schiessen */

		Entity space_bar_Listener = new Entity("Space_bar_Listener");
		KeyPressedEvent space_bar_pressed = new KeyPressedEvent(Input.KEY_SPACE);
		space_bar_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (userInteractionAllowed) {
					// Waehrend des Flugs des Projektils keine Spielerinteraktion erlaubt
					userInteractionAllowed = false;

					//MR: removeAimline() sollte eigentlich nicht in GameplayState sein...
					removeAimeLine();
					controlPanel.setPanelAndComponentsVisible(false);

					// Abfragen von initialer Position und Geschwindigkeit
					Ape activeApe = activePlayer.getApe();
					Vector2f position = activeApe.getCoordinates();
					float startDirection = activeApe.getAngleOfView_global();
					float startVelocity = activeApe.getThrowStrength();
					Vector2f velocity = Utils.toCartesianCoordinates(startVelocity, startDirection);

					// Projektil wird erzeugt
					Projectile projectile = new Projectile("Projectile", position, velocity, planetData);
					try {
						projectile.addComponent(new ImageRenderComponent(new Image("/assets/coconut.png")));
					} catch (SlickException e) {
						System.err.println("Cannot find file assets/coconut.png");
						e.printStackTrace();
					}

					// Loop Event
					LoopEvent projectileLoop = new LoopEvent();
					projectileLoop.addAction(new Action() {
						// Action, die fortlaufend wiederholt werden soll:
						@Override
						public void update(GameContainer gc, StateBasedGame sb, int timeDelta, Component event) {
							if (projectile.explizitEulerStep(timeDelta) == false) {
								// Wenn Kollision mit Planet
								entityManager.removeEntity(stateID, projectile);
								changeActivePlayerToNextPlayer();
								// Zeige Explosion
								AnimatedEntity explosion = new AnimatedEntity("Explosion", projectile.getCoordinates());
								Image[] images = new Image[4];
								try {
									images[0] = new Image("/assets/explosion/explosion1.png");
									images[1] = new Image("/assets/explosion/explosion2.png");
									images[2] = new Image("/assets/explosion/explosion3.png");
									images[3] = new Image("/assets/explosion/explosion4.png");

								} catch (SlickException e) {
									System.err.println("Cannot find image for explosion");
								}
								explosion.setImages(images);
								float explosionSizeInPixel = 300;
								float explosionSizeInWorldUnits = Utils.pixelLengthToWorldLength(explosionSizeInPixel);
								float desiredExplosionSize = 1f;
								float explosionScalingFactor = desiredExplosionSize / explosionSizeInWorldUnits;
								explosion.scaleAndRotateAnimation(explosionScalingFactor, Utils.randomFloat(0, 360));
								explosion.addAnimation(0.012f, false);
								entityManager.addEntity(stateID, explosion);

							}
							if (Math.abs(projectile.getCoordinates().x) > 1.3f * Utils.worldWidth / 2
									|| Math.abs(projectile.getCoordinates().y) > 1.3f * Utils.worldHeight / 2) {
								// Zu weit ausserhalb des Bildes
								entityManager.removeEntity(stateID, projectile);
								changeActivePlayerToNextPlayer();
							}
						}
					});
					projectile.addComponent(projectileLoop);
					entityManager.addEntity(stateID, projectile);
				}
			}
		});
		space_bar_Listener.addComponent(space_bar_pressed);
		entityManager.addEntity(stateID, space_bar_Listener);

		/* Koordinatenabfrage */

		// Bei Mausklick und druecken der Shift-Taste sollen Koordinaten an der
		// Mausposition abgefragt werden
		Entity mouse_Clicked_With_Shift_Listener = new Entity("Mouse_Clicked_With_Shift_Listener");
		ANDEvent mouse_Clicked_With_Shift = new ANDEvent(new KeyDownEvent(Input.KEY_LSHIFT), new MouseClickedEvent());
		mouse_Clicked_With_Shift.addAction(new DisplayCoordinatesAction());
		mouse_Clicked_With_Shift_Listener.addComponent(mouse_Clicked_With_Shift);
		entityManager.addEntity(stateID, mouse_Clicked_With_Shift_Listener);

		/* ESC-Taste */

		// Bei Druecken der ESC-Taste zurueck ins Hauptmenue wechseln
		Entity esc_Listener = new Entity("ESC_Listener");
		KeyPressedEvent esc_pressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		esc_pressed.addAction(new ChangeStateAction(Launch.MAINMENU_STATE));
		esc_Listener.addComponent(esc_pressed);
		entityManager.addEntity(stateID, esc_Listener);

	}

	public void changeActivePlayerToNextPlayer() {
		int indexActivePlayer = listOfAllPlayers.indexOf(activePlayer);
		int indexNextPlayer = indexActivePlayer + 1;
		if (indexNextPlayer >= listOfAllPlayers.size()) {
			indexNextPlayer = 0; // Nach dem letzten Spieler in der Liste, ist wieder der erste dran
		}
		activePlayer = listOfAllPlayers.get(indexNextPlayer);
		java.lang.System.out.println("Am Zug: " + activePlayer.iD);
		controlPanel.activePlayer = activePlayer;
		controlPanel.setPanelAndComponentsVisible(true);
		userInteractionAllowed = true;
	}

	/**
	 * Berechnet eine vorhergesagte Trajektorie eines Projektils
	 * 
	 * @param ape             Affe von dem die Trajektorie berechnet werden soll
	 * @param planetData      Planetendaten fuer die Berechnung benoetigt
	 * @param flightTime      Laenge der vorhergesagten Flugbahn in ms
	 * @param updateFrequency Frequenz in ms in der die Euler Schritte ausgefuehrt
	 *                        werden (sollte fuer gute Vorhersage nahe an der
	 *                        Frequenz liegen, die bei der Echtzeit Berechnung
	 *                        auftritt)
	 * @param draw            true, wenn die Bahn durch Punkte gezeichnet werden
	 *                        soll
	 */
	public void clacTrajectory(Ape ape, List<float[]> planetData, int flightTime, int updateFrequency, boolean draw) {
		removeAimeLine();
		Vector2f position = ape.getCoordinates();
		float startDirection = ape.getAngleOfView_global();
		float startVelocity = ape.getThrowStrength();
		Vector2f velocity = Utils.toCartesianCoordinates(startVelocity, startDirection);

		int iterations = (int) flightTime / updateFrequency;

		// Hilfsprojektil wird erzeugt
		Projectile projectile = new Projectile("Help_Projectile", position, velocity, planetData);
		for (int i = 1; i < iterations; i++) {
			if (projectile.explizitEulerStep(updateFrequency) == false) {
				// Wenn Kollision mit Planet
				break;
			}
			if (draw && i % (100 / updateFrequency) == 0) { // In bestimmten Abstaenden werden Punkte der Hilfslinie
															// gesetzt
				Entity dot = new Entity("dot"); // Entitaet fuer einen Punkt der Linie
				dot.setPosition(Utils.toPixelCoordinates(projectile.getCoordinates()));
				dot.setScale(1 - (i * 0.8f / iterations));
				try {
					dot.addComponent(new ImageRenderComponent(new Image("/assets/dot.png")));
				} catch (SlickException e) {
					System.err.println("Cannot find image for dot");
				}
				entityManager.addEntity(stateID, dot);
			}
		}
		entityManager.removeEntity(stateID, projectile);
	}

	/**
	 * Entfernt alle Hilfslinien Punkte
	 */
	public void removeAimeLine() {
		for (int i = 0; i < 100; i++) {
			Entity dot = entityManager.getEntity(stateID, "dot");
			if (dot == null) {
				break;
			}
			entityManager.removeEntity(stateID, dot);
		}

	}
	
	/**
	 * Liefert den Spieler zurück, der aktuell am Zug ist
	 */
	public Player getActivePlayer() {
		return activePlayer;
	}

	/**
	 * Wird vor dem Frame ausgefuehrt
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		// StatedBasedEntityManager soll alle Entities aktualisieren
		entityManager.updateEntities(container, game, delta);
	}

	/**
	 * Wird mit dem Frame ausgefuehrt
	 */
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// StatedBasedEntityManager soll alle Entities rendern
		entityManager.renderEntities(container, game, g);
		// g.drawString("TEST", 100, 100);
	}

	@Override
	public int getID() {
		return stateID;
	}
}
