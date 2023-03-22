package spaceapes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.basicactions.*;
import eea.engine.entity.Entity;
import eea.engine.event.Event;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.*;
import events.LessThan2ApesLeftEvent;
import factories.ProjectileFactory.MovementType;
import map.Map;

/**
 * @author Timo Baehr
 *
 *         Diese Klasse repraesentiert das Spielfenster
 */
public class GameplayState extends BasicGameState {

	private int stateID; // Identifier dieses BasicGameState
	private StateBasedEntityManager entityManager; // zugehoeriger entityManager
	private Map map;
	private Music music; // Musik dieses GameStates

	GameplayState(int stateID) {
		this.stateID = stateID;
		this.entityManager = StateBasedEntityManager.getInstance();
		this.map = Map.getInstance();
	}

	/**
	 * Wird vor dem (erstmaligen) Starten dieses States ausgefuehrt
	 */
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {

		if (Launch.renderImages) { // wird bei den Tests immer manuell gemacht
			// Map erstellen
			// Vector2f coordinatesPlanet1 = new Vector2f(Constants.WORLD_WIDTH * 0.5f / 2,
			// Constants.WORLD_HEIGHT * 0 / 2);
			Vector2f coordinatesPlanet1 = null;
			Vector2f coordinatesPlanet2 = null;
			map.parse(coordinatesPlanet1, coordinatesPlanet2, true, MovementType.LINEAR);
		}

		// Die dummyEntity steuert die Wechsel der States
		Entity dummyEntity = new Entity("Dummy");

		/* ESC-Taste */
		// Bei Druecken der ESC-Taste zurueck ins Hauptmenue wechseln
		Event escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		escPressed.addAction(new ChangeStateAction(Launch.HIGHSCORE_STATE));
		dummyEntity.addComponent(escPressed);
		
		// Bei Druecken der ESC-Taste zurueck ins Hauptmenue wechseln
		Event lessThan2Apes = new LessThan2ApesLeftEvent();
		lessThan2Apes.addAction(new ChangeStateAction(Launch.HIGHSCORE_STATE));
		dummyEntity.addComponent(lessThan2Apes);
		
		// Hier kommen alle weiteren Events hinzu...

		entityManager.addEntity(stateID, dummyEntity);

		if (Launch.renderImages) { // muss im Test manuell gemacht werden, da sonst die map entities noch nicht erzeugt sind...!
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
		entityManager.updateEntities(container, game, delta);
		// System.out.println("Gameplaystate Updatefrequenz: " + delta + " ms");
	}

	/**
	 * Wird mit dem Frame ausgefuehrt
	 */
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// StatedBasedEntityManager soll alle Entities rendern
		entityManager.renderEntities(container, game, g);
		// System.out.println("Gameplaystate Render");
	}

	@Override
	public int getID() {
		return stateID;
	}

	public StateBasedEntityManager getEntityManager() {
		return entityManager;
	}
}
