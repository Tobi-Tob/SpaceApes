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

/**
 * Diese Klasse repraesentiert das Menuefenster
 */
public class PauseState extends BasicGameState {

	private int stateID;

	private Entity menuFirstLayer;
	private Entity menuMidLayer;
	private Entity menuLastLayer;
	private int maxPixelToShiftFirstLayer = 0;

	public PauseState(int sid) {
		stateID = sid;
	}

	/**
	 * Wird vor dem (erstmaligen) Starten dieses States ausgefuehrt
	 */
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {

		StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();

		// Copy moving layers of MainMenuState
		MainMenuState mainMenu = (MainMenuState) game.getState(SpaceApes.MAINMENU_STATE);
		this.menuFirstLayer = mainMenu.menuFirstLayer;
		this.menuMidLayer = mainMenu.menuMidLayer;
		this.menuLastLayer = mainMenu.menuLastLayer;
		this.maxPixelToShiftFirstLayer = mainMenu.maxPixelToShiftFirstLayer;

		entityManager.addEntity(stateID, this.menuLastLayer);
		entityManager.addEntity(stateID, this.menuMidLayer);
		entityManager.addEntity(stateID, this.menuFirstLayer);

		/* Neues Spiel starten-Entitaet */
		Entity newGameEntity = new Entity("SpielStarten");
		newGameEntity.setPosition(new Vector2f(SpaceApes.WIDTH / 4f, SpaceApes.HEIGHT / 2));
		newGameEntity.setScale((float) SpaceApes.HEIGHT * 0.00035f);
		if (SpaceApes.renderImages) {
			newGameEntity.addComponent(new ImageRenderComponent(new Image("img/assets/button_start.png")));
		} else {
			System.err.println("Problem with start button image");
		}

		ANDEvent continueGameEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		// Ausserdem soll das Druecken der Esc-Taste das Spiel fortsetzen
		KeyPressedEvent escKeyEvent = new KeyPressedEvent(Input.KEY_ESCAPE);
		Action continueGameAction = new NextGameStateAction();
		continueGameEvent.addAction(continueGameAction);
		escKeyEvent.addAction(continueGameAction);
		newGameEntity.addComponent(continueGameEvent);
		newGameEntity.addComponent(escKeyEvent);
		
		entityManager.addEntity(this.stateID, newGameEntity);

		/* Beenden-Entitaet */
		Entity quitEntity = new Entity("Beenden");
		quitEntity.setPosition(new Vector2f(SpaceApes.WIDTH / 4.4f, SpaceApes.HEIGHT / 1.4f));
		quitEntity.setScale((float) SpaceApes.HEIGHT * 0.00035f);
		if (SpaceApes.renderImages) {
			quitEntity.addComponent(new ImageRenderComponent(new Image("img/assets/button_quit.png")));
		} else {
			System.err.println("Problem with quit button image");
		}

		ANDEvent quitGameMouseEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action quit_Action = new QuitAction();
		quitGameMouseEvent.addAction(quit_Action);
		quitEntity.addComponent(quitGameMouseEvent);

		entityManager.addEntity(this.stateID, quitEntity);
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
