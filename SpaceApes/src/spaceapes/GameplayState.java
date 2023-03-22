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
			// Wähle zufällige Erstellung der Map:
//			Vector2f coordinatesPlanet1 = null;
//			Vector2f coordinatesPlanet2 = null;
//			float radiusPlanet1 = 0;
//			float radiusPlanet2 = 0;
//			int massPlanet1 = 0;
//			int massPlanet2 = 0;
//			boolean createNonPlayerPlanets = true;
//			MovementType projectileMovementType = MovementType.EXPLICIT_EULER;
//			float angleOnPlanetApe1 = 999;
//			float angleOnPlanetApe2 = 999;
			
//			Die folgenden Parameter sind nur fürs Debugging
			Vector2f coordinatesPlanet1 = new Vector2f(-4.0f, 0.0f);
			Vector2f coordinatesPlanet2 = new Vector2f(4.0f, 0.0f);
			MovementType projectileMovementType = MovementType.EXPLICIT_EULER;
			float radiusPlanet1 = 1.5f;
			float radiusPlanet2 = 1.5f;
			int massPlanet1 = 65;
			int massPlanet2 = 65;
			float angleOnPlanetApe1 = -25f;
			float angleOnPlanetApe2 = -180f;
			boolean createNonPlayerPlanets = false;
			map.parse(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, createNonPlayerPlanets, projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2);
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
