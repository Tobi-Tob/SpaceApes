package spaceapes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import actions.DisplayCoordinatesAction;
import eea.engine.action.basicactions.*;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.event.ANDEvent;
import eea.engine.event.Event;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.*;
import events.LessThan2ApesLeftEvent;
import map.Map;
import utils.Utils;

/**
 * Diese Klasse repraesentiert das Spielfenster
 */
public class GameplayState extends BasicGameState {

	private int stateID; // Identifier dieses BasicGameState

	GameplayState(int stateID) {
		this.stateID = stateID;
	}

	/**
	 * Wird vor dem (erstmaligen) Starten dieses States ausgefuehrt
	 */
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {

		StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();

		if (SpaceApes.renderImages) {
			/* Hintergrund */
			Entity background = new Entity(Constants.BACKGROUND_ID);

			background.setPosition(Utils.toPixelCoordinates(0, 0));
			try {
				Image image = new Image("img/assets/space1.jpg");
				background.addComponent(new ImageRenderComponent(image));
				background.setScale((float) SpaceApes.HEIGHT / image.getHeight());
			} catch (SlickException e) {
				System.err.println("Problem with image for background");
			}

			// Zeige Koordinateninformationen, wenn Shift + Linksklick auf den Hintergrund
			Event mouseAndShiftPressed = new ANDEvent(new KeyDownEvent(Input.KEY_LSHIFT), new MouseClickedEvent());
			mouseAndShiftPressed.addAction(new DisplayCoordinatesAction());
			background.addComponent(mouseAndShiftPressed);

			entityManager.addEntity(SpaceApes.GAMEPLAY_STATE, background);
		}

		/* Initialisiern der Map */
		Map.getInstance().init();
		Map.getInstance().useAirFriction(true); // needs to be done here so that the tests work

		// Die dummyEntity steuert entitaetslose Events
		Entity dummyEntity = new Entity("Dummy");

		/* ESC Event */
		// Bei Druecken der ESC-Taste zurueck ins Hauptmenue wechseln
		Event escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		escPressed.addAction(new ChangeStateAction(SpaceApes.MAINMENU_STATE));
		dummyEntity.addComponent(escPressed);

		/* Weniger als 2 Affen uebrig Event */
		// zurueck ins Hauptmenue wechseln
		Event lessThan2Apes = new LessThan2ApesLeftEvent();
		lessThan2Apes.addAction(new ChangeStateAction(SpaceApes.HIGHSCORE_STATE));
		dummyEntity.addComponent(lessThan2Apes);

		entityManager.addEntity(stateID, dummyEntity);

		if (SpaceApes.renderImages) { // muss im Test manuell gemacht werden, da sonst die map entities noch nicht
										// erzeugt sind...!
			// Initialisierung der Aimline
			Map.getInstance().updateAimline();
		}
	}

	/**
	 * Wird vor dem Frame ausgefuehrt
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		// StatedBasedEntityManager soll alle Entities aktualisieren
		StateBasedEntityManager.getInstance().updateEntities(container, game, delta);
	}

	/**
	 * Wird mit dem Frame ausgefuehrt
	 */
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// StatedBasedEntityManager soll alle Entities rendern
		StateBasedEntityManager.getInstance().renderEntities(container, game, g);
	}

	@Override
	public int getID() {
		return stateID;
	}
}
