package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.entity.Entity;
import spaceapes.SpaceApes;
import utils.Resources;

/**
 * This class is responsible for animating entities that act as buttons
 * 
 * @author Tobi
 *
 */
public abstract class ButtonAnimationAction implements Action {
	private Entity button;
	private float buttonScaleIfNotPressed;

	public ButtonAnimationAction(Entity button) {
		this.button = button;
		if (button != null) {
			this.buttonScaleIfNotPressed = button.getScale();
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta, Component event) {
		if (button != null) {
			if (button.isVisible()) {
				if (button.getScale() == buttonScaleIfNotPressed) {
					button.setScale(buttonScaleIfNotPressed * 0.9f);
				} else {
					button.setScale(buttonScaleIfNotPressed);
				}
				// Button Sound
				if (SpaceApes.PLAY_SOUNDS && !Resources.PLOP_SOUND.playing()) {
					Resources.PLOP_SOUND.play(1.5f, 0.2f);
				}
				updateToPerform(container, game, delta, event);
			}
		} else {
			updateToPerform(container, game, delta, event);
		}
	}

	// Von erbenden Klassen zu Implementierende Methode
	protected abstract void updateToPerform(GameContainer container, StateBasedGame game, int delta, Component event);

}
