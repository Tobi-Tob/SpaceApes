package spaceapes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.action.basicactions.QuitAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
import utils.Utils;

/**
 * @author Timo Baehr
 *
 *         Diese Klasse repraesentiert das Menuefenster
 */
public class MainMenuState extends BasicGameState {

	private int stateID; // Identifier dieses BasicGameState
	private StateBasedEntityManager entityManager; // zugehoeriger entityManager
	private Music music; // Musik dieses GameStates

	MainMenuState(int sid) {
		stateID = sid; // MAINMENU_STATE = 0
		entityManager = StateBasedEntityManager.getInstance();
		try {
			this.music = new Music("snd/song1.ogg");
		} catch (SlickException e) {
			System.err.println("Problem with main menu music");
		}
	}

	/**
	 * Wird vor dem (erstmaligen) Starten dieses States ausgefuehrt
	 */
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {

		/* Menu Hintergrund */

		Entity menuBackground = new Entity("Menu"); // Entitaet fuer Hintergrund erzeugen
		menuBackground.setPosition(Utils.toPixelCoordinates(0, 0)); // Startposition des Hintergrunds (Mitte des Fensters)
		Image image = new Image("img/assets/menuSP.png");
		menuBackground.addComponent(new ImageRenderComponent(image)); // Bildkomponente
		menuBackground.setScale((float) Launch.HEIGHT / image.getHeight()); // Skalieren des Hintergrunds
		entityManager.addEntity(stateID, menuBackground); // Hintergrund-Entitaet an StateBasedEntityManager uebergeben

		/* Neues Spiel starten-Entitaet */

		Entity newGameEntity = new Entity("SpielStarten");
		// Setze Position und Bildkomponente
		newGameEntity.setPosition(new Vector2f(Launch.WIDTH / 4f, Launch.HEIGHT / 2));
		newGameEntity.setScale((float) Launch.HEIGHT / 3200);
		newGameEntity.addComponent(new ImageRenderComponent(new Image("img/assets/button_start.png")));

		// Erstelle das Ausloese-Event und die zugehoerige Action
		ANDEvent startGameEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action startGameAction = new ChangeStateAction(Launch.GAMEPLAY_STATE);
		startGameEvent.addAction(startGameAction);
		newGameEntity.addComponent(startGameEvent);
		entityManager.addEntity(this.stateID, newGameEntity); // Fuege die Entity zum StateBasedEntityManager hinzu

		/* Beenden-Entitaet */

		Entity quitEntity = new Entity("Beenden");
		// Setze Position und Bildkomponente
		quitEntity.setPosition(new Vector2f(Launch.WIDTH / 4.4f, Launch.HEIGHT / 1.4f));
		quitEntity.setScale((float) Launch.HEIGHT / 3200);
		quitEntity.addComponent(new ImageRenderComponent(new Image("img/assets/button_beenden.png")));

		// Erstelle das Ausloese-Event und die zugehoerige Action
		ANDEvent quitGameEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action quit_Action = new QuitAction();
		quitGameEvent.addAction(quit_Action);
		quitEntity.addComponent(quitGameEvent);
		entityManager.addEntity(this.stateID, quitEntity); // Fuege die Entity zum StateBasedEntityManager hinzu

	}

	private void startMusic(float pitch, float volume, int fadeInTime) {
		music.loop(pitch, 0);
		music.fade(fadeInTime, volume, false);
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
		if(!music.playing()) {
			this.startMusic(1, 0.15f, 1000);
		}
	}

	@Override
	public int getID() {
		return stateID;
	}

}
