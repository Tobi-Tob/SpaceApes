package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import spaceapes.SpaceApes;
import utils.Resources;

/**
 * Action to pause the game
 * 
 * @author Tobi
 *
 */
public class PauseGameAction implements Action {

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		Resources.GAMEPLAY_MUSIC.pause();
		
		if (SpaceApes.PLAY_SOUNDS) {
			Resources.PAUSE_SOUND.play(1f, 1f);
		}
		gc.setPaused(true);
		sb.enterState(SpaceApes.MAINMENU_STATE);
		
	}

}
