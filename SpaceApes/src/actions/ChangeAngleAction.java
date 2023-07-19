package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.component.Component;
import eea.engine.entity.Entity;
import entities.Ape;
import spaceapes.Map;

/**
 * Action to change the angle parameter of an Ape
 * @author Tobi
 *
 */
public class ChangeAngleAction extends ButtonAnimationAction {
	private float angleToChange;

	public ChangeAngleAction(float angleToChange, Entity button) {
		super(button);
		this.angleToChange = angleToChange;
	}

	@Override
	protected void updateToPerform(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		Ape ape = Map.getInstance().getActiveApe();
		if (ape.isInteractionAllowed()) {
			ape.changeAngleOfView(angleToChange);
			Map.getInstance().updateAimline();
		}

	}

}
