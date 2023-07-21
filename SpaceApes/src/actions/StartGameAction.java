package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.component.Component;
import eea.engine.entity.Entity;
import spaceapes.SpaceApes;
import utils.Resources;

/**
 * Action to start the game
 * 
 * @author Tobi
 *
 */
public class StartGameAction extends ButtonAnimationAction {

	boolean firstTimeStarted = true;

	public StartGameAction(Entity button) {
		super(button);
	}

	@Override
	protected void updateToPerform(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		if (SpaceApes.PLAY_SOUNDS) {
			if (gc.isPaused()) {
				gc.setPaused(false);
				Resources.PAUSE_SOUND.play(1f, 1f);
			} else {
				Resources.START_SOUND.play(1f, 1f);
			}
		}
		sb.enterState(SpaceApes.GAMEPLAY_STATE);
	}

}
