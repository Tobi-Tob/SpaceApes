package gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import actions.DisplayCoordinatesAction;
import actions.NextGameStateAction;
import actions.PauseGameAction;
import actions.ShootAction;
import eea.engine.action.basicactions.*;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.event.ANDEvent;
import eea.engine.event.Event;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.*;
import events.LessThan2ApesLeftEvent;
import factories.ProjectileFactory.MovementType;
import spaceapes.Map;
import spaceapes.SpaceApes;
import utils.Utils;
import utils.Constants;
import utils.Resources;

/**
 * Diese Klasse repraesentiert das Spielfenster
 */
public class GameplayState extends BasicGameState {

	private int stateID; // Identifier dieses BasicGameState
	private int gameTime; // in ms

	public GameplayState(int stateID) {
		this.stateID = stateID;
		this.gameTime = 0;
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

		// Die dummyEntity steuert entitaetslose Events
		Entity dummyEntity = new Entity("Dummy");

		/* ESC Event */
		// Bei Druecken der ESC-Taste zurueck ins Hauptmenue wechseln
		Event escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		escPressed.addAction(new PauseGameAction());
		dummyEntity.addComponent(escPressed);

		/* Weniger als 2 Affen uebrig Event */
		// zurueck ins Hauptmenue wechseln
		Event lessThan2Apes = new LessThan2ApesLeftEvent();
		lessThan2Apes.addAction(new NextGameStateAction());
		dummyEntity.addComponent(lessThan2Apes);

		/* Schuss Event */
		// Event, dass auf Druecken der Space Taste reagiert. Ausgeloest wird ein Schuss
		// des aktiven Affen, falls interactionAllowed = true und isAIControlled = false
		Event spaceKeyPressed = new KeyPressedEvent(Input.KEY_SPACE);
		spaceKeyPressed.addAction(new ShootAction(MovementType.EXPLICIT_EULER, false));
		dummyEntity.addComponent(spaceKeyPressed);

		entityManager.addEntity(stateID, dummyEntity);

		int random = (int) Utils.randomFloat(0, SpaceApes.players.size());
		for (int i = 0; i <= random; i++) {
			Map.getInstance().changeTurn(); // Method is called between 1 and numOfPlayers times
		}
	}

	/**
	 * Wird vor dem Frame ausgefuehrt
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		this.gameTime += delta;
		if (SpaceApes.PLAY_MUSIC && !Resources.GAMEPLAY_MUSIC.playing() && this.gameTime > 20000) {
			if (Resources.GAMEPLAY_MUSIC.paused()) {
				Resources.GAMEPLAY_MUSIC.play();
			} else {
				Utils.startMusic(Resources.GAMEPLAY_MUSIC, 1f, 0.5f, 3000);
			}
		}

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
