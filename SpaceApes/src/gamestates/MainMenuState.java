package gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import actions.NextGameStateAction;
import eea.engine.action.Action;
import eea.engine.action.basicactions.QuitAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
import spaceapes.SpaceApes;
import utils.Resources;
import utils.Utils;

/**
 * Diese Klasse repraesentiert das Menuefenster
 */
public class MainMenuState extends BasicGameState {

	private int stateID; // Identifier dieses BasicGameState

	public Entity menuFirstLayer;
	public Entity menuMidLayer;
	public Entity menuLastLayer;
	public int maxPixelToShiftFirstLayer = 0;

	public MainMenuState(int sid) {
		stateID = sid; // MAINMENU_STATE = 0
	}

	/**
	 * Wird vor dem (erstmaligen) Starten dieses States ausgefuehrt
	 */
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {

		Resources.init(); // Initialisiere Font und Sound Objekte

		StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();

		/* Menu Hintergrund */
		this.menuLastLayer = new Entity("MenuLastLayer"); // Entitaet fuer Hintergrund erzeugen
		menuLastLayer.setPosition(Utils.toPixelCoordinates(0, 0)); // Mitte des Fensters
		this.menuMidLayer = new Entity("MenuMidLayer");
		menuMidLayer.setPosition(Utils.toPixelCoordinates(0, 0));
		this.menuFirstLayer = new Entity("MenuFirstLayer");
		menuFirstLayer.setPosition(Utils.toPixelCoordinates(0, 0));

		if (SpaceApes.renderImages) {
			Image imageLayer3 = new Image("img/assets/menu_layer3.png");
			menuLastLayer.addComponent(new ImageRenderComponent(imageLayer3));
			menuLastLayer.setScale((float) SpaceApes.HEIGHT / imageLayer3.getHeight());
			Image imageLayer2 = new Image("img/assets/menu_layer2.png");
			menuMidLayer.addComponent(new ImageRenderComponent(imageLayer2));
			menuMidLayer.setScale((float) SpaceApes.HEIGHT / imageLayer2.getHeight());
			Image imageLayer1 = new Image("img/assets/menu_layer1.png");
			menuFirstLayer.addComponent(new ImageRenderComponent(imageLayer1));
			float scale1 = (float) SpaceApes.HEIGHT / imageLayer1.getHeight();
			menuFirstLayer.setScale(scale1);

			float maxPixelToShiftFirstLayer = (scale1 * imageLayer1.getWidth() - SpaceApes.WIDTH) / 2;
			if (maxPixelToShiftFirstLayer < 0)
				maxPixelToShiftFirstLayer = 0;
			if (maxPixelToShiftFirstLayer > scale1 * 200)
				maxPixelToShiftFirstLayer = scale1 * 200;
			float effectStrength = 0.4f;
			this.maxPixelToShiftFirstLayer = (int) (effectStrength * maxPixelToShiftFirstLayer);
		}
		entityManager.addEntity(stateID, menuLastLayer); // Hintergrund-Entitaet an StateBasedEntityManager uebergeben
		entityManager.addEntity(stateID, menuMidLayer);
		entityManager.addEntity(stateID, menuFirstLayer);

		/* Neues Spiel starten-Entitaet */
		Entity newGameEntity = new Entity("SpielStarten");
		// Setze Position und Bildkomponente
		newGameEntity.setPosition(new Vector2f(SpaceApes.WIDTH / 4f, SpaceApes.HEIGHT / 2));
		newGameEntity.setScale((float) SpaceApes.HEIGHT * 0.00035f);
		if (SpaceApes.renderImages) {
			newGameEntity.addComponent(new ImageRenderComponent(new Image("img/assets/button_start.png")));
		} else {
			System.err.println("Problem with start button image");
		}

		// Erstelle das Ausloese-Event und die zugehoerige Action
		ANDEvent startGameEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action startGameAction = new NextGameStateAction();
		startGameEvent.addAction(startGameAction);
		newGameEntity.addComponent(startGameEvent);
		entityManager.addEntity(this.stateID, newGameEntity);

		/* Beenden-Entitaet */

		Entity quitEntity = new Entity("Beenden");
		// Setze Position und Bildkomponente
		quitEntity.setPosition(new Vector2f(SpaceApes.WIDTH / 4.4f, SpaceApes.HEIGHT / 1.4f));
		quitEntity.setScale((float) SpaceApes.HEIGHT * 0.00035f);
		if (SpaceApes.renderImages) {
			quitEntity.addComponent(new ImageRenderComponent(new Image("img/assets/button_quit.png")));
		} else {
			System.err.println("Problem with quit button image");
		}

		// Erstelle das Ausloese-Event und die zugehoerige Action
		ANDEvent quitGameMouseEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action quit_Action = new QuitAction();
		quitGameMouseEvent.addAction(quit_Action);
		quitEntity.addComponent(quitGameMouseEvent);
		// Ausserdem soll das Druecken der Esc-Taste das Spiel beenden
		KeyPressedEvent quitGameEscKeyEvent = new KeyPressedEvent(Input.KEY_ESCAPE);
		quitGameEscKeyEvent.addAction(new QuitAction());
		quitEntity.addComponent(quitGameEscKeyEvent);
		entityManager.addEntity(this.stateID, quitEntity); // Fuege die Entity zum StateBasedEntityManager hinzu
	}

	/**
	 * Wird vor dem Frame ausgefuehrt
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		float mouseX = container.getInput().getMouseX();
		float halfScreenWidth = SpaceApes.WIDTH / 2;
		float pixelToShiftFirstLayer = -this.maxPixelToShiftFirstLayer * (mouseX - halfScreenWidth) / halfScreenWidth;
		menuFirstLayer.setPosition(new Vector2f(pixelToShiftFirstLayer + halfScreenWidth, SpaceApes.HEIGHT / 2));
		menuMidLayer.setPosition(new Vector2f(pixelToShiftFirstLayer / 2 + halfScreenWidth, SpaceApes.HEIGHT / 2));

		if (SpaceApes.PLAY_MUSIC && !Resources.TITLE_MUSIC.playing()) {
			Utils.startMusic(Resources.TITLE_MUSIC, 1f, 0.5f, 1000);
		}
		StateBasedEntityManager.getInstance().updateEntities(container, game, delta);
	}

	/**
	 * Wird mit dem Frame ausgefuehrt
	 */
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		StateBasedEntityManager.getInstance().renderEntities(container, game, g);
	}

	@Override
	public int getID() {
		return stateID;
	}

}
