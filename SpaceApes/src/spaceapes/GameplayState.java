package spaceapes;

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
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.LeavingScreenEvent;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.event.basicevents.MouseClickedEvent;

/**
 * @author Timo Baehr
 *
 *         Diese Klasse repraesentiert das Spielfenster.
 */
public class GameplayState extends BasicGameState {

	private int stateID; // Identifier dieses BasicGameState
	private StateBasedEntityManager entityManager; // zugehoeriger entityManager

	GameplayState(int sid) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
	}

	/**
	 * Wird vor dem (erstmaligen) Starten dieses States ausgefuehrt
	 */
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {

		// Hintergrund laden
		Entity background = new Entity("background"); // Entitaet fuer Hintergrund
		background.setPosition(Utils.toPixelCoordinates(0, 0)); // Startposition des Hintergrunds (Mitte des Fensters)
		background.setScale(0.9f);
		background.addComponent(new ImageRenderComponent(new Image("/assets/stars1.jpg"))); // Bildkomponente

		entityManager.addEntity(stateID, background); // Hintergrund-Entitaet an StateBasedEntityManager uebergeben

		// Planeten an zufaelliger Position initialisieren
		PlanetEntity planet1 = new PlanetEntity("planet1", Utils.randomFloat(-6, -2), Utils.randomFloat(-4, 4));
		PlanetEntity planet2 = new PlanetEntity("planet2", Utils.randomFloat(2, 6), Utils.randomFloat(-4, 4));
		// Bild hinzufuegen
		planet1.addComponent(new ImageRenderComponent(new Image("/assets/planet1.png")));
		planet2.addComponent(new ImageRenderComponent(new Image("/assets/planet1.png")));
		java.lang.System.out.println(planet1.getID() + " size: " + planet1.size() + " mass: " + planet1.mass());
		java.lang.System.out.println(planet2.getID() + " size: " + planet2.size() + " mass: " + planet2.mass());
		// entityManager uebergeben
		entityManager.addEntity(stateID, planet1);
		entityManager.addEntity(stateID, planet2);

		// Bei Druecken der ESC-Taste zurueck ins Hauptmenue wechseln
		Entity esc_Listener = new Entity("ESC_Listener");
		KeyPressedEvent esc_pressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		esc_pressed.addAction(new ChangeStateAction(Launch.MAINMENU_STATE));
		esc_Listener.addComponent(esc_pressed);
		entityManager.addEntity(stateID, esc_Listener);

		// Bei Mausklick soll Kokosnuss erscheinen
		Entity mouse_Clicked_Listener = new Entity("Mouse_Clicked_Listener");
		MouseClickedEvent mouse_Clicked = new MouseClickedEvent();

		mouse_Clicked.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
				// Kokusnuss wird erzeugt
				Entity drop = new Entity("drop of coconut");
				drop.setPosition(new Vector2f(gc.getInput().getMouseX(), gc.getInput().getMouseY()));
				drop.setRotation(Utils.randomFloat(0, 360));

				try {
					// Bild laden und zuweisen
					drop.addComponent(new ImageRenderComponent(new Image("assets/coconut.png")));
				} catch (SlickException e) {
					System.err.println("Cannot find file assets/coconut.png!");
					e.printStackTrace();
				}

				// Kokusnuss faellt nach unten
				LoopEvent loop = new LoopEvent();
				loop.addAction(new MoveDownAction(0.5f));
				drop.addComponent(loop);

				// Wenn der Bildschirm verlassen wird, dann ...
				LeavingScreenEvent lse = new LeavingScreenEvent();

				// ... zerstoere Kokosnuss
				lse.addAction(new DestroyEntityAction());

				drop.addComponent(lse);
				entityManager.addEntity(stateID, drop);
			}
		});
		mouse_Clicked_Listener.addComponent(mouse_Clicked);

		entityManager.addEntity(stateID, mouse_Clicked_Listener);

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
