package spaceapes;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map.Entry;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.Event;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
import entities.Ape;
import map.Map;
import utils.Utils;

public class HighscoreState extends BasicGameState {

	private int stateID; // Identifier dieses BasicGameState
	private Entity highScoreSignLeft;
	private Entity highScoreSignRight;
	private int maxPixelToShift = 0;
	TrueTypeFont font;

	HighscoreState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {

		StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();

		/* Background-Entitaet */
		Entity highscoreBackground = new Entity("HighscoreBackground");
		highscoreBackground.setPosition(Utils.toPixelCoordinates(0, 0));
		if (SpaceApes.renderImages) {
			Image image = new Image("img/assets/space1.jpg");
			highscoreBackground.addComponent(new ImageRenderComponent(image));
			highscoreBackground.setScale((float) SpaceApes.HEIGHT / image.getHeight());
		} else {
			System.err.println("Problem with HighscoreBackground image");
		}
		entityManager.addEntity(stateID, highscoreBackground);

		/* HighScoreSign-Entitaeten */
		Entity highScoreSign = new Entity("highScoreSign"); // Entitaet fuer Hintergrund erzeugen
		highScoreSign.setPosition(Utils.toPixelCoordinates(0, 0)); // Mitte des Fensters
		Entity highScoreSignLeft = new Entity("highScoreSignLeft");
		Entity highScoreSignRight = new Entity("highScoreSignRight");

		if (SpaceApes.renderImages) {
			Image imageMid = new Image("img/assets/highscoreSign.png");
			highScoreSign.addComponent(new ImageRenderComponent(imageMid));
			float scale1 = (float) SpaceApes.HEIGHT / (1.2f * imageMid.getHeight());
			highScoreSign.setScale(scale1);
			Image imageLeft = new Image("img/assets/highscoreSignLeft.png");
			highScoreSignLeft.addComponent(new ImageRenderComponent(imageLeft));
			highScoreSignLeft.setScale((float) SpaceApes.HEIGHT / (1.5f * imageLeft.getHeight()));
			Image imageRight = new Image("img/assets/highscoreSignRight.png");
			highScoreSignRight.addComponent(new ImageRenderComponent(imageRight));
			highScoreSignRight.setScale((float) SpaceApes.HEIGHT / (3f * imageRight.getHeight()));

			this.maxPixelToShift = SpaceApes.WIDTH / 30;
		}
		entityManager.addEntity(stateID, highScoreSignLeft);
		entityManager.addEntity(stateID, highScoreSignRight);
		entityManager.addEntity(stateID, highScoreSign);
		this.highScoreSignLeft = highScoreSignLeft;
		this.highScoreSignRight = highScoreSignRight;

		/* Quit-Highscore-Entitaet */
		Entity quitHighscoreEntity = new Entity("QuitHighscoreEntity");
		quitHighscoreEntity.setPosition(new Vector2f(SpaceApes.WIDTH / 1.68f, SpaceApes.HEIGHT / 1.15f));
		quitHighscoreEntity.setScale((float) SpaceApes.HEIGHT * 0.00035f);
		if (SpaceApes.renderImages) {
			quitHighscoreEntity.addComponent(new ImageRenderComponent(new Image("img/assets/button_quit.png")));
		} else {
			System.err.println("Problem with quitHighscoreEntity image");
		}
		ANDEvent quitHighscoreMouseEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		quitHighscoreMouseEvent.addAction(new ChangeStateAction(SpaceApes.MAINMENU_STATE));
		quitHighscoreEntity.addComponent(quitHighscoreMouseEvent);
		entityManager.addEntity(stateID, quitHighscoreEntity);

		/* ESC-Taste */
		// Bei Druecken der ESC-Taste zurueck ins Hauptmenue wechseln
		Entity dummyEntity = new Entity("DummyHighscore");
		entityManager.addEntity(stateID, dummyEntity);
		Event escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		escPressed.addAction(new ChangeStateAction(SpaceApes.MAINMENU_STATE));
		dummyEntity.addComponent(escPressed);

		if (SpaceApes.renderImages) {
			font = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, 20), true);
		}

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if (SpaceApes.renderImages) {
			HashMap<String, String[]> statisticsTable = Map.getInstance().getStatistics();
			int row = 0;
			for (Entry<String, String[]> entry : statisticsTable.entrySet()) {
				String key = entry.getKey();
				String[] value = entry.getValue();
				
				Vector2f keyPos = new Vector2f(0, row * 30);
				font.drawString(keyPos.x, keyPos.y, key, Color.black);

				for (int collum = 0; collum < value.length; collum++) {
					Vector2f valuePos = new Vector2f(30 + collum * 30, row * 30);
					font.drawString(valuePos.x, valuePos.y, value[collum], Color.black);
					// System.out.println("joooooo");
				}
				row ++;
			}

		}
		StateBasedEntityManager.getInstance().renderEntities(container, game, g);

	}

	/**
	 * Wird vor dem Frame ausgefuehrt
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		float mouseX = container.getInput().getMouseX();
		float halfScreenWidth = SpaceApes.WIDTH / 2;
		float pixelToShift = -this.maxPixelToShift * (mouseX - halfScreenWidth) / halfScreenWidth;
		highScoreSignLeft.setPosition(new Vector2f(pixelToShift + SpaceApes.WIDTH / 3f, SpaceApes.HEIGHT / 2));
		highScoreSignRight.setPosition(new Vector2f(SpaceApes.WIDTH / 1.5f, pixelToShift + SpaceApes.HEIGHT / 2));
		StateBasedEntityManager.getInstance().updateEntities(container, game, delta);
	}

	@Override
	public int getID() {
		return stateID;
	}

}
