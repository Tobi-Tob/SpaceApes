package spaceapes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
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
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;

/**
 * @author Timo Baehr
 *
 *         Diese Klasse repraesentiert das Menuefenster
 */
public class MainMenuState extends BasicGameState {

	private int stateID; // Identifier dieses BasicGameState
	private Music music; // Musik dieses GameStates

	private Entity menuFirstLayer;
	private Entity menuMidLayer;
	private int maxPixelToShiftFirstLayer = 0;

	MainMenuState(int sid) {
		stateID = sid; // MAINMENU_STATE = 0
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
		StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();
		/* Menu Hintergrund */

		Entity menuLastLayer = new Entity("MenuLastLayer"); // Entitaet fuer Hintergrund erzeugen
		menuLastLayer.setPosition(new Vector2f(SpaceApes.WIDTH / 2, SpaceApes.HEIGHT / 2)); // Mitte des Fensters
		Entity menuMidLayer = new Entity("MenuMidLayer");
		menuMidLayer.setPosition(new Vector2f(SpaceApes.WIDTH / 2, SpaceApes.HEIGHT / 2));
		Entity menuFirstLayer = new Entity("MenuFirstLayer");
		menuFirstLayer.setPosition(new Vector2f(SpaceApes.WIDTH / 2, SpaceApes.HEIGHT / 2));

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
		this.menuMidLayer = menuMidLayer;
		this.menuFirstLayer = menuFirstLayer;

		/* Neues Spiel starten-Entitaet */
		Entity newGameEntity = new Entity("SpielStarten");
		// Setze Position und Bildkomponente
		newGameEntity.setPosition(new Vector2f(SpaceApes.WIDTH / 4f, SpaceApes.HEIGHT / 2));
		newGameEntity.setScale((float) SpaceApes.HEIGHT * 0.00035f);
		if (SpaceApes.renderImages) {
			newGameEntity.addComponent(new ImageRenderComponent(new Image("img/assets/button_start.png")));
		} else {
			// System.out.println("noRenderImages: assign start button image.");
		}

		// Erstelle das Ausloese-Event und die zugehoerige Action
		ANDEvent startGameByMouseEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action startGameAction = new ChangeStateAction(SpaceApes.GAMEPLAY_STATE);
		startGameByMouseEvent.addAction(startGameAction);
		newGameEntity.addComponent(startGameByMouseEvent);
		// Ausserdem soll das Druecken der n-Taste das Spiel starten
		KeyPressedEvent startGameByNKeyEvent = new KeyPressedEvent(Input.KEY_N);
		startGameByNKeyEvent.addAction(new ChangeStateAction(SpaceApes.GAMEPLAY_STATE));
		newGameEntity.addComponent(startGameByNKeyEvent);
		entityManager.addEntity(this.stateID, newGameEntity); // Fuege die Entity zum StateBasedEntityManager hinzu

		/* Beenden-Entitaet */

		Entity quitEntity = new Entity("Beenden");
		// Setze Position und Bildkomponente
		quitEntity.setPosition(new Vector2f(SpaceApes.WIDTH / 4.4f, SpaceApes.HEIGHT / 1.4f));
		quitEntity.setScale((float) SpaceApes.HEIGHT * 0.00035f);
		if (SpaceApes.renderImages) {
			quitEntity.addComponent(new ImageRenderComponent(new Image("img/assets/button_quit.png")));
		} else {
			// System.out.println("noRenderImages: assign beenden button image.");
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

	private void startMusic(float pitch, float volume, int fadeInTime) {
		music.loop(pitch, 0);
		music.fade(fadeInTime, volume, false);
	}

	public Music getMusic() {
		return music;
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
		StateBasedEntityManager.getInstance().updateEntities(container, game, delta);
		// System.out.println("Main Menu Updatefrequenz: " + delta + " ms");
	}

	/**
	 * Wird mit dem Frame ausgefuehrt
	 */
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		StateBasedEntityManager.getInstance().renderEntities(container, game, g);
		if (SpaceApes.PLAY_MUSIC && !music.playing()) {
			this.startMusic(1, 0.15f, 1000);
		}
		// System.out.println("Main Menu Render");
	}

	@Override
	public int getID() {
		return stateID;
	}

}
