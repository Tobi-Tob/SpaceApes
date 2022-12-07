package spaceapes;

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
import eea.engine.action.basicactions.RotateRightAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.CollisionEvent;
import eea.engine.event.basicevents.KeyDownEvent;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.LeavingScreenEvent;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.event.basicevents.MouseClickedEvent;

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
		entityManager.addEntity(stateID, map.listOfPlanets.get(0));
		entityManager.addEntity(stateID, map.listOfPlanets.get(1));

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
					// Abfragen von initialer Position und Geschwindigkeit
					Vector2f position = activePlayer.getApe().getCoordinates();
					float startDirection = activePlayer.getApe().getAngleOfView_global();
					float startVelocity = 5f; // Einheit: Koordinaten/Sekunde
					Vector2f velocity = Utils.toCartesianCoordinates(startVelocity, startDirection);
					
					// Projektil wird erzeugt
					Projectile projectile = new Projectile("Projectile", position, velocity, planetData);
					
					// CollisionEvent
					CollisionEvent ce = new CollisionEvent();
					ce.addAction(new DestroyEntityAction());
					//projectile.addComponent(ce);
					
					// Loop Event
					LoopEvent projectileLoop = new LoopEvent();
					projectileLoop.addAction(new Action() {
						// Action, die fortlaufend wiederholt werden soll:
						@Override
						public void update(GameContainer gc, StateBasedGame sb, int timeDelta, Component event) {
							projectile.explizitEulerStep(timeDelta);
							if (projectile.collides(map.listOfPlanets.get(0))) {
								java.lang.System.out.println("Kollision");
							}
						}
					});
					projectile.addComponent(projectileLoop);

					// Wenn der Bildschirm verlassen wird, zerstoere Entitaet
					LeavingScreenEvent lse = new LeavingScreenEvent();
					lse.addAction(new DestroyEntityAction());
					projectile.addComponent(lse);

					entityManager.addEntity(stateID, projectile);
				}
			}
		});
		space_bar_Listener.addComponent(space_bar_pressed);
		entityManager.addEntity(stateID, space_bar_Listener);

		/* Kokusnuss */

		// Bei Mausklick soll Kokosnuss erscheinen
		Entity mouse_Clicked_Listener = new Entity("Mouse_Clicked_Listener");
		MouseClickedEvent mouse_Clicked = new MouseClickedEvent();
		mouse_Clicked.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				// Kokusnuss wird erzeugt
				Entity coconut = new Entity("Coconut");
				coconut.setPosition(new Vector2f(gc.getInput().getMouseX(), gc.getInput().getMouseY()));
				coconut.setScale(0.7f);
				coconut.setRotation(Utils.randomFloat(0, 360));

				try {
					// Bild laden und zuweisen
					coconut.addComponent(new ImageRenderComponent(new Image("/assets/coconut.png")));
				} catch (SlickException e) {
					System.err.println("Cannot find file assets/coconut.png");
					e.printStackTrace();
				}

				// Kokusnuss faellt nach unten
				LoopEvent loop = new LoopEvent();
				loop.addAction(new MoveDownAction(0.5f));
				loop.addAction(new RotateRightAction(Utils.randomFloat(-0.2f, 0.2f)));
				coconut.addComponent(loop);

				// Wenn der Bildschirm verlassen wird, dann ...
				LeavingScreenEvent lse = new LeavingScreenEvent();
				// ... zerstoere Kokosnuss
				lse.addAction(new DestroyEntityAction());

				coconut.addComponent(lse);
				entityManager.addEntity(stateID, coconut);
			}
		});
		mouse_Clicked_Listener.addComponent(mouse_Clicked);

		entityManager.addEntity(stateID, mouse_Clicked_Listener);

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
