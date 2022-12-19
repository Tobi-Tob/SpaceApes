package spaceapes;

import java.text.DecimalFormat;
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
import eea.engine.action.basicactions.DestroyEntityAction;
import eea.engine.action.basicactions.MoveDownAction;
import eea.engine.action.basicactions.QuitAction;
import eea.engine.action.basicactions.RotateRightAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.KeyDownEvent;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.LeavingScreenEvent;
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
	private Player activePlayer; // Spieler, der am Zug ist
	private boolean PlayerInteractionAllowed = true;
	private StateBasedEntityManager entityManager; // zugehoeriger entityManager

	GameplayState(int sid) {
		stateID = sid; // GAMEPLAY_STATE = 1
		entityManager = StateBasedEntityManager.getInstance();
	}

	/**
	 * Wird vor dem (erstmaligen) Starten dieses States ausgefuehrt
	 */
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {

		/* Spieler */

		Player playerA = new Player("PlayerA");
		Player playerB = new Player("PlayerB");
		Random random = new Random(); // Zuaelligen Spieler zum Starten auswaehlen
		if (random.nextBoolean()) {
			activePlayer = playerA;
		} else {
			activePlayer = playerB;
		}
		java.lang.System.out.println("Am Zug: " + activePlayer.iD);

		/* Erzeugen des Objekts Map */

		Map map = new Map(); // Objekt zum Speichern von Hintergrund, Planeten, etc

		/* Hintergrund */

		map.initBackground();
		entityManager.addEntity(stateID, map.background); // Hintergrund-Entitaet an StateBasedEntityManager uebergeben

		/* Planeten */

		map.initPlanets();
		List<float[]> planetData = map.generatePlanetData();

		// Alle Planeten erhalten ein Click_Event als Componente, welches Informationen ueber sie ausgibt
		for (int i = 0; i < map.listOfPlanets.size(); i++) {
			ANDEvent clicked_On_Planet_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
			clicked_On_Planet_Event.addAction(new Action() {
				@Override
				public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
					Planet planet = (Planet) event.getOwnerEntity();
					java.lang.System.out.println(planet.getID());
					java.lang.System.out.println("Mass: " + planet.getMass() + " Radius: " + planet.getRadius());
				}
			});
			map.listOfPlanets.get(i).addComponent(clicked_On_Planet_Event);
			entityManager.addEntity(stateID, map.listOfPlanets.get(i));
		}

		/* Affen */

		map.initApes(playerA, playerB);
		entityManager.addEntity(stateID, map.listOfApes.get(0));
		entityManager.addEntity(stateID, map.listOfApes.get(1));

		/* Affenbewegung */
		// Bei Druecken der Pfeiltasten Taste soll Affe nach rechts/links laufen
		Entity right_Listener = new Entity("Right_Listener");
		Entity left_Listener = new Entity("Left_Listener");
		KeyDownEvent right_key_pressed = new KeyDownEvent(Input.KEY_RIGHT);
		KeyDownEvent left_key_pressed = new KeyDownEvent(Input.KEY_LEFT);
		right_key_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (PlayerInteractionAllowed) {
					activePlayer.getApe().stepOnPlanet(1); // 1 = rechts rum
				}
			}
		});
		left_key_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (PlayerInteractionAllowed) {
					activePlayer.getApe().stepOnPlanet(-1); // -1 = links rum
				}
			}
		});
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
				if (PlayerInteractionAllowed) {
					// Waehrend des Flugs des Projektils keine Spielerinteraktion erlaubt
					PlayerInteractionAllowed = false;
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
								PlayerInteractionAllowed = true;
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
								// Zu weit auﬂerhalb des Bildes
								entityManager.removeEntity(stateID, projectile);
								PlayerInteractionAllowed = true;
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
		mouse_Clicked_With_Shift.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				Vector2f mousePixelPos = new Vector2f(gc.getInput().getMouseX(), gc.getInput().getMouseY());
				Vector2f mouseCoords = Utils.toWorldCoordinates(mousePixelPos);
				int px = (int) mousePixelPos.x;
				int py = (int) mousePixelPos.y;
				DecimalFormat formatter = new DecimalFormat("#.##");
				java.lang.System.out.println("World coords (" + formatter.format(mouseCoords.x) + ", "
						+ formatter.format(mouseCoords.y) + "); Pixel pos (" + px + ", " + py + ")");
			}
		});
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
