package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.entity.Entity;

public abstract class ButtonPressedAction implements Action {
	private boolean buttonPressed = false;
	private float buttonScaleNotPressed;

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta, Component event) {
		Entity button = event.getOwnerEntity();
		//System.out.println("buttonPressed: " + buttonPressed);
		if (!buttonPressed) {
			buttonScaleNotPressed = button.getScale();
			button.setScale(buttonScaleNotPressed * 0.9f);
			buttonPressed = true;
		} else {
			button.setScale(buttonScaleNotPressed);
			buttonPressed = false;
		}
		updateToPerform(container, game, delta, event);
	}

	// Von erbenden Klassen zu Implementierende Methode
	protected abstract void updateToPerform(GameContainer container, StateBasedGame game, int delta, Component event);

}
