package spaceapes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateInitAction;
import eea.engine.action.basicactions.QuitAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;

/**
 * @author Timo Baehr
 *
 *         Diese Klasse repraesentiert das Menuefenster
 */
public class MainMenuState extends BasicGameState {

	private int stateID; // Identifier dieses BasicGameState
	private StateBasedEntityManager entityManager; // zugehoeriger entityManager

	MainMenuState(int sid) {
		stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
	}

	/**
	 * Wird vor dem (erstmaligen) Starten dieses States ausgefuehrt
	 */
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {

		/* Menu Hintergrund */

		Entity menuBackground = new Entity("menu"); // Entitaet fuer Hintergrund erzeugen
		menuBackground.setPosition(Utils.toPixelCoordinates(0, 0)); // Startposition des Hintergrunds (Mitte des Fensters)
		menuBackground.setScale((float) Launch.HEIGHT / 1440); // Skalieren des Hintergrunds
		menuBackground.addComponent(new ImageRenderComponent(new Image("/assets/menuSP.png"))); // Bildkomponente
		entityManager.addEntity(stateID, menuBackground); // Hintergrund-Entitaet an StateBasedEntityManager uebergeben

		/* Neues Spiel starten-Entitaet */

		Entity newGameEntity = new Entity("Spiel starten");
		// Setze Position und Bildkomponente
		newGameEntity.setPosition(Utils.toPixelCoordinates(-5, 0.5f));
		newGameEntity.setScale(0.25f);
		newGameEntity.addComponent(new ImageRenderComponent(new Image("assets/button_start.png")));

		// Erstelle das Ausloese-Event und die zugehoerige Action
		ANDEvent start_Game_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action start_Game_Action = new ChangeStateInitAction(Launch.GAMEPLAY_STATE);
		start_Game_Event.addAction(start_Game_Action);
		newGameEntity.addComponent(start_Game_Event);
		entityManager.addEntity(this.stateID, newGameEntity); // Fuege die Entity zum StateBasedEntityManager hinzu

		/* Beenden-Entitaet */

		Entity quitEntity = new Entity("Beenden");
		// Setze Position und Bildkomponente
		quitEntity.setPosition(Utils.toPixelCoordinates(-5, 3));
		quitEntity.setScale(0.25f);
		quitEntity.addComponent(new ImageRenderComponent(new Image("assets/button_beenden.png")));

		// Erstelle das Ausloese-Event und die zugehoerige Action
		ANDEvent quit_Game_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action quit_Action = new QuitAction();
		quit_Game_Event.addAction(quit_Action);
		quitEntity.addComponent(quit_Game_Event);
		entityManager.addEntity(this.stateID, quitEntity); // Fuege die Entity zum StateBasedEntityManager hinzu

	}

	/**
	 * Wird vor dem Frame ausgefuehrt
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		entityManager.updateEntities(container, game, delta);
	}

	/**
	 * Wird mit dem Frame ausgefuehrt
	 */
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		entityManager.renderEntities(container, game, g);
	}

	@Override
	public int getID() {
		return stateID;
	}

}
