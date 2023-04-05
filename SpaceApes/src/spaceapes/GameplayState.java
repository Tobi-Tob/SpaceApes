package spaceapes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
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
import factories.ProjectileFactory.MovementType;
import map.Map;
import utils.Utils;

/**
 * @author Timo Baehr
 *
 *         Diese Klasse repraesentiert das Spielfenster
 */
public class GameplayState extends BasicGameState {

	private int stateID; // Identifier dieses BasicGameState
	private Map map;

	GameplayState(int stateID) {
		this.stateID = stateID;
		this.map = Map.getInstance();
	}

	/**
	 * Wird vor dem (erstmaligen) Starten dieses States ausgefuehrt
	 */
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
		StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();

		if (SpaceApes.renderImages) { // wird bei den Tests immer manuell gemacht
			// Map erstellen
			// Wähle zufällige Erstellung der Map:
			Vector2f coordinatesPlanet1 = null;
			Vector2f coordinatesPlanet2 = null;
			float radiusPlanet1 = 0;
			float radiusPlanet2 = 0;
			int massPlanet1 = 0;
			int massPlanet2 = 0;
			boolean createNonPlayerPlanets = true;
			MovementType projectileMovementType = MovementType.EXPLICIT_EULER;
			float angleOnPlanetApe1 = 999;
			float angleOnPlanetApe2 = 999;
			boolean antiPlanetAndBlackHole = false;

//			Die folgenden Parameter sind nur fürs Debugging
//			Vector2f coordinatesPlanet1 = new Vector2f(-4.0f, 0.0f);
//			Vector2f coordinatesPlanet2 = new Vector2f(4.0f, 0.0f);
//			float radiusPlanet1 = 1.5f;
//			float radiusPlanet2 = 1.5f;
//			int massPlanet1 = 65;
//			int massPlanet2 = 65;
//			boolean createNonPlayerPlanets = false;
//			MovementType projectileMovementType = MovementType.EXPLICIT_EULER;
//			float angleOnPlanetApe1 = -25f;
//			float angleOnPlanetApe2 = 155f;
//			boolean antiPlanetAndBlackHole = true;
			
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
			
			/* Initialisiern der Map */
			
			map.init(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, createNonPlayerPlanets,
					projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2, antiPlanetAndBlackHole);
			Map.getInstance().useAirFriction(true); // needs to be done here so that the tests work
		}

		// Die dummyEntity steuert entitaetslose Events
		Entity dummyEntity = new Entity("Dummy");

		/* ESC-Taste */
		// Bei Druecken der ESC-Taste zurueck ins Hauptmenue wechseln
		Event escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		escPressed.addAction(new ChangeStateAction(SpaceApes.MAINMENU_STATE));
		dummyEntity.addComponent(escPressed);

		/* Weniger als 2 Affen uebrig */
		// zurueck ins Hauptmenue wechseln
		Event lessThan2Apes = new LessThan2ApesLeftEvent();
		lessThan2Apes.addAction(new ChangeStateAction(SpaceApes.MAINMENU_STATE));
		dummyEntity.addComponent(lessThan2Apes);

		entityManager.addEntity(stateID, dummyEntity);

		if (SpaceApes.renderImages) { // muss im Test manuell gemacht werden, da sonst die map entities noch nicht
										// erzeugt sind...!
			// Initialisierung der Aimline
			map.updateAimline();
		}
	}

	/**
	 * Wird vor dem Frame ausgefuehrt
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		// StatedBasedEntityManager soll alle Entities aktualisieren
		StateBasedEntityManager.getInstance().updateEntities(container, game, delta);
		// System.out.println("Gameplaystate Updatefrequenz: " + delta + " ms");
	}

	/**
	 * Wird mit dem Frame ausgefuehrt
	 */
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// StatedBasedEntityManager soll alle Entities rendern
		StateBasedEntityManager.getInstance().renderEntities(container, game, g);
		// System.out.println("Gameplaystate Render");
	}

	@Override
	public int getID() {
		return stateID;
	}
}
