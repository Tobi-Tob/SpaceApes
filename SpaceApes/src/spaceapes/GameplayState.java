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

	private int stateID; // Identifier dieses BasicGameState
	private List<Player> listOfAllPlayers = new ArrayList<>(); // Liste die alle Spieler enthaelt
	private Player activePlayer; // Spieler, der am Zug ist
	private StateBasedEntityManager entityManager; // zugehoeriger entityManager

	GameplayState(int sid) {
		stateID = sid; // GAMEPLAY_STATE = 1
		entityManager = StateBasedEntityManager.getInstance();
		listOfAllPlayers.add(new Player("Player1"));
		listOfAllPlayers.add(new Player("Player2"));
		listOfAllPlayers.add(new Player("Player3"));
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
		activePlayer.setInteractionAllowed(true);
		java.lang.System.out.println("Am Zug: " + activePlayer.iD);

		/* Erzeugen des Objekts Map */

		Map map = new Map(); // Objekt zum Speichern von Hintergrund, Planeten, etc

		/* Hintergrund */

		map.initBackground();
		entityManager.addEntity(stateID, map.background); // Hintergrund-Entitaet an StateBasedEntityManager uebergeben

		/* Planeten */

		map.initPlanets();
		List<float[]> planetData = map.generatePlanetData();

		// Alle Planeten erhalten ein Click_Event als Componente, welches Informationen
		// ueber sie ausgibt
		for (int i = 0; i < map.listOfPlanets.size(); i++) {
			ANDEvent clicked_On_Planet_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
			clicked_On_Planet_Event.addAction(new DisplayPlanetInfoAction());
			map.listOfPlanets.get(i).addComponent(clicked_On_Planet_Event);
			entityManager.addEntity(stateID, map.listOfPlanets.get(i));
		}

		/* Affen */

		map.initApes(listOfAllPlayers);
		for (Ape ape : map.listOfApes) {
			entityManager.addEntity(stateID, ape);
		}

		/* Affenbewegung */
		// Bei Druecken der Pfeiltasten Taste soll Affe nach rechts/links laufen
		Entity right_Listener = new Entity("Right_Listener");
		Entity left_Listener = new Entity("Left_Listener");
		KeyDownEvent right_key_pressed = new KeyDownEvent(Input.KEY_RIGHT);
		KeyDownEvent left_key_pressed = new KeyDownEvent(Input.KEY_LEFT);
		for (Player player : listOfAllPlayers) {
			left_key_pressed.addAction(new MoveLeftOnPlanetAction(player));
			right_key_pressed.addAction(new MoveRightOnPlanetAction(player));
		}
		right_Listener.addComponent(right_key_pressed);
		left_Listener.addComponent(left_key_pressed);
		entityManager.addEntity(stateID, right_Listener);
		entityManager.addEntity(stateID, left_Listener);

		/* Schiessen */

		Entity space_bar_Listener = new Entity("Space_bar_Listener");
		KeyPressedEvent space_bar_pressed = new KeyPressedEvent(Input.KEY_SPACE);
		space_bar_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (activePlayer.isInteractionAllowed()) {
					// Waehrend des Flugs des Projektils keine Spielerinteraktion erlaubt
					activePlayer.setInteractionAllowed(false);
					
					// Abfragen von initialer Position und Geschwindigkeit
					Vector2f position = activePlayer.getApe().getCoordinates();
					float startDirection = activePlayer.getApe().getAngleOfView_global();
					float startVelocity = 5f; // Einheit: Koordinaten/Sekunde
					Vector2f velocity = Utils.toCartesianCoordinates(startVelocity, startDirection);

					// Projektil wird erzeugt
					Projectile projectile = new Projectile("Projectile", position, velocity, planetData);

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
								explosion.scaleAndRotateAnimation(0.3f, Utils.randomFloat(0, 360));
								explosion.addAnimation(0.012f, false);
								entityManager.addEntity(stateID, explosion);

							}
							if (Math.abs(projectile.getCoordinates().x) > 10
									|| Math.abs(projectile.getCoordinates().y) > 8) {
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

	private void changeActivePlayerToNextPlayer() {
		int indexActivePlayer = listOfAllPlayers.indexOf(activePlayer);
		int indexNextPlayer = indexActivePlayer + 1;
		if (indexNextPlayer >= listOfAllPlayers.size()) {
			indexNextPlayer = 0; // Nach dem letzten Spieler in der Liste, ist wieder der erste dran
		}
		activePlayer = listOfAllPlayers.get(indexNextPlayer);
		activePlayer.setInteractionAllowed(true);
		java.lang.System.out.println("Am Zug: " + activePlayer.iD);
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
	}

	@Override
	public int getID() {
		return stateID;
	}
}
