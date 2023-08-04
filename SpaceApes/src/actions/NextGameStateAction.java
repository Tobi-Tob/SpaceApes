package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import spaceapes.SpaceApes;
import utils.Resources;

/**
 * Action to start the game
 * 
 * @author Tobi
 *
 */
public class NextGameStateAction implements Action {

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		/* MainMenuState -> GameplayState */
		if (sb.getCurrentState().getID() == SpaceApes.MAINMENU_STATE) {
			if (SpaceApes.PLAY_MUSIC) {
				Resources.TITLE_MUSIC.stop();
			}
			if (SpaceApes.PLAY_SOUNDS) {
				Resources.PLOP_SOUND.play(1.3f, 0.3f);
				Resources.START_SOUND.play(1f, 1f);
			}
			sb.enterState(SpaceApes.GAMEPLAY_STATE);
		}
		/* PauseState -> GameplayState */
		if (sb.getCurrentState().getID() == SpaceApes.PAUSE_STATE) {
			gc.setPaused(false);
			if (SpaceApes.PLAY_MUSIC && Resources.GAMEPLAY_MUSIC.paused()) {
				Resources.GAMEPLAY_MUSIC.resume();
			}
			if (SpaceApes.PLAY_SOUNDS) {
				Resources.PAUSE_SOUND.play(1f, 1f);
			}
			sb.enterState(SpaceApes.GAMEPLAY_STATE);
		}
		/* GameplayState -> HighscoreState */
		if (sb.getCurrentState().getID() == SpaceApes.GAMEPLAY_STATE) {
			if (SpaceApes.PLAY_MUSIC) {
				Resources.GAMEPLAY_MUSIC.stop();
			}
			if (SpaceApes.PLAY_SOUNDS) {
				Resources.BELL_SOUND.play(1f, 0.3f);
			}
			sb.enterState(SpaceApes.HIGHSCORE_STATE);
		}
		/* HighscoreState -> MainMenuState */
		if (sb.getCurrentState().getID() == SpaceApes.HIGHSCORE_STATE) {
			if (SpaceApes.PLAY_SOUNDS) {
				Resources.PLOP_SOUND.play(1.3f, 0.3f);
			}
			sb.enterState(SpaceApes.MAINMENU_STATE);
		}
	}

}
