package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.entity.Entity;

/**
 * This class is responsible for animating entities that act as buttons
 * @author Tobi
 *
 */
public abstract class ButtonAnimationAction implements Action {
	private Entity button;
	private float buttonScaleIfNotPressed;
	
	public ButtonAnimationAction(Entity button) {
		this.button = button;
		this.buttonScaleIfNotPressed = button.getScale();
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta, Component event) {
		if (button.getScale() == buttonScaleIfNotPressed) {
			button.setScale(buttonScaleIfNotPressed * 0.9f);
		} else {
			button.setScale(buttonScaleIfNotPressed);
		}
		updateToPerform(container, game, delta, event);
	}

	// Von erbenden Klassen zu Implementierende Methode
	protected abstract void updateToPerform(GameContainer container, StateBasedGame game, int delta, Component event);

}
