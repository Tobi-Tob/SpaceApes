package spaceapes;

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

		/* Hintergrund */

		Entity background = new Entity("background"); // Entitaet fuer Hintergrund erzeugen
		background.setPosition(Utils.toPixelCoordinates(0, 0)); // Startposition des Hintergrunds (Mitte des Fensters)
		background.setScale(0.9f); // Skalieren des Hintergrunds
		background.addComponent(new ImageRenderComponent(new Image("/assets/stars2.jpeg"))); // Bildkomponente
		entityManager.addEntity(stateID, background); // Hintergrund-Entitaet an StateBasedEntityManager uebergeben

		/* Planeten */

		// Zufaellig auf der linken und rechten Haelfte des Spielfelds platzieren
		Planet planet1 = new Planet("Planet1", Utils.randomFloat(-6, -2), Utils.randomFloat(-4, 4));
		Planet planet2 = new Planet("Planet2", Utils.randomFloat(2, 6), Utils.randomFloat(-4, 4));
		planet1.addComponent(new ImageRenderComponent(new Image("/assets/planet1.png")));
		planet2.addComponent(new ImageRenderComponent(new Image("/assets/planet1.png")));
		// Ausgabe zum testen
		java.lang.System.out
				.println(planet1.getID() + " -> Radius: " + planet1.getRadius() + " Mass: " + planet1.getMass());
		java.lang.System.out
				.println(planet2.getID() + " -> Radius: " + planet2.getRadius() + " Mass: " + planet2.getMass());
		entityManager.addEntity(stateID, planet1);
		entityManager.addEntity(stateID, planet2);

		/* Affen */

		Ape ape1 = new Ape("ape1", planet1, playerA); // Spieler A (linke Spielhaelfte)
		Ape ape2 = new Ape("ape2", planet2, playerB); // Spieler B (rechte Spielhaelfte)
		ape1.addComponent(new ImageRenderComponent(new Image("/assets/ape_blue.png")));
		ape2.addComponent(new ImageRenderComponent(new Image("/assets/ape_yellow.png")));
		entityManager.addEntity(stateID, ape1);
		entityManager.addEntity(stateID, ape2);

		// Bei Druecken der Pfeiltasten Taste soll Affe nach rechts/links laufen
		Entity right_Listener = new Entity("Right_Listener");
		Entity left_Listener = new Entity("Left_Listener");
		KeyDownEvent right_key_pressed = new KeyDownEvent(Input.KEY_RIGHT);
		KeyDownEvent left_key_pressed = new KeyDownEvent(Input.KEY_LEFT);
		right_key_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (PlayerInteractionAllowed) {
					activePlayer.getApe().stepOnPlanet(1); // rechts rum
				}
			}
		});
		left_key_pressed.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				if (PlayerInteractionAllowed) {
					activePlayer.getApe().stepOnPlanet(-1); // links rum
				}
			}
		});
		right_Listener.addComponent(right_key_pressed);
		left_Listener.addComponent(left_key_pressed);
		entityManager.addEntity(stateID, right_Listener);
		entityManager.addEntity(stateID, left_Listener);

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
					System.err.println("Cannot find file assets/coconut.png!");
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
