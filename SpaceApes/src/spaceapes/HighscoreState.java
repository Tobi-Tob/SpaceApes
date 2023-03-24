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

import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.Event;
import eea.engine.event.basicevents.KeyPressedEvent;
import utils.Utils;

public class HighscoreState extends BasicGameState {

	private int stateID; // Identifier dieses BasicGameState
	private StateBasedEntityManager entityManager; // zugehoeriger entityManager
	private Music music; // Musik dieses GameStates

	HighscoreState(int stateID) {
		this.stateID = stateID;
		this.entityManager = StateBasedEntityManager.getInstance();
		try {
			this.music = new Music("snd/song1.ogg");
		} catch (SlickException e) {
			System.err.println("Problem with highscore menu music");
		}
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		Entity highscoreBackground = new Entity("Highscore");
		highscoreBackground.setPosition(Utils.toPixelCoordinates(0, 0));
		if (SpaceApes.renderImages) {
			Image image = new Image("img/assets/menuSP.jpg");
			highscoreBackground.addComponent(new ImageRenderComponent(image));
			highscoreBackground.setScale((float) SpaceApes.HEIGHT / image.getHeight());
		} else {
			//System.out.println("noRenderImages: assign HighscoreState image.");
		}
		entityManager.addEntity(stateID, highscoreBackground);

		/* Neues Spiel starten-Entitaet */

		Entity highscoreImage = new Entity("HighscoreImage");
		// Setze Position und Bildkomponente
		highscoreImage.setPosition(new Vector2f(SpaceApes.WIDTH / 4.5f, SpaceApes.HEIGHT / 2.5f));
		highscoreImage.setScale((float) SpaceApes.HEIGHT / 1500);
		if (SpaceApes.renderImages) {
			highscoreImage.addComponent(new ImageRenderComponent(new Image("img/assets/highscore.png")));
		} else {
			//System.out.println("noRenderImages: assign highscore image.");
		}
		entityManager.addEntity(this.stateID, highscoreImage);

		// Die dummyEntity steuert die Wechsel der States
		Entity dummyEntity = new Entity("DummyHighscore");

		/* ESC-Taste */
		// Bei Druecken der ESC-Taste zurueck ins Hauptmenue wechseln
		Event escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		escPressed.addAction(new ChangeStateAction(SpaceApes.MAINMENU_STATE));
		dummyEntity.addComponent(escPressed);

		entityManager.addEntity(stateID, dummyEntity);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		entityManager.renderEntities(container, game, g);
		if (SpaceApes.PLAY_MUSIC && !music.playing()) {
			this.startMusic(1, 0.15f, 1000);
		}
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

	@Override
	public int getID() {
		return stateID;
	}

}
